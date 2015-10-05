package com.mvrt.superscout;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.mvrt.mvrtlib.util.Constants;
import com.mvrt.mvrtlib.util.FragmentPagerAdapter;
import com.mvrt.mvrtlib.util.JSONUtils;
import com.mvrt.mvrtlib.util.MatchInfo;
import com.mvrt.mvrtlib.util.Snacker;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author Bubby
 */
public class SuperScoutActivity extends ActionBarActivity implements ChildEventListener {


    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    IntentFilter[] intentFilters;

    ArrayList<JSONObject> scoutData = new ArrayList<>();
    MatchInfo matchInfo;
    Firebase mainRef;

    SuperCommentsFragment superCommentsFragment;
    SuperMatchInfoFragment superMatchInfoFragment;
    SuperDataFragment superDataFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_scout);

        loadFirebase();
        loadIntentData();
        loadUI();
        loadFragments();
        initNFC();
        sendMatchData();
    }

    public void loadFirebase(){
        Firebase.setAndroidContext(getApplicationContext());
        mainRef = new Firebase("https://teamdata.firebaseio.com/");
        mainRef.addChildEventListener(this);
    }

    public void loadIntentData(){
        matchInfo = (MatchInfo) getIntent().getSerializableExtra(Constants.INTENT_EXTRA_MATCHINFO);
    }

    public void loadUI(){
        Toolbar t = (Toolbar)findViewById(R.id.matchscout_toolbar);
        setSupportActionBar(t);
    }

    public void loadFragments(){
        superMatchInfoFragment = new SuperMatchInfoFragment();
        superCommentsFragment = new SuperCommentsFragment();
        superDataFragment = new SuperDataFragment();

        Bundle b = new Bundle();
        b.putSerializable(Constants.INTENT_EXTRA_MATCHINFO, matchInfo);
        superMatchInfoFragment.setArguments(b);
        superCommentsFragment.setArguments(b);

        FragmentPagerAdapter tabAdapter = new FragmentPagerAdapter(getFragmentManager());
        tabAdapter.addFragment(new FragmentPagerAdapter.TabFragment(superMatchInfoFragment, "Match Info"));
        tabAdapter.addFragment(new FragmentPagerAdapter.TabFragment(superCommentsFragment, "Super Comments"));
        tabAdapter.addFragment(new FragmentPagerAdapter.TabFragment(superDataFragment, "Scouting Data"));

        ViewPager pager = (ViewPager)findViewById(R.id.matchscout_pager);
        pager.setAdapter(tabAdapter);

        TabLayout tabs = (TabLayout)findViewById(R.id.matchscout_tablayout);
        tabs.setupWithViewPager(pager);
    }

    public void initNFC(){
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(nfcAdapter == null){
            Toast.makeText(this, "NFC not available", Toast.LENGTH_SHORT).show();
            return;
        }

        pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, SuperScoutActivity.class)
                        .addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING), 0);

        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        ndef.addDataScheme("vnd.android.nfc");
        ndef.addDataPath(Constants.NDEF_DATA_PATH, 0);
        ndef.addDataAuthority("ext", null);
        intentFilters = new IntentFilter[]{ndef};

        nfcAdapter.setNdefPushMessage(new NdefMessage(
                NdefRecord.createExternal(Constants.NDEF_RECORD_DOMAIN, Constants.NDEF_RECORD_TYPE_MATCHINFO, matchInfo.toString().getBytes())
        ), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())){
            handleIntent(getIntent());
        }
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);
        super.onResume();
    }

    public void handleIntent(Intent intent){
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage msg = (NdefMessage) rawMsgs[0];
        String data = new String(msg.getRecords()[0].getPayload());
        Log.d("MVRT", "Data recieved NFC");
        addMatchData(data);
    }

    public void sendScoutData(JSONObject data) {
        Log.d("MVRT", "Sending scout data");
        Log.d("MVRT", data.toString());
        try{
            Map<String, Object> map = JSONUtils.jsonToMap(data);
            mainRef.child("matches")
                    .child(data.getString("Match Info"))
                    .child("scout").updateChildren(map);
        }catch(Exception e){
            Log.d("MVRT", "Exception");
            e.printStackTrace();
        }
    }

    public void sendSuperData(){
        String team1 = superCommentsFragment.getTeam1();
        String team2 = superCommentsFragment.getTeam2();
        String team3 = superCommentsFragment.getTeam3();

        Firebase matchRef = mainRef.child("matches");

        matchRef.child(matchInfo.singleTeamString(0))
                .child("super")
                .setValue(team1);

        matchRef.child(matchInfo.singleTeamString(1))
                .child("super")
                .setValue(team2);

        matchRef.child(matchInfo.singleTeamString(2))
                .child("super")
                .setValue(team3);
    }

    public void sendMatchData(){
        for(int id = 0; id < 3; id++) {
            Firebase r = mainRef.child("matches").child(matchInfo.singleTeamString(id));
            r.child("team").setValue(matchInfo.getTeam(id));
            r.child("alliance").setValue(matchInfo.getAlliance());
            r.child("match").setValue(matchInfo.getMatchNo());
            r.child("tournament").setValue(matchInfo.getTournament());
        }
    }

    @Override
    protected void onPause() {
        nfcAdapter.disableForegroundDispatch(this);
        super.onPause();
    }

    public void addMatchData(String str){
        try{
            JSONObject o = new JSONObject(str);
            JSONObject data = o.getJSONObject("data");
            scoutData.add(data);

            Log.d("MVRT", o.toString());

            String verify = "N/A";
            if(o.has("verif")) verify = o.getString("verif");
            Log.d("MVRT", "Match Info: " + data.getString("Match Info"));
            MatchInfo i = MatchInfo.parse(data.getString("Match Info"));
            int team = -1;
            if(i != null)team = i.getTeam(0);
            superDataFragment.addData(team, verify);
            Log.d("MVRT", "Add Data from Team: " + team + ", verify: " + verify);

            sendScoutData(data);

        }catch(Exception e){
            Log.d("MVRT", "Error addding scouting data - not JSON");
            Log.d("MVRT", "Data: " + str);
            Log.d("MVRT", e.toString());
        }
    }

    /** FIREBASE CALLBACKS */

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Log.d("MVRT", "Firebase onChildAdded!");
    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {
        Toast.makeText(this, "Firebase Error: " + firebaseError, Toast.LENGTH_LONG).show();
        Log.e("MVRT", "Firebase Error: " + firebaseError);
    }

    public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
    public void onChildRemoved(DataSnapshot dataSnapshot) {}
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
}


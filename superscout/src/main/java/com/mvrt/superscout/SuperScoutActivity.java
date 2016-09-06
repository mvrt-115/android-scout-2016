package com.mvrt.superscout;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mvrt.mvrtlib.util.Constants;
import com.mvrt.mvrtlib.util.FragmentPagerAdapter;
import com.mvrt.mvrtlib.util.JSONUtils;
import com.mvrt.mvrtlib.util.MatchInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author Bubby and Akhil
 */
public class SuperScoutActivity extends AppCompatActivity {

    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    IntentFilter[] intentFilters;

    ArrayList<JSONObject> scoutData = new ArrayList<>();
    MatchInfo matchInfo;

    SuperCommentsFragment superCommentsFragment;
    SuperMatchInfoFragment superMatchInfoFragment;
    SuperDataFragment superDataFragment;

    DatabaseReference matchDataRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_scout);

        loadIntentData();
        initFirebase();
        loadUI();
        loadFragments();
        initNFC();
    }

    private void initFirebase(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
        matchDataRef = database.getReference("matches");
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
        try {
            ndef.addDataType("application/json");
        } catch (IntentFilter.MalformedMimeTypeException e) { e.printStackTrace(); }
        intentFilters = new IntentFilter[]{ndef};

        NdefRecord r = NdefRecord.createMime("text/mvrt", matchInfo.toString().getBytes());
        NdefMessage message = new NdefMessage(r);
        nfcAdapter.setNdefPushMessage(message, this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())){
            getIntent().setAction(Intent.ACTION_DEFAULT);
            addNFCMatchData(getIntent());
        }
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);
        super.onResume();
    }

    @Override
    protected void onPause() {
        nfcAdapter.disableForegroundDispatch(this);
        super.onPause();
    }

    public void addNFCMatchData(Intent intent){
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage msg = (NdefMessage) rawMsgs[0];
        String data = new String(msg.getRecords()[0].getPayload());
        try{
            JSONObject obj = new JSONObject(data);
            addScoutMatchData(obj.getJSONObject("data"), obj.getString("verif"));
        }catch(JSONException e){ e.printStackTrace(); }
    }

    public void addScoutMatchData(JSONObject object, String verifCode){
        scoutData.add(object);
        try{
            MatchInfo i = MatchInfo.parse(object.getString(Constants.JSON_DATA_MATCHINFO));
            int scoutId = object.getInt(Constants.JSON_DATA_SCOUTID);
            superDataFragment.addData(i.getTeams()[scoutId], (verifCode == null)?"N/A":verifCode);
            Log.d("MVRT", "data: " + object.toString());

            matchDataRef.child(i.toDbKey(scoutId)).updateChildren(JSONUtils.jsonToMap(object));
        }catch(JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendSuperData(){
        matchDataRef.child(matchInfo.toDbKey(0)).child("super")
                .setValue(superCommentsFragment.getTeam1());
        matchDataRef.child(matchInfo.toDbKey(1)).child("super")
                .setValue(superCommentsFragment.getTeam2());
        matchDataRef.child(matchInfo.toDbKey(2)).child("super")
                .setValue(superCommentsFragment.getTeam3());

        for(int id = 0; id < 3; id++){
            DatabaseReference r = matchDataRef.child(matchInfo.toDbKey(id));
            r.child("team").setValue(matchInfo.getTeams()[id]);
            r.child("alliance").setValue(matchInfo.getAlliance() + "");
            r.child("match").setValue(matchInfo.getMatchNo());
            r.child("tournament").setValue(matchInfo.getTournament());
            r.child("matchinfo").setValue(matchInfo.toString());
        }
    }

    public void finishScouting(){
        sendSuperData();
        finish();
    }

    public void addQRMatchData(String str){
        try{
            JSONObject o = new JSONObject(str);
            addScoutMatchData(o.getJSONObject("data"), o.getString("verif"));
        }catch(JSONException e){ e.printStackTrace(); }
    }

}


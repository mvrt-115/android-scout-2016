package com.mvrt.superscout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.mvrt.mvrtlib.util.Constants;
import com.mvrt.mvrtlib.util.FragmentPagerAdapter;
import com.mvrt.mvrtlib.util.JSONUtils;
import com.mvrt.mvrtlib.util.MatchInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class SuperScoutActivity extends AppCompatActivity implements ChildEventListener {

    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    IntentFilter[] intentFilters;

    ArrayList<JSONObject> scoutData = new ArrayList<>();
    MatchInfo matchInfo;

    SuperCommentsFragment superCommentsFragment;
    SuperMatchInfoFragment superMatchInfoFragment;
    SuperDataFragment superDataFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_scout);

        loadIntentData();
        loadUI();
        loadFragments();
        initNFC();
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
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())){
            getIntent().setAction(Intent.ACTION_DEFAULT);
            addNFCMatchData(getIntent());
        }
        //nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);
        super.onResume();
    }

    protected void onPause() {
        //nfcAdapter.disableForegroundDispatch(this);
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

            final JSONObject obj = object;
            final int teamNo = matchInfo.getTeam(scoutId);
            new Thread(){
                public void run(){
                    try {
                        Log.e("JSON", obj.toString());
                        URL url = new URL(Constants.SuperscoutUrl+teamNo);
                        String data = obj.toString();
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                        conn.setRequestProperty("Accept", "application/json");
                        conn.setFixedLengthStreamingMode(data.getBytes().length);

                        conn.setDoOutput(true);
                        conn.setDoInput(true);
                        conn.connect();

                        OutputStream os = new BufferedOutputStream(conn.getOutputStream());
                        os.write(data.getBytes());
                        os.flush();
                        os.close();

                        InputStream ip = conn.getInputStream();
                        BufferedReader br1 = new BufferedReader(new InputStreamReader(ip));

                        // Print the response code
                        // and response message from server.
                        System.out.println("Response Code:" + conn.getResponseCode());
                        System.out.println("Response Message:" + conn.getResponseMessage());

                        Log.e("response", "Response Code:" + conn.getResponseCode());
                        Log.e("response", "Response Message:" + conn.getResponseMessage());

                        // to print the 1st header field.
                        System.out.println("Header field 1:" + conn.getHeaderField(1));

                        // print the response
                        StringBuilder response = new StringBuilder();
                        String responseSingle = null;
                        while ((responseSingle = br1.readLine()) != null)
                        {
                            response.append(responseSingle);
                        }

                        Log.e("response", response.toString());
                        ip.close();
                        conn.disconnect();
                    } catch(Exception e) {
                        Log.e("response", e.toString());
                    }
                }
            }.start();

        }catch(JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendSuperData(){
        /*
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

         */
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

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        String matchInfo = (String)dataSnapshot.child(Constants.JSON_DATA_MATCHINFO).getValue();
        MatchInfo i = MatchInfo.parse(matchInfo);
        int scoutId = ((Long)dataSnapshot.child(Constants.JSON_DATA_SCOUTID).getValue()).intValue();
        superDataFragment.addData(i.getTeams()[scoutId], "N/A, Synced Online");
        Log.d("MVRT", "DB Child Added: " + i.toString());
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}


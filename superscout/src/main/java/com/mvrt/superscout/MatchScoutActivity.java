package com.mvrt.superscout;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.mvrt.mvrtlib.util.Constants;
import com.mvrt.mvrtlib.util.FragmentPagerAdapter;
import com.mvrt.mvrtlib.util.MatchInfo;
import com.mvrt.mvrtlib.util.MatchScoutingData;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.orchestrate.client.Client;
import io.orchestrate.client.CollectionSearchResource;
import io.orchestrate.client.EventResource;
import io.orchestrate.client.KvListResource;
import io.orchestrate.client.KvMetadata;
import io.orchestrate.client.KvObject;
import io.orchestrate.client.KvResource;
import io.orchestrate.client.OrchestrateClient;
import io.orchestrate.client.OrchestrateRequest;
import io.orchestrate.client.RelationResource;
import io.orchestrate.client.ResponseAdapter;
import io.orchestrate.client.ResponseListener;

/**
 * @author Bubby
 */
public class MatchScoutActivity extends ActionBarActivity {

    MatchInfo matchInfo;

    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    IntentFilter[] intentFilters;
    ArrayList<String> dataList = new ArrayList<String>();
    Firebase mainRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_scout);
        Firebase.setAndroidContext(this);
        loadIntentData();
        loadUI();
        loadFragments();
        initNFC();
        mainRef = new Firebase("https://teamdata.firebaseio.com/");
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

    public void sendData(int counter) {
        mainRef.child(matchInfo.getTournament())
                .child(matchInfo.getMatchNo())
                .child(matchInfo.getAllianceString())
                .child("Team :" + matchInfo.getTeam(counter))
                .setValue(dataList.get(counter));
    }

    @Override
    public void finish() {
        super.finish();
    }

    public void handleIntent(Intent intent){
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage msg = (NdefMessage) rawMsgs[0];
        String data = new String(msg.getRecords()[0].getPayload());
        dataList.add(data);
        if(dataList.size() > 3) return;
        sendData(dataList.size()-1);
        //Toast.makeText(this, data, Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onPause() {
        nfcAdapter.disableForegroundDispatch(this);
        super.onPause();
    }

    public void loadIntentData(){
        matchInfo = (MatchInfo) getIntent().getSerializableExtra(Constants.INTENT_EXTRA_MATCHINFO);
    }

    public void loadUI(){
        Toolbar t = (Toolbar)findViewById(R.id.matchscout_toolbar);
        setSupportActionBar(t);
    }

    public void loadFragments(){
        MatchInfoFragment f = new MatchInfoFragment();
        ScannerFragment sf = new ScannerFragment();
        Bundle b = new Bundle();
        b.putSerializable(Constants.INTENT_EXTRA_MATCHINFO, matchInfo);
        f.setArguments(b);

        TabLayout tabs = (TabLayout)findViewById(R.id.matchscout_tablayout);

        FragmentPagerAdapter tabAdapter = new FragmentPagerAdapter(getFragmentManager());
        ViewPager pager = (ViewPager)findViewById(R.id.matchscout_pager);
        pager.setAdapter(tabAdapter);
        tabAdapter.addFragment(new FragmentPagerAdapter.TabFragment(f, "Match Info"));
        tabAdapter.addFragment(new FragmentPagerAdapter.TabFragment(sf, "Scan QR"));

        tabs.setupWithViewPager(pager);
    }

    public void initNFC(){
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(nfcAdapter == null){
            Toast.makeText(this, "NFC not available", Toast.LENGTH_SHORT).show();
            return;
        }

        pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MatchScoutActivity.class)
                        .addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING), 0);

        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        ndef.addDataScheme("vnd.android.nfc");
        ndef.addDataPath("/mvrt.com:matchdata", 0);
        ndef.addDataAuthority("ext", null);
        intentFilters = new IntentFilter[]{
                ndef,
        };

        nfcAdapter.setNdefPushMessage(new NdefMessage(
                NdefRecord.createExternal(
                        "mvrt.com",
                        "matchinfo",
                        matchInfo.toString().getBytes())
            ), this);
    }

}


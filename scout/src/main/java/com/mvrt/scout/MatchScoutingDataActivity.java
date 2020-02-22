package com.mvrt.scout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;
import com.mvrt.mvrtlib.util.Constants;
import com.mvrt.mvrtlib.util.FragmentPagerAdapter;
import com.mvrt.mvrtlib.util.MatchInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;

public class MatchScoutingDataActivity extends AppCompatActivity{

    private NfcAdapter nfcAdapter;
    private JSONObject data;
    private JSONObject scoutData;
    private MatchInfo info;

    private MatchDataFragment dataFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_scouting_data);

        String filename = getIntent().getStringExtra(Constants.INTENT_EXTRA_FILENAME);
        Log.d("MVRT", "filename: " + filename);

        try{
            FileInputStream fis = openFileInput(getIntent().getStringExtra(Constants.INTENT_EXTRA_FILENAME));

            int size = fis.available();
            byte[] buffer = new byte[size];
            fis.read(buffer);
            fis.close();

            data = new JSONObject();
            scoutData = new JSONObject(new String(buffer));
            int code = (int)(Math.random() * 8999 + 1000);

            data.put("data", scoutData);
            data.put("verif", code);

            String matchInfoString = scoutData.getString(Constants.JSON_DATA_MATCHINFO);
            info = MatchInfo.parse(matchInfoString);

            loadUI();
            loadFragments();

        }catch(Exception e){
            data = null;
            e.printStackTrace();
        }

        //initNFC();
    }

    public JSONObject getData(){
        return data;
    }

    public MatchInfo getInfo(){
        return info;
    }

    /* Don't exit when the back button is pressed */
    @Override
    public void onBackPressed() {
        if(dataFragment != null)dataFragment.verify();
    }

    public void loadUI(){
        Toolbar t = (Toolbar)findViewById(R.id.matchdata_toolbar);
        setSupportActionBar(t);
    }

    public void loadFragments() throws JSONException {
        dataFragment = new MatchDataFragment();

        MatchInfoFragment infoFragment = new MatchInfoFragment();
        Bundle b = new Bundle();
        b.putSerializable(Constants.INTENT_EXTRA_MATCHINFO, info);
        b.putInt(Constants.INTENT_EXTRA_SCOUTID, scoutData.getInt(Constants.JSON_DATA_SCOUTID));
        infoFragment.setArguments(b);
        Log.d("MVRT", "Fragments Arguments Passed");

        TabLayout tabs = (TabLayout)findViewById(R.id.matchdata_tablayout);

        FragmentPagerAdapter tabAdapter = new FragmentPagerAdapter(getFragmentManager());
        ViewPager pager = (ViewPager)findViewById(R.id.matchdata_pager);
        pager.setAdapter(tabAdapter);

        tabAdapter.addFragment(new FragmentPagerAdapter.TabFragment(infoFragment, "Match Info"));
        tabAdapter.addFragment(new FragmentPagerAdapter.TabFragment(dataFragment, "Send Match Data"));

        tabs.setupWithViewPager(pager);
    }

    public void initNFC(){
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(nfcAdapter == null){
            Log.d("MVRT", "NFC not available");
            return;
        }

        NdefRecord r = NdefRecord.createMime("application/json", data.toString().getBytes());
        NdefMessage message = new NdefMessage(r);
        nfcAdapter.setNdefPushMessage(message, this);
    }

}

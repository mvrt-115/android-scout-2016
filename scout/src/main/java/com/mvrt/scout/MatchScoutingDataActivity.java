package com.mvrt.scout;

import android.app.Activity;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.mvrt.mvrtlib.util.Constants;
import com.mvrt.mvrtlib.util.FragmentPagerAdapter;
import com.mvrt.mvrtlib.util.MatchInfo;

/**
 * @author Bubby
 * End of match activity to send data to super.
 */
public class MatchScoutingDataActivity extends ActionBarActivity{

    NfcAdapter nfcAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_data);
        loadUI();
        loadFragments();
        initNFC();
    }

    /* Don't exit when the back button is pressed */
    @Override
    public void onBackPressed() {}


    public void loadUI(){
        Toolbar t = (Toolbar)findViewById(R.id.matchdata_toolbar);
        setSupportActionBar(t);
    }

    public void loadFragments(){
        MatchDataFragment f = new MatchDataFragment();
        Bundle b = new Bundle();
        f.setArguments(b);

        TabLayout tabs = (TabLayout)findViewById(R.id.matchdata_tablayout);

        FragmentPagerAdapter tabAdapter = new FragmentPagerAdapter(getFragmentManager());
        ViewPager pager = (ViewPager)findViewById(R.id.matchdata_pager);
        pager.setAdapter(tabAdapter);
        tabAdapter.addFragment(new FragmentPagerAdapter.TabFragment(f, "Send Match Data"));

        tabs.setupWithViewPager(pager);
    }

    public void initNFC(){
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(nfcAdapter == null){
            Toast.makeText(this, "NFC not available", Toast.LENGTH_LONG).show();
            return;
        }

        nfcAdapter.setNdefPushMessage(new NdefMessage(
                NdefRecord.createExternal(
                        "mvrt.com",
                        "matchdata",
                        "Match Data".getBytes())
        ), this);

    }

}

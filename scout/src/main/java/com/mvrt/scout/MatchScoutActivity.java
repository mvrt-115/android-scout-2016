package com.mvrt.scout;

import android.app.Fragment;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.mvrt.mvrtlib.util.Constants;
import com.mvrt.mvrtlib.util.FragmentPagerAdapter;
import com.mvrt.mvrtlib.util.MatchInfo;


public class MatchScoutActivity extends ActionBarActivity {

    MatchInfo matchInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_scout);
        loadIntentData();
        loadUI();
        loadFragments();
    }



    public void loadIntentData(){
        matchInfo = (MatchInfo)getIntent().getSerializableExtra(Constants.INTENT_EXTRA_MATCHINFO);
    }

    public void loadUI(){
        Toolbar t = (Toolbar)findViewById(R.id.matchscout_toolbar);
        setSupportActionBar(t);
    }

    public void loadFragments(){
        MatchInfoFragment f = new MatchInfoFragment();
        StandScoutAutonFragment ssaf = new StandScoutAutonFragment();
        StandScoutTeleopFragment sstf = new StandScoutTeleopFragment();
        StandScoutPostgameFragment sspf = new StandScoutPostgameFragment();
        Bundle b = new Bundle();
        b.putSerializable(Constants.INTENT_EXTRA_MATCHINFO, matchInfo);
        f.setArguments(b);

        TabLayout tabs = (TabLayout)findViewById(R.id.matchscout_tablayout);

        FragmentPagerAdapter tabAdapter = new FragmentPagerAdapter(getFragmentManager());
        ViewPager pager = (ViewPager)findViewById(R.id.matchscout_pager);
        pager.setAdapter(tabAdapter);
        tabAdapter.addFragment(new FragmentPagerAdapter.TabFragment(f, "Match Info"));
        tabAdapter.addFragment(new FragmentPagerAdapter.TabFragment(ssaf, "Auton"));
        tabAdapter.addFragment(new FragmentPagerAdapter.TabFragment(sstf, "Teleop"));
        tabAdapter.addFragment(new FragmentPagerAdapter.TabFragment(sspf, "Post"));

        tabs.setupWithViewPager(pager);
    }

    public void stop(){
        Intent i = new Intent(this, MatchScoutingDataActivity.class);
        startActivity(i);
        finish();
    }

}

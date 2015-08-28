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

import org.json.JSONException;
import org.json.JSONObject;


public class MatchScoutActivity extends ActionBarActivity {

    MatchInfo matchInfo;
    StandScoutAutonFragment ssaf;
    StandScoutTeleopFragment sstf;
    StandScoutPostgameFragment sspf;

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
        ssaf = new StandScoutAutonFragment();
        sstf = new StandScoutTeleopFragment();
        sspf = new StandScoutPostgameFragment();
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
        JSONObject obj = new JSONObject();
        try {
            obj.put("Auton Data", ssaf.getData());
            obj.put("Teleop Data", sstf.getData());
            obj.put("PostGame Data", sspf.getData());
        }catch(JSONException je){
            je.printStackTrace();
        }
        Intent i = new Intent(this, MatchScoutingDataActivity.class);
        i.putExtra("Match Data", obj.toString());
        startActivity(i);
        finish();
    }

}

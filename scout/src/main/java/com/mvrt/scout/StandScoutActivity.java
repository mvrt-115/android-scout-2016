package com.mvrt.scout;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.mvrt.mvrtlib.util.Constants;
import com.mvrt.mvrtlib.util.FragmentPagerAdapter;
import com.mvrt.mvrtlib.util.MatchInfo;
import com.mvrt.mvrtlib.util.Snacker;

import org.json.JSONException;
import org.json.JSONObject;


public class StandScoutActivity extends ActionBarActivity {

    MatchInfo matchInfo;
    int scoutId;

    StandScoutAutonFragment standScoutAutonFragment;
    StandScoutTeleopFragment standScoutTeleopFragment;
    StandScoutPostgameFragment standScoutPostgameFragment;

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
        scoutId = getSharedPreferences(Constants.SHARED_PREFS_NAME_SCOUT, Activity.MODE_PRIVATE).getInt(Constants.PREFS_SCOUTID_KEY, 0);
        Log.d("MVRT", "Scout ID is " + scoutId);
        setTitle(matchInfo.userFriendlySingleTeamString(scoutId));
    }

    public void loadUI(){
        Toolbar t = (Toolbar)findViewById(R.id.matchscout_toolbar);
        setSupportActionBar(t);
    }

    public void loadFragments(){
        MatchInfoFragment matchInfoFragment = new MatchInfoFragment();
        standScoutAutonFragment = new StandScoutAutonFragment();
        standScoutTeleopFragment = new StandScoutTeleopFragment();
        standScoutPostgameFragment = new StandScoutPostgameFragment();

        Bundle b = new Bundle();
        b.putSerializable(Constants.INTENT_EXTRA_MATCHINFO, matchInfo);
        matchInfoFragment.setArguments(b);

        FragmentPagerAdapter tabAdapter = new FragmentPagerAdapter(getFragmentManager());
        tabAdapter.addFragment(new FragmentPagerAdapter.TabFragment(matchInfoFragment, "Match Info"));
        tabAdapter.addFragment(new FragmentPagerAdapter.TabFragment(standScoutAutonFragment, "Auton"));
        tabAdapter.addFragment(new FragmentPagerAdapter.TabFragment(standScoutTeleopFragment, "Teleop"));
        tabAdapter.addFragment(new FragmentPagerAdapter.TabFragment(standScoutPostgameFragment, "Post"));

        ViewPager pager = (ViewPager)findViewById(R.id.matchscout_pager);
        pager.setAdapter(tabAdapter);
        TabLayout tabs = (TabLayout)findViewById(R.id.matchscout_tablayout);
        tabs.setupWithViewPager(pager);
    }

    /* Don't exit when the back button is pressed */
    @Override
    public void onBackPressed() {
        //TODO: PRESS BACK AGAIN TO EXIT
    }

    public void stopScouting(){
        JSONObject obj = new JSONObject();

        ViewPager pager = (ViewPager)findViewById(R.id.matchscout_pager);

        if(!standScoutAutonFragment.validate()){
            pager.setCurrentItem(1);
            Snacker.snack("Please make sure you filled in all of the data", this, Snackbar.LENGTH_LONG);
        }
        else if(!standScoutTeleopFragment.validate()){
            pager.setCurrentItem(2);
            Snacker.snack("Please make sure you filled in all of the data", this, Snackbar.LENGTH_LONG);
        }
        else if(!standScoutPostgameFragment.validate()){
            pager.setCurrentItem(3);
            Snacker.snack("Please make sure you filled in all of the data", this, Snackbar.LENGTH_LONG);
        }
        else {
            try {
                obj.put("Auton Data", standScoutAutonFragment.getData());
                obj.put("Teleop Data", standScoutTeleopFragment.getData());
                obj.put("PostGame Data", standScoutPostgameFragment.getData());
                obj.put("Match Info", matchInfo.singleTeamString(scoutId));
            } catch (JSONException je) {
                je.printStackTrace();
            }
            Intent i = new Intent(this, MatchScoutingDataActivity.class);
            i.putExtra(Constants.INTENT_EXTRA_MATCHDATA, obj.toString());
            startActivity(i);
            finish();
        }
    }

}

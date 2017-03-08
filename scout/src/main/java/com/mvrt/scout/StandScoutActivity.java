package com.mvrt.scout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.mvrt.mvrtlib.util.Constants;
import com.mvrt.mvrtlib.util.FragmentPagerAdapter;
import com.mvrt.mvrtlib.util.JSONUtils;
import com.mvrt.mvrtlib.util.MatchInfo;
import com.mvrt.mvrtlib.util.Snacker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;


public class StandScoutActivity extends AppCompatActivity {

    MatchInfo matchInfo;
    int scoutId;

    DatabaseReference matchReference;

    StandScoutAutonFragment standScoutAutonFragment;
    StandScoutTeleopFragment standScoutTeleopFragment;
    StandScoutPostgameFragment standScoutPostgameFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_scout);
        initFirebase();
        loadIntentData();
        loadUI();
        loadFragments();
    }

    private void initFirebase(){
        matchReference = FirebaseUtils.getDatabase().getReference("matches");
    }

    public void loadIntentData(){
        scoutId = getSharedPreferences(Constants.SHARED_PREFS_NAME_SCOUT, Activity.MODE_PRIVATE).getInt(Constants.PREFS_SCOUTID_KEY, 0);
        matchInfo = (MatchInfo) getIntent().getSerializableExtra(Constants.INTENT_EXTRA_MATCHINFO);
        setTitle(matchInfo.userFriendlyString(scoutId));
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
        b.putInt(Constants.INTENT_EXTRA_SCOUTID, scoutId);
        matchInfoFragment.setArguments(b);

        FragmentPagerAdapter tabAdapter = new FragmentPagerAdapter(getFragmentManager());
        tabAdapter.addFragment(new FragmentPagerAdapter.TabFragment(matchInfoFragment, "Match Info"));
        tabAdapter.addFragment(new FragmentPagerAdapter.TabFragment(standScoutAutonFragment, "Auton"));
        tabAdapter.addFragment(new FragmentPagerAdapter.TabFragment(standScoutTeleopFragment, "Teleop"));
        tabAdapter.addFragment(new FragmentPagerAdapter.TabFragment(standScoutPostgameFragment, "Postgame"));

        ViewPager pager = (ViewPager)findViewById(R.id.matchscout_pager);
        pager.setAdapter(tabAdapter);
        TabLayout tabs = (TabLayout)findViewById(R.id.matchscout_tablayout);
        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabs.setupWithViewPager(pager);
    }

    /* Don't exit when the back button is pressed */
    @Override
    public void onBackPressed() {
        //TODO: PRESS BACK AGAIN TO EXIT
    }

    public void nextTab(){
        ViewPager pager = (ViewPager)findViewById(R.id.matchscout_pager);
        pager.setCurrentItem(pager.getCurrentItem() + 1);
    }

    public void setTab(int tab){
        ViewPager pager = (ViewPager)findViewById(R.id.matchscout_pager);
        pager.setCurrentItem(tab);
    }

    public void stopScouting(){
        JSONObject obj = new JSONObject();

        ViewPager pager = (ViewPager)findViewById(R.id.matchscout_pager);

        if(!standScoutAutonFragment.validate()){
            pager.setCurrentItem(1);
            Snacker.snack("Robots can either reach or cross, not both", this, Snackbar.LENGTH_LONG);
        }
        else if(!standScoutPostgameFragment.validate()){
            pager.setCurrentItem(4);
            Snacker.snack("Please make sure you filled in all of the data", this, Snackbar.LENGTH_LONG);
        }
        else {
            try {
                obj.put(Constants.JSON_DATA_AUTON, standScoutAutonFragment.getData());
                obj.put(Constants.JSON_DATA_TELEOP, standScoutTeleopFragment.getData());
                obj.put(Constants.JSON_DATA_POSTGAME, standScoutPostgameFragment.getData());
                obj.put(Constants.JSON_DATA_MATCHINFO, matchInfo.toString());
                obj.put(Constants.JSON_DATA_SCOUTID, scoutId);

                uploadData(matchInfo, scoutId, obj);

                String filename = writeToFile(obj, matchInfo, scoutId);

                Intent i = new Intent(this, MatchScoutingDataActivity.class);
                i.putExtra(Constants.INTENT_EXTRA_FILENAME, filename);
                startActivity(i);

            } catch (JSONException je) {
                je.printStackTrace();
            }
            Log.d("MVRT", "FINISH");
            finish();
        }
    }

    public MatchInfo getMatchInfo(){
        return matchInfo;
    }

    public String writeToFile(JSONObject data, MatchInfo matchInfo, int scoutId) throws JSONException {
        String filename = matchInfo.getFilename(scoutId);
        try {
            FileOutputStream fos = openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(data.toString().getBytes());
            Snacker.snack("Written to file: " + filename, this, Snackbar.LENGTH_SHORT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filename;
    }

    private void uploadData(MatchInfo info, int scoutId, JSONObject scoutData){
        try{
            matchReference.child(info.toDbKey(scoutId)).updateChildren(JSONUtils.jsonToMap(new JSONObject(scoutData.toString()))).addOnSuccessListener(syncCompleteListener);
        }catch(JSONException e){
            Toast.makeText(this, "Upload JSONException", Toast.LENGTH_SHORT).show();
            Log.e("MVRT", "Upload JSONException");
        }
    }

    OnSuccessListener<Void> syncCompleteListener = new OnSuccessListener<Void>() {

        @Override
        public void onSuccess(Void aVoid) {
            Toast.makeText(getApplicationContext(), "Synced Online!", Toast.LENGTH_LONG).show();
        }

    };

}

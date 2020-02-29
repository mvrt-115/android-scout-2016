package com.mvrt.scout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.mvrt.mvrtlib.util.Constants;
import com.mvrt.mvrtlib.util.FragmentPagerAdapter;
import com.mvrt.mvrtlib.util.JSONUtils;
import com.mvrt.mvrtlib.util.MatchInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class StandScoutActivity extends AppCompatActivity {

    MatchInfo matchInfo;
    int scoutId;

    StandScoutAutonFragment standScoutAutonFragment;
    StandScoutTeleopFragment standScoutTeleopFragment;
    StandScoutPostgameFragment standScoutPostgameFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stand_scout);
        loadIntentData();
        loadUI();
        loadFragments();
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
        tabAdapter.addFragment(new FragmentPagerAdapter.TabFragment(standScoutAutonFragment, "Sandstorm"));
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
            Toast.makeText(getApplicationContext(), "Robots can either reach or cross, not both", Toast.LENGTH_LONG).show();
        }
        else if(!standScoutPostgameFragment.validate()){
            pager.setCurrentItem(4);
            Toast.makeText(getApplicationContext(), "Please make sure you filled in all of the data", Toast.LENGTH_LONG).show();
        }
        else {
            try {

                obj.put("matchNum", matchInfo.getMatchNo());
                obj.put("teamNum", matchInfo.getTeam(scoutId));
                obj = standScoutAutonFragment.getData(obj);
                obj = standScoutTeleopFragment.getData(obj);
                obj = standScoutPostgameFragment.getData(obj);
                obj.put("comments", "");
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
            Toast.makeText(getApplicationContext(), "Written to file: " + filename, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filename;
    }

    private void uploadData(MatchInfo matchInfo, int scoutId, JSONObject scoutData){
        try{
            final JSONObject json = scoutData;
            final int id = scoutId;
            final MatchInfo info = matchInfo;
            new Thread(){
                public void run(){
                    try {
                        Log.e("JSON", json.toString());
                        URL url = new URL(Constants.ScoutUrl);
                        String data = json.toString();
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

                        Toast feed = Toast.makeText(getApplicationContext(),
                                "Sent Data.",
                                Toast.LENGTH_SHORT);
                        feed.show();
                    } catch(Exception e) {
                        Log.e("response", e.toString());
                    }
                }
            }.start();
        }catch(Exception e){
            Toast.makeText(this, "Upload Exception. Data not sent", Toast.LENGTH_SHORT).show();
            Log.e("MVRT", e.toString());
        }
    }

    OnSuccessListener<Void> syncCompleteListener = new OnSuccessListener<Void>() {

        @Override
        public void onSuccess(Void aVoid) {
            Toast.makeText(getApplicationContext(), "Synced Online!", Toast.LENGTH_LONG).show();
        }

    };

}

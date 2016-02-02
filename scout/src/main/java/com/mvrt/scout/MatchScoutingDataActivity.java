package com.mvrt.scout;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.mvrt.mvrtlib.util.Constants;
import com.mvrt.mvrtlib.util.FragmentPagerAdapter;
import com.mvrt.mvrtlib.util.MatchInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;

/**
 * @author Bubby
 * End of match activity to send data to super.
 */
public class MatchScoutingDataActivity extends AppCompatActivity{

    private NfcAdapter nfcAdapter;
    private JSONObject data;
    private MatchInfo info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_data);

        String filename = getIntent().getStringExtra(Constants.INTENT_EXTRA_FILENAME);
        Log.d("MVRT", "filename: " + filename);

        try{
            FileInputStream fis = openFileInput(getIntent().getStringExtra(Constants.INTENT_EXTRA_FILENAME));

            int size = fis.available();
            byte[] buffer = new byte[size];
            fis.read(buffer);
            fis.close();
            data = new JSONObject(new String(buffer));
            Log.d("MVRT", data.toString());

            String matchInfoString = data.getString(Constants.JSON_DATA_MATCHINFO);
            Log.d("MVRT", "matchInfoString: " + matchInfoString);


            info = MatchInfo.parse(matchInfoString);
            Log.d("MVRT", "info created");

            Log.d("MVRT", info.userFriendlyString());

            loadUI();
            loadFragments();

        }catch(Exception e){
            data = null;
            e.printStackTrace();
            Log.e("MVRT", "Data is null");
        }

        initNFC();
    }

    public JSONObject getData(){
        return data;
    }

    public MatchInfo getInfo(){
        return info;
    }

    /* Don't exit when the back button is pressed */
    @Override
    public void onBackPressed() {}

    public void loadUI(){
        Toolbar t = (Toolbar)findViewById(R.id.matchdata_toolbar);
        setSupportActionBar(t);
    }

    public void loadFragments() throws JSONException{
        MatchDataFragment dataFragment = new MatchDataFragment();

        MatchInfoFragment infoFragment = new MatchInfoFragment();
        Bundle b = new Bundle();
        b.putSerializable(Constants.INTENT_EXTRA_MATCHINFO, info);
        b.putInt(Constants.INTENT_EXTRA_SCOUTID, data.getInt(Constants.JSON_DATA_SCOUTID));
        infoFragment.setArguments(b);

        TabLayout tabs = (TabLayout)findViewById(R.id.matchdata_tablayout);

        FragmentPagerAdapter tabAdapter = new FragmentPagerAdapter(getFragmentManager());
        ViewPager pager = (ViewPager)findViewById(R.id.matchdata_pager);
        pager.setAdapter(tabAdapter);

        tabAdapter.addFragment(new FragmentPagerAdapter.TabFragment(infoFragment, "Match Info"));
        tabAdapter.addFragment(new FragmentPagerAdapter.TabFragment(dataFragment, "Send Match Data"));

        tabs.setupWithViewPager(pager);
    }

    public void initNFC(){
        if(data == null){
            Log.d("MVRT", "data not working");
            return;
        }
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(nfcAdapter == null){
            Log.d("MVRT", "NFC not available");
            return;
        }

        nfcAdapter.setNdefPushMessage(new NdefMessage(
                NdefRecord.createExternal(
                        "mvrt.com",
                        "matchdata",
                        data.toString().getBytes())
        ), this);

    }

}

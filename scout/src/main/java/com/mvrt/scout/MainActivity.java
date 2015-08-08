package com.mvrt.scout;

import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.nfc.NfcManager;
import android.nfc.Tag;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mvrt.mvrtlib.util.Constants;
import com.mvrt.mvrtlib.util.MatchInfo;

public class MainActivity extends ActionBarActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navView;
    FrameLayout contentView;

    Toolbar toolbar;

    StartMatchFragment startMatchFragment;
    SettingsFragment settingsFragment;

    IntentFilter[] intentFilters;
    String[][] techLists;
    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        startMatchFragment = new StartMatchFragment();
        settingsFragment = new SettingsFragment();

        toolbar = (Toolbar)findViewById(R.id.mainactivity_toolbar);
        setSupportActionBar(toolbar);

        setupNavDrawer();
        initNFC();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            processIntent(getIntent());
        }
    }

    public void onNewIntent(Intent intent) {

        Log.d("com.mvrt.scout", "intent from nfc");
        setIntent(intent);
    }

    void processIntent(Intent intent) {
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage msg = (NdefMessage) rawMsgs[0];
        startScouting(MatchInfo.parse(new String(msg.getRecords()[0].getPayload())));
    }


    private void setupNavDrawer(){
        contentView = (FrameLayout)findViewById(R.id.mainactivity_framelayout);
        drawerLayout = (DrawerLayout)findViewById(R.id.mainactivity_drawerlayout);
        navView = (NavigationView)findViewById(R.id.mainactivity_navview);
        navView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        setVisibleFragment(startMatchFragment);

    }

    private void initNFC(){
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(nfcAdapter == null){
            snackBar("NFC not available", Snackbar.LENGTH_SHORT);
            finish();
            return;
        }

        pendingIntent = PendingIntent.getActivity(this, 0,
                        new Intent(this, MatchScoutActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    }

    public void startScouting(MatchInfo match){
        if(match == null) {
            snackBar("Invalid match info", Snackbar.LENGTH_SHORT);
            return;
        }
        Intent i = new Intent(this, MatchScoutActivity.class);
        i.putExtra(Constants.INTENT_EXTRA_MATCHINFO, match);
        startActivity(i);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        menuItem.setChecked(true);
        switch (menuItem.getItemId()){
            case R.id.draweritem_start_scouting:
                setVisibleFragment(startMatchFragment);
                break;
            case R.id.draweritem_settings:
                setVisibleFragment(settingsFragment);
                break;
            default:
                setVisibleFragment(startMatchFragment);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setVisibleFragment(Fragment newFrag){
        getFragmentManager().beginTransaction().
                replace(R.id.mainactivity_framelayout, newFrag).commit();
    }

    public void snackBar(String text, int length){
        View view = contentView.getChildAt(0);
        Snackbar b = Snackbar.make(view, text, length);
        ((TextView)b.getView().findViewById(android.support.design.R.id.snackbar_text)).setTextColor(getResources().getColor(R.color.text_primary_light));
        b.show();
    }


}

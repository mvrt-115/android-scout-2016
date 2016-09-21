package com.mvrt.scout;

import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.mvrt.mvrtlib.util.MatchInfo;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navView;
    FrameLayout contentView;

    Toolbar toolbar;

    StartMatchFragment startMatchFragment;
    LocalDataFragment localDataFragment;
    SettingsFragment settingsFragment;

    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    IntentFilter[] intentFilters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        startMatchFragment = new StartMatchFragment();
        localDataFragment = new LocalDataFragment();
        settingsFragment = new SettingsFragment();

        toolbar = (Toolbar)findViewById(R.id.mainactivity_toolbar);
        setSupportActionBar(toolbar);
        setupNavDrawer();

        initFirebase();
        initNFC();
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

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        menuItem.setChecked(true);
        switch (menuItem.getItemId()){
            case R.id.draweritem_start_scouting:
                setVisibleFragment(startMatchFragment);
                break;
            case R.id.draweritem_localsave:
                setVisibleFragment(localDataFragment);
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

    private void setVisibleFragment(Fragment newFrag) {
        getFragmentManager().beginTransaction().
                replace(R.id.mainactivity_framelayout, newFrag).commit();
    }

    public void initNFC(){
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(nfcAdapter == null){
            Toast.makeText(this, "NFC not available", Toast.LENGTH_SHORT).show();
            return;
        }

        pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class)
                        .addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING), 0);

        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndef.addDataType("text/mvrt");
        } catch (IntentFilter.MalformedMimeTypeException e) { e.printStackTrace(); }
        intentFilters = new IntentFilter[]{ndef};
    }

    private void initFirebase() {
        final FirebaseRemoteConfig mRemoteConfig = FirebaseRemoteConfig.getInstance();

        mRemoteConfig.setDefaults(R.xml.remote_config_defaults);
        mRemoteConfig.fetch().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mRemoteConfig.activateFetched();
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())){
            getIntent().setAction(Intent.ACTION_DEFAULT);
            startScoutingNFC();
        }
        if(nfcAdapter != null) nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);
        super.onResume();
    }

    @Override
    protected void onPause() {
        nfcAdapter.disableForegroundDispatch(this);
        super.onPause();
    }

    public void startScoutingNFC() {
        Parcelable[] rawMsgs = getIntent().getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage msg = (NdefMessage) rawMsgs[0];
        String matchInfoString = new String(msg.getRecords()[0].getPayload());
        MatchInfo matchInfo = MatchInfo.parse(matchInfoString);
        startMatchFragment.startScouting(matchInfo);
    }
}

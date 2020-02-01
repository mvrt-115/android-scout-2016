package com.mvrt.superscout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Fragment;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    StartMatchFragment startMatchFragment;
    SettingsFragment settingsFragment;

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navView;
    FrameLayout contentView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startMatchFragment = new StartMatchFragment();
        settingsFragment = new SettingsFragment();

        initFirebaseRemoteConfig();
        setupUI();
    }

    private void initFirebaseRemoteConfig() {
        /*
        final FirebaseRemoteConfig mRemoteConfig = FirebaseRemoteConfig.getInstance();

        mRemoteConfig.setDefaults(R.xml.remote_config_defaults);
        mRemoteConfig.fetch().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mRemoteConfig.activateFetched();
            }
        });
         */
    }

    private void setupUI() {
        toolbar = (Toolbar)findViewById(R.id.mainactivity_toolbar);
        //etSupportActionBar(toolbar);

        contentView = (FrameLayout) findViewById(R.id.mainactivity_framelayout);
        drawerLayout = (DrawerLayout) findViewById(R.id.mainactivity_drawerlayout);
        navView = (NavigationView) findViewById(R.id.mainactivity_navview);
        navView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        setVisibleFragment(startMatchFragment);

    }


    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        menuItem.setChecked(true);
        switch (menuItem.getItemId()) {
            case R.id.draweritem_start_scouting:
                setVisibleFragment(startMatchFragment);
                break;
            case R.id.draweritem_settings:
                setVisibleFragment(settingsFragment);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setVisibleFragment(Fragment newFrag) {
        getFragmentManager().beginTransaction().
                replace(R.id.mainactivity_framelayout, newFrag).commit();
    }

}

package com.mvrt.superscout;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public class MainActivity extends ActionBarActivity implements NavigationView.OnNavigationItemSelectedListener {

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

        setupUI();
    }

    private void setupUI() {
        toolbar = (Toolbar)findViewById(R.id.mainactivity_toolbar);
        setSupportActionBar(toolbar);

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

package com.mvrt.superscout;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mvrt.mvrtlib.util.Constants;
import com.mvrt.mvrtlib.util.MatchInfo;

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

        toolbar = (Toolbar)findViewById(R.id.mainactivity_toolbar);
        setSupportActionBar(toolbar);

        setupNavDrawer();
    }

    private void setupNavDrawer() {
        contentView = (FrameLayout) findViewById(R.id.mainactivity_framelayout);
        drawerLayout = (DrawerLayout) findViewById(R.id.mainactivity_drawerlayout);
        navView = (NavigationView) findViewById(R.id.mainactivity_navview);
        navView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        setVisibleFragment(startMatchFragment);

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

    public void snackBar(String text, int length){
        View view = contentView.getChildAt(0);
        Snackbar b = Snackbar.make(view, text, length);
        ((TextView)b.getView().findViewById(android.support.design.R.id.snackbar_text)).setTextColor(getResources().getColor(R.color.text_primary_light));
        b.show();
    }


}

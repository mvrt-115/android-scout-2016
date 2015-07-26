package com.mvrt.scout;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.mvrt.mvrtlib.nav.drawer.NavDrawer;

public class MainActivity extends ActionBarActivity {

    NavDrawer navDrawer;
    StartMatchFragment startMatchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startMatchFragment = new StartMatchFragment();

        navDrawer = new NavDrawer(this, startMatchFragment);

        setContentView(navDrawer.getParentView());

        Toolbar toolbar = (Toolbar)findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);

        navDrawer.setupToggle(toolbar);
    }

}

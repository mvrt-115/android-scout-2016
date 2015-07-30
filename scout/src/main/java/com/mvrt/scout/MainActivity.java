package com.mvrt.scout;

import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.mvrt.mvrtlib.nav.drawer.DrawerFragment;
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

    public void scanQR(View view){
        try {
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes
            startActivityForResult(intent, 0);
        } catch (Exception e) {
            Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
            Intent marketIntent = new Intent(Intent.ACTION_VIEW,marketUri);
            startActivity(marketIntent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 0){
            if(resultCode == RESULT_OK){
                startActivity(data.setClass(this, MatchScoutActivity.class));
            }
        }
    }
}

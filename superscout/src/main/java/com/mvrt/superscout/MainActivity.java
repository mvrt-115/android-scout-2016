package com.mvrt.superscout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.mvrt.mvrtlib.nav.drawer.NavDrawer;
import com.mvrt.mvrtlib.util.MatchInfo;
import com.zxing.Contents;
import com.zxing.QRCodeEncoder;

public class MainActivity extends ActionBarActivity {

    DeployMatchFragment deployMatchFragment;

    NavDrawer navDrawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        deployMatchFragment = new DeployMatchFragment();

        navDrawer = new NavDrawer(this, deployMatchFragment);

        setContentView(navDrawer.getParentView());

        Toolbar toolbar = (Toolbar)findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);

        navDrawer.setupToggle(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startMatchScoutActivity(View v){
        Intent startMatch = new Intent(this, MatchScoutActivity.class);
        startMatch.putExtra("Match Info", getQRInfo());
        startMatch.setClass(this, MatchScoutActivity.class);
        startActivity(startMatch);
    }


    public MatchInfo getQRInfo() {
        EditText matchData = (EditText) findViewById(R.id.match_data);
        EditText t1 = (EditText) findViewById(R.id.team_one);
        EditText t2 = (EditText) findViewById(R.id.team_two);
        EditText t3 = (EditText) findViewById(R.id.team_three);
        EditText alliance = (EditText) findViewById(R.id.alliance);

        String match = matchData.getText().toString();

        String tournament = match.substring(0, match.indexOf("q"));
        String matchNo = match.substring(match.indexOf("q"), match.length());
        String team1 = t1.getText().toString();
        String team2 = t2.getText().toString();
        String team3 = t3.getText().toString();
        String side = "";
        if (alliance.getText().toString().equalsIgnoreCase("blue")) {
            side = "b";
        } else if (alliance.getText().toString().equalsIgnoreCase("red")) {
            side = "r";
        }

        String data = matchNo + "@" + tournament + ":" + side + "[" + team1 + "," + team2 + "," + team3 + "]";

        return MatchInfo.parse(data);
    }
}

package com.mvrt.scout;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import com.mvrt.mvrtlib.util.Constants;
import com.mvrt.mvrtlib.util.MatchInfo;


public class MatchScoutActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_scout);
        loadIntentData();
    }

    int team;
    String matchKey;
    char alliance;

    public void loadIntentData(){
        MatchInfo info = (MatchInfo)getIntent().getSerializableExtra(Constants.INTENT_EXTRA_MATCHINFO);
        Toast.makeText(this, "Result: " + info, Toast.LENGTH_LONG).show();

    }

}

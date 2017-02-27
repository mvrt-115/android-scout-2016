package com.mvrt.scout;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.mvrt.mvrtlib.util.Constants;
import com.mvrt.mvrtlib.util.MatchInfo;
import com.mvrt.mvrtlib.util.Snacker;

public class StartMatchFragment extends Fragment implements View.OnClickListener{

    EditText matchText;
    EditText teamText;
    Spinner alliance;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_start_match, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        ImageButton qr = (ImageButton)view.findViewById(R.id.startmatch_qrbutton);
        qr.setOnClickListener(this);
        matchText = (EditText)view.findViewById(R.id.startmatch_matchno);
        teamText = (EditText)view.findViewById(R.id.startmatch_team);
        alliance = (Spinner)view.findViewById(R.id.startmatch_alliance);

        CharSequence[] allianceArray = {"Red Alliance", "Blue Alliance"};
        ArrayAdapter<CharSequence> alliances = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_dropdown_item, allianceArray);
        alliance.setAdapter(alliances);

        FloatingActionButton fab = (FloatingActionButton) getView().findViewById(R.id.startmatch_fab_start);
        fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startmatch_qrbutton:
                scanQR();
                break;
            case R.id.startmatch_fab_start:
                startManual();
                break;
        }
    }


    public void startManual(){
        char alliance = Constants.ALLIANCE_BLUE;
        if(this.alliance.getSelectedItem().equals("Red Alliance"))alliance = Constants.ALLIANCE_RED;

        if(matchText.getText().length() == 0){
            matchText.setError("Please enter a match number"); return;
        }else matchText.setError(null);
        int match = Integer.parseInt(matchText.getText().toString());

        if(teamText.getText().length() == 0){
            teamText.setError("Please enter a team number"); return;
        }else teamText.setError(null);
        int team = Integer.parseInt(teamText.getText().toString());

        FirebaseRemoteConfig mRemoteConfig = FirebaseRemoteConfig.getInstance();
        String tourn = mRemoteConfig.getString(Constants.FBCONFIG_TOURN_KEY);

        MatchInfo mInfo = new MatchInfo(match, tourn, alliance, team);
        startScouting(mInfo);
    }

    public void startScouting(MatchInfo match){
        if(match == null) {
            Snacker.snack("Invalid match info", getActivity(), Snackbar.LENGTH_SHORT);
            return;
        }
        Intent i = new Intent(getActivity(), StandScoutActivity.class);
        i.putExtra(Constants.INTENT_EXTRA_MATCHINFO, match);
        i.setAction(Constants.INTENT_ACTION_SCOUT);
        startActivity(i);
    }

    public void scanQR(){
        try {
            Intent intent = new Intent(Constants.INTENT_QR);
            intent.putExtra(Constants.INTENT_QR_SCANMODE_KEY, Constants.INTENT_QR_SCANMODE); // "PRODUCT_MODE for bar codes
            startActivityForResult(intent, Constants.REQUEST_QR_SCAN);
        } catch (Exception e) {
            Snackbar.make(getView(), "Error launching QR scanner", Snackbar.LENGTH_LONG);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Constants.REQUEST_QR_SCAN){
            if(resultCode == Activity.RESULT_OK){
                String result = data.getStringExtra(Constants.INTENT_QR_SCANRESULT_KEY);
                startScouting(MatchInfo.parse(result));
            } else Snacker.snack("Error getting QR data", getActivity(), Snackbar.LENGTH_LONG);
        }
    }

}

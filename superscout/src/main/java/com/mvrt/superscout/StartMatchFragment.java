package com.mvrt.superscout;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.mvrt.mvrtlib.util.Constants;
import com.mvrt.mvrtlib.util.MatchInfo;


public class StartMatchFragment extends Fragment implements View.OnClickListener {

    TextView settingsView;
    TextView[] teamViews;
    TextView matchNo;

    char alliance;
    String tournament;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_start_match, container, false);
    }

    @Override
    public void onStart(){
        super.onStart();
        loadAllianceAndTournament();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        settingsView = (TextView) view.findViewById(R.id.startmatch_settingsindicator);
        teamViews = new TextView[3];
        teamViews[0] = (TextView) view.findViewById(R.id.startmatch_team1);
        teamViews[1] = (TextView) view.findViewById(R.id.startmatch_team2);
        teamViews[2] = (TextView) view.findViewById(R.id.startmatch_team3);
        matchNo = (TextView) view.findViewById(R.id.startmatch_matchno);
        view.findViewById(R.id.startmatch_fab_start).setOnClickListener(this);
    }

    public void loadAllianceAndTournament(){
        SharedPreferences prefs = getActivity().getSharedPreferences(Constants.SHARED_PREFS_NAME_SUPER, Context.MODE_PRIVATE);

        tournament = Constants.TOURNAMENT_KEY;

        alliance = (char)prefs.getInt(Constants.PREFS_ALLIANCE_KEY, (int)Constants.ALLIANCE_BLUE);
        settingsView.setText(MatchInfo.getAllianceString(alliance) + " @ " + tournament);
        settingsView.setTextColor(getResources().getColor(MatchInfo.getAllianceColorId(alliance)));
    }

    public void startManual() {
        loadAllianceAndTournament();

        if(matchNo.getText().length() == 0){
            matchNo.setError("Please enter a match number"); return;
        }else matchNo.setError(null);
        int match = Integer.parseInt(matchNo.getText().toString());

        int[] teams = new int[3];
        for(int i = 0; i < 3; i++) {
            TextView teamText = teamViews[i];
            if(teamText.getText().length() == 0){
                teamText.setError("Please enter a team number"); return;
            }else teamText.setError(null);
            teams[i] = Integer.parseInt(teamText.getText().toString());
        }

        startScouting(new MatchInfo(match, tournament, alliance, teams));
    }

    public void startScouting(MatchInfo match){
        if(match == null) {
            Toast.makeText(getActivity().getApplicationContext(), "Invalid match info", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent i = new Intent(getActivity(), SuperScoutActivity.class);
        i.putExtra(Constants.INTENT_EXTRA_MATCHINFO, match);
        startActivity(i);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.startmatch_fab_start:
                startManual();
                break;
        }
    }

}

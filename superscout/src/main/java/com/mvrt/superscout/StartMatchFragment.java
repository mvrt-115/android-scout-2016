package com.mvrt.superscout;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.mvrt.mvrtlib.util.Constants;
import com.mvrt.mvrtlib.util.MatchInfo;
import com.mvrt.mvrtlib.util.Snacker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bubby and Akhil
 */
public class StartMatchFragment extends Fragment implements View.OnClickListener {

    TextView settingsView;
    TextView[] teamViews;
    TextView matchNo;

    Button loadTeams;

    char alliance;
    String tournament;

    String tbaUrl = "https://www.thebluealliance.com/api/v3/match/";

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
        loadTeams = (Button) view.findViewById(R.id.load_teams);
        loadTeams.setOnClickListener(this);
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
            Snacker.snack("Invalid match info", getActivity(), Snackbar.LENGTH_SHORT);
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
            case R.id.load_teams:
                loadAllianceAndTournament();
                RequestQueue queue = Volley.newRequestQueue(getContext());
                String queryUrl = tbaUrl + "2018" + tournament.toLowerCase() + "_" + "qm" + matchNo.getText() + "?X-TBA-Auth-Key=znzJkTVulz6gKfK1tyM1n246EoofFAbTMg94MrDkeSsMF1QEwIHVwNOs1gi9bSuJ";

                StringRequest jsonRequest = new StringRequest(Request.Method.GET, queryUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("super", response.toString());
                                JSONObject matchAlliance;
                                try {
                                    JSONObject responses = new JSONObject(response);
                                   matchAlliance = responses.getJSONObject("alliances").getJSONObject(alliance == 'b' ? "blue" : "red");
                                   JSONArray teamList = matchAlliance.getJSONArray("team_keys");
                                   for(int i=0; i<3; i++) {
                                       Log.d("super", "TEAM LIST: " + teamList.get(i).toString());
                                       teamViews[i].setText(teamList.get(i).toString().substring(3));
                                   }
                                } catch(Exception e) {

                                }
                            }
                        }, new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        Log.d("super", error.toString());
                    }
                });
                queue.add(jsonRequest);
        }
    }

}

package com.mvrt.superscout;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mvrt.mvrtlib.util.Constants;
import com.mvrt.mvrtlib.util.DefenseManager;
import com.mvrt.mvrtlib.util.DefenseSelectorDialogFragment;
import com.mvrt.mvrtlib.util.MatchInfo;
import com.mvrt.mvrtlib.util.Snacker;

/**
 * @author Bubby and Akhil
 */
public class StartMatchFragment extends Fragment implements View.OnClickListener, DefenseSelectorDialogFragment.DefenseSelectedListener {

    TextView settingsView;
    TextView[] teamViews;
    TextView matchNo;

    DefenseSelectorDialogFragment defenseSelectorDialogFragment;
    ImageView[] defenseViews;
    Button editDefenses;

    char alliance;
    String tournament;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_start_match, container, false);
    }

    private void initDefenseSelectors(View view){
        defenseSelectorDialogFragment = new DefenseSelectorDialogFragment();
        defenseSelectorDialogFragment.setListener(this);

        editDefenses = (Button)view.findViewById(R.id.startmatch_editdefenses);
        editDefenses.setOnClickListener(this);

        defenseViews = new ImageView[4];
        defenseViews[0] = (ImageView)view.findViewById(R.id.startmatch_defense1);
        defenseViews[1] = (ImageView)view.findViewById(R.id.startmatch_defense2);
        defenseViews[2] = (ImageView)view.findViewById(R.id.startmatch_defense3);
        defenseViews[3] = (ImageView)view.findViewById(R.id.startmatch_defense4);
        refreshDefenseViews();
    }

    public void selectDefenses(){
        defenseSelectorDialogFragment.show(getFragmentManager(), "MVRT");
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

        initDefenseSelectors(view);
    }

    public void loadAllianceAndTournament(){
        SharedPreferences prefs = getActivity().getSharedPreferences(Constants.SHARED_PREFS_NAME_SUPER, Context.MODE_PRIVATE);

        tournament = prefs.getString(Constants.PREFS_TOURNAMENT_KEY, Constants.PREFS_TOURNAMENT_DEFAULT).toUpperCase();
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

        startScouting(new MatchInfo(match, tournament, alliance, teams, defenseSelectorDialogFragment.getSelectedDefenses()));
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
            case R.id.startmatch_editdefenses:
                selectDefenses();
                break;
        }
    }

    @Override
    public void onDefenseSelected() {
        refreshDefenseViews();
    }

    private void refreshDefenseViews(){
        String[] defenses = defenseSelectorDialogFragment.getSelectedDefenses();
        for(int i = 0; i < defenses.length; i++){
            defenseViews[i].setImageDrawable(DefenseManager.getDrawable(getActivity(), defenses[i]));
        }
    }
}

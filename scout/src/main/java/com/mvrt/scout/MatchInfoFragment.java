package com.mvrt.scout;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mvrt.mvrtlib.util.Constants;
import com.mvrt.mvrtlib.util.MatchInfo;

public class MatchInfoFragment extends Fragment{

    MatchInfo matchInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_matchinfo, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        loadData(view);
    }

    public void loadData(View v){
        matchInfo = (MatchInfo)getArguments().getSerializable(Constants.INTENT_EXTRA_MATCHINFO);

        int scoutId = getActivity().getSharedPreferences(Constants.SHARED_PREFS_NAME_SCOUT, Activity.MODE_PRIVATE).getInt(Constants.PREFS_SCOUTID_KEY, 0);

        TextView alliance = (TextView)v.findViewById(R.id.matchinfo_alliance);
        alliance.setText(matchInfo.getAllianceString());
        alliance.setTextColor(getResources().getColor(matchInfo.getAllianceColorId()));

        TextView matchKey = (TextView)v.findViewById(R.id.matchinfo_matchkey);
        matchKey.setText(matchInfo.getMatchNo() + " @ " + matchInfo.getTournament());

        TextView teams = (TextView)v.findViewById(R.id.matchinfo_teams);
        teams.setText("Team " + matchInfo.getTeam(scoutId));

        TextView key = (TextView)v.findViewById(R.id.matchinfo_key);
        key.setText(matchInfo.toString());
    }

}

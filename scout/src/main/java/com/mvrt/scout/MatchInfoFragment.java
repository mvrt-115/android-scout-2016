package com.mvrt.scout;

import android.os.Bundle;

import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mvrt.mvrtlib.util.Constants;
import com.mvrt.mvrtlib.util.MatchInfo;


public class MatchInfoFragment extends Fragment implements View.OnClickListener{

    MatchInfo matchInfo;

    //Button startMatch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_match_info, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        loadData(view);
    }

    public void loadData(View v){
        matchInfo = (MatchInfo)getArguments().getSerializable(Constants.INTENT_EXTRA_MATCHINFO);
        int scoutId = getArguments().getInt(Constants.INTENT_EXTRA_SCOUTID);

        TextView alliance = (TextView)v.findViewById(R.id.matchinfo_alliance);
        alliance.setText(matchInfo.getAllianceString());
        alliance.setTextColor(getResources().getColor(matchInfo.getAllianceColorId()));

        TextView matchKey = (TextView)v.findViewById(R.id.matchinfo_matchkey);
        matchKey.setText(matchInfo.getMatchNo() + " @ " + matchInfo.getTournament());

        TextView teams = (TextView)v.findViewById(R.id.matchinfo_teams);
        teams.setText("Team " + matchInfo.getTeams()[scoutId]);

        TextView key = (TextView)v.findViewById(R.id.matchinfo_key);
        key.setText(matchInfo.toString());

        /*
        startMatch = (Button)v.findViewById(R.id.start_match);
        startMatch.setOnClickListener(this);
         */
    }

    public void onClick(View v)
    {
        /*
        switch(v.getId())
        {
            case R.id.start_match:
                ((StandScoutActivity)getActivity()).nextTab();
                break;
            break;
        }
         */
    }
}

package com.mvrt.scout;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mvrt.mvrtlib.util.Constants;
import com.mvrt.mvrtlib.util.MatchInfo;

public class MatchInfoFragment extends Fragment implements View.OnClickListener {

    MatchInfo matchInfo;
    Button startMatch;

    int buttonID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_matchinfo, container, false);
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState){
        loadData(v);
        startMatch = (Button) v.findViewById(R.id.bt_info_start);

        if(buttonID==1) {
            startMatch.setVisibility(View.GONE);
        }

        else {
            startMatch.setOnClickListener(this);
        }
    }

    public void onClick(View v) {
        ((StandScoutActivity)getActivity()).nextTab();
        Log.d("com.mvrt.scout", "MATCH INFO FINISHED");
    }

    public void loadData(View v){
        matchInfo = (MatchInfo)getArguments().getSerializable(Constants.INTENT_EXTRA_MATCHINFO);
        int scoutId = getArguments().getInt(Constants.INTENT_EXTRA_SCOUTID);
        buttonID = getArguments().getInt(Constants.INTENT_EXTRA_BUTTONID);

        TextView alliance = (TextView)v.findViewById(R.id.matchinfo_alliance);
        alliance.setText(matchInfo.getAllianceString());
        alliance.setTextColor(getResources().getColor(matchInfo.getAllianceColorId()));

        TextView matchKey = (TextView)v.findViewById(R.id.matchinfo_matchkey);
        matchKey.setText(matchInfo.getMatchNo() + " @ " + matchInfo.getTournament());

        TextView teams = (TextView)v.findViewById(R.id.matchinfo_teams);
        teams.setText("Team " + matchInfo.getTeams()[scoutId]);

        TextView key = (TextView)v.findViewById(R.id.matchinfo_key);
        key.setText(matchInfo.toString());
    }

}

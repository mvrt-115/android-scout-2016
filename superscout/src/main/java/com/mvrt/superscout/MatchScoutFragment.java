package com.mvrt.superscout;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mvrt.mvrtlib.util.Constants;
import com.mvrt.mvrtlib.util.MatchInfo;

/**
 * @author Bubby
 */
public class MatchScoutFragment extends Fragment {

    MatchInfo matchInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_match_scout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        loadData(view);
        Button end = (Button)view.findViewById(R.id.done_scouting);
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MatchScoutActivity)getActivity()).sendSuperData();
            }
        });
    }

    public void loadData(View v){
        matchInfo = (MatchInfo)getArguments().getSerializable(Constants.INTENT_EXTRA_MATCHINFO);

        TextView team1 = (TextView)v.findViewById(R.id.team1);
        team1.setText("Team: " + matchInfo.getTeam(0));
        team1.setTextColor(getResources().getColor(matchInfo.getAllianceColorId()));

        TextView team2 = (TextView)v.findViewById(R.id.team2);
        team2.setText("Team: " + matchInfo.getTeam(1));

        TextView team3 = (TextView)v.findViewById(R.id.team3);
        team3.setText("Team: " + matchInfo.getTeam(2));
    }

    public String getTeam1(View v){
        EditText comments = (EditText)v.findViewById(R.id.team1_comments);
        return comments.getText().toString();
    }

    public String getTeam2(View v){
        EditText comments = (EditText)v.findViewById(R.id.team2_comments);
        return comments.getText().toString();
    }

    public String getTeam3(View v){
        EditText comments = (EditText)v.findViewById(R.id.team3_comments);
        return comments.getText().toString();
    }


}

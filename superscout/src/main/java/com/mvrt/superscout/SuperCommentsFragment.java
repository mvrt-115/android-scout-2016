package com.mvrt.superscout;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.mvrt.mvrtlib.util.Constants;
import com.mvrt.mvrtlib.util.MatchInfo;


public class SuperCommentsFragment extends Fragment {

    MatchInfo matchInfo;

    EditText t1;
    EditText t2;
    EditText t3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_super_comments, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        loadData(view);
        t1 = (EditText)view.findViewById(R.id.team1_comments);
        t2 = (EditText)view.findViewById(R.id.team2_comments);
        t3 = (EditText)view.findViewById(R.id.team3_comments);
        Log.d("MVRT", "SuperCommentFragment OnViewCreated");
    }

    public void loadData(View v){
        matchInfo = (MatchInfo)getArguments().getSerializable(Constants.INTENT_EXTRA_MATCHINFO);

        TextView team1 = (TextView)v.findViewById(R.id.team1);
        team1.setText("Team: " + matchInfo.getTeams()[0]);
        team1.setTextColor(getResources().getColor(matchInfo.getAllianceColorId()));

        TextView team2 = (TextView)v.findViewById(R.id.team2);
        team2.setText("Team: " + matchInfo.getTeams()[1]);

        TextView team3 = (TextView)v.findViewById(R.id.team3);
        team3.setText("Team: " + matchInfo.getTeams()[2]);
    }

    public String getTeam1(){
        return t1.getText().toString();
    }

    public String getTeam2(){
        return t2.getText().toString();
    }

    public String getTeam3(){
        return t3.getText().toString();
    }


}

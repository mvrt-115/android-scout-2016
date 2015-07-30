package com.mvrt.mvrtlib.util;

import android.widget.EditText;

import java.io.Serializable;

/**
 * Created by Samster on 7/29/2015.
 */
public class MatchInfo implements Serializable {

    String matchNo;
    String tournament;
    char alliance;
    int[] teams;

    public MatchInfo(String matchNo, String tournament, char alliance, int[] teams){
        this.matchNo = matchNo;
        this.tournament = tournament;
        this.alliance = alliance;
        this.teams = teams;
    }

    public static MatchInfo parse(String data){
        String matchNo = data.substring(0, data.indexOf("@"));
        String tournament = data.substring(data.indexOf("@")+1, data.indexOf(":"));
        String alliance = data.substring(data.indexOf(":")+1, data.indexOf("["));
        String[] teamArray = data.split(",");

        teamArray[0] = teamArray[0].substring(teamArray[0].indexOf("[")+1, teamArray[0].length());
        teamArray[1] = teamArray[1].substring(0, teamArray[1].length());
        teamArray[2] = teamArray[2].substring(0, teamArray[2].length()-1);

        int[] teams = new int[3];

        char side = alliance.charAt(0);

        for(int i=0; i<teamArray.length; i++)
            teams[i] = Integer.parseInt(teamArray[i]);

        return new MatchInfo(matchNo, tournament, side, teams);
    }

    @Override
    public String toString() {
        return matchNo + "@" + tournament + ":" + alliance + "[" + teams[0] + "," + teams[1] + "," + teams[2] + "]";
    }
}

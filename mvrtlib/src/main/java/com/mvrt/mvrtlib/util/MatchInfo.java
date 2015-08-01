package com.mvrt.mvrtlib.util;

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

    /*
        Constructor for only 1 team
     */
    public MatchInfo(String matchNo, String tournament, char alliance, int team){
        this.matchNo = matchNo;
        this.tournament = tournament;
        this.alliance = alliance;
        this.teams = new int[3];
        for(int i=0;i<teams.length;i++)teams[i]=team;
    }

    public static MatchInfo parse(String data){
        //format is MATCH@TOURN:ALLIANCE[TEAM,TEAM,TEAM]
        String[] split = data.split("@|:|,|\\[|\\]");

        if(split.length < 4)return null;

        String matchNo = split[0];
        String tournament = split[1];
        char alliance = split[2].charAt(0);

        int[] teams = new int[3];

        for(int i = 0; i < teams.length; i++){
            teams[i] = Integer.parseInt( //if not all 3 teams are present, set all teams to the first team
                    (split.length<6)?(split[3]):(split[3 + i]));
        }

        return new MatchInfo(matchNo, tournament, alliance, teams);
    }

    @Override
    public String toString() {
        return matchNo + "@" + tournament + ":" + alliance + "[" + teams[0] + "," + teams[1] + "," + teams[2] + "]";
    }

    public int getTeam(int scoutId) {
        return teams[scoutId];
    }

}

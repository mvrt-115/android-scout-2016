package com.mvrt.mvrtlib.util;

import com.mvrt.mvrtlib.R;

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
        this.matchNo = matchNo.toLowerCase();
        this.tournament = tournament.toUpperCase();
        this.alliance = alliance;
        this.teams = teams;
    }

    /*
        Constructor for only 1 team
     */
    public MatchInfo(String matchNo, String tournament, char alliance, int team){
        this.matchNo = matchNo.toLowerCase();
        this.tournament = tournament.toUpperCase();
        this.alliance = alliance;
        this.teams = new int[3];
        for(int i=0;i<teams.length;i++)teams[i]=team;
    }

    public static MatchInfo parse(String data){
        //format is MATCH@TOURN:ALLIANCE[TEAM,TEAM,TEAM]
        String[] split = data.toLowerCase().split("@|:|,|\\[|\\]");

        if(split.length < 4)return null;

        String matchNo = split[0];
        String tournament = split[1].toUpperCase();
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

    public char getAlliance(){
        return alliance;
    }

    public static String getAllianceString(char alliance){
        return (alliance == Constants.ALLIANCE_BLUE)?"Blue Alliance":"Red Alliance";
    }

    public String getAllianceString(){
        return getAllianceString(alliance);
    }

    public static int getAllianceColorId(char alliance){
        return ((alliance == Constants.ALLIANCE_BLUE) ? R.color.alliance_blue : R.color.alliance_red);
    }

    public int getAllianceColorId(){
        return getAllianceColorId(alliance);
    }

    public String getTournament(){
        return tournament;
    }

    public String getMatchNo(){
        return matchNo;
    }

}

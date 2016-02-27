package com.mvrt.mvrtlib.util;


import android.util.Log;

import com.mvrt.mvrtlib.R;

import java.io.Serializable;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Akhil Palla and Ishan Mitra
 */
public class MatchInfo implements Serializable {

    int matchNo;
    String tournament;
    char alliance;
    int[] teams;
    String[] defenses;

    public MatchInfo(int matchNo, String tournament, char alliance, int[] teams, String[] defenses){
        this.matchNo = matchNo;
        this.tournament = tournament.toUpperCase();
        this.alliance = alliance;
        this.teams = teams;
        this.defenses = defenses;
    }

    public MatchInfo(int matchNo, String tournament, char alliance, int team, String[] defenses){
        this.matchNo = matchNo;
        this.tournament = tournament.toUpperCase();
        this.alliance = alliance;
        this.teams = new int[]{team};
        this.defenses = defenses;
    }

    /**
     * FORMAT: 10@SVR:r[115,254,1678](a1,b2,c2,d2)
     */
    public static MatchInfo parse(String data){
        data = data.replaceAll(" ", "");

        Log.d("MVRT", "Validating");
        if(!validate(data))return null;
        Log.d("MVRT", "Validated");

        Pattern p = Pattern.compile("\\d+(?=@)");
        Matcher m = p.matcher(data);
        if(!m.find())return null;
        int matchNo = Integer.parseInt(m.group());

        p = Pattern.compile("\\w+(?=:)");
        m = p.matcher(data);
        if(!m.find())return null;
        String tourn = m.group();

        p = Pattern.compile("\\w(?=\\[)");
        m = p.matcher(data);
        if(!m.find())return null;
        char alliance = m.group().charAt(0);

        p = Pattern.compile("(\\d+,?)+(?=])");
        m = p.matcher(data);
        if(!m.find())return null;
        String[] teamString = m.group().split(",");
        Log.d("MVRT", "teamString array: " + Arrays.toString(teamString));
        int[] teams = new int[teamString.length];
        for(int i = 0; i < teamString.length; i++)teams[i] = Integer.parseInt(teamString[i]);

        p = Pattern.compile("(\\w\\d,?)+(?=\\))");
        m = p.matcher(data);
        if(!m.find())return null;
        String[] defenses = m.group().split(",");

        return new MatchInfo(matchNo, tourn, alliance, teams, defenses);
    }

    @Override
    public String toString() {
        String str = matchNo + "@" + tournament + ":" + alliance + Arrays.toString(teams);
        str += "(" + defenses[0] + "," + defenses[1] + "," + defenses[2] + "," + defenses[3] + ")";
        str = str.replaceAll(" ", "");
        return str;
    }

    public String toString(int id) {
        String str = matchNo + "@" + tournament + ":" + alliance + "[" + teams[id] + "]";
        str += "(" + defenses[0] + "," + defenses[1] + "," + defenses[2] + "," + defenses[3] + ")";
        str = str.replaceAll(" ", "");
        return str;
    }

    public String toDbKey(int id){
        String str = matchNo + "@" + tournament + ":" + alliance + "-" + teams[id];
        str = str.replaceAll(" ", "");
        return str;
    }

    public String userFriendlyString(){
        return "Teams " + Arrays.toString(teams) + " - " + alliance +  " (" + matchNo +  "@" + tournament + ")";
        // Teams [115,254,1678] - b (q12@SVR)
    }

    public String userFriendlyString(int id){
        return "Team " + teams[id] + ", " + alliance +  " (" + matchNo +  "@" + tournament + ")";
        // Team 115, b (q12@SVR)
    }

    public String getFilename(){
        String str = matchNo + "@" + tournament + ":" + alliance + Arrays.toString(teams) + ".json";
        str = str.replaceAll(" ", "");
        return str;
    }

    public String getFilename(int id){
        return matchNo + "@" + tournament + ":" + alliance + "[" + teams[id] + "].json";
    }

    public char getAlliance(){
        return alliance;
    }

    public int[] getTeams(){
        return teams;
    }

    public String[] getDefenses(){
        return defenses;
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

    public int getMatchNo(){
        return matchNo;
    }

    public static boolean validate(String str){
        return str.matches("\\d+@\\w+:(r|b)\\[(\\d+,?)+\\]\\((,?\\w\\d)+\\)");
    }

    public static boolean validateFilename(String str){
        return str.matches("\\d+@\\w+:(r|b)\\[(,? ?\\d+)+\\].json");
    }

}

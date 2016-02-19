package com.mvrt.mvrtlib.util;

/**
 * @author Akhil Palla
 */
public class DefenseCrossing {

    private String defense;
    private long duration;

    public DefenseCrossing(String defense, long start, long end){
        this.defense = defense;
        this.duration = end - start;
    }

    public DefenseCrossing(String defense, long duration){
        this.defense = defense;
        this.duration = duration;
    }

    public String getDefense(){
        return defense;
    }

    public long getDurationMillis(){
        return duration;
    }

    public double getDurationSeconds(){
        return duration/1000.0;
    }

    public String toString(){
        return defense + ":" + duration;
    }

}

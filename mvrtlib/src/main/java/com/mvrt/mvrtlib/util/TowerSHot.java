package com.mvrt.mvrtlib.util;

/**
 * Created by Akhil on 2/26/2016.
 */
public class TowerShot {

    private int x;
    private int y;
    private boolean made;
    private boolean highGoal;

    public static final String COORD_X = "x";
    public static final String COORD_Y = "y";
    public static final String SHOT_MADE = "m";
    public static final String GOAL_HIGH = "h";

    public TowerShot(int[] coords, boolean made, boolean highGoal){
        this.x = coords[0];
        this.y = coords[1];
        this.made = made;
        this.highGoal = highGoal;
    }

    public TowerShot(int x, int y, boolean made, boolean highGoal){
        this.x = x;
        this.y = y;
        this.made = made;
        this.highGoal = highGoal;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public int[] getCoords(){
        return new int[]{x, y};
    }

    public boolean getMade(){
        return made;
    }

    public boolean getHighGoal(){
        return highGoal;
    }

    public String toString(){
        return (made?"y":"n") + "(" + x + "," + y + ")" + (highGoal?"h":"l");
    }

}

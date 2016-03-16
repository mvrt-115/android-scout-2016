package com.mvrt.mvrtlib.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.mvrt.mvrtlib.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Akhil Palla
 */
public class DefenseManager {

    public static String getString(String code){
        String str = "";
        switch(code){
            case Constants.DEFENSE_PORTCULLIS:
                str = "Portcullis";
                break;
            case Constants.DEFENSE_CHEVALDEFRISE:
                str = "Cheval De Frise";
                break;
            case Constants.DEFENSE_MOAT:
                str = "Moat";
                break;
            case Constants.DEFENSE_RAMPART:
                str = "Rampart";
                break;
            case Constants.DEFENSE_DRAWBRIDGE:
                str = "Drawbridge";
                break;
            case Constants.DEFENSE_SALLYPORT:
                str = "Sally Port";
                break;
            case Constants.DEFENSE_ROCKWALL:
                str = "Rock Wall";
                break;
            case Constants.DEFENSE_ROUGHTERRAIN:
                str = "Rough Terrain";
                break;
            case Constants.DEFENSE_LOWBAR:
                str = "Low Bar";
                break;
        }
        return str;
    }

    public static Drawable getDrawable(Context ctx, String code){
        int id = -1;
        switch(code){
            case Constants.DEFENSE_PORTCULLIS:
                id = R.mipmap.a1;
                break;
            case Constants.DEFENSE_CHEVALDEFRISE:
                id = R.mipmap.a2;
                break;
            case Constants.DEFENSE_MOAT:
                id = R.mipmap.b1;
                break;
            case Constants.DEFENSE_RAMPART:
                id = R.mipmap.b2;
                break;
            case Constants.DEFENSE_DRAWBRIDGE:
                id = R.mipmap.c1;
                break;
            case Constants.DEFENSE_SALLYPORT:
                id = R.mipmap.c2;
                break;
            case Constants.DEFENSE_ROCKWALL:
                id = R.mipmap.d1;
                break;
            case Constants.DEFENSE_ROUGHTERRAIN:
                id = R.mipmap.d2;
                break;
            case Constants.DEFENSE_LOWBAR:
                id = R.mipmap.e1;
                break;
        }

        return (id > -1)? ContextCompat.getDrawable(ctx, id):null;
    }

    public static String[] addLowBar(String[] defenses, char alliance){
        ArrayList<String> newList = new ArrayList<String>(Arrays.asList(defenses));
        if(alliance == Constants.ALLIANCE_RED){
            newList.add(Constants.DEFENSE_LOWBAR);
        }else newList.add(0, Constants.DEFENSE_LOWBAR);
        return newList.toArray(new String[0]);
    }

}

package com.mvrt.scout;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;

import com.mvrt.mvrtlib.util.Constants;
import com.mvrt.mvrtlib.util.DataCollectionFragment;

import org.json.JSONObject;

/**
 * @author Akhil Palla
 */
public class StandScoutPostgameFragment extends DataCollectionFragment {

    RatingBar highAccuracy;
    RatingBar gearAccuracy;
    RatingBar gearCycleTime;
    RatingBar pilotRating;
    RatingBar driverRating;
    RatingBar defenseRating;
    RatingBar spinningRotors;
    CheckBox disabled;
    CheckBox interfere;
    EditText comments;
    Button finish;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_standscout_postgame, container, false);
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        highAccuracy = (RatingBar)v.findViewById(R.id.postgame_highaccuracy);
        gearAccuracy = (RatingBar)v.findViewById(R.id.postgame_gearaccuracy);
        gearCycleTime = (RatingBar)v.findViewById(R.id.postgame_gearcycletime);
        pilotRating = (RatingBar)v.findViewById(R.id.postgame_pilot);
        driverRating = (RatingBar)v.findViewById(R.id.postgame_driving);
        defenseRating = (RatingBar)v.findViewById(R.id.postgame_defense);
        spinningRotors = (RatingBar)v.findViewById(R.id.postgame_rotors);
        interfere = (CheckBox)v.findViewById(R.id.postgame_interfere);
        disabled = (CheckBox) v.findViewById(R.id.postgame_disabled);
        comments = (EditText)v.findViewById(R.id.postgame_comments);
        finish = (Button)v.findViewById(R.id.postgame_finish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((StandScoutActivity)getActivity()).stopScouting();
            }
        });
    }

    @Override
    public String getTitle(){
        return "Postgame";
    }

    @Override
    public boolean validate() {
        if (highAccuracy.getRating() == 0) { return false; }
        else if (gearAccuracy.getRating() == 0) { return false; }
        else if (driverRating.getRating() == 0) { return false; }
        else if (pilotRating.getRating() == 0) { return false; }
        else if (defenseRating.getRating() == 0) { return false; }
        else { return true; }
    }

    @Override
    public JSONObject getData(){
        JSONObject o = new JSONObject();
        try{
            o.put(Constants.JSON_POSTGAME_HIGHACCURACY, highAccuracy.getRating());
            o.put(Constants.JSON_POSTGAME_GEARACCURACY, gearAccuracy.getRating());
            o.put(Constants.JSON_POSTGAME_GEARCYCLETIME, gearCycleTime.getRating());
            o.put(Constants.JSON_POSTGAME_DRIVING, driverRating.getRating());
            o.put(Constants.JSON_POSTGAME_DEFENSE, defenseRating.getRating());
            o.put(Constants.JSON_POSTGAME_PILOT, pilotRating.getRating());
            o.put(Constants.JSON_POSTGAME_ROTORS, spinningRotors.getRating());
            o.put(Constants.JSON_POSTGAME_DISABLED, disabled.isChecked());
            o.put(Constants.JSON_POSTGAME_INTERFERES, interfere.isChecked());
            o.put(Constants.JSON_POSTGAME_COMMENTS, comments.getText().toString());
        } catch(Exception e){
           e.printStackTrace();
        }
        return o;
    }
}
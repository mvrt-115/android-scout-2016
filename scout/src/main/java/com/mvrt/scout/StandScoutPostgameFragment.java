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

    RatingBar cargoAccuracy;
    RatingBar hatchAccuracy;
    RatingBar cycleTime;
    RatingBar driverRating;
    RatingBar defenseRating;
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
        cargoAccuracy = (RatingBar)v.findViewById(R.id.postgame_cargoaccuracy);
        hatchAccuracy = (RatingBar)v.findViewById(R.id.postgame_hatchaccuracy);
        cycleTime = (RatingBar)v.findViewById(R.id.postgame_cycletime);
        driverRating = (RatingBar)v.findViewById(R.id.postgame_driving);
        defenseRating = (RatingBar)v.findViewById(R.id.postgame_defense);
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
        if (cargoAccuracy.getRating() == 0) { return false; }
        else if (hatchAccuracy.getRating() == 0) { return false; }
        else if (driverRating.getRating() == 0) { return false; }
        else if (defenseRating.getRating() == 0) { return false; }
        else { return true; }
    }

    @Override
    public JSONObject getData(){
        JSONObject o = new JSONObject();
        try{
            o.put(Constants.JSON_POSTGAME_CARGOACCURACY, cargoAccuracy.getRating());
            o.put(Constants.JSON_POSTGAME_HATCHACCURACY, hatchAccuracy.getRating());
            o.put(Constants.JSON_POSTGAME_CYCLETIME, cycleTime.getRating());
            o.put(Constants.JSON_POSTGAME_DRIVING, driverRating.getRating());
            o.put(Constants.JSON_POSTGAME_DEFENSE, defenseRating.getRating());
            //o.put(Constants.JSON_POSTGAME_PILOT, pilotRating.getRating());
            //o.put(Constants.JSON_POSTGAME_ROTORS, spinningRotors.getRating());
            o.put(Constants.JSON_POSTGAME_DISABLED, disabled.isChecked());
            o.put(Constants.JSON_POSTGAME_INTERFERES, interfere.isChecked());
            o.put(Constants.JSON_POSTGAME_COMMENTS, comments.getText().toString());
        } catch(Exception e){
           e.printStackTrace();
        }
        return o;
    }
}
package com.mvrt.scout;

import android.os.Bundle;
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

    RatingBar speed;
    RatingBar cubeAccuracy;
    RatingBar cubeCycleTime;
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
        speed = (RatingBar)v.findViewById(R.id.postgame_speed);
        cubeAccuracy = (RatingBar)v.findViewById(R.id.postgame_cubeaccuracy);
        cubeCycleTime = (RatingBar)v.findViewById(R.id.postgame_cubecycletime);
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
        if (speed.getRating() == 0) { return false; }
        else if (cubeAccuracy.getRating() == 0) { return false; }
        else if (driverRating.getRating() == 0) { return false; }
        else if (defenseRating.getRating() == 0) { return false; }
        else { return true; }
    }

    @Override
    public JSONObject getData(){
        JSONObject o = new JSONObject();
        try{
            o.put(Constants.JSON_POSTGAME_SPEED, speed.getRating());
            o.put(Constants.JSON_POSTGAME_CUBEACCURACY, cubeAccuracy.getRating());
            o.put(Constants.JSON_POSTGAME_CUBECYCLETIME, cubeCycleTime.getRating());
            o.put(Constants.JSON_POSTGAME_DRIVING, driverRating.getRating());
            o.put(Constants.JSON_POSTGAME_DEFENSE, defenseRating.getRating());
            o.put(Constants.JSON_POSTGAME_DISABLED, disabled.isChecked());
            o.put(Constants.JSON_POSTGAME_INTERFERES, interfere.isChecked());
            o.put(Constants.JSON_POSTGAME_COMMENTS, comments.getText().toString());
        } catch(Exception e){
           e.printStackTrace();
        }
        return o;
    }
}
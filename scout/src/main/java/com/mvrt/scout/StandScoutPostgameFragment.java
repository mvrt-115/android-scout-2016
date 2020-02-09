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

public class StandScoutPostgameFragment extends DataCollectionFragment {

    CheckBox parked;
    CheckBox stuck;
    CheckBox disabled;
    Button finish;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_stand_scout_postgame, container, false);
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {



        parked = (CheckBox)v.findViewById(R.id.cb_endgame_parked);
        stuck = (CheckBox)v.findViewById(R.id.cb_endgame_stuck);
        disabled = (CheckBox) v.findViewById(R.id.cb_endgame_disabled);
        finish = (Button)v.findViewById(R.id.bt_postgame_finish);
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
        return true;
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
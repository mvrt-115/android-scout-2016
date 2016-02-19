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

    RatingBar defense;
    RatingBar intake;
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
        defense = (RatingBar)v.findViewById(R.id.postgame_defense);
        intake = (RatingBar)v.findViewById(R.id.postgame_intake);
        interfere = (CheckBox)v.findViewById(R.id.postgame_interfere);
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
        if (intake.getRating() == 0) { return false; }
        else if (defense.getRating() == 0) { return false; }
        else { return true; }
    }

    @Override
    public JSONObject getData(){
        JSONObject o = new JSONObject();
        try{
            o.put(Constants.JSON_POSTGAME_INTAKE, intake.getRating());
            o.put(Constants.JSON_POSTGAME_DEFENSE, defense.getRating());
            o.put(Constants.JSON_POSTGAME_DISABLED, disabled.isChecked());
            o.put(Constants.JSON_POSTGAME_INTERFERES, interfere.isChecked());
            o.put(Constants.JSON_POSTGAME_COMMENTS, comments.getText().toString());
        } catch(Exception e){
           Log.e("MVRT", "JSON Error");
        }
        return o;
    }
}
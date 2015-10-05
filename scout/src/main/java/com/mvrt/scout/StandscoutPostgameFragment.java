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

import com.mvrt.mvrtlib.util.DataCollectionFragment;

import org.json.JSONObject;

/**
 * @author Aditya Kalari
 */
public class StandScoutPostgameFragment extends DataCollectionFragment {

    RatingBar coop;
    RatingBar stacking;
    RatingBar capping;
    RatingBar intake;
    RatingBar litter;
    CheckBox tippy;
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
        coop = (RatingBar)v.findViewById(R.id.postgame_cooprating);
        stacking = (RatingBar)v.findViewById(R.id.postgame_stackrating);
        capping = (RatingBar)v.findViewById(R.id.postgame_caprating);
        intake = (RatingBar)v.findViewById(R.id.postgame_intakerating);
        litter = (RatingBar)v.findViewById(R.id.postgame_litterrating);
        tippy = (CheckBox)v.findViewById(R.id.postgame_tippy);
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
        if (coop.getRating() == 0) { return false; }
        else if (stacking.getRating() == 0) { return false; }
        else if (capping.getRating() == 0) { return false; }
        else if (intake.getRating() == 0) { return false; }
        else if (litter.getRating() == 0) { return false; }
        else { return true; }
    }

    @Override
    public JSONObject getData(){
        JSONObject o = new JSONObject();
        try{
            o.put("Coop Rating", coop.getRating());
            o.put("Stacking Rating", stacking.getRating());
            o.put("Capping Rating", capping.getRating());
            o.put("Intake Rating", intake.getRating());
            o.put("Litter Rating", litter.getRating());
            o.put("Tippy", tippy.isChecked());
            o.put("Interferes", interfere.isChecked());
            o.put("Scout Comments", comments.getText().toString());
        } catch(Exception e){
           Log.e("MVRT", "JSON Error");
        }
        return o;
    }
}
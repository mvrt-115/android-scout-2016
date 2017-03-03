package com.mvrt.scout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.mvrt.mvrtlib.util.Constants;
import com.mvrt.mvrtlib.util.DataCollectionFragment;

import org.json.JSONObject;


public class StandScoutAutonFragment extends DataCollectionFragment implements View.OnClickListener {


    CheckBox startWithGear;
    CheckBox startWithBalls;
    CheckBox mobility;
    CheckBox hopper;
    CheckBox groundIntake;

    Button highGoal;
    Button lowGoal;
    Button placeGear;
    Button minusGear;
    Button finishAuton;

    Button minusHigh;
    Button minusLow;

    int gearsPlaced = 0;
    int highGoals = 0;
    int lowGoals = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_standscout_auton, container, false);
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        startWithBalls = (CheckBox)v.findViewById(R.id.ch_auton_balls);
        startWithGear = (CheckBox)v.findViewById(R.id.ch_auton_gear);
        mobility = (CheckBox)v.findViewById(R.id.ch_auton_mobility);
        hopper = (CheckBox)v.findViewById(R.id.ch_auton_hopper);
        groundIntake = (CheckBox)v.findViewById(R.id.ch_auton_groundintake);

        highGoal = (Button)v.findViewById(R.id.bt_auton_high);
        highGoal.setOnClickListener(this);
        lowGoal = (Button)v.findViewById(R.id.bt_auton_low);
        lowGoal.setOnClickListener(this);

        placeGear = (Button)v.findViewById(R.id.bt_auton_gear);
        placeGear.setOnClickListener(this);
        minusGear = (Button)v.findViewById(R.id.bt_auton_gear_minus);
        minusGear.setOnClickListener(this);

        minusHigh = (Button)v.findViewById(R.id.bt_auton_high_minus);
        minusHigh.setOnClickListener(this);

        minusLow = (Button)v.findViewById(R.id.bt_auton_low_minus);
        minusLow.setOnClickListener(this);

        finishAuton = (Button)v.findViewById(R.id.bt_auton_finish);
        finishAuton.setOnClickListener(this);
    }

    private void refreshGearUI(){
        placeGear.setText("Place Gear (" + gearsPlaced + ")");
    }

    private void placeGear(){
        if(gearsPlaced < 3) gearsPlaced++;
        refreshGearUI();
        startWithGear.setChecked(true);
    }

    private void removeGear(){
        if(gearsPlaced > 0) gearsPlaced--;
        refreshGearUI();
    }

    @Override
    public String getTitle() {
        return "Auton";
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bt_auton_gear:
                placeGear();
                break;
            case R.id.bt_auton_gear_minus:
                removeGear();
                break;
            case R.id.bt_auton_high:
                highGoals+= 5;
                highGoal.setText("High (" + highGoals + ")");
                break;
            case R.id.bt_auton_low:
                lowGoals+= 5;
                lowGoal.setText("Low (" + lowGoals + ")");
                break;
            case R.id.bt_auton_finish:
                ((StandScoutActivity)getActivity()).nextTab();
                break;
            case R.id.bt_auton_high_minus:
                if(highGoals > 0) highGoals -= 5;
                highGoal.setText("High (" + highGoals + ")");
                break;
            case R.id.bt_auton_low_minus:
                if(lowGoals > 0) lowGoals -= 5;
                lowGoal.setText("Low (" + lowGoals + ")");
        }
    }


    public JSONObject getData(){
        JSONObject obj = new JSONObject();
        try {
            obj.put(Constants.JSON_AUTON_GEARS, gearsPlaced);
            obj.put(Constants.JSON_AUTON_HIGH, highGoals);
            obj.put(Constants.JSON_AUTON_LOW, lowGoals);
            obj.put(Constants.JSON_AUTON_MOBILITY, mobility.isChecked());
            obj.put(Constants.JSON_AUTON_STARTBALLS, startWithBalls.isChecked());
            obj.put(Constants.JSON_AUTON_STARTGEARS, startWithGear.isChecked());
            obj.put(Constants.JSON_AUTON_HOPPER, hopper.isChecked());
            obj.put(Constants.JSON_AUTON_GROUNDINTAKE, groundIntake.isChecked());
        }catch(Exception e){
            e.printStackTrace();
        }
        return obj;
    }

    @Override
    public boolean validate () {
        return true;
    }


}
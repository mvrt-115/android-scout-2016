package com.mvrt.scout;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.mvrt.mvrtlib.util.Constants;
import com.mvrt.mvrtlib.util.DataCollectionFragment;
import com.mvrt.mvrtlib.util.DefenseCrossing;
import com.mvrt.mvrtlib.util.DefenseCrossingDialogFragment;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class StandScoutAutonFragment extends DataCollectionFragment implements View.OnClickListener {


    Button crossDefense;
    boolean crosses = false;

    Button reachDefense;
    boolean reaches = false;

    Button intakeBall;
    Button removeBall;
    int intakedBalls = 0;

    Button shootButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_standscout_auton, container, false);
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        initShootUI(v);
        initIntakeUI(v);
        initCrossUI(v);
        initReachUI(v);
        refreshUi();
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    private void initIntakeUI(View v){
        intakeBall = (Button)v.findViewById(R.id.auton_intake);
        intakeBall.setOnClickListener(this);
        removeBall = (Button)v.findViewById(R.id.auton_intakeremove);
        removeBall.setOnClickListener(this);
    }

    private void refreshIntakeUI(){
        intakeBall.setText("Intake Boulder (" + intakedBalls + ")");
    }

    private void initReachUI(View v) {
        reachDefense = (Button)v.findViewById(R.id.auton_reach);
        reachDefense.setOnClickListener(this);
    }

    private void intakeBall(){
        intakedBalls++;
        refreshIntakeUI();
    }

    private void removeBall(){
        if(intakedBalls > 0)intakedBalls--;
        refreshIntakeUI();
    }

    private void initShootUI(View v){
        shootButton = (Button)v.findViewById(R.id.auton_shoot);
        shootButton.setOnClickListener(this);
    }

    private void initCrossUI(View v){
        crossDefense = (Button)v.findViewById(R.id.auton_cross);
        crossDefense.setOnClickListener(this);
    }

    private void shootBall(){
        ((StandScoutActivity)getActivity()).setTab(3);
    }

    private void crossDefense(){
        crosses = !crosses;
        if (crosses)
            crossDefense.setText("Crossed!");
        else
            crossDefense.setText("Did Not Cross!");
    }

    private void reachDefense() {
        reaches = !reaches;
        if (reaches)
            reachDefense.setText("Reached!");
        else
            reachDefense.setText("Did Not Reach!");
    }

    @Override
    public String getTitle() {
        return "Auton";
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.auton_intake:
                intakeBall();
                break;
            case R.id.auton_intakeremove:
                removeBall();
                break;
            case R.id.auton_shoot:
                shootBall();
                break;
            case R.id.auton_cross:
                crossDefense();
                break;
            case R.id.auton_reach:
                reachDefense();
                break;
        }
        refreshUi();
    }

    public void refreshUi(){
        refreshIntakeUI();
    }

    public JSONObject getData(){
        JSONObject obj = new JSONObject();
        try {
            obj.put(Constants.JSON_AUTON_CROSSINGS, crosses);
            obj.put(Constants.JSON_AUTON_INTAKE, intakedBalls);
            obj.put(Constants.JSON_AUTON_REACH, reaches);
            obj.put(Constants.JSON_AUTON_SHOOT, shots);
        }catch(Exception e){}
        return obj;
    }

    // Unfortunately nothing to do here, as robot may have done nothing
    @Override
    public boolean validate () {
        return !(reaches && crosses);
    }

}
package com.mvrt.scout;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.mvrt.mvrtlib.util.Constants;
import com.mvrt.mvrtlib.util.DataCollectionFragment;
import com.mvrt.mvrtlib.util.DefenseCrossing;
import com.mvrt.mvrtlib.util.DefenseCrossingDialogFragment;
import com.mvrt.mvrtlib.util.DefenseManager;
import com.mvrt.mvrtlib.util.Snacker;

import org.json.JSONObject;


public class StandScoutAutonFragment extends DataCollectionFragment implements View.OnClickListener, DefenseCrossingDialogFragment.DefenseSelectedListener {


    Button intakeBall;
    Button removeBall;
    int intakedBalls = 0;

    Button crossDefense;
    DefenseCrossing crossing;
    DefenseCrossingDialogFragment crossingDialogFragment;
    Button cancelCrossDefense;

    CheckBox reachBox;

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
        refreshUi();
    }

    private void initCrossUI(View v){
        crossDefense = (Button)v.findViewById(R.id.auton_cross);
        crossDefense.setOnClickListener(this);
        crossingDialogFragment = new DefenseCrossingDialogFragment();
        crossingDialogFragment.setDefenses(((StandScoutActivity) getActivity()).getMatchInfo());
        crossingDialogFragment.setListener(this);
        reachBox = (CheckBox)v.findViewById(R.id.auton_reach);
        cancelCrossDefense = (Button)v.findViewById(R.id.auton_cross_cancel);
        cancelCrossDefense.setOnClickListener(this);
    }

    private void initIntakeUI(View v){
        intakeBall = (Button)v.findViewById(R.id.auton_intake);
        intakeBall.setOnClickListener(this);
        removeBall = (Button)v.findViewById(R.id.auton_intakeremove);
        removeBall.setOnClickListener(this);
    }

    private void initShootUI(View v){
        shootButton = (Button)v.findViewById(R.id.auton_shoot);
        shootButton.setOnClickListener(this);
    }

    private void refreshIntakeUI(){
        intakeBall.setText("Intake Boulder (" + intakedBalls + ")");
    }

    private void refreshCrossingUI(){
        if(crossing != null){
            crossDefense.setText("Crossed " + DefenseManager.getString(crossing.getDefense()));
            reachBox.setEnabled(false);
            reachBox.setChecked(false);
        }else{
            reachBox.setEnabled(true);
        }
    }

    private void intakeBall(){
        intakedBalls++;
        refreshIntakeUI();
    }

    private void removeBall(){
        if(intakedBalls > 0)intakedBalls--;
        refreshIntakeUI();
    }

    private void shootBall(){
        ((StandScoutActivity)getActivity()).shoot(true);
    }

    private void crossDefense(){
        if(crossing != null){
            Snacker.snack("Can only score one crossing in auton", getActivity(), Snackbar.LENGTH_SHORT);
        }else{
            crossingDialogFragment.show(getFragmentManager(), "MVRT");
        }
    }

    private void cancelCross() {
        Snacker.snack("Cross Canceled", getActivity(), Snackbar.LENGTH_SHORT);
        crossing = null;
        crossDefense.setText("Cross A Defense");
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
            case R.id.auton_cross_cancel:
                cancelCross();
                break;
        }
        refreshUi();
    }

    public void refreshUi(){
        refreshIntakeUI();
        refreshCrossingUI();
    }

    public JSONObject getData(){
        JSONObject obj = new JSONObject();
        try {
            obj.put(Constants.JSON_AUTON_INTAKE, intakedBalls);
            obj.put(Constants.JSON_AUTON_REACH, reachBox.isChecked());
            obj.put(Constants.JSON_AUTON_CROSSING, crossing.getDefense());
        }catch(Exception e){}
        return obj;
    }

    @Override
    public boolean validate () {
        return true;
    }

    @Override
    public void onDefenseSelected(String defense) {
        crossing = new DefenseCrossing(defense, 0);
        refreshCrossingUI();
    }

    @Override
    public void defenseSelectionCanceled() {}

}
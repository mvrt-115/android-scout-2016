package com.mvrt.scout;

import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;

import com.mvrt.mvrtlib.util.Constants;
import com.mvrt.mvrtlib.util.DataCollectionFragment;

import org.json.JSONObject;


public class StandScoutAutonFragment extends DataCollectionFragment implements View.OnClickListener {


    CheckBox startWithCube;
    RadioButton startLeft;
    RadioButton startCenter;
    RadioButton startRight;


    Button mobility;
    Button placeSwitch;
    Button placeScale;
    Button finishAuton;

    Button switchMinus;
    Button scaleMinus;

    int switchCubesPlaced = 0;
    int scaleCubesPlaced = 0;

    boolean autoMobility = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_standscout_auton, container, false);
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        startLeft = (RadioButton) v.findViewById(R.id.rb_auton_left);
        startRight = (RadioButton)v.findViewById(R.id.rb_auton_right);
        startCenter = (RadioButton)v.findViewById(R.id.rb_auton_center);
        startWithCube = (CheckBox) v.findViewById(R.id.ch_auton_cube);

        mobility = (Button)v.findViewById(R.id.bt_auton_mobility);
        mobility.setOnClickListener(this);
        placeSwitch = (Button)v.findViewById(R.id.bt_auton_switch);
        placeSwitch.setOnClickListener(this);
        placeScale = (Button)v.findViewById(R.id.bt_auton_scale);
        placeScale.setOnClickListener(this);
        switchMinus = (Button)v.findViewById(R.id.bt_auton_switch_minus);
        switchMinus.setOnClickListener(this);
        scaleMinus = (Button)v.findViewById(R.id.bt_auton_scale_minus);
        scaleMinus.setOnClickListener(this);

        finishAuton = (Button)v.findViewById(R.id.bt_auton_finish);
        finishAuton.setOnClickListener(this);
    }

    @Override
    public String getTitle() {
        return "Auton";
    }

    private void setMobility() {
        if(!autoMobility) {
            mobility.setText("Crossed");
            autoMobility = true;
        }
        else{
            mobility.setText("MOBILITY (CROSSES AUTOLINE)");
            autoMobility = false;
        }
    }

    private void placeSwitch() {
        switchCubesPlaced++;
        placeSwitch.setText("Placed Cube (" + switchCubesPlaced + ")");
    }

    private void placeScale() {
        scaleCubesPlaced++;
        placeScale.setText("Placed Cube (" + scaleCubesPlaced + ")");
    }

    private void minusSwitch() {
        if(switchCubesPlaced>0) {
            switchCubesPlaced--;
            placeSwitch.setText("Placed Cube (" + switchCubesPlaced + ")");
        }
    }

    private void minusScale() {
        if(scaleCubesPlaced>0){
            scaleCubesPlaced--;
            placeScale.setText("Placed Cube (" + scaleCubesPlaced + ")");
        }
    }



    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bt_auton_mobility:
                setMobility();
                Log.d("com.mvrt.scout", "MOBILITY (CROSSES AUTOLINE)");
                break;
            case R.id.bt_auton_switch:
                placeSwitch();
                Log.d("com.mvrt.scout", "PLACED SWITCH");
                break;
            case R.id.bt_auton_scale:
                placeScale();
                Log.d("com.mvrt.scout", "PLACED SCALE");
                break;
            case R.id.bt_auton_switch_minus:
                minusSwitch();
                Log.d("com.mvrt.scout", "DECREMENTED SWITCH");
                break;
            case R.id.bt_auton_scale_minus:
                minusScale();
                Log.d("com.mvrt.scout", "DECREMENTED SCALE");
                break;
            case R.id.bt_auton_finish:
                ((StandScoutActivity)getActivity()).nextTab();
                Log.d("com.mvrt.scout", "AUTON FINISHED");
                break;
        }
    }


    public JSONObject getData(){
        JSONObject obj = new JSONObject();
        try {
            obj.put(Constants.JSON_AUTON_SWITCH, switchCubesPlaced);
            obj.put(Constants.JSON_AUTON_SWITCH, switchCubesPlaced);
            obj.put(Constants.JSON_AUTON_SCALE, scaleCubesPlaced);
            obj.put(Constants.JSON_AUTON_STARTLEFT, startLeft.isChecked());
            obj.put(Constants.JSON_AUTON_STARTCENTER, startCenter.isChecked());
            obj.put(Constants.JSON_AUTON_STARTRIGHT, startRight.isChecked());
            obj.put(Constants.JSON_AUTON_STARTCUBE, startWithCube.isChecked());
            obj.put(Constants.JSON_AUTON_MOBILITY, autoMobility);
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
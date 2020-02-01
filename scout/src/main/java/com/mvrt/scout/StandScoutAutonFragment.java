package com.mvrt.scout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import com.mvrt.mvrtlib.util.Constants;
import com.mvrt.mvrtlib.util.DataCollectionFragment;

import org.json.JSONObject;

public class StandScoutAutonFragment extends DataCollectionFragment implements View.OnClickListener {


    RadioButton startWithHatch;
    RadioButton startWithCargo;
    RadioButton noneLoaded;

    RadioButton startLevel1;
    RadioButton startLevel2;

    Button crossHABLine;

    Button rocketCargoLevel1;
    Button rocketCargoLevel2;
    Button rocketCargoLevel3;

    Button minusRocketCargoLevel1;
    Button minusRocketCargoLevel2;
    Button minusRocketCargoLevel3;

    Button rocketHatchLevel1;
    Button rocketHatchLevel2;
    Button rocketHatchLevel3;

    Button minusRocketHatchLevel1;
    Button minusRocketHatchLevel2;
    Button minusRocketHatchLevel3;

    Button cargoShipCargo;
    Button minusCargoShipCargo;

    Button cargoShipHatch;
    Button minusCargoShipHatch;

    Button finishSandstorm;

    int rocketCargoLevel1Num = 0;
    int rocketCargoLevel2Num = 0;
    int rocketCargoLevel3Num = 0;

    int rocketHatchLevel1Num = 0;
    int rocketHatchLevel2Num = 0;
    int rocketHatchLevel3Num = 0;

    int cargoShipCargoNum = 0;
    int cargoShipHatchNum = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_stand_scout_auton, container, false);
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        startWithHatch = (RadioButton) v.findViewById(R.id.radio_hatch);
        startWithCargo = (RadioButton) v.findViewById(R.id.radio_cargo);
        noneLoaded = (RadioButton) v.findViewById(R.id.radio_no_content);

        startLevel1 = (RadioButton) v.findViewById(R.id.radio_level1);
        startLevel2 = (RadioButton) v.findViewById(R.id.radio_level2);

        crossHABLine = (Button) v.findViewById(R.id.bt_sandstorm_hab_cross);
        crossHABLine.setOnClickListener(this);

        rocketCargoLevel1 = (Button) v.findViewById(R.id.bt_sandstorm_rocket_cargo_level1);
        rocketCargoLevel1.setOnClickListener(this);
        rocketCargoLevel2 = (Button) v.findViewById(R.id.bt_sandstorm_rocket_cargo_level2);
        rocketCargoLevel2.setOnClickListener(this);
        rocketCargoLevel3 = (Button) v.findViewById(R.id.bt_sandstorm_rocket_cargo_level3);
        rocketCargoLevel3.setOnClickListener(this);

        minusRocketCargoLevel1 = (Button) v.findViewById(R.id.bt_sandstorm_rocket_cargo_level1_minus);
        minusRocketCargoLevel1.setOnClickListener(this);
        minusRocketCargoLevel2 = (Button) v.findViewById(R.id.bt_sandstorm_rocket_cargo_level2_minus);
        minusRocketCargoLevel2.setOnClickListener(this);
        minusRocketCargoLevel3 = (Button) v.findViewById(R.id.bt_sandstorm_rocket_cargo_level3_minus);
        minusRocketCargoLevel3.setOnClickListener(this);

        rocketHatchLevel1 = (Button) v.findViewById(R.id.bt_sandstorm_rocket_hatch_level1);
        rocketHatchLevel1.setOnClickListener(this);
        rocketHatchLevel2 = (Button) v.findViewById(R.id.bt_sandstorm_rocket_hatch_level2);
        rocketHatchLevel2.setOnClickListener(this);
        rocketHatchLevel3 = (Button) v.findViewById(R.id.bt_sandstorm_rocket_hatch_level3);
        rocketHatchLevel3.setOnClickListener(this);

        minusRocketHatchLevel1 = (Button) v.findViewById(R.id.bt_sandstorm_rocket_hatch_level1_minus);
        minusRocketHatchLevel1.setOnClickListener(this);
        minusRocketHatchLevel2 = (Button) v.findViewById(R.id.bt_sandstorm_rocket_hatch_level2_minus);
        minusRocketHatchLevel2.setOnClickListener(this);
        minusRocketHatchLevel3 = (Button) v.findViewById(R.id.bt_sandstorm_rocket_hatch_level3_minus);
        minusRocketHatchLevel3.setOnClickListener(this);

        cargoShipCargo = (Button) v.findViewById(R.id.bt_sandstorm_cargoship_cargo);
        cargoShipCargo.setOnClickListener(this);
        minusCargoShipCargo = (Button) v.findViewById(R.id.bt_sandstorm_cargoship_cargo_minus);
        minusCargoShipCargo.setOnClickListener(this);

        cargoShipHatch = (Button) v.findViewById(R.id.bt_sandstorm_cargoship_hatch);
        cargoShipHatch.setOnClickListener(this);
        minusCargoShipHatch = (Button) v.findViewById(R.id.bt_sandstorm_cargoship_hatch_minus);
        minusCargoShipHatch.setOnClickListener(this);

        finishSandstorm = (Button) v.findViewById(R.id.bt_sandstorm_finish);
        finishSandstorm.setOnClickListener(this);
    }

    private void refreshHABCrossUI() {
        if (!crossHABLine.getText().equals("Crossed"))
            crossHABLine.setText("Crossed");
        else
            crossHABLine.setText("Crosses HAB Line");
    }

    private void refreshRocketCargo1UI() {
        if (rocketCargoLevel1Num > 0) rocketCargoLevel1Num--;
        rocketCargoLevel1.setText("Level 1 (" + rocketCargoLevel1Num + ")");
    }

    private void refreshRocketCargo2UI() {
        if (rocketCargoLevel2Num > 0) rocketCargoLevel2Num--;
        rocketCargoLevel2.setText("Level 2 (" + rocketCargoLevel2Num + ")");
    }

    private void refreshRocketCargo3UI() {
        if (rocketCargoLevel3Num > 0) rocketCargoLevel3Num--;
        rocketCargoLevel3.setText("Level 3 (" + rocketCargoLevel3Num + ")");
    }

    private void refreshRocketHatch1UI() {
        if (rocketHatchLevel1Num > 0) rocketHatchLevel1Num--;
        rocketHatchLevel1.setText("Level 1 (" + rocketHatchLevel1Num + ")");
    }

    private void refreshRocketHatch2UI() {
        if (rocketHatchLevel2Num > 0) rocketHatchLevel2Num--;
        rocketHatchLevel2.setText("Level 2 (" + rocketHatchLevel2Num + ")");
    }

    private void refreshRocketHatch3UI() {
        if (rocketHatchLevel3Num > 0) rocketHatchLevel3Num--;
        rocketHatchLevel3.setText("Level 3 (" + rocketHatchLevel3Num + ")");
    }

    private void refreshCargoShipCargoUI() {
        if (cargoShipCargoNum > 0) cargoShipCargoNum--;
        cargoShipCargo.setText("Placed Cargo (" + cargoShipCargoNum + ")");
    }

    private void refreshCargoShipHatchUI() {
        if (cargoShipHatchNum > 0) cargoShipHatchNum--;
        cargoShipHatch.setText("Placed Hatch (" + cargoShipHatchNum + ")");
    }

    @Override
    public String getTitle() {
        return "Sandstorm";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_sandstorm_hab_cross:
                refreshHABCrossUI();
                break;
            case R.id.bt_sandstorm_rocket_cargo_level1:
                rocketCargoLevel1Num++;
                rocketCargoLevel1.setText("Level 1 (" + rocketCargoLevel1Num + ")");
                break;
            case R.id.bt_sandstorm_rocket_cargo_level2:
                rocketCargoLevel2Num++;
                rocketCargoLevel2.setText("Level 2 (" + rocketCargoLevel2Num + ")");
                break;
            case R.id.bt_sandstorm_rocket_cargo_level3:
                rocketCargoLevel3Num++;
                rocketCargoLevel3.setText("Level 3 (" + rocketCargoLevel3Num + ")");
                break;
            case R.id.bt_sandstorm_rocket_cargo_level1_minus:
                refreshRocketCargo1UI();
                break;
            case R.id.bt_sandstorm_rocket_cargo_level2_minus:
                refreshRocketCargo2UI();
                break;
            case R.id.bt_sandstorm_rocket_cargo_level3_minus:
                refreshRocketCargo3UI();
                break;
            case R.id.bt_sandstorm_rocket_hatch_level1:
                rocketHatchLevel1Num++;
                rocketHatchLevel1.setText("Level 1 (" + rocketHatchLevel1Num + ")");
                break;
            case R.id.bt_sandstorm_rocket_hatch_level2:
                rocketHatchLevel2Num++;
                rocketHatchLevel2.setText("Level 2 (" + rocketHatchLevel2Num + ")");
                break;
            case R.id.bt_sandstorm_rocket_hatch_level3:
                rocketHatchLevel3Num++;
                rocketHatchLevel3.setText("Level 3 (" + rocketHatchLevel3Num + ")");
                break;
            case R.id.bt_sandstorm_rocket_hatch_level1_minus:
                refreshRocketHatch1UI();
                break;
            case R.id.bt_sandstorm_rocket_hatch_level2_minus:
                refreshRocketHatch2UI();
                break;
            case R.id.bt_sandstorm_rocket_hatch_level3_minus:
                refreshRocketHatch3UI();
                break;
            case R.id.bt_sandstorm_cargoship_cargo:
                cargoShipCargoNum++;
                cargoShipCargo.setText("Placed Cargo (" + cargoShipCargoNum + ")");
                break;
            case R.id.bt_sandstorm_cargoship_cargo_minus:
                refreshCargoShipCargoUI();
                break;
            case R.id.bt_sandstorm_cargoship_hatch:
                cargoShipHatchNum++;
                cargoShipHatch.setText("Placed Hatch (" + cargoShipHatchNum + ")");
                break;
            case R.id.bt_sandstorm_cargoship_hatch_minus:
                refreshCargoShipHatchUI();
                break;
            case R.id.bt_sandstorm_finish:
                ((StandScoutActivity) getActivity()).nextTab();
                break;
        }
    }


    public JSONObject getData() {
        JSONObject obj = new JSONObject();
        try {
            obj.put(Constants.JSON_SANDSTORM_STARTHATCH, startWithHatch.isChecked());
            obj.put(Constants.JSON_SANDSTORM_STARTCARGO, startWithCargo.isChecked());
            obj.put(Constants.JSON_SANDSTORM_STARTNONE, noneLoaded.isChecked());
            obj.put(Constants.JSON_SANDSTORM_LEVEL1, startLevel1.isChecked());
            obj.put(Constants.JSON_SANDSTORM_LEVEL2, startLevel2.isChecked());
            obj.put(Constants.JSON_SANDSTORM_HAB, crossHABLine.getText().equals("Crossed"));
            obj.put(Constants.JSON_SANDSTORM_ROCKETCARGO1, rocketCargoLevel1Num);
            obj.put(Constants.JSON_SANDSTORM_ROCKETCARGO2, rocketCargoLevel2Num);
            obj.put(Constants.JSON_SANDSTORM_ROCKETCARGO3, rocketCargoLevel3Num);
            obj.put(Constants.JSON_SANDSTORM_TOTALROCKETCARGO, rocketCargoLevel1Num + rocketCargoLevel2Num + rocketCargoLevel3Num);
            obj.put(Constants.JSON_SANDSTORM_ROCKETHATCH1, rocketHatchLevel1Num);
            obj.put(Constants.JSON_SANDSTORM_ROCKETHATCH2, rocketHatchLevel2Num);
            obj.put(Constants.JSON_SANDSTORM_ROCKETHATCH3, rocketHatchLevel3Num);
            obj.put(Constants.JSON_SANDSTORM_TOTALROCKETHATCH, rocketHatchLevel1Num + rocketHatchLevel2Num + rocketHatchLevel3Num);
            obj.put(Constants.JSON_SANDSTORM_CARGOSHIPCARGO, cargoShipCargoNum);
            obj.put(Constants.JSON_SANDSTORM_CARGOSHIPHATCH, cargoShipHatchNum);
            //obj.put(Constants.JSON_AUTON_GEARS, gearsPlaced);
            //obj.put(Constants.JSON_AUTON_HIGH, highGoals);
            //obj.put(Constants.JSON_AUTON_LOW, lowGoals);
            //obj.put(Constants.JSON_AUTON_MOBILITY, mobility.isChecked());
            //obj.put(Constants.JSON_AUTON_HOPPER, hopper.isChecked());
            //obj.put(Constants.JSON_AUTON_GROUNDINTAKE, groundIntake.isChecked());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    @Override
    public boolean validate() {
        return true;
    }
}


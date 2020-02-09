package com.mvrt.scout;

import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.mvrt.mvrtlib.util.Constants;
import com.mvrt.mvrtlib.util.DataCollectionFragment;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;


public class StandScoutTeleopFragment extends DataCollectionFragment implements View.OnClickListener{

    Button climbStart;
    Button climbSuccess;
    Button climbFail;
    Button climbCancel;
    Button climbConfirm;
    TextView climbStatus;
    boolean cancelConfirm = false;
    boolean successConfirm = false;
    String climbState = Constants.CLIMB_NO;
    long climbStartTime = 0, climbEndTime = 0;
    Timer climbTimer;
    TextView climbTimerTextView;

    Button teleopRocketCargoLevel1;
    Button teleopRocketCargoLevel2;
    Button teleopRocketCargoLevel3;

    Button teleopMinusRocketCargoLevel1;
    Button teleopMinusRocketCargoLevel2;
    Button teleopMinusRocketCargoLevel3;

    Button teleopRocketHatchLevel1;
    Button teleopRocketHatchLevel2;
    Button teleopRocketHatchLevel3;

    Button teleopMinusRocketHatchLevel1;
    Button teleopMinusRocketHatchLevel2;
    Button teleopMinusRocketHatchLevel3;

    Button teleopCargoShipCargo;
    Button teleopMinusCargoShipCargo;

    Button teleopCargoShipHatch;
    Button teleopMinusCargoShipHatch;

    RadioButton endLevel1;
    RadioButton endLevel2;
    RadioButton endLevel3;
    RadioButton endNone;

    Button teleopFinish;

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
        return inflater.inflate(R.layout.fragment_stand_scout_teleop, container, false);
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        initClimbUI(v);

        teleopRocketCargoLevel1 = (Button)v.findViewById(R.id.bt_teleop_rocket_cargo_level1);
        teleopRocketCargoLevel1.setOnClickListener(this);
        teleopRocketCargoLevel2 = (Button)v.findViewById(R.id.bt_teleop_rocket_cargo_level2);
        teleopRocketCargoLevel2.setOnClickListener(this);
        teleopRocketCargoLevel3 = (Button)v.findViewById(R.id.bt_teleop_rocket_cargo_level3);
        teleopRocketCargoLevel3.setOnClickListener(this);

        teleopMinusRocketCargoLevel1 = (Button)v.findViewById(R.id.bt_teleop_rocket_cargo_level1_minus);
        teleopMinusRocketCargoLevel1.setOnClickListener(this);
        teleopMinusRocketCargoLevel2 = (Button)v.findViewById(R.id.bt_teleop_rocket_cargo_level2_minus);
        teleopMinusRocketCargoLevel2.setOnClickListener(this);
        teleopMinusRocketCargoLevel3 = (Button)v.findViewById(R.id.bt_teleop_rocket_cargo_level3_minus);
        teleopMinusRocketCargoLevel3.setOnClickListener(this);

        teleopRocketHatchLevel1 = (Button)v.findViewById(R.id.bt_teleop_rocket_hatch_level1);
        teleopRocketHatchLevel1.setOnClickListener(this);
        teleopRocketHatchLevel2 = (Button)v.findViewById(R.id.bt_teleop_rocket_hatch_level2);
        teleopRocketHatchLevel2.setOnClickListener(this);
        teleopRocketHatchLevel3 = (Button)v.findViewById(R.id.bt_teleop_rocket_hatch_level3);
        teleopRocketHatchLevel3.setOnClickListener(this);

        teleopMinusRocketHatchLevel1 = (Button)v.findViewById(R.id.bt_teleop_rocket_hatch_level1_minus);
        teleopMinusRocketHatchLevel1.setOnClickListener(this);
        teleopMinusRocketHatchLevel2 = (Button)v.findViewById(R.id.bt_teleop_rocket_hatch_level2_minus);
        teleopMinusRocketHatchLevel2.setOnClickListener(this);
        teleopMinusRocketHatchLevel3 = (Button)v.findViewById(R.id.bt_teleop_rocket_hatch_level3_minus);
        teleopMinusRocketHatchLevel3.setOnClickListener(this);

        teleopCargoShipCargo = (Button)v.findViewById(R.id.bt_teleop_cargoship_cargo);
        teleopCargoShipCargo.setOnClickListener(this);
        teleopMinusCargoShipCargo = (Button)v.findViewById(R.id.bt_teleop_cargoship_cargo_minus);
        teleopMinusCargoShipCargo.setOnClickListener(this);

        teleopCargoShipHatch = (Button)v.findViewById(R.id.bt_teleop_rotation_control);
        teleopCargoShipHatch.setOnClickListener(this);
        teleopMinusCargoShipHatch = (Button)v.findViewById(R.id.bt_teleop_cargoship_hatch_minus);
        teleopMinusCargoShipHatch.setOnClickListener(this);

        endLevel1 = (RadioButton)v.findViewById(R.id.radio_end_level1);
        endLevel2 = (RadioButton)v.findViewById(R.id.radio_end_level2);
        endLevel3 = (RadioButton)v.findViewById(R.id.radio_end_level3);
        endNone = (RadioButton)v.findViewById(R.id.radio_end_none);

        teleopFinish = (Button)v.findViewById(R.id.bt_teleop_finish);
        teleopFinish.setOnClickListener(this);

        refreshUI();
    }

    @Override
    public void onStop(){
        super.onStop();
        climbTimer.cancel();
        climbTimer.purge();
    }

    private void initClimbUI(View v){
        climbStart = (Button)v.findViewById(R.id.teleop_climb_start);
        climbStart.setOnClickListener(this);
        climbSuccess = (Button)v.findViewById(R.id.teleop_climb_success);
        climbSuccess.setOnClickListener(this);
        climbFail = (Button)v.findViewById(R.id.teleop_climb_fail);
        climbFail.setOnClickListener(this);
        climbCancel = (Button)v.findViewById(R.id.teleop_climb_cancel);
        climbCancel.setOnClickListener(this);
        climbConfirm = (Button)v.findViewById(R.id.teleop_climb_confirm);
        climbConfirm.setOnClickListener(this);
        climbStatus = (TextView)v.findViewById(R.id.teleop_climb_status);
        climbTimerTextView = (TextView)v.findViewById(R.id.teleop_climb_timer);
        climbTimer = new Timer();
        climbTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                String text = "";
                if (climbState == Constants.CLIMB_PROGRESS || climbState == Constants.CLIMB_CONFIRM) {
                    long timeElapsed = SystemClock.elapsedRealtime() - climbStartTime;
                    final double seconds = timeElapsed / 1000.0;
                    text = Double.toString(seconds) + " seconds";
                } else text = "0.00 seconds";
                final String timerText = text;
                if(getActivity() != null)getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        climbTimerTextView.setText(timerText);
                    }
                });
            }
        }, 0, 50);
    }

    private void refreshClimbUI(){
        double timeSec = (climbEndTime - climbStartTime)/1000.0;
        Log.d("MVRT", "Time elapsed: " + timeSec);
        switch(climbState){
            case Constants.CLIMB_NO:
                climbStatus.setText("Climb (timed, incl. align)");
                climbStart.setVisibility(View.VISIBLE);
                climbSuccess.setVisibility(View.GONE);
                climbConfirm.setVisibility(View.GONE);
                climbFail.setVisibility(View.GONE);
                climbCancel.setVisibility(View.GONE);
                climbCancel.setText("Cancel");
                break;
            case Constants.CLIMB_PROGRESS:
                climbStatus.setText("Climbing");
                climbStart.setVisibility(View.GONE);
                climbSuccess.setVisibility(View.VISIBLE);
                climbConfirm.setVisibility(View.GONE);
                climbFail.setVisibility(View.VISIBLE);
                climbCancel.setVisibility(View.VISIBLE);
                break;
            case Constants.CLIMB_CONFIRM:
                climbStatus.setText("Confirm Climb");
                climbStart.setVisibility(View.GONE);
                climbSuccess.setVisibility(View.GONE);
                climbConfirm.setVisibility(View.VISIBLE);
                climbFail.setVisibility(View.VISIBLE);
                climbCancel.setVisibility(View.VISIBLE);
                break;
            case Constants.CLIMB_FAIL:
                climbStatus.setText("Climb Failed in " + timeSec + " seconds");
                climbStart.setVisibility(View.GONE);
                climbSuccess.setVisibility(View.GONE);
                climbConfirm.setVisibility(View.GONE);
                climbFail.setVisibility(View.GONE);
                climbCancel.setVisibility(View.VISIBLE);
                break;
            case Constants.CLIMB_SUCCESS:
                climbStatus.setText("Climb Successful in " + timeSec + " seconds");
                climbStart.setVisibility(View.GONE);
                climbSuccess.setVisibility(View.GONE);
                climbConfirm.setVisibility(View.GONE);
                climbFail.setVisibility(View.GONE);
                climbCancel.setVisibility(View.GONE);
                break;
        }
    }

    private void startClimbing(){
        climbState = Constants.CLIMB_PROGRESS;
        climbEndTime = climbStartTime = SystemClock.elapsedRealtime();
    }

    private void climbSuccess(){
        if(climbState == Constants.CLIMB_PROGRESS)
            climbEndTime = SystemClock.elapsedRealtime();
        climbState = Constants.CLIMB_CONFIRM;
        refreshClimbUI();
    }

    private void climbFail(){
        if(climbState == Constants.CLIMB_PROGRESS)climbEndTime = SystemClock.elapsedRealtime();
        climbState = Constants.CLIMB_FAIL;
    }

    private void climbCancel(){
        if(cancelConfirm) {
            cancelConfirm = false;
            climbStartTime = climbEndTime = 0;
            climbState = Constants.CLIMB_NO;
            refreshClimbUI();
        }else{
            cancelConfirm = true;
            climbCancel.setText("Confirm Cancel?");
        }
    }

    private void climbConfirm(){
        successConfirm = true;
        refreshClimbUI();
        climbState = Constants.CLIMB_SUCCESS;
    }

    private void refreshRocketCargo1UI(){
        if(rocketCargoLevel1Num > 0) rocketCargoLevel1Num--;
        teleopRocketCargoLevel1.setText("Level 1 (" + rocketCargoLevel1Num + ")");
    }

    private void refreshRocketCargo2UI(){
        if(rocketCargoLevel2Num > 0) rocketCargoLevel2Num--;
        teleopRocketCargoLevel2.setText("Level 2 (" + rocketCargoLevel2Num + ")");
    }

    private void refreshRocketCargo3UI(){
        if(rocketCargoLevel3Num > 0) rocketCargoLevel3Num--;
        teleopRocketCargoLevel3.setText("Level 3 (" + rocketCargoLevel3Num + ")");
    }

    private void refreshRocketHatch1UI(){
        if(rocketHatchLevel1Num > 0) rocketHatchLevel1Num--;
        teleopRocketHatchLevel1.setText("Level 1 (" + rocketHatchLevel1Num + ")");
    }

    private void refreshRocketHatch2UI(){
        if(rocketHatchLevel2Num > 0) rocketHatchLevel2Num--;
        teleopRocketHatchLevel2.setText("Level 2 (" + rocketHatchLevel2Num + ")");
    }

    private void refreshRocketHatch3UI(){
        if(rocketHatchLevel3Num > 0) rocketHatchLevel3Num--;
        teleopRocketHatchLevel3.setText("Level 3 (" + rocketHatchLevel3Num + ")");
    }

    private void refreshCargoShipCargoUI(){
        if(cargoShipCargoNum > 0) cargoShipCargoNum--;
        teleopCargoShipCargo.setText("Placed Cargo (" + cargoShipCargoNum + ")");
    }

    private void refreshCargoShipHatchUI(){
        if(cargoShipHatchNum > 0) cargoShipHatchNum--;
        teleopCargoShipHatch.setText("Placed Hatch (" + cargoShipHatchNum + ")");
    }

    @Override
    public String getTitle() {
        return "Teleop";
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.teleop_climb_start:
                startClimbing();
                break;
            case R.id.teleop_climb_success:
                climbSuccess();
                break;
            case R.id.teleop_climb_fail:
                climbFail();
                break;
            case R.id.teleop_climb_cancel:
                climbCancel();
                break;
            case R.id.teleop_climb_confirm:
                climbConfirm();
                break;
            case R.id.bt_teleop_rocket_cargo_level1:
                rocketCargoLevel1Num++;
                teleopRocketCargoLevel1.setText("Level 1 (" + rocketCargoLevel1Num + ")");
                break;
            case R.id.bt_teleop_rocket_cargo_level2:
                rocketCargoLevel2Num++;
                teleopRocketCargoLevel2.setText("Level 2 (" + rocketCargoLevel2Num + ")");
                break;
            case R.id.bt_teleop_rocket_cargo_level3:
                rocketCargoLevel3Num++;
                teleopRocketCargoLevel3.setText("Level 3 (" + rocketCargoLevel3Num +")");
                break;
            case R.id.bt_teleop_rocket_cargo_level1_minus:
                refreshRocketCargo1UI();
                break;
            case R.id.bt_teleop_rocket_cargo_level2_minus:
                refreshRocketCargo2UI();
                break;
            case R.id.bt_teleop_rocket_cargo_level3_minus:
                refreshRocketCargo3UI();
                break;
            case R.id.bt_teleop_rocket_hatch_level1:
                rocketHatchLevel1Num++;
                teleopRocketHatchLevel1.setText("Level 1 (" + rocketHatchLevel1Num +")");
                break;
            case R.id.bt_teleop_rocket_hatch_level2:
                rocketHatchLevel2Num++;
                teleopRocketHatchLevel2.setText("Level 2 (" + rocketHatchLevel2Num +")");
                break;
            case R.id.bt_teleop_rocket_hatch_level3:
                rocketHatchLevel3Num++;
                teleopRocketHatchLevel3.setText("Level 3 (" + rocketHatchLevel3Num +")");
                break;
            case R.id.bt_teleop_rocket_hatch_level1_minus:
                refreshRocketHatch1UI();
                break;
            case R.id.bt_teleop_rocket_hatch_level2_minus:
                refreshRocketHatch2UI();
                break;
            case R.id.bt_teleop_rocket_hatch_level3_minus:
                refreshRocketHatch3UI();
                break;
            case R.id.bt_teleop_cargoship_cargo:
                cargoShipCargoNum++;
                teleopCargoShipCargo.setText("Placed Cargo (" + cargoShipCargoNum + ")");
                break;
            case R.id.bt_teleop_cargoship_cargo_minus:
                refreshCargoShipCargoUI();
                break;
            case R.id.bt_teleop_rotation_control:
                cargoShipHatchNum++;
                teleopCargoShipHatch.setText("Placed Hatch (" + cargoShipHatchNum + ")");
                break;
            case R.id.bt_teleop_cargoship_hatch_minus:
                refreshCargoShipHatchUI();
                break;
            case R.id.bt_teleop_finish:
                ((StandScoutActivity)getActivity()).nextTab();
        }
        refreshUI();
    }

    public void refreshUI() {
        refreshClimbUI();
    }

    public JSONObject getData(){
        JSONObject obj = new JSONObject();
        try {
            obj.put(Constants.JSON_TELEOP_ROCKETCARGOLEVEL1, rocketCargoLevel1Num);
            obj.put(Constants.JSON_TELEOP_ROCKETCARGOLEVEL2, rocketCargoLevel2Num);
            obj.put(Constants.JSON_TELEOP_ROCKETCARGOLEVEL3, rocketCargoLevel3Num);
            obj.put(Constants.JSON_TELEOP_TOTALROCKETCARGO, rocketCargoLevel1Num+rocketCargoLevel2Num+rocketCargoLevel3Num);
            obj.put(Constants.JSON_TELEOP_ROCKETHATCHLEVEL1, rocketHatchLevel1Num);
            obj.put(Constants.JSON_TELEOP_ROCKETHATCHLEVEL2, rocketHatchLevel2Num);
            obj.put(Constants.JSON_TELEOP_ROCKETHATCHLEVEL3, rocketHatchLevel3Num);
            obj.put(Constants.JSON_TELEOP_TOTALROCKETHATCH, rocketHatchLevel1Num+rocketHatchLevel2Num+rocketHatchLevel3Num);
            obj.put(Constants.JSON_TELEOP_CARGOSHIPCARGO, cargoShipCargoNum);
            obj.put(Constants.JSON_TELEOP_CARGOSHIPHATCH, cargoShipHatchNum);

            obj.put(Constants.JSON_TELEOP_CLIMBRESULT, climbState);
            obj.put(Constants.JSON_TELEOP_CLIMBTIME, climbEndTime - climbStartTime);

            obj.put(Constants.JSON_TELEOP_LEVEL1, endLevel1.isChecked());
            obj.put(Constants.JSON_TELEOP_LEVEL2, endLevel2.isChecked());
            obj.put(Constants.JSON_TELEOP_LEVEL3, endLevel3.isChecked());


            //obj.put(Constants.JSON_TELEOP_TOUCHPADTRIGGERED, touchpad.isChecked());

            //obj.put(Constants.JSON_TELEOP_GEARSPLACED, gearsPlaced);
            //obj.put(Constants.JSON_TELEOP_GEARSTAKEN, gearsTaken);
            //obj.put(Constants.JSON_TELEOP_GEARSDROPPED, gearsDropped);

            //obj.put(Constants.JSON_TELEOP_HIGHBALLS, highBalls);
            //obj.put(Constants.JSON_TELEOP_LOWBALLS, lowBalls);
            //obj.put(Constants.JSON_TELEOP_HOPPERCYCLES, hopperCycles);

        }catch(Exception e){
            e.printStackTrace();
        }
        return obj;
    }

    // Unfortunately nothing to do here, as robot may have done nothing
    @Override
    public boolean validate () { return true;}

}
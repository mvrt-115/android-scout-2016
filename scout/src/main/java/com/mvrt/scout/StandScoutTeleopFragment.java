package com.mvrt.scout;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.mvrt.mvrtlib.util.Constants;
import com.mvrt.mvrtlib.util.DataCollectionFragment;
import com.mvrt.mvrtlib.util.Snacker;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;


public class StandScoutTeleopFragment extends DataCollectionFragment implements View.OnClickListener{

    Button climbStart;
    Button climbSuccess;
    Button climbFail;
    Button climbCancel;
    TextView climbStatus;
    boolean cancelConfirm = false;
    String climbState = Constants.CLIMB_NO;
    long climbStartTime = 0, climbEndTime = 0;
    Timer climbTimer;
    TextView climbTimerTextView;

    CheckBox touchpad;

    Button getGear;
    Button loseGear;
    Button placeGear;
    int gearsTaken = 0, gearsPlaced = 0;

    Button highCycle;
    Button lowCycle;
    Button hopper;
    int highCycles = 0, lowCycles = 0, hopperCycles = 0;

    Button teleopFinish;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_standscout_teleop, container, false);
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        initClimbUI(v);

        touchpad = (CheckBox)v.findViewById(R.id.ch_teleop_climb);

        getGear = (Button)v.findViewById(R.id.bt_teleop_getgear);
        getGear.setOnClickListener(this);
        loseGear = (Button)v.findViewById(R.id.bt_teleop_gear_minus);
        loseGear.setOnClickListener(this);
        placeGear = (Button)v.findViewById(R.id.bt_teleop_putgear);
        placeGear.setOnClickListener(this);

        highCycle = (Button)v.findViewById(R.id.bt_teleop_high);
        highCycle.setOnClickListener(this);
        lowCycle = (Button)v.findViewById(R.id.bt_teleop_low);
        lowCycle.setOnClickListener(this);
        hopper = (Button)v.findViewById(R.id.bt_teleop_hopper);
        hopper.setOnClickListener(this);

        teleopFinish = (Button)v.findViewById(R.id.bt_teleop_finish);
        teleopFinish.setOnClickListener(this);
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
        climbStatus = (TextView)v.findViewById(R.id.teleop_climb_status);
        climbTimerTextView = (TextView)v.findViewById(R.id.teleop_climb_timer);
        climbTimer = new Timer();
        climbTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                String text = "";
                if (climbState == Constants.CLIMB_PROGRESS) {
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

        refreshClimbUI();
    }

    private void refreshClimbUI(){
        double timeSec = (climbEndTime - climbStartTime)/1000.0;
        Log.d("MVRT", "Time elapsed: " + timeSec);
        switch(climbState){
            case Constants.CLIMB_NO:
                climbStatus.setText("Climb (timed!)");
                climbStart.setVisibility(View.VISIBLE);
                climbSuccess.setVisibility(View.GONE);
                climbFail.setVisibility(View.GONE);
                climbCancel.setVisibility(View.GONE);
                climbCancel.setText("Cancel");
                break;
            case Constants.CLIMB_PROGRESS:
                climbStatus.setText("Climbing");
                climbStart.setVisibility(View.GONE);
                climbSuccess.setVisibility(View.VISIBLE);
                climbFail.setVisibility(View.VISIBLE);
                climbCancel.setVisibility(View.VISIBLE);
                break;
            case Constants.CLIMB_FAIL:
                climbStatus.setText("Climb Failed in " + timeSec + " seconds");
                climbStart.setVisibility(View.GONE);
                climbSuccess.setVisibility(View.GONE);
                climbFail.setVisibility(View.GONE);
                climbCancel.setVisibility(View.VISIBLE);
                break;
            case Constants.CLIMB_SUCCESS:
                climbStatus.setText("Climb Successful in " + timeSec + " seconds");
                climbStart.setVisibility(View.GONE);
                climbSuccess.setVisibility(View.GONE);
                climbFail.setVisibility(View.VISIBLE);
                climbCancel.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void startClimbing(){
        climbState = Constants.CLIMB_PROGRESS;
        climbEndTime = climbStartTime = SystemClock.elapsedRealtime();
        refreshClimbUI();
    }

    private void climbSuccess(){
        if(climbState == Constants.CLIMB_PROGRESS)climbEndTime = SystemClock.elapsedRealtime();
        climbState = Constants.CLIMB_SUCCESS;
        refreshClimbUI();
    }

    private void climbFail(){
        if(climbState == Constants.CLIMB_PROGRESS)climbEndTime = SystemClock.elapsedRealtime();
        climbState = Constants.CLIMB_FAIL;
        refreshClimbUI();
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
            case R.id.bt_teleop_putgear:
                if(gearsPlaced < 12)gearsPlaced++;
                else Snacker.snack("There are only 12 needed gears!", getActivity(), Snackbar.LENGTH_SHORT);
                placeGear.setText("Place (" + gearsPlaced + ")");
                break;
            case R.id.bt_teleop_getgear:
                gearsTaken++;
                getGear.setText("Get (" + gearsTaken + ")");
                break;
            case R.id.bt_teleop_gear_minus:
                if(gearsTaken > 0) gearsTaken--;
                getGear.setText("Get (" + gearsTaken + ")");
                break;
            case R.id.bt_teleop_high:
                highCycles++;
                highCycle.setText("High (" + highCycles + ")");
                break;
            case R.id.bt_teleop_low:
                lowCycles++;
                lowCycle.setText("Low (" + lowCycles + ")");
                break;
            case R.id.bt_teleop_hopper:
                if(hopperCycles < 4)hopperCycles++;
                else Snacker.snack("There are only 4 hoppers!", getActivity(), Snackbar.LENGTH_SHORT);
                hopper.setText("Hopper (" + hopperCycles + ")");
                break;
            case R.id.bt_teleop_finish:
                ((StandScoutActivity)getActivity()).nextTab();
        }
    }


    public JSONObject getData(){
        JSONObject obj = new JSONObject();
        try {
            obj.put(Constants.JSON_TELEOP_CLIMBRESULT, climbState);
            obj.put(Constants.JSON_TELEOP_CLIMBTIME, climbEndTime - climbStartTime);
            obj.put(Constants.JSON_TELEOP_GEARSPLACED, gearsPlaced);
            obj.put(Constants.JSON_TELEOP_GEARSTAKEN, gearsTaken);
            obj.put(Constants.JSON_TELEOP_HIGHCYCLES, highCycles);
            obj.put(Constants.JSON_TELEOP_LOWCYCLES, lowCycles);
            obj.put(Constants.JSON_TELEOP_HOPPERCYCLES, hopperCycles);

        }catch(Exception e){
            e.printStackTrace();
        }
        return obj;
    }

    // Unfortunately nothing to do here, as robot may have done nothing
    @Override
    public boolean validate () { return true;}

}
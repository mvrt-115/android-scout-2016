package com.mvrt.scout;

import android.graphics.Color;
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

    Button parked;
    Button oppSwitch;
    Button oppSwitchMinus;
    Button scaleBtn;
    Button scaleBtnMinus;
    Button vaultBtn;
    Button vaultBtnMinus;
    Button allianceSwitch;
    Button allianceSwitchMinus;
    Button teleopFinish;

    int opp_switch = 0, scale = 0, vault = 0, all_switch = 0;

    boolean teleopParked = true;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_standscout_teleop, container, false);
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        initClimbUI(v);

        parked = (Button)v.findViewById(R.id.bt_teleop_parked);
        parked.setOnClickListener(this);

        vaultBtn = (Button)v.findViewById(R.id.bt_teleop_vault);
        vaultBtn.setOnClickListener(this);

        vaultBtnMinus = (Button)v.findViewById(R.id.bt_teleop_vault_minus);
        vaultBtnMinus.setOnClickListener(this);

        oppSwitch = (Button)v.findViewById(R.id.bt_teleop_opp_switch);
        oppSwitch.setOnClickListener(this);

        oppSwitchMinus = (Button)v.findViewById(R.id.bt_teleop_opp_switch_minus);
        oppSwitchMinus.setOnClickListener(this);

        scaleBtn = (Button)v.findViewById(R.id.bt_teleop_scale);
        scaleBtn.setOnClickListener(this);

        scaleBtnMinus = (Button)v.findViewById(R.id.bt_teleop_scale_minus);
        scaleBtnMinus.setOnClickListener(this);

        allianceSwitch = (Button)v.findViewById(R.id.bt_teleop_alliance_switch);
        allianceSwitch.setOnClickListener(this);

        allianceSwitchMinus = (Button)v.findViewById(R.id.bt_teleop_alliance_switch_minus);
        allianceSwitchMinus.setOnClickListener(this);

        teleopFinish = (Button)v.findViewById(R.id.bt_teleop_finish);
        teleopFinish.setOnClickListener(this);

        refreshUI();
    }

    public void parked() {
        if (teleopParked) {
            parked.setText("Parked");
            teleopParked = false;
        }
        else {
            parked.setText("Robot Parked?");
            teleopParked = true;
        }
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
                }
                else text = "0.00 seconds";
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
                climbStatus.setTextColor(Color.BLACK);
                climbStatus.setText("Climb (timed, incl. align)");
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
                climbStatus.setTextColor(Color.rgb(255,0,0));
                climbStart.setVisibility(View.GONE);
                climbFail.setVisibility(View.GONE);
                climbSuccess.setVisibility(View.VISIBLE);
                climbCancel.setVisibility(View.VISIBLE);
                break;
            case Constants.CLIMB_SUCCESS:
                climbStatus.setText("Climb Successful in " + timeSec + " seconds");
                climbStatus.setTextColor(Color.rgb(40,180,120));
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
    }

    private void climbSuccess(){
        if(climbState == Constants.CLIMB_PROGRESS)climbEndTime = SystemClock.elapsedRealtime();
        climbState = Constants.CLIMB_SUCCESS;
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
            case R.id.bt_teleop_opp_switch:
                opp_switch++;
                break;
            case R.id.bt_teleop_opp_switch_minus:
                if(opp_switch>0) {
                    opp_switch--;
                }
                break;
            case R.id.bt_teleop_scale:
                scale++;
                break;
            case R.id.bt_teleop_scale_minus:
                if(scale>0) {
                    scale--;
                }
                break;
            case R.id.bt_teleop_alliance_switch:
                all_switch++;
                break;
            case R.id.bt_teleop_alliance_switch_minus:
                if(all_switch>0){
                    all_switch--;
                }
                break;
            case R.id.bt_teleop_vault:
                vault++;
                break;
            case R.id.bt_teleop_vault_minus:
                if(vault>0){
                    vault--;
                }
                break;
            case R.id.bt_teleop_parked:
                parked();
                break;
            case R.id.bt_teleop_finish:
                ((StandScoutActivity)getActivity()).nextTab();
        }
        refreshUI();
    }

    public void refreshUI() {

        oppSwitch.setText("Opponent Switch" + ((opp_switch== 0)?"":" (" + opp_switch+ ")"));
        scaleBtn.setText("Scale" + ((scale == 0)?"":" (" + scale + ")"));
        allianceSwitch.setText("Switch" + ((all_switch== 0)?"":" (" + all_switch+ ")"));
        vaultBtn.setText("Vault" + ((vault== 0)?"":" (" + vault+ ")"));

        refreshClimbUI();
    }

    public JSONObject getData(){
        JSONObject obj = new JSONObject();
        try {
            obj.put(Constants.JSON_TELEOP_CLIMBRESULT, climbState);
            obj.put(Constants.JSON_TELEOP_CLIMBTIME, climbEndTime - climbStartTime);
            obj.put(Constants.JSON_TELEOP_PARKED, teleopParked);
            obj.put(Constants.JSON_TELEOP_OPPSWITCH, opp_switch);
            obj.put(Constants.JSON_TELEOP_SCALE, scale);
            obj.put(Constants.JSON_TELEOP_SWITCH, all_switch);
            obj.put(Constants.JSON_TELEOP_VAULT, vault);

        }catch(Exception e){
            e.printStackTrace();
        }
        return obj;
    }

    // Unfortunately nothing to do here, as robot may have done nothing
    @Override
    public boolean validate () { return true;}

}
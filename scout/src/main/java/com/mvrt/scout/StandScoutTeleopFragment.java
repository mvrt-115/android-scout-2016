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


public class StandScoutTeleopFragment extends DataCollectionFragment implements View.OnClickListener, DefenseCrossingDialogFragment.DefenseSelectedListener {

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

    CheckBox challengeTower;

    Button crossDefense;
    long crossStartTime = 0;
    ArrayList<DefenseCrossing> crossings;
    DefenseCrossingDialogFragment crossingDialogFragment;

    Button intakeBall;
    Button removeBall;
    int intakedBalls = 0;


    Button shootButton;

    public StandScoutTeleopFragment(){
        crossings = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_standscout_teleop, container, false);
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        initShootUI(v);
        initIntakeUI(v);
        initClimbUI(v);
        initCrossUI(v);
        refreshUi();
    }

    @Override
    public void onStop(){
        super.onStop();
        climbTimer.cancel();
        climbTimer.purge();
        Log.d("MVRT", "Climb Timer canceled and purged");
    }

    private void initCrossUI(View v){
        crossingDialogFragment = new DefenseCrossingDialogFragment();
        crossingDialogFragment.setDefenses(((StandScoutActivity) getActivity()).getMatchInfo());
        crossingDialogFragment.setListener(this);

        crossDefense = (Button)v.findViewById(R.id.teleop_cross);
        crossDefense.setOnClickListener(this);
    }

    private void initIntakeUI(View v){
        intakeBall = (Button)v.findViewById(R.id.teleop_intake);
        intakeBall.setOnClickListener(this);
        removeBall = (Button)v.findViewById(R.id.teleop_intakeremove);
        removeBall.setOnClickListener(this);
    }

    private void refreshIntakeUI(){
        intakeBall.setText("Intake Boulder (" + intakedBalls + ")");
    }

    private void intakeBall(){
        intakedBalls++;
        refreshIntakeUI();
    }

    private void removeBall(){
        if(intakedBalls > 0)intakedBalls--;
        refreshIntakeUI();
    }

    private void initClimbUI(View v){
        challengeTower = (CheckBox)v.findViewById(R.id.teleop_challenge);

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
                challengeTower.setEnabled(true);
                break;
            case Constants.CLIMB_PROGRESS:
                climbStatus.setText("Climbing");
                climbStart.setVisibility(View.GONE);
                climbSuccess.setVisibility(View.VISIBLE);
                climbFail.setVisibility(View.VISIBLE);
                climbCancel.setVisibility(View.VISIBLE);
                challengeTower.setEnabled(false);
                challengeTower.setChecked(false);
                break;
            case Constants.CLIMB_FAIL:
                climbStatus.setText("Climb Failed in " + timeSec + " seconds");
                climbStart.setVisibility(View.GONE);
                climbSuccess.setVisibility(View.GONE);
                climbFail.setVisibility(View.GONE);
                climbCancel.setVisibility(View.VISIBLE);
                challengeTower.setEnabled(true);
                break;
            case Constants.CLIMB_SUCCESS:
                climbStatus.setText("Climb Successful in " + timeSec + " seconds");
                climbStart.setVisibility(View.GONE);
                climbSuccess.setVisibility(View.GONE);
                climbFail.setVisibility(View.VISIBLE);
                climbCancel.setVisibility(View.VISIBLE);
                challengeTower.setEnabled(false);
                challengeTower.setChecked(false);
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

    private void initShootUI(View v){
        shootButton = (Button)v.findViewById(R.id.teleop_shoot);
        shootButton.setOnClickListener(this);
    }

    private void shootBall(){
        ((StandScoutActivity)getActivity()).shoot(false);
    }

    private void crossDefense(){
        crossStartTime = SystemClock.elapsedRealtime();
        crossingDialogFragment.show(getFragmentManager(), "MVRT");
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
            case R.id.teleop_intake:
                intakeBall();
                break;
            case R.id.teleop_intakeremove:
                removeBall();
                break;
            case R.id.teleop_shoot:
                shootBall();
                break;
            case R.id.teleop_cross:
                crossDefense();
                break;
        }
        refreshUi();
    }

    public void refreshUi(){
        refreshIntakeUI();
        refreshClimbUI();
    }

    public JSONObject getData(){
        JSONObject obj = new JSONObject();
        try {
            obj.put(Constants.JSON_TELEOP_CLIMBRESULT, climbState);
            obj.put(Constants.JSON_TELEOP_CLIMBTIME, climbEndTime - climbStartTime);
            obj.put(Constants.JSON_TELEOP_CROSSINGS, crossings);
            obj.put(Constants.JSON_TELEOP_INTAKE, intakedBalls);
        }catch(Exception e){}
        return obj;
    }

    // Unfortunately nothing to do here, as robot may have done nothing
    @Override
    public boolean validate () { return true;}

    @Override
    public void onDefenseSelected(String defense) {
        crossings.add(new DefenseCrossing(defense, crossStartTime, SystemClock.elapsedRealtime()));
        crossStartTime = 0;
        Log.d("MVRT", "New crossing: " + crossings.toString());
    }

    @Override
    public void defenseSelectionCanceled() {
        Log.d("MVRT", "Selection Canceled");
        crossStartTime = 0;
    }
}
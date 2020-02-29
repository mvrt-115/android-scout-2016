package com.mvrt.scout;

import android.os.Bundle;

import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mvrt.mvrtlib.util.Constants;
import com.mvrt.mvrtlib.util.DataCollectionFragment;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class StandScoutPostgameFragment extends DataCollectionFragment implements View.OnClickListener {

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

    Button hangAttempt;

    RadioButton hangSuccess;
    RadioButton hangFailed;

    Button levelAttempt;

    RadioButton levelSuccess;
    RadioButton levelFailed;

    RadioButton soloClimb;
    RadioButton multiClimb;

    Button finish;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_stand_scout_postgame, container, false);
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        initClimbUI(v);

        hangAttempt = (Button) v.findViewById(R.id.bt_endgame_hang_attempt);
        hangAttempt.setOnClickListener(this);

        hangSuccess = (RadioButton) v.findViewById(R.id.radio_endgame_hang_success);
        hangFailed = (RadioButton) v.findViewById(R.id.radio_endgame_hang_failed);

        levelAttempt = (Button) v.findViewById(R.id.bt_endgame_leveling_attempt);
        levelAttempt.setOnClickListener(this);

        levelSuccess = (RadioButton) v.findViewById(R.id.radio_endgame_level_success);
        levelFailed = (RadioButton) v.findViewById(R.id.radio_endgame_level_failed);

        soloClimb = (RadioButton) v.findViewById(R.id.radio_endgame_solo_climb);
        multiClimb = (RadioButton) v.findViewById(R.id.radio_endgame_multi_climb);

        finish = (Button)v.findViewById(R.id.bt_postgame_finish);
        finish.setOnClickListener(this);
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

    @Override
    public String getTitle(){
        return "Postgame";
    }

    @Override
    public boolean validate() {
        return true;
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
            case R.id.bt_endgame_hang_attempt:
                refreshHangAttempt();
                break;
            case R.id.bt_endgame_leveling_attempt:
                refreshLevelAttempt();
                break;
            case R.id.bt_postgame_finish:
                ((StandScoutActivity)getActivity()).stopScouting();
        }
        refreshUI();
    }

    public void refreshUI() {
        refreshClimbUI();
    }


    private void refreshHangAttempt() {
        if (!hangAttempt.getText().equals("Attempted"))
            hangAttempt.setText("Attempted");
        else
            hangAttempt.setText("No Attempt");
    }

    private void refreshLevelAttempt() {
        if (!levelAttempt.getText().equals("Attempted"))
            levelAttempt.setText("Attempted");
        else
            levelAttempt.setText("No Attempt");
    }

    @Override
    public JSONObject getData(JSONObject o){
        try {
            o.put("climbTime", (double)(climbEndTime - climbStartTime));

            o.put("hangFail", hangFailed.isChecked());
            o.put("levelFail", levelFailed.isChecked());


            o.put("attemptHang", hangAttempt.getText().equals("Attempted"));
            o.put("attemptLevel", levelAttempt.getText().equals("Attempted"));

            o.put("buddy", multiClimb.isChecked());
        } catch(Exception e){
            e.printStackTrace();
        }
        return o;
    }
}
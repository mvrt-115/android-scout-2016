package com.mvrt.scout;

import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.mvrt.mvrtlib.util.Constants;
import com.mvrt.mvrtlib.util.DataCollectionFragment;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;


public class StandScoutTeleopFragment extends DataCollectionFragment implements View.OnClickListener {

    Button inner;
    Button outer;
    Button bottom;
    Button cycles;

    Button minusInner;
    Button minusOuter;
    Button minusBottom;
    Button minusCycles;

    Button innerMissed;
    Button outerMissed;
    Button bottomMissed;

    Button minusMissedInner;
    Button minusMissedOuter;
    Button minusMissedBottom;

    Button rotationControl;
    Button positionControl;
    Button trech;

    Button finishTeleop;

    RadioButton defenseYes;
    RadioButton defenseNo;

    CheckBox disabled;
    CheckBox stuck;

    int innerNum = 0;
    int outerNum = 0;
    int bottomNum = 0;

    int innerMissedNum = 0;
    int outerMissedNum = 0;
    int bottomMissedNum = 0;

    int cyclesNum = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_stand_scout_teleop, container, false);
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {

        inner = (Button) v.findViewById(R.id.bt_teleop_inner);
        inner.setOnClickListener(this);
        outer = (Button) v.findViewById(R.id.bt_teleop_outer);
        outer.setOnClickListener(this);
        bottom = (Button) v.findViewById(R.id.bt_teleop_bottom);
        bottom.setOnClickListener(this);
        cycles = (Button) v.findViewById(R.id.bt_teleop_cycles);
        cycles.setOnClickListener(this);

        minusInner = (Button) v.findViewById(R.id.bt_teleop_inner_minus);
        minusInner.setOnClickListener(this);
        minusOuter = (Button) v.findViewById(R.id.bt_teleop_outer_minus);
        minusOuter.setOnClickListener(this);
        minusBottom = (Button) v.findViewById(R.id.bt_teleop_bottom_minus);
        minusBottom.setOnClickListener(this);
        minusCycles = (Button) v.findViewById(R.id.bt_teleop_cycles_minus);
        minusCycles.setOnClickListener(this);

        innerMissed = (Button) v.findViewById(R.id.bt_teleop_missed_inner);
        innerMissed.setOnClickListener(this);
        outerMissed = (Button) v.findViewById(R.id.bt_teleop_missed_outer);
        outerMissed.setOnClickListener(this);
        bottomMissed = (Button) v.findViewById(R.id.bt_teleop_missed_bottom);
        bottomMissed.setOnClickListener(this);

        minusMissedInner = (Button) v.findViewById(R.id.bt_teleop_missed_inner_minus);
        minusMissedInner.setOnClickListener(this);
        minusMissedOuter = (Button) v.findViewById(R.id.bt_teleop_missed_outer_minus);
        minusMissedOuter.setOnClickListener(this);
        minusMissedBottom = (Button) v.findViewById(R.id.bt_teleop_missed_bottom_minus);
        minusMissedBottom.setOnClickListener(this);

        rotationControl = (Button) v.findViewById(R.id.bt_teleop_rotation_control);
        rotationControl.setOnClickListener(this);

        positionControl = (Button) v.findViewById(R.id.bt_teleop_position_control);
        positionControl.setOnClickListener(this);

        trech = (Button) v.findViewById(R.id.bt_teleop_trench);
        trech.setOnClickListener(this);

        defenseYes = (RadioButton) v.findViewById(R.id.radio_teleop_defense_yes);
        defenseNo = (RadioButton) v.findViewById(R.id.radio_teleop_defense_no);

        disabled = (CheckBox) v.findViewById(R.id.cb_teleop_disabled);
        stuck = (CheckBox) v.findViewById(R.id.cb_teleop_stuck);

        finishTeleop = (Button) v.findViewById(R.id.bt_teleop_finish);
        finishTeleop.setOnClickListener(this);
    }

    private void refreshInner() {
        if (innerNum > 0) innerNum--;
        inner.setText("INNER (" + innerNum + ")");
    }

    private void refreshOuter() {
        if (outerNum > 0) outerNum--;
        outer.setText("OUTER (" + outerNum + ")");
    }

    private void refreshBottom() {
        if (bottomNum > 0) bottomNum--;
        bottom.setText("BOTTOM (" + bottomNum + ")");
    }

    private void refreshInnerMissed() {
        if (innerMissedNum > 0) innerMissedNum--;
        innerMissed.setText("MISSED INNER (" + innerMissedNum + ")");
    }

    private void refreshOuterMissed() {
        if (outerMissedNum > 0) outerMissedNum--;
        outerMissed.setText("MISSED OUTER (" + outerMissedNum + ")");
    }

    private void refreshBottomMissed() {
        if (bottomMissedNum > 0) bottomMissedNum--;
        bottomMissed.setText("MISSED BOTTOM (" + bottomMissedNum + ")");
    }

    private void refreshCycles() {
        if (cyclesNum > 0) cyclesNum--;
        cycles.setText("CYCLES (" + cyclesNum + ")");
    }

    private void refreshRotationControl() {
        if (!rotationControl.getText().equals("Rotation CONTROL ENABLED"))
            rotationControl.setText("Rotation CONTROL ENABLED");
        else
            rotationControl.setText("Rotation CONTROL DISABLED");
    }

    private void refreshPositionControl() {
        if (!positionControl.getText().equals("POSITION CONTROL ENABLED"))
            positionControl.setText("POSITION CONTROL ENABLED");
        else
            positionControl.setText("POSITION CONTROL DISABLED");
    }

    private void refreshTrench() {
        if (!trech.getText().equals("YES"))
            trech.setText("YES");
        else
            trech.setText("NO");
    }

    @Override
    public String getTitle() {
        return "Teleop";
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bt_teleop_inner:
                innerNum++;
                inner.setText("INNER (" + innerNum + ")");
                break;
            case R.id.bt_teleop_outer:
                outerNum++;
                outer.setText("OUTER (" + outerNum + ")");
                break;
            case R.id.bt_teleop_bottom:
                bottomNum++;
                bottom.setText("BOTTOM (" + bottomNum + ")");
                break;
            case R.id.bt_teleop_cycles:
                cyclesNum++;
                cycles.setText("CYCLES (" + cyclesNum + ")");
            case R.id.bt_teleop_inner_minus:
                refreshInner();
                break;
            case R.id.bt_teleop_outer_minus:
                refreshOuter();
                break;
            case R.id.bt_teleop_bottom_minus:
                refreshBottom();
                break;
            case R.id.bt_teleop_missed_inner:
                innerMissedNum++;
                innerMissed.setText("MISSED INNER (" + innerMissedNum + ")");
                break;
            case R.id.bt_teleop_missed_outer:
                outerMissedNum++;
                outerMissed.setText("MISSED OUTER (" + outerMissedNum + ")");
                break;
            case R.id.bt_teleop_missed_bottom:
                bottomMissedNum++;
                bottomMissed.setText("MISSED BOTTOM (" + bottomMissedNum + ")");
                break;
            case R.id.bt_teleop_missed_inner_minus:
                refreshInnerMissed();
                break;
            case R.id.bt_teleop_missed_outer_minus:
                refreshOuterMissed();
                break;
            case R.id.bt_teleop_missed_bottom_minus:
                refreshBottomMissed();
                break;
            case R.id.bt_teleop_cycles_minus:
                refreshCycles();
                break;
            case R.id.bt_teleop_rotation_control:
                refreshRotationControl();
                break;
            case R.id.bt_teleop_position_control:
                refreshPositionControl();
                break;
            case R.id.bt_teleop_trench:
                refreshTrench();
                break;

            case R.id.bt_teleop_finish:
                ((StandScoutActivity)getActivity()).nextTab();
        }

        int innerMissedNum = 0;
        int outerMissedNum = 0;
        int bottomMissedNum = 0;
    }

    public JSONObject getData(JSONObject obj){
        try {
            obj.put("teleopUpper", innerNum+outerNum);
            obj.put("teleopUpperMissed", innerMissedNum+outerMissedNum);
            obj.put("teleopBottom", bottomNum);
            obj.put("teleopBottomMissed", bottomMissedNum);

            obj.put("trench", trech.getText().equals("YES"));

            obj.put("defense", defenseYes.isChecked());

            obj.put("rotation", rotationControl.getText().equals("ROTATION CONTROL ENABLED"));
            obj.put("position", positionControl.getText().equals("POSITION CONTROL ENABLED"));

            obj.put("stuck", stuck.isChecked());
            obj.put("disabled", disabled.isChecked());
            obj.put("cycles", cyclesNum);

        }catch(Exception e){
            e.printStackTrace();
        }
        return obj;
    }


    @Override
    public boolean validate () { return true;}

}
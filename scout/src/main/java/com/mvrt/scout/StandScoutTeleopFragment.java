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


public class StandScoutTeleopFragment extends DataCollectionFragment implements View.OnClickListener {

    Button inner;
    Button outer;
    Button bottom;

    Button minusInner;
    Button minusOuter;
    Button minusBottom;

    Button rotationControl;
    Button positionControl;
    Button trech;

    Button finishTeleop;

    int innerNum = 0;
    int outerNum = 0;
    int bottomNum = 0;


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

        minusInner = (Button) v.findViewById(R.id.bt_teleop_inner_minus);
        minusInner.setOnClickListener(this);
        minusOuter = (Button) v.findViewById(R.id.bt_teleop_outer_minus);
        minusOuter.setOnClickListener(this);
        minusBottom = (Button) v.findViewById(R.id.bt_teleop_bottom_minus);
        minusBottom.setOnClickListener(this);

        rotationControl = (Button) v.findViewById(R.id.bt_teleop_rotation_control);
        rotationControl.setOnClickListener(this);

        positionControl = (Button) v.findViewById(R.id.bt_teleop_position_control);
        positionControl.setOnClickListener(this);

        trech = (Button) v.findViewById(R.id.bt_teleop_trench);
        trech.setOnClickListener(this);

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
            case R.id.bt_auton_inner:
                innerNum++;
                inner.setText("INNER (" + innerNum + ")");
                break;
            case R.id.bt_auton_outer:
                outerNum++;
                outer.setText("INNER (" + outerNum + ")");
                break;
            case R.id.bt_auton_bottom:
                bottomNum++;
                bottom.setText("BOTTOM (" + bottomNum + ")");
                break;
            case R.id.bt_auton_inner_minus:
                refreshInner();
                break;
            case R.id.bt_auton_outer_minus:
                refreshOuter();
                break;
            case R.id.bt_auton_bottom_minus:
                refreshBottom();
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
    }

    public JSONObject getData(){
        JSONObject obj = new JSONObject();
        try {
            obj.put(Constants.JSON_TELEOP_INNER, innerNum);
            obj.put(Constants.JSON_TELEOP_OUTER, outerNum);
            obj.put(Constants.JSON_TELEOP_BOTTOM, bottomNum);
            obj.put(Constants.JSON_TELEOP_TOTAL, innerNum + outerNum + bottomNum);

            obj.put(Constants.JSON_TELEOP_ROTATION, positionControl.getText().equals("ROTATION CONTROL ENABLED"));
            obj.put(Constants.JSON_TELEOP_POSITION, crossInitiationLine.getText().equals("POSITION CONTROL ENABLED"));
            obj.put(Constants.JSON_TELEOP_TRENCH, crossInitiationLine.getText().equals("YES"));

            obj.put(Constants.JSON_TELEOP_DEFENSE_YES, endLevel1.isChecked());
            obj.put(Constants.JSON_TELEOP_DEFENSE_NO, endLevel2.isChecked());


        }catch(Exception e){
            e.printStackTrace();
        }
        return obj;
    }

    @Override
    public boolean validate () { return true;}

}
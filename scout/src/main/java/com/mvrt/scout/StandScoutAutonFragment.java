package com.mvrt.scout;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import com.mvrt.mvrtlib.util.Constants;
import com.mvrt.mvrtlib.util.DataCollectionFragment;

import org.json.JSONObject;

public class StandScoutAutonFragment extends DataCollectionFragment implements View.OnClickListener {


    RadioButton level1;
    RadioButton level2;
    RadioButton level3;

    Button powerCell;

    Button minusPowerCell;

    Button crossInitiationLine;

    Button inner;
    Button outer;
    Button bottom;

    Button minusInner;
    Button minusOuter;
    Button minusBottom;

    Button innerMissed;
    Button outerMissed;
    Button bottomMissed;

    Button minusMissedInner;
    Button minusMissedOuter;
    Button minusMissedBottom;

    Button finishAuton;

    int powerCellNum;

    int innerNum = 0;
    int outerNum = 0;
    int bottomNum = 0;

    int innerMissedNum = 0;
    int outerMissedNum = 0;
    int bottomMissedNum = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_stand_scout_auton, container, false);
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        level1 = (RadioButton) v.findViewById(R.id.radio_auton_level1);
        level2 = (RadioButton) v.findViewById(R.id.radio_auton_level2);
        level3 = (RadioButton) v.findViewById(R.id.radio_auton_level3);

        powerCell = (Button) v.findViewById(R.id.bt_auton_power_cells);
        powerCell.setOnClickListener(this);
        minusPowerCell = (Button) v.findViewById(R.id.bt_auton_power_cells_minus);
        minusPowerCell.setOnClickListener(this);

        crossInitiationLine = (Button) v.findViewById(R.id.bt_auton_initiation_line);
        crossInitiationLine.setOnClickListener(this);

        inner = (Button) v.findViewById(R.id.bt_auton_inner);
        inner.setOnClickListener(this);
        outer = (Button) v.findViewById(R.id.bt_auton_outer);
        outer.setOnClickListener(this);
        bottom = (Button) v.findViewById(R.id.bt_auton_bottom);
        bottom.setOnClickListener(this);

        minusInner = (Button) v.findViewById(R.id.bt_auton_inner_minus);
        minusInner.setOnClickListener(this);
        minusOuter = (Button) v.findViewById(R.id.bt_auton_outer_minus);
        minusOuter.setOnClickListener(this);
        minusBottom = (Button) v.findViewById(R.id.bt_auton_bottom_minus);
        minusBottom.setOnClickListener(this);

        innerMissed = (Button) v.findViewById(R.id.bt_auton_missed_inner);
        innerMissed.setOnClickListener(this);
        outerMissed = (Button) v.findViewById(R.id.bt_auton_missed_outer);
        outerMissed.setOnClickListener(this);
        bottomMissed = (Button) v.findViewById(R.id.bt_auton_missed_bottom);
        bottomMissed.setOnClickListener(this);

        minusMissedInner = (Button) v.findViewById(R.id.bt_auton_missed_inner_minus);
        minusMissedInner.setOnClickListener(this);
        minusMissedOuter = (Button) v.findViewById(R.id.bt_auton_missed_outer_minus);
        minusMissedOuter.setOnClickListener(this);
        minusMissedBottom = (Button) v.findViewById(R.id.bt_auton_missed_bottom_minus);
        minusMissedBottom.setOnClickListener(this);

        finishAuton = (Button) v.findViewById(R.id.bt_auton_finish);
        finishAuton.setOnClickListener(this);
    }

    private void refreshInitationLine() {
        if (!crossInitiationLine.getText().equals("Crossed"))
            crossInitiationLine.setText("Crossed");
        else
            crossInitiationLine.setText("Does not Cross Initiation Line");
    }

    private void refreshPowerCell() {
        if (powerCellNum > 0) powerCellNum--;
        powerCell.setText("POWER CELL (" + powerCellNum + ")");
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

    @Override
    public String getTitle() {
        return "Sandstorm";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_auton_power_cells:
                powerCellNum++;
                powerCell.setText("POWER CELLS (" + powerCellNum + ")");
                break;
            case R.id.bt_auton_power_cells_minus:
                refreshPowerCell();
                break;

            case R.id.bt_auton_initiation_line:
                refreshInitationLine();
                break;

            case R.id.bt_auton_inner:
                innerNum++;
                inner.setText("INNER (" + innerNum + ")");
                break;
            case R.id.bt_auton_outer:
                outerNum++;
                outer.setText("OUTER (" + outerNum + ")");
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
            case R.id.bt_auton_missed_inner:
                innerMissedNum++;
                innerMissed.setText("MISSED INNER (" + innerMissedNum + ")");
                break;
            case R.id.bt_auton_missed_outer:
                outerMissedNum++;
                outerMissed.setText("MISSED OUTER (" + outerMissedNum + ")");
                break;
            case R.id.bt_auton_missed_bottom:
                bottomMissedNum++;
                bottomMissed.setText("MISSED BOTTOM (" + bottomMissedNum + ")");
                break;
            case R.id.bt_auton_missed_inner_minus:
                refreshInnerMissed();
                break;
            case R.id.bt_auton_missed_outer_minus:
                refreshOuterMissed();
                break;
            case R.id.bt_auton_missed_bottom_minus:
                refreshBottomMissed();
                break;
            case R.id.bt_auton_finish:
                ((StandScoutActivity) getActivity()).nextTab();
                break;
        }
    }


    public JSONObject getData(JSONObject obj) {
        try {
            obj.put("startLocations", level1.isChecked()+","+level2.isChecked()+","+level3.isChecked() );

            obj.put("initLineCrosses", crossInitiationLine.getText().equals("Crossed"));

            obj.put("preloads", powerCellNum);

            obj.put("autonUpper", innerNum+outerNum);
            obj.put("autonUpperMissed", innerMissedNum+outerMissedNum);

            obj.put("autonBottom", bottomNum);
            obj.put("autonBottomMissed", bottomMissedNum);
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


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

    Button finishAuton;

    int powerCellNum;

    int innerNum = 0;
    int outerNum = 0;
    int bottomNum = 0;

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

        finishAuton = (Button) v.findViewById(R.id.bt_auton_finish);
        finishAuton.setOnClickListener(this);
    }

    private void refreshInitationLine() {
        if (!crossInitiationLine.getText().equals("Crossed"))
            crossInitiationLine.setText("Crossed");
        else
            crossInitiationLine.setText("Crosses Initiation Line");
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

            case R.id.bt_auton_finish:
                ((StandScoutActivity) getActivity()).nextTab();
                break;
        }
    }


    public JSONObject getData() {
        JSONObject obj = new JSONObject();
        try {
            obj.put(Constants.JSON_AUTON_LEVEL1, level1.isChecked());
            obj.put(Constants.JSON_AUTON_LEVEL2, level2.isChecked());
            obj.put(Constants.JSON_AUTON_LEVEL3, level3.isChecked());

            obj.put(Constants.JSON_AUTON_PRELOAD, powerCellNum);

            obj.put(Constants.JSON_AUTON_CROSSD_LINE, crossInitiationLine.getText().equals("Crossed"));

            obj.put(Constants.JSON_AUTON_INNER, innerNum);
            obj.put(Constants.JSON_AUTON_OUTER, outerNum);
            obj.put(Constants.JSON_AUTON_BOTTOM, bottomNum);
            obj.put(Constants.JSON_AUTON_TOTAL, innerNum + outerNum + bottomNum);
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


package com.mvrt.scout;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.mvrt.mvrtlib.util.DataCollectionFragment;

import org.json.JSONObject;


public class StandScoutAutonFragment extends DataCollectionFragment implements CompoundButton.OnCheckedChangeListener {

    // Set up variables for items
    RadioGroup startingPos;
    CheckBox binsFromStep;
    EditText numberBinsFromStep;
    CheckBox yellowTotes;
    EditText numberYellowTotes;
    CheckBox greyTotes;
    CheckBox mobility;
    CheckBox interference;
    CheckBox noshow;

    // RadioButton for ErrorCreation
    RadioButton startStaging;

    // Inflaters
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_standscout_auton, container, false);
    }

    // FindViewById for all items
    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        startingPos = (RadioGroup) v.findViewById(R.id.autonscout_start_radio);
        binsFromStep = (CheckBox) v.findViewById(R.id.autonscout_bins_checkbox);
        binsFromStep.setOnCheckedChangeListener(this);
        numberBinsFromStep = (EditText) v.findViewById(R.id.autonscout_bins_number);
        yellowTotes = (CheckBox) v.findViewById(R.id.autonscout_totes_yellow);
        yellowTotes.setOnCheckedChangeListener(this);
        numberYellowTotes = (EditText) v.findViewById(R.id.autonscout_yellow_number);
        greyTotes = (CheckBox) v.findViewById(R.id.autonscout_totes_interact);
        mobility = (CheckBox) v.findViewById(R.id.autonscout_mobility);
        interference = (CheckBox) v.findViewById(R.id.autonscout_interfere);
        noshow = (CheckBox) v.findViewById(R.id.autonscout_noshow);

        // ErrorHandling RadioButton
        startStaging = (RadioButton) v.findViewById(R.id.autonscout_start_staging);
    }

    // Send title of tab
    @Override
    public String getTitle() {
        return "Autonomous";
    }

    // Clear all errors
    public void clearErrors() {
        numberBinsFromStep.setError(null);
    }

    // Send Data to JSONObject
    @Override
    public JSONObject getData() {

        // CLear errors before starting
        clearErrors();

        // Get Starting Position
        String starting_pos;
        switch (startingPos.getCheckedRadioButtonId()) {
            case R.id.autonscout_start_staging:
                starting_pos = "Staging Area";
                break;
            case R.id.autonscout_start_landfill:
                starting_pos = "Landfill";
                break;
            default:
                Toast.makeText(getActivity(), "Select a starting position", Toast.LENGTH_LONG).show();
                return null;
        }

        // Get bins from Step including numbers
        int bins_from_step = 0;
        String numberStep = numberBinsFromStep.getText().toString();
        if(numberStep.equals("")) bins_from_step = 0;
        else
            bins_from_step = Integer.parseInt(numberStep);

        // Get yellow totes including numbers
        int yellow_totes = 0;
        String numberYellow = numberYellowTotes.getText().toString();
        yellow_totes = Integer.parseInt(numberYellow);

        // Create JSONObject and send data with try Catch
        JSONObject data = new JSONObject();
        try {
            data.put("Starting Position", starting_pos);
            data.put("Bins from Step", bins_from_step);
            data.put("Yellow Totes", yellow_totes);
            // Send data based on checkbox as boolean
            data.put("Grey Totes", greyTotes.isChecked());
            data.put("Mobility", mobility.isChecked());
            data.put("Intereference", interference.isChecked());
            data.put("No Show", noshow.isChecked());
        } catch (Exception e) {
            Log.e("MVRT", "JSON Error");
        }

        return data;
    }

    @Override
    public boolean validate() {

        // Track value so that errors are still run
        boolean completed = true;

        // Check so that 0 cannot be entered
        if (binsFromStep.isChecked()) {
            String number = numberBinsFromStep.getText().toString();
            if (number.length() == 0) {
                numberBinsFromStep.setError("Enter a valid number of totes");
                completed = false;
            }
        }

        // Check so that 0 cannot be entered
        if (yellowTotes.isChecked()) {
            String number = numberYellowTotes.getText().toString();
            if (number.length() == 0) {
                numberYellowTotes.setError("Enter a valid number of totes");
                completed = false;
            }
        }

        // Might have to rewrite, not sure if this works
        if (startingPos.getCheckedRadioButtonId() == -1) {
            startStaging.setError("Select a Starting Position");
            completed = false;
        }

        return completed;

    }

    // Pop the underside EditText if boxes are checked
    @Override
    public void onCheckedChanged(CompoundButton view, boolean isChecked) {
        if (view.getId() == R.id.autonscout_bins_checkbox) {
            numberBinsFromStep.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        } else if (view.getId() == R.id.autonscout_totes_yellow) {
            numberYellowTotes.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        }
    }

}
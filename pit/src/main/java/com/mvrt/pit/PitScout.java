package com.mvrt.pit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;

public class PitScout extends AppCompatActivity implements View.OnClickListener {

    EditText team;

    CheckBox lowBar;
    CheckBox rockWall;
    CheckBox roughTerrain;
    CheckBox drawbridge;
    CheckBox sallyport;
    CheckBox portcullis;
    CheckBox cheval;
    CheckBox ramparts;
    CheckBox moat;

    CheckBox climb;
    CheckBox challenge;

    CheckBox intake;
    CheckBox highGoal;
    CheckBox lowGoal;

    EditText numMotors;
    EditText numWheels;
    EditText typeWheels;

    CheckBox autonReach;
    CheckBox autonBreak;
    CheckBox autonHighShot;
    CheckBox autonLowShot;

    EditText driverExp;
    EditText weight;
    EditText strategy;

    Button finish;
    Button clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pit_scout);
    }

    private void initUI () {
        initToolbar();
        initDefenseUI();
        initBallUI();
        initClimbUI();
        initDriveUI();
        initAutonUI();
        initStrategyUI();
        initOtherUI();
    }

    private void initToolbar(){
        setTitle("Pit Scout");
        Toolbar t = (Toolbar)findViewById(R.id.pitscout_toolbar);
        setSupportActionBar(t);
    }

    private void initDefenseUI () {
        lowBar = (CheckBox)findViewById(R.id.low_bar);
        rockWall = (CheckBox)findViewById(R.id.rock_wall);
        roughTerrain = (CheckBox)findViewById(R.id.rough_terrain);
        drawbridge = (CheckBox)findViewById(R.id.drawbridge);
        sallyport = (CheckBox)findViewById(R.id.sallyport);
        portcullis = (CheckBox)findViewById(R.id.portcullis);
        cheval = (CheckBox)findViewById(R.id.low_bar);
        ramparts = (CheckBox)findViewById(R.id.ramparts);
        moat = (CheckBox)findViewById(R.id.moat);
    }

    private void initClimbUI () {
        climb = (CheckBox)findViewById(R.id.climb);
        challenge = (CheckBox)findViewById(R.id.challenge);
    }

    private void initDriveUI () {
        numMotors = (EditText)findViewById(R.id.numMotors);
        numWheels = (EditText)findViewById(R.id.numWheels);
        typeWheels = (EditText)findViewById(R.id.wheelType);
    }

    private void initBallUI () {
        intake = (CheckBox)findViewById(R.id.intake);
        highGoal = (CheckBox)findViewById(R.id.high_goal);
        lowGoal = (CheckBox)findViewById(R.id.low_goal);
    }

    private void initAutonUI () {
        autonReach = (CheckBox)findViewById(R.id.auton_reach);
        autonBreak = (CheckBox)findViewById(R.id.auton_break);
        autonHighShot = (CheckBox)findViewById(R.id.auton_high_goal);
        autonLowShot = (CheckBox)findViewById(R.id.auton_low_goal);
    }

    private void initStrategyUI () {
        driverExp = (EditText)findViewById(R.id.driverExperience);
        weight = (EditText)findViewById(R.id.weight);
        strategy = (EditText)findViewById(R.id.strategy);
    }

    private void initOtherUI () {
        team = (EditText)findViewById(R.id.team);
        finish = (Button)findViewById(R.id.pit_finish);
        finish.setOnClickListener(this);
        clear = (Button)findViewById(R.id.pit_clear);
        clear.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pit_scout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_localdata) {
            Intent i = new Intent(this, LocalDataActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick (View v) {
        switch (v.getId()) {
            case R.id.pit_finish:
                pushData();
                break;
            case R.id.pit_clear:
                clearData();
                break;
        }
    }

    private void pushData(){
        int teamNo = Integer.parseInt(team.getText().toString());

        JSONObject obj = new JSONObject();
        try{
            obj.put("Team",teamNo);

            obj.put("Low Bar", lowBar.isChecked());
            obj.put("Rock Wall", rockWall.isChecked());
            obj.put("Rough Terrain", roughTerrain.isChecked());
            obj.put("Ramparts", ramparts.isChecked());
            obj.put("Moat", moat.isChecked());
            obj.put("Drawbridge", drawbridge.isChecked());
            obj.put("Sally Port", sallyport.isChecked());
            obj.put("Portcullis", portcullis.isChecked());
            obj.put("Cheval", cheval.isChecked());

            obj.put("Climber", climb.isChecked());
            obj.put("Challenge", challenge.isChecked());

            obj.put("Intake", intake.isChecked());
            obj.put("High Goal", highGoal.isChecked());
            obj.put("Low Goal", lowGoal.isChecked());

            obj.put("Number of Motors", Integer.parseInt(numMotors.getText().toString()));
            obj.put("Number of Wheels", Integer.parseInt(numWheels.getText().toString()));
            obj.put("Type of Wheels", typeWheels.getText().toString());

            obj.put("Auton Reach", autonReach.isChecked());
            obj.put("Auton Break", autonBreak.isChecked());
            obj.put("Auton Low", autonLowShot.isChecked());
            obj.put("Auton High", autonHighShot.isChecked());

            obj.put("Driver Regionals", Integer.parseInt(driverExp.getText().toString()));
            obj.put("Weight", Integer.parseInt(weight.getText().toString()));
            obj.put("Strategy", strategy.getText().toString());

        } catch(Exception e){
            Log.e("MVRT", "JSON Error");
        }
        try {
            writeToFile(obj, teamNo);

        } catch (JSONException e) {
            Log.e("MVRT", "JSON Error");
        }
        Toast feed = Toast.makeText(getApplicationContext(),
                "Sent Data. Close app and re-open.",
                Toast.LENGTH_SHORT);
        feed.show();
    }

    private void clearData() {
        team.setText("");
        team.setHint("Team Number");

        lowBar.setChecked(false);
        rockWall.setChecked(false);
        roughTerrain.setChecked(false);
        sallyport.setChecked(false);

    }

    public String writeToFile(JSONObject data, int team) throws JSONException {
        String filename = "pitscout-" + team + ".json";
        try {
            FileOutputStream fos = openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(data.toString().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filename;
    }

}

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
    EditText weight;
    EditText maxHeight;
    EditText drivetrain;
    EditText speed;
    EditText driver_experience;
    EditText programming_language;
    EditText cyclesPerMatch;
    EditText autoPaths;
    EditText teleopPaths;

    CheckBox hang;
    CheckBox level;

    CheckBox bottom_port_shooter;
    CheckBox inner_port_shooter;
    CheckBox outer_port_shooter;

    CheckBox rotation_control;
    CheckBox position_control;

    EditText intake;
    EditText storage;

    Button finish;
    Button clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pit_scout);
    }

    private void initUI () {
        team = (EditText)findViewById(R.id.team);
        weight = (EditText)findViewById(R.id.weight);
        maxHeight = (EditText)findViewById(R.id.max_height);
        drivetrain = (EditText)findViewById(R.id.drivetrain);
        speed = (EditText)findViewById(R.id.speed);
        driver_experience = (EditText)findViewById(R.id.driver_experience);
        programming_language = (EditText)findViewById(R.id.programming_language);
        cyclesPerMatch = (EditText)findViewById(R.id.cycles_per_match);
        autoPaths = (EditText)findViewById(R.id.auto_paths);
        teleopPaths = (EditText)findViewById(R.id.teleop_paths);

        hang = (CheckBox)findViewById(R.id.hang);
        level = (CheckBox)findViewById(R.id.level);

        bottom_port_shooter = (CheckBox)findViewById(R.id.bottom_port_shooter);
        inner_port_shooter = (CheckBox)findViewById(R.id.inner_port_shooter);
        outer_port_shooter = (CheckBox)findViewById(R.id.outer_port_shooter);

        rotation_control = (CheckBox)findViewById(R.id.rotation_control);
        position_control = (CheckBox)findViewById(R.id.position_control);

        intake = (EditText)findViewById(R.id.intake);
        storage = (EditText)findViewById(R.id.storage);

        finish = (Button)findViewById(R.id.pit_finish);
        finish.setOnClickListener(this);
        clear = (Button)findViewById(R.id.pit_clear);
        clear.setOnClickListener(this);
    }

    private void initToolbar(){
        setTitle("Pit Scout");
        Toolbar t = (Toolbar)findViewById(R.id.pitscout_toolbar);
        setSupportActionBar(t);
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
            obj.put("team", Integer.parseInt(team.getText().toString()));
            obj.put("weight", Integer.parseInt(weight.getText().toString()));
            obj.put("maxHeight", Integer.parseInt(maxHeight.getText().toString()));
            obj.put("drivetrain", drivetrain.getText().toString());
            obj.put("speed", Integer.parseInt(speed.getText().toString()));
            obj.put("driver_experience", driver_experience.getText().toString());
            obj.put("programming_language", programming_language.getText().toString());
            obj.put("cyclesPerMatch", Integer.parseInt(cyclesPerMatch.getText().toString()));
            obj.put("autoPaths", autoPaths.getText().toString());
            obj.put("teleopPaths", teleopPaths.getText().toString());

            obj.put("hang", hang.isChecked());
            obj.put("level", level.isChecked());

            obj.put("bottom_port_shooter", bottom_port_shooter.isChecked());
            obj.put("inner_port_shooter", inner_port_shooter.isChecked());
            obj.put("Rough outer_port_shooter", outer_port_shooter.isChecked());

            obj.put("rotation_control", rotation_control.isChecked());
            obj.put("position_control", position_control.isChecked());

            obj.put("intake", intake.getText().toString());
            obj.put("storage", storage.getText().toString());

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
        weight.setText("");
        maxHeight.setText("");
        drivetrain.setText("");
        speed.setText("");
        driver_experience.setText("");
        programming_language.setText("");
        cyclesPerMatch.setText("");
        autoPaths.setText("");
        teleopPaths.setText("");

        hang.setChecked(false);
        level.setChecked(false);

        bottom_port_shooter.setChecked(false);
        inner_port_shooter.setChecked(false);
        outer_port_shooter.setChecked(false);

        rotation_control.setChecked(false);
        position_control.setChecked(false);

        intake.setText("");
        storage.setText("");
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

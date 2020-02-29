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

import com.mvrt.mvrtlib.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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
    EditText climber;
    EditText centerOfGravity;

    CheckBox hang;
    CheckBox level;

    CheckBox bottom_port_shooter;
    CheckBox inner_port_shooter;
    CheckBox outer_port_shooter;

    CheckBox rotation_control;
    CheckBox position_control;

    EditText intake;
    EditText hopperCapacity;

    Button finish;
    Button clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pit_scout);

        initUI();
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
        climber = (EditText) findViewById(R.id.climber);
        centerOfGravity = (EditText)findViewById(R.id.centerOfGravity);

        hang = (CheckBox)findViewById(R.id.hang);
        level = (CheckBox)findViewById(R.id.level);

        bottom_port_shooter = (CheckBox)findViewById(R.id.bottom_port_shooter);
        inner_port_shooter = (CheckBox)findViewById(R.id.inner_port_shooter);
        outer_port_shooter = (CheckBox)findViewById(R.id.outer_port_shooter);

        rotation_control = (CheckBox)findViewById(R.id.rotation_control);
        position_control = (CheckBox)findViewById(R.id.position_control);

        intake = (EditText)findViewById(R.id.intake);
        hopperCapacity = (EditText)findViewById(R.id.hopper_capacity);

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
        Toast feed = Toast.makeText(getApplicationContext(),
                ""+v.getId(),
                Toast.LENGTH_SHORT);
        feed.show();
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
        try {

            final int teamNo = Integer.parseInt(team.getText().toString());

            final JSONObject obj = new JSONObject();
            try{
                obj.put("teamNum", Integer.parseInt(team.getText().toString()));
                obj.put("drivetrain", drivetrain.getText().toString());
                obj.put("weight", Integer.parseInt(weight.getText().toString()));
                obj.put("height", Integer.parseInt(maxHeight.getText().toString()));

                obj.put("climber", climber.getText().toString());
                obj.put("centerOfGravity", centerOfGravity.getText().toString());

                obj.put("canHang", hang.isChecked());
                obj.put("canLevel", level.isChecked());
                obj.put("speed", Integer.parseInt(speed.getText().toString()));
                obj.put("innerShoot", inner_port_shooter.isChecked());
                obj.put("outerShoot", outer_port_shooter.isChecked());
                obj.put("bottomShoot", bottom_port_shooter.isChecked());
                obj.put("canRotation", rotation_control.isChecked());
                obj.put("canPosition", position_control.isChecked());
                obj.put("hopperCapacity", hopperCapacity.getText().toString());
                obj.put("auton", autoPaths.getText().toString());
                obj.put("avgCycles", Integer.parseInt(cyclesPerMatch.getText().toString()));
                obj.put("intake", intake.getText().toString());
                obj.put("language", programming_language.getText().toString());
                obj.put("generalPaths", teleopPaths.getText().toString());
                obj.put("driveteamExp", driver_experience.getText().toString());

            } catch(Exception e){
                Log.e("MVRT", "JSON Error");

            }
            try {
                writeToFile(obj, teamNo);

            } catch (JSONException e) {
                Log.e("MVRT", "JSON Error");
            }

            new Thread(){
                public void run(){
                    try {
                        Log.e("JSON", obj.toString());
                        URL url = new URL(Constants.PitUrl);
                        String data = obj.toString();
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                        conn.setRequestProperty("Accept", "application/json");
                        conn.setFixedLengthStreamingMode(data.getBytes().length);

                        conn.setDoOutput(true);
                        conn.setDoInput(true);
                        conn.connect();

                        OutputStream os = new BufferedOutputStream(conn.getOutputStream());
                        os.write(data.getBytes());
                        os.flush();
                        os.close();

                        InputStream ip = conn.getInputStream();
                        BufferedReader br1 = new BufferedReader(new InputStreamReader(ip));

                        // Print the response code
                        // and response message from server.
                        System.out.println("Response Code:" + conn.getResponseCode());
                        System.out.println("Response Message:" + conn.getResponseMessage());

                        Log.e("response", "Response Code:" + conn.getResponseCode());
                        Log.e("response", "Response Message:" + conn.getResponseMessage());

                        // to print the 1st header field.
                        System.out.println("Header field 1:" + conn.getHeaderField(1));

                        // print the response
                        StringBuilder response = new StringBuilder();
                        String responseSingle = null;
                        while ((responseSingle = br1.readLine()) != null)
                        {
                            response.append(responseSingle);
                        }

                        Log.e("response", response.toString());
                        ip.close();
                        conn.disconnect();
                        Toast feed = Toast.makeText(getApplicationContext(),
                                "Sent Data. Close app and re-open.",
                                Toast.LENGTH_SHORT);
                        feed.show();
                    } catch(Exception e) {
                        Log.e("response", e.toString());
                    }
                }
            }.start();

        } catch(NumberFormatException e){
            Toast feed = Toast.makeText(getApplicationContext(),
                    "Invalid response",
                    Toast.LENGTH_SHORT);
            feed.show();
        }

    }

    private void clearData() {
        Toast feed = Toast.makeText(getApplicationContext(),
                "Clear Data",
                Toast.LENGTH_SHORT);
        feed.show();
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
        hopperCapacity.setText("");
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

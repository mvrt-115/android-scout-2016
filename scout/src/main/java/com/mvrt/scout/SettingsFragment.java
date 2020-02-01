package com.mvrt.scout;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mvrt.mvrtlib.util.Constants;

public class SettingsFragment extends Fragment implements View.OnClickListener {

    Spinner scoutID;
    SharedPreferences prefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

        prefs = getActivity().getSharedPreferences(Constants.SHARED_PREFS_NAME_SCOUT, Activity.MODE_PRIVATE);

        scoutID = (Spinner)getView().findViewById(R.id.settings_scoutid);

        CharSequence[] idArray = {"1", "2",  "3"};
        ArrayAdapter<CharSequence> ids = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_dropdown_item, idArray);
        scoutID.setAdapter(ids);

        FloatingActionButton fab = (FloatingActionButton) getView().findViewById(R.id.settings_save);
        fab.setOnClickListener(this);
    }

    @Override
    public void onStart(){
        super.onStart();
        loadSettings();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.settings_save:
                saveSettings();
                break;
        }
    }

    public void loadSettings(){
        int scoutid = prefs.getInt(Constants.PREFS_SCOUTID_KEY, 0);
        scoutID.setSelection(scoutid);
    }

    public void saveSettings(){
        int scoutid = scoutID.getSelectedItemPosition(); //ID IS 0,1,2
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(Constants.PREFS_SCOUTID_KEY, scoutid);
        Log.d("MVRT", "Scout id is " + scoutid);
        editor.commit();
        Toast.makeText(getActivity().getApplicationContext(), "Settings saved", Toast.LENGTH_LONG).show();

        int a = prefs.getInt(Constants.PREFS_SCOUTID_KEY, 5);
        Log.d("MVRT", "Scout ID is " + a);
    }


}

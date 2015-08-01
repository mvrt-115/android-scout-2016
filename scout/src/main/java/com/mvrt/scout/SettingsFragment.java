package com.mvrt.scout;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.mvrt.mvrtlib.util.Constants;

public class SettingsFragment extends Fragment implements View.OnClickListener {

    EditText tournamentText;
    Spinner scoutID;
    SharedPreferences prefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

        prefs = getActivity().getPreferences(Activity.MODE_PRIVATE);

        tournamentText = (EditText)getView().findViewById(R.id.settings_tournament);
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
        String tournament = prefs.getString(Constants.PREFS_TOURNAMENT_KEY, Constants.PREFS_TOURNAMENT_DEFAULT);
        tournamentText.setText(tournament);

        int scoutid = prefs.getInt(Constants.PREFS_SCOUTID_KEY, 0);
        scoutID.setSelection(scoutid);
    }

    public void saveSettings(){
        String tournament = tournamentText.getText().toString().toUpperCase();
        if(tournament.length() == 0){
            tournamentText.setError("Please Enter a Tournament Code");
            return;
        }else tournamentText.setError(null);
        int scoutid = scoutID.getSelectedItemPosition(); //ID IS 0,1,2
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.PREFS_TOURNAMENT_KEY, tournament);
        editor.putInt(Constants.PREFS_SCOUTID_KEY, scoutid);
        editor.commit();
        snackBar("Settings saved", Snackbar.LENGTH_SHORT);
    }

    private void snackBar(String text, int length){
        Snackbar b = Snackbar.make(getView(), text, length);
        ((TextView)b.getView().findViewById(android.support.design.R.id.snackbar_text)).setTextColor(getResources().getColor(R.color.text_primary_light));
        b.show();
    }

}

package com.mvrt.scout;

import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mvrt.mvrtlib.util.Constants;
import com.mvrt.mvrtlib.util.MatchInfo;

/**
 * @author Bubby
 */
public class MatchDataFragment extends Fragment implements View.OnClickListener{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_matchdata, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        loadData(view);
        Button ble = (Button)view.findViewById(R.id.start_ble);
        ble.setOnClickListener(this);

        Button finish = (Button)view.findViewById(R.id.finish_scouting);
        finish.setOnClickListener(this);

    }

    public void loadData(View v){
        TextView matchData = (TextView)v.findViewById(R.id.match_data);
        matchData.setText("Data should be put here..");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.start_ble:
                Snackbar b = Snackbar.make(view, "BLE Started", Snackbar.LENGTH_SHORT);
                ((TextView)b.getView().findViewById(android.support.design.R.id.snackbar_text)).setTextColor(getResources().getColor(R.color.text_primary_light));
                b.show();
                break;
            case R.id.finish_scouting:
                MatchScoutingDataActivity activity = (MatchScoutingDataActivity) getActivity();
                activity.finish();
                break;
            default:
                break;
        }
    }
}

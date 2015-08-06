package com.mvrt.scout;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.Layout;
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

    private boolean sentData = false;

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
                sentData = true;
                makeSnack(view, "BLE Started", Snackbar.LENGTH_SHORT);
                break;
            case R.id.finish_scouting:
                if(sentData)
                    getActivity().finish();
                else
                    new VerificationDialog().show(getFragmentManager(), "Code Verification");
                break;
            default:
                break;
        }
    }

    public void makeSnack(View view, String text, int time){
        Snackbar b = Snackbar.make(view, text, time);
        ((TextView)b.getView().findViewById(android.support.design.R.id.snackbar_text)).setTextColor(getResources().getColor(R.color.text_primary_light));
        b.show();
    }

    public static class VerificationDialog extends DialogFragment{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = getActivity().getLayoutInflater();

            builder.setTitle("Verify your code!");
            builder.setView(inflater.inflate(R.layout.dialog_code_verification, null))
                    .setPositiveButton("Verify", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //Verify entered code against code created
                        }
                    })
                    .setNegativeButton("Force Quit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            getActivity().finish();
                        }
                    });
            return builder.create();
        }
    }

}

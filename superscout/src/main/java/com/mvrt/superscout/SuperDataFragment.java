package com.mvrt.superscout;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mvrt.mvrtlib.util.Constants;

/**
 * @author Bubby
 */
public class SuperDataFragment extends Fragment{

    TextView list;
    String queue = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_super_data, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        final ImageButton startQr = (ImageButton) view.findViewById(R.id.getdata_qrbutton);
        final Button finishButton = (Button) view.findViewById(R.id.finish);
        list = (TextView)view.findViewById(R.id.superdata_list);
        list.setText(queue);
        startQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQR();

            }
        });
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishScouting();
            }
        });
    }

    public void addData(int team, String code){
        String toAppend = "T " + team + ", verif. code: " + code;
        queue += toAppend + System.getProperty("line.separator");
        if(list != null){
            list.setText(queue);
        }
    }

    public void startQR(){
        try {
            Intent intent = new Intent(Constants.INTENT_QR);
            intent.putExtra(Constants.INTENT_QR_SCANMODE_KEY, Constants.INTENT_QR_SCANMODE);
            startActivityForResult(intent, Constants.REQUEST_QR_SCAN);
        }catch (Exception e){
            Snackbar.make(getView(), "Cannot launch scanner", Snackbar.LENGTH_LONG);
        }
    }

    public void finishScouting(){
        ((SuperScoutActivity)getActivity()).finishScouting();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if(requestCode == Constants.REQUEST_QR_SCAN){
            if(resultCode == Activity.RESULT_OK) {
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String result = data.getStringExtra(Constants.INTENT_QR_SCANRESULT_KEY);
                        ((SuperScoutActivity) getActivity()).addQRMatchData(result);
                    }
                }, 500);
            }
        }
    }
}

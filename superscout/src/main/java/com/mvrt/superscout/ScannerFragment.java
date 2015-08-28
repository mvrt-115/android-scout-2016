package com.mvrt.superscout;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.mvrt.mvrtlib.util.Constants;
/**
 * @author Bubby
 */
public class ScannerFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_start_scan, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        final ImageButton startQr = (ImageButton) view.findViewById(R.id.getdata_qrbutton);
        final Button finishButton = (Button) view.findViewById(R.id.finish);
        startQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQR();
            }
        });
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Constants.REQUEST_QR_SCAN){
            if(resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra(Constants.INTENT_QR_SCANRESULT_KEY);
                Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
            }
        }
    }
}

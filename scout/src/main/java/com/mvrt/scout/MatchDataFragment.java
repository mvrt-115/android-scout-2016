package com.mvrt.scout;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.mvrt.mvrtlib.util.Constants;
import com.mvrt.mvrtlib.util.MatchInfo;
import com.zxing.Contents;
import com.zxing.QRCodeEncoder;

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
        loadQr("LOLZ this is not real data pls fix meaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy" +
                "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");  //TODO: fix
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

    /*
        Generates a QR code to send back match data

        Tested on 8/15, works for "hella long data" - akhil :P
        http://puu.sh/jBPRS/9480591718.png <- could be scanned relatively easily, so we're good
     */
    public void loadQr(String data) {
        ImageView imageView = (ImageView)getView().findViewById(R.id.matchdata_qr);

        int qrCodeDimension = 1000;

        QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(data, null,
                Contents.Type.TEXT, BarcodeFormat.QR_CODE.toString(), qrCodeDimension);

        try {
            Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
            imageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
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

            builder.setTitle("Verify Data Transfer");
            builder.setView(inflater.inflate(R.layout.dialog_code_verification, null))
                    .setNeutralButton("Verify", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //Verify entered code against code created
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    }).setPositiveButton("Exit Anyways", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getActivity().finish();
                        }
                    });
            return builder.create();
        }
    }

}

package com.mvrt.scout;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import android.app.Fragment;
import androidx.appcompat.app.AlertDialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.zxing.Contents;
import com.zxing.QRCodeEncoder;

import org.json.JSONObject;


public class MatchDataFragment extends Fragment implements View.OnClickListener{

    private boolean sentData = false;
    private String verificationCode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_match_data, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        loadData(view);

        Button finish = (Button)view.findViewById(R.id.finish_scouting);
        finish.setOnClickListener(this);

    }

    public String getVerificationCode(){
        return verificationCode;
    }

    public void loadData(View v){
        JSONObject obj = ((MatchScoutingDataActivity) getActivity()).getData();

        verificationCode = "";

        try{
            verificationCode = "" + obj.getInt("verif");
        }catch(Exception e){}

        loadQr(obj.toString());
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.finish_scouting:
                if(sentData)
                    getActivity().finish();
                else {
                    verify();
                }
                break;
            default:
                break;
        }
    }

    public void verify() {
        VerificationDialog d = new VerificationDialog();
        Bundle b = new Bundle();
        b.putString("verif", verificationCode);
        d.setArguments(b);
        d.show(getFragmentManager(), "Code Verification");
    }

    /*
        Generates a QR code to send back match data

        Tested on 8/15, works for "hella long data" - akhil :P
     */
    public void loadQr(String data) {
        ImageView imageView = (ImageView)getView().findViewById(R.id.matchdata_qr);

        int qrCodeDimension = 1000;

        QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(data, null,
                Contents.Type.TEXT, BarcodeFormat.QR_CODE.toString(), qrCodeDimension);

        try {
            Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
            imageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class VerificationDialog extends DialogFragment {

        String verification;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            verification = getArguments().getString("verif");

            LayoutInflater inflater = getActivity().getLayoutInflater();

            builder.setTitle("Verify Data Transfer");
            builder.setView(inflater.inflate(R.layout.fragment_confirm_sync_dialog, null))
                    .setNeutralButton("Verify", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Dialog d = (Dialog)dialog;
                            EditText verCode = (EditText) d.findViewById(R.id.verification_enter);
                            if(verCode.getText().toString().equalsIgnoreCase(verification)){
                                getActivity().finish();
                            }else{
                                Toast.makeText(getActivity().getApplicationContext(), "Incorrect Code", Toast.LENGTH_LONG).show();
                                d.cancel();
                            }
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

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
//kappa
import com.mvrt.mvrtlib.util.Snacker;
import com.zxing.Contents;
import com.zxing.QRCodeEncoder;

import org.json.JSONObject;

/**
 * @author Bubby
 */
public class MatchDataFragment extends Fragment implements View.OnClickListener{

    private boolean sentData = false;
    String verificationCode;

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
        JSONObject obj = ((MatchScoutingDataActivity) getActivity()).getData();
        JSONObject parent = new JSONObject();

        int code = (int)(Math.random() * 8999 + 1000);
        verificationCode = "" + code;

        try{
            parent.put("data", obj);
            parent.put("verif", code);
        }catch(Exception e){}

        loadQr(parent.toString());
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.start_ble:
                Snacker.snack("BLE Unavailable. Use NFC or QR", getActivity(), Snackbar.LENGTH_LONG);
                break;
            case R.id.finish_scouting:
                if(sentData)
                    getActivity().finish();
                else {
                    VerificationDialog d = new VerificationDialog();
                    Bundle b = new Bundle();
                    b.putString("verif", verificationCode);
                    d.setArguments(b);
                    d.show(getFragmentManager(), "Code Verification");
                }
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

    public static class VerificationDialog extends DialogFragment{

        String verification;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            verification = getArguments().getString("verif");

            LayoutInflater inflater = getActivity().getLayoutInflater();

            builder.setTitle("Verify Data Transfer");
            builder.setView(inflater.inflate(R.layout.dialog_code_verification, null))
                    .setNeutralButton("Verify", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Dialog d = (Dialog)dialog;
                            EditText verCode = (EditText) d.findViewById(R.id.verification_enter);
                            if(verCode.getText().toString().equalsIgnoreCase(verification)){
                                getActivity().finish();
                            }else{
                                Snacker.snack("Incorrect Code", getActivity(), Snackbar.LENGTH_SHORT);
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

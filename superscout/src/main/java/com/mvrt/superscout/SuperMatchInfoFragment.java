package com.mvrt.superscout;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.mvrt.mvrtlib.util.Constants;
import com.mvrt.mvrtlib.util.MatchInfo;
import com.zxing.Contents;
import com.zxing.QRCodeEncoder;

public class SuperMatchInfoFragment extends Fragment{

    MatchInfo matchInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_super_matchinfo, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        loadData(view);
        loadQr();
    }

    public void loadData(View v){
        matchInfo = (MatchInfo)getArguments().getSerializable(Constants.INTENT_EXTRA_MATCHINFO);

        TextView alliance = (TextView)v.findViewById(R.id.matchinfo_alliance);
        alliance.setText(matchInfo.getAllianceString());
        alliance.setTextColor(getResources().getColor(matchInfo.getAllianceColorId()));

        TextView matchKey = (TextView)v.findViewById(R.id.matchinfo_matchkey);
        matchKey.setText(matchInfo.getMatchNo() + " @ " + matchInfo.getTournament());

        TextView teams = (TextView)v.findViewById(R.id.matchinfo_teams);
        teams.setText("Teams " + matchInfo.getTeam(0) + ", " + matchInfo.getTeam(1) + ", " + matchInfo.getTeam(2));

        TextView key = (TextView)v.findViewById(R.id.matchinfo_key);
        key.setText(matchInfo.toString());

    }

    public void loadQr() {
        ImageView imageView = (ImageView)getView().findViewById(R.id.matchinfo_qr);

        int qrCodeDimension = 1000;

        QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(matchInfo.toString(), null,
                Contents.Type.TEXT, BarcodeFormat.QR_CODE.toString(), qrCodeDimension);

        try {
            Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
            imageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

}

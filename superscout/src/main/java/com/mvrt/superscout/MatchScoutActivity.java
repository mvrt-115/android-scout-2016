package com.mvrt.superscout;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.mvrt.mvrtlib.util.MatchInfo;
import com.zxing.Contents;
import com.zxing.QRCodeEncoder;

import java.io.Serializable;

/**
 * Created by Samster on 7/28/2015.
 */
public class MatchScoutActivity extends ActionBarActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_match_scout);
            loadIntentData();
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_match_scout, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }

            return super.onOptionsItemSelected(item);
        }


        int team;
        String matchKey;
        char alliance;

        public void loadIntentData(){
            Serializable data = getIntent().getSerializableExtra("Match Info");
            ImageView imageView = (ImageView)findViewById(R.id.match_qrCode);

            MatchInfo info = (MatchInfo)data;

            int qrCodeDimension = 1000;

            QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(data.toString(), null,
                    Contents.Type.TEXT, BarcodeFormat.QR_CODE.toString(), qrCodeDimension);

            try {
                Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
                imageView.setImageBitmap(bitmap);
            } catch (WriterException e) {
                e.printStackTrace();
            }

            Toast.makeText(this, info.toString(), Toast.LENGTH_LONG).show();
        }

    }


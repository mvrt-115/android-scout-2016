package com.mvrt.scout;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mvrt.mvrtlib.util.Constants;
import com.mvrt.mvrtlib.util.DataCollectionFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class ShootingFragment extends DataCollectionFragment implements View.OnTouchListener, View.OnClickListener {

    char alliance = Constants.ALLIANCE_RED;

    Canvas canvas;
    ImageView img;
    TextView textView;
    Bitmap crossHatch;
    Bitmap shotMade;
    Bitmap shotMissed;
    Bitmap field;

    float[] coords = null;

    ArrayList<float[]> madeShots;
    ArrayList<float[]> missedShots;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_standscout_postgame, container, false);
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        initImages();
    }

    private void initImages(){
        textView = (TextView)(getView().findViewById(R.id.selectlocation_textview));

        field = BitmapFactory.decodeResource(getResources(),
                (alliance == Constants.ALLIANCE_RED)?R.drawable.red_field:R.drawable.blue_field);
        Bitmap temp = field.copy(Bitmap.Config.ARGB_8888, true);
        crossHatch = BitmapFactory.decodeResource(getResources(), R.mipmap.location_crosshatch);
        shotMade = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_shotmade);
        shotMissed = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_shotmissed);

        canvas = new Canvas(temp);
        img = (ImageView)(getView().findViewById(R.id.selectlocation_imageview));
        img.setOnTouchListener(this);
        img.setImageBitmap(temp);
        madeShots = new ArrayList<>();
        missedShots = new ArrayList<>();

        ImageButton make = (ImageButton)(getView().findViewById(R.id.selectlocation_make));
        make.setOnClickListener(this);
        ImageButton miss = (ImageButton)(getView().findViewById(R.id.selectlocation_miss));
        miss.setOnClickListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent e) {
        coords = touchToGlobal(e.getX(), e.getY());

        Log.d("MVRT", Arrays.toString(coords));

        drawCanvas();
        return false;
    }

    public void updateTextView(){
        String stats = "Shots made: " + madeShots.size() + " / " + (madeShots.size() + missedShots.size());
        textView.setText(stats);
    }

    public void drawCanvas(){
        canvas.drawBitmap(field, 0, 0, new Paint());

        for(float[] shot: madeShots) {
            float[] imageCoords = globalToImage(shot);
            canvas.drawBitmap(shotMade, imageCoords[0] - shotMade.getWidth() / 2,
                    imageCoords[1] - shotMade.getHeight() / 2, new Paint());
        }
        for(float[] shot: missedShots) {
            float[] imageCoords = globalToImage(shot);
            canvas.drawBitmap(shotMissed, imageCoords[0] - shotMissed.getWidth() / 2,
                    imageCoords[1] - shotMissed.getHeight() / 2, new Paint());
        }

        if(coords != null) {
            float[] imageCoords = globalToImage(coords);
            canvas.drawBitmap(crossHatch, imageCoords[0] - crossHatch.getWidth() / 2,
                    imageCoords[1] - crossHatch.getHeight() / 2, new Paint());
        }

        img.invalidate();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.selectlocation_make:
                if(coords != null)madeShots.add(coords);
                break;
            case R.id.selectlocation_miss:
                if(coords != null)missedShots.add(coords);
                break;
        }
        coords = null;
        drawCanvas();
        updateTextView();
    }

    public float[] touchToImage(float touchX, float touchY){
        float imageX = (touchX) * canvas.getWidth() / (float)img.getWidth();
        float imageY = (touchY) * canvas.getHeight() / (float)img.getHeight();
        return new float[]{imageX, imageY};
    }

    public float[] imageToGlobal(float[] imageCoords){
        float globalX = imageCoords[0] * 100f / canvas.getWidth();
        float globalY = imageCoords[1] * 100f / canvas.getHeight();
        if(alliance == Constants.ALLIANCE_BLUE){
            globalX = 100 - globalX;
            globalY = 100 - globalY;
        }
        return new float[]{globalX, globalY};
    }

    public float[] globalToImage(float[] globalCoords){
        float globalX = globalCoords[0];
        float globalY = globalCoords[1];
        if(alliance == Constants.ALLIANCE_BLUE){
            globalX = 100 - globalX;
            globalY = 100 - globalY;
        }
        float imageX = globalX * canvas.getWidth() / 100f;
        float imageY = globalY * canvas.getHeight() / 100f;
        return new float[]{imageX, imageY};
    }

    public float[] touchToGlobal(float touchX, float touchY){
        return imageToGlobal(touchToImage(touchX, touchY));
    }

    @Override
    public JSONObject getData() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("hello", "wow");
        } catch (JSONException e) {}
        return obj;
    }

    @Override
    public boolean validate() {
        return true;
    }

    @Override
    public String getTitle() {
        return "Shooting";
    }
}
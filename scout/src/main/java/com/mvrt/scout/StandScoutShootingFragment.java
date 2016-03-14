package com.mvrt.scout;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mvrt.mvrtlib.util.Constants;
import com.mvrt.mvrtlib.util.DataCollectionFragment;
import com.mvrt.mvrtlib.util.MatchInfo;
import com.mvrt.mvrtlib.util.Snacker;
import com.mvrt.mvrtlib.util.TowerShot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class StandScoutShootingFragment extends DataCollectionFragment implements View.OnTouchListener, View.OnClickListener {

    char alliance = Constants.ALLIANCE_RED;

    Canvas canvas;
    ImageView img;
    TextView textView;
    Bitmap crossHatch;
    Bitmap highMade, highMissed, lowMade, lowMissed;
    Bitmap field;

    int noHighMade = 0;
    int noHighMissed = 0;
    int noLowMade = 0;
    int noLowMissed = 0;

    int[] coords = null;

    ArrayList<TowerShot> shots;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_standscout_shooting, container, false);
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        loadData();
        initUI();
    }

    private void loadData(){
        MatchInfo matchInfo = (MatchInfo)getArguments().getSerializable(Constants.INTENT_EXTRA_MATCHINFO);
        if(matchInfo != null)alliance = matchInfo.getAlliance();
    }

    private void initUI(){
        textView = (TextView)(getView().findViewById(R.id.selectlocation_textview));

        field = BitmapFactory.decodeResource(getResources(),
                (alliance == Constants.ALLIANCE_RED)?R.drawable.red_field:R.drawable.blue_field);
        Bitmap temp = field.copy(Bitmap.Config.ARGB_8888, true);
        crossHatch = BitmapFactory.decodeResource(getResources(), R.mipmap.location_crosshatch);

        highMade = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_high_make);
        highMissed = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_high_miss);
        lowMade = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_low_make);
        lowMissed = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_low_miss);

        canvas = new Canvas(temp);
        img = (ImageView)(getView().findViewById(R.id.selectlocation_imageview));
        img.setOnTouchListener(this);
        img.setImageBitmap(temp);
        shots = new ArrayList<>();

        getView().findViewById(R.id.shoot_high_make).setOnClickListener(this);
        getView().findViewById(R.id.shoot_high_miss).setOnClickListener(this);
        getView().findViewById(R.id.shoot_low_make).setOnClickListener(this);
        getView().findViewById(R.id.shoot_low_miss).setOnClickListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent e) {
        coords = touchToGlobal(e.getX(), e.getY());

        Log.d("MVRT", Arrays.toString(coords));

        drawCanvas();
        return false;
    }

    public void updateTextView(){
        String high = "High: " + noHighMade + "/" + (noHighMade + noHighMissed);
        String low = ", Low: " + noLowMade + "/" + (noLowMade + noLowMissed);
        textView.setText(high + low);
    }

    public void drawCanvas(){
        canvas.drawBitmap(field, 0, 0, new Paint());

        for(TowerShot shot: shots){
            if(shot.getHighGoal() && shot.getMade())drawIcon(shot.getCoords(), highMade);
            else if(!shot.getHighGoal() && shot.getMade())drawIcon(shot.getCoords(), lowMade);
            else if(shot.getHighGoal())drawIcon(shot.getCoords(), highMissed);
            else drawIcon(shot.getCoords(), lowMissed);
        }

        if(coords != null) {
            float[] imageCoords = globalToImage(coords);
            canvas.drawBitmap(crossHatch, imageCoords[0] - crossHatch.getWidth() / 2,
                    imageCoords[1] - crossHatch.getHeight() / 2, new Paint());
        }

        img.invalidate();
    }

    private void drawIcon(int[] globalCoords, Bitmap map){
        float[] imageCoords = globalToImage(globalCoords);
        canvas.drawBitmap(map, imageCoords[0] - map.getWidth() / 2,
                imageCoords[1] - map.getHeight() / 2, new Paint());
    }

    @Override
    public void onClick(View view) {
        if(coords == null){
            Snacker.snack("Please tap to select coordinates", getActivity(), Snackbar.LENGTH_LONG);
            return;
        }
        switch(view.getId()){
            case R.id.shoot_high_make:
                shots.add(new TowerShot(coords, true, true));
                noHighMade++;
                break;
            case R.id.shoot_high_miss:
                shots.add(new TowerShot(coords, false, true));
                noHighMissed++;
                break;
            case R.id.shoot_low_make:
                shots.add(new TowerShot(coords, true, false));
                noLowMade++;
                break;
            case R.id.shoot_low_miss:
                shots.add(new TowerShot(coords, false, false));
                noLowMissed++;
                break;
        }
        coords = null;
        drawCanvas();
        updateTextView();
        ((StandScoutActivity)getActivity()).setTab(1);
    }

    public float[] touchToImage(float touchX, float touchY){
        float imageX = (touchX) * canvas.getWidth() / (float)img.getWidth();
        float imageY = (touchY) * canvas.getHeight() / (float)img.getHeight();
        return new float[]{imageX, imageY};
    }

    public int[] imageToGlobal(float[] imageCoords){
        int globalX = (int)imageCoords[0] * 100 / canvas.getWidth();
        int globalY = (int)imageCoords[1] * 100 / canvas.getHeight();
        if(alliance == Constants.ALLIANCE_BLUE){
            globalX = 100 - globalX;
            globalY = 100 - globalY;
        }
        return new int[]{globalX, globalY};
    }

    public float[] globalToImage(int[] globalCoords){
        int globalX = globalCoords[0];
        int globalY = globalCoords[1];
        if(alliance == Constants.ALLIANCE_BLUE){
            globalX = 100 - globalX;
            globalY = 100 - globalY;
        }
        float imageX = globalX * canvas.getWidth() / 100f;
        float imageY = globalY * canvas.getHeight() / 100f;
        return new float[]{imageX, imageY};
    }

    public int[] touchToGlobal(float touchX, float touchY){
        return imageToGlobal(touchToImage(touchX, touchY));
    }

    @Override
    public JSONObject getData() {
        JSONObject o = new JSONObject();
        JSONArray shotArray = new JSONArray();
        try{
            for(TowerShot shot:shots)shotArray.put(shot.toString());
            o.put(Constants.JSON_SHOOTING_SHOTS, shotArray);
        }catch (JSONException e){ e.printStackTrace(); }
        return o;
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
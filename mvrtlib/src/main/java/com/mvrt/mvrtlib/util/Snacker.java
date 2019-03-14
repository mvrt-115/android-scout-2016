package com.mvrt.mvrtlib.util;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import com.mvrt.mvrtlib.R;

/**
 * Created by Akhil on 9/15/2015.
 */
public class Snacker {

    public static void snack(String text, View v, Context c, int duration){
        Snackbar b = Snackbar.make(v, text, duration);
        ((TextView)b.getView().findViewById(R.id.snackbar_text)).setTextColor(c.getResources().getColor(R.color.text_primary_light));
        b.show();
    }

    public static void snack(String text, Activity a, int duration){
        snack(text, a.getWindow().getDecorView(), a, duration);
    }


}

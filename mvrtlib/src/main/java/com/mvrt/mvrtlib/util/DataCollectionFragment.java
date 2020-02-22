package com.mvrt.mvrtlib.util;

import android.app.Fragment;

import org.json.JSONObject;

/**
 * Created by Aditya on 8/3/15.
 */

public abstract class DataCollectionFragment extends Fragment {

    public abstract JSONObject getData(JSONObject obj);
    public abstract boolean validate();
    public abstract String getTitle();

}

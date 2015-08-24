package com.mvrt.scout.view;

import android.support.v4.app.Fragment;

import org.json.JSONObject;

/**
 * Created by Aditya on 8/3/15.
 */

public abstract class DataCollectionFragment extends Fragment {

    public abstract JSONObject getData();
    public abstract boolean validate();
    public abstract String getTitle();

}

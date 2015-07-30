package com.mvrt.superscout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mvrt.mvrtlib.nav.drawer.DrawerFragment;

/**
 * Created by Samster on 7/28/2015.
 */
public class DeployMatchFragment extends DrawerFragment {

    @Override
    public String getName() {
        return "Deploy Match";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_deploy_match, container, false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}

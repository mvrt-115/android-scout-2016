package com.mvrt.mvrtlib.nav.drawer;

import android.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;

import com.mvrt.mvrtlib.R;

public class NavDrawer implements View.OnClickListener{

    View view;

    RecyclerView navRecycler;
    DrawerFragmentListAdapter fragmentListAdapter;
    DrawerLayout drawerLayout;
    ActionBarActivity activity;

    DrawerFragment visibleFragment;

    public NavDrawer(ActionBarActivity act, DrawerFragment... frags){

        activity = act;
        view = View.inflate(act, R.layout.nav_drawer, null);

        navRecycler = (RecyclerView) view.findViewById(R.id.nav_drawer_recycler);
        navRecycler.setLayoutManager(new LinearLayoutManager(act));
        fragmentListAdapter = new DrawerFragmentListAdapter(this, frags);
        navRecycler.setAdapter(fragmentListAdapter);

        drawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);

        if(frags.length > 0)showFragment(0);
    }

    public void setupToggle(final Toolbar toolbar){
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(activity, drawerLayout,
                toolbar, R.string.drawer_open, R.string.drawer_close);

        drawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }

    public CharSequence getFragmentTitle(CharSequence appTitle){
        if(visibleFragment == null)return appTitle;
        else return visibleFragment.getName();
    }

    public View getParentView(){
        return view;
    }

    public void addFragment(DrawerFragment frag){
        fragmentListAdapter.addFragment(frag);
    }

    public void clearFragments(){
        fragmentListAdapter.clearFragments();
    }

    public void showDrawer(boolean show){
        if(show)drawerLayout.openDrawer(Gravity.START);
        else drawerLayout.closeDrawers();
    }

    @Override
    public void onClick(View v) {
        int pos = navRecycler.getChildPosition(v);
        showFragment(pos);
        showDrawer(false);
    }

    public void showFragment(int pos){
        visibleFragment = fragmentListAdapter.getFragment(pos);
        FragmentManager fragmentManager = activity.getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, visibleFragment).commit();
    }
}

package com.mvrt.mvrtlib.nav.drawer;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mvrt.mvrtlib.R;

import java.util.ArrayList;
import java.util.List;

public class DrawerFragmentListAdapter extends RecyclerView.Adapter<DrawerFragmentListAdapter.ViewHolder>{

    List<DrawerFragment> fragments;
    View.OnClickListener clickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView titleView;
        public ViewHolder(View v){
            super(v);
            titleView = (TextView)v.findViewById(R.id.nav_drawer_item_title);
        }
        public void setTitle(String title){
            titleView.setText(title);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public DrawerFragmentListAdapter(View.OnClickListener listener, DrawerFragment... frags) {
        clickListener = listener;
        fragments = new ArrayList<>();
        for(DrawerFragment f:frags)fragments.add(f);
    }

    public void addFragment(DrawerFragment f){
        fragments.add(f);
        notifyDataSetChanged();
    }

    public DrawerFragment getFragment(int pos){
        return fragments.get(pos);
    }

    public void clearFragments(){
        fragments.clear();
        notifyDataSetChanged();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_drawer_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        v.setOnClickListener(clickListener);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.setTitle(fragments.get(position).getName());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return fragments.size();
    }

}

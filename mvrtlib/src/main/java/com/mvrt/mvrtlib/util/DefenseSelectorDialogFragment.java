package com.mvrt.mvrtlib.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mvrt.mvrtlib.R;

import java.util.Arrays;
import java.util.List;

/**
 * @author Akhil Palla
 */
public class DefenseSelectorDialogFragment extends DialogFragment{

    DefenseSelectedListener listener;

    ViewPager viewPagerOne;
    ViewPager viewPagerTwo;
    ViewPager viewPagerThree;
    ViewPager viewPagerFour;
    String[] defenses = new String[]{"a1", "a2", "b1", "b2", "c1", "c2", "d1", "d2"};
    String[] selectedDefenses = new String[]{"a1", "b1", "c1", "d1"};

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction

        Log.d("MVRT", "OnCreateDialog");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.defensesdialog, null);

        builder.setView(v);
        builder.setTitle("Select Defenses");

        viewPagerOne = (ViewPager)v.findViewById(R.id.defensedialog_viewpagerone);
        viewPagerOne.setAdapter(new DefensePagerAdapter(defenses));
        viewPagerTwo = (ViewPager)v.findViewById(R.id.defensedialog_viewpagertwo);
        viewPagerTwo.setAdapter(new DefensePagerAdapter(defenses));
        viewPagerThree = (ViewPager)v.findViewById(R.id.defensedialog_viewpagerthree);
        viewPagerThree.setAdapter(new DefensePagerAdapter(defenses));
        viewPagerFour = (ViewPager)v.findViewById(R.id.defensedialog_viewpagerfour);
        viewPagerFour.setAdapter(new DefensePagerAdapter(defenses));

        reloadUI();

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(listener != null){
                    String[] def = new String[]{
                            defenses[viewPagerOne.getCurrentItem()],
                            defenses[viewPagerTwo.getCurrentItem()],
                            defenses[viewPagerThree.getCurrentItem()],
                            defenses[viewPagerFour.getCurrentItem()],
                    };
                    setSelectedDefenses(def);
                    listener.onDefenseSelected();
                }
            }
        });



        // Create the AlertDialog object and return it
        return builder.create();
    }

    public void setSelectedDefenses(String[] selectedDefenses){
        if(selectedDefenses.length < 4)return;
        Log.d("MVRT", "set selected");
        this.selectedDefenses = selectedDefenses;
        reloadUI();
    }

    public void reloadUI(){
        List defenseList = Arrays.asList(defenses);
        viewPagerOne.setCurrentItem(defenseList.indexOf(selectedDefenses[0]));
        viewPagerTwo.setCurrentItem(defenseList.indexOf(selectedDefenses[1]));
        viewPagerThree.setCurrentItem(defenseList.indexOf(selectedDefenses[2]));
        viewPagerFour.setCurrentItem(defenseList.indexOf(selectedDefenses[3]));
    }

    public void setListener(DefenseSelectedListener listener){
        this.listener = listener;
    }

    public String[] getSelectedDefenses(){
        return selectedDefenses;
    }

    private class DefensePagerAdapter extends PagerAdapter{

        String[] defenses;

        public DefensePagerAdapter(String[] defenses){
            this.defenses = defenses;
        }

        @Override
        public int getCount() {
            return defenses.length;
        }

        public void setDefenses(String[] defenses){
            this.defenses = defenses;
            notifyDataSetChanged();
        }

        @Override
        public Object instantiateItem(ViewGroup vg, int position){
            LayoutInflater inflater = (LayoutInflater)vg.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            RelativeLayout v = (RelativeLayout)inflater.inflate(R.layout.defensecard, vg, false);
            ImageView imgView = (ImageView)v.findViewById(R.id.defenseimage);
            imgView.setImageDrawable(DefenseManager.getDrawable(getActivity(), defenses[position]));
            vg.addView(v);
            return v;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return object.equals(view);
        }

        @Override
        public void destroyItem(ViewGroup vg, int position, Object obj){
            vg.removeView((View)obj);
        }

    }

    public interface DefenseSelectedListener{
        public void onDefenseSelected();
    }

}

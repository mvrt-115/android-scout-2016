package com.mvrt.mvrtlib.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.mvrt.mvrtlib.R;

/**
 * @author Akhil Palla
 */
public class DefenseCrossingDialogFragment extends DialogFragment implements View.OnClickListener {

    DefenseSelectedListener listener;

    String[] defenses = new String[]{"a1", "b1", "c1", "d1", "e1"};
    ImageView[] imageViews;
    CardView[] cardViews;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.defensecrossingdialog, null);

        builder.setView(v);
        builder.setTitle("Select Defenses");

        initDefenseCards(v);

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(listener != null)listener.defenseSelectionCanceled();
                dialog.cancel();
            }
        });

        // Create the AlertDialog object and return it
        return builder.create();

    }

    public void initDefenseCards(View v){
        imageViews = new ImageView[5];
        imageViews[0] = (ImageView)v.findViewById(R.id.defensecross_img1);
        imageViews[1] = (ImageView)v.findViewById(R.id.defensecross_img2);
        imageViews[2] = (ImageView)v.findViewById(R.id.defensecross_img3);
        imageViews[3] = (ImageView)v.findViewById(R.id.defensecross_img4);
        imageViews[4] = (ImageView)v.findViewById(R.id.defensecross_img5);
        cardViews = new CardView[5];
        cardViews[0] = (CardView)v.findViewById(R.id.defensecross_card1);
        cardViews[1] = (CardView)v.findViewById(R.id.defensecross_card2);
        cardViews[2] = (CardView)v.findViewById(R.id.defensecross_card3);
        cardViews[3] = (CardView)v.findViewById(R.id.defensecross_card4);
        cardViews[4] = (CardView)v.findViewById(R.id.defensecross_card5);
        for(CardView cv:cardViews)cv.setOnClickListener(this);
        setDefenseImages();
    }

    public void setDefenseImages(){
        for(int i = 0; i < 5; i++){
            imageViews[i].setImageDrawable(DefenseManager.getDrawable(getActivity(), defenses[i]));
        }
    }

    public void setDefenses(MatchInfo info){
        this.defenses = DefenseManager.addLowBar(info.getDefenses(), info.getAlliance());
        if(imageViews != null)setDefenseImages();
    }

    public void setListener(DefenseSelectedListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        int selectedDefense = 0;
        if(v.getId() == R.id.defensecross_card1)selectedDefense = 0;
        else if(v.getId() == R.id.defensecross_card2)selectedDefense = 1;
        else if(v.getId() == R.id.defensecross_card3)selectedDefense = 2;
        else if(v.getId() == R.id.defensecross_card4)selectedDefense = 3;
        else if(v.getId() == R.id.defensecross_card5)selectedDefense = 4;
        else return;

        if(listener != null)listener.onDefenseSelected(defenses[selectedDefense]);
        this.dismiss();
    }

    public interface DefenseSelectedListener{
        public void onDefenseSelected(String defense);
        public void defenseSelectionCanceled();
    }

}

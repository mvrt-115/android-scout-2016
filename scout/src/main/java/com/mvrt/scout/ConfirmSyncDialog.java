package com.mvrt.scout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import android.app.DialogFragment;

public class ConfirmSyncDialog extends DialogFragment {

    ConfirmSyncListener csl;

    public void setConfirmSyncListener(ConfirmSyncListener csl){
        this.csl = csl;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Sure you wanna sync? Make sure you are connected to the internet!")
                .setPositiveButton("Let's do this!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        csl.confirmSync();
                    }
                })
                .setNegativeButton("Oops not yet", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}

package com.mvrt.scout;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by akhil on 9/21/2016.
 */
public class FirebaseUtils {

    private static FirebaseDatabase mDatabase;

    public static FirebaseDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
        return mDatabase;
    }

}

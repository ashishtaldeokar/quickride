package com.sspm.quickride.firebase_database;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sspm.quickride.ui.interfaces.Callback;
import com.sspm.quickride.ui.interfaces.Callback_v2;

/**
 * Created by Siddhesh on 25-01-2017.
 */

public abstract class AbstractDatabaseReference {
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

    abstract public void initiateDatabase(Callback_v2 callback);
    abstract public void dataChange(DataSnapshot dataSnapshot);

    private static String myMobile;

    public static String getMyMobile() {
        return myMobile;
    }

    public static void setMyMobile(String myMobile) {
        AbstractDatabaseReference.myMobile = myMobile;
    }
}

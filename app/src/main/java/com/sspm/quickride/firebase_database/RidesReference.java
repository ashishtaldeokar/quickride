package com.sspm.quickride.firebase_database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.sspm.quickride.pojo.Ride;
import com.sspm.quickride.util.Constants;

/**
 * Created by Siddhesh on 25-01-2017.
 */

public class RidesReference extends AbstractDatabaseReference {
    private DatabaseReference mRideRef;
    private ValueEventListener listener;
    @Override
    public void initiateDatabase() {
        mRideRef = mDatabase.getReference(Constants.RIDE_REFERENCE + getMyMobile());
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataChange(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mRideRef.addValueEventListener(listener);
    }

    public void addMyRide(Ride ride){
        mRideRef.setValue(ride);
    }

    @Override
    public void dataChange(DataSnapshot dataSnapshot){

    }
}

package com.sspm.quickride.firebase_database;

import android.location.Location;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.sspm.quickride.ui.interfaces.Callback_v2;
import com.sspm.quickride.util.Constants;

/**
 * Created by Siddhesh on 03-02-2017.
 */

public class GeoFireReference extends AbstractDatabaseReference {

    private DatabaseReference mGeoFireRef;
    private ValueEventListener listener;
    GeoFire geoUsersFire;

    @Override
    public void initiateDatabase(Callback_v2 callback_v2) {
        mGeoFireRef = mDatabase.getReference(Constants.GEO_FIRE_REFERENCE);
        geoUsersFire = new GeoFire(mGeoFireRef);
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataChange(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mGeoFireRef.addValueEventListener(listener);
    }

    public void setLocation(Location location){
        geoUsersFire.setLocation(AbstractDatabaseReference.getMyMobile(), new GeoLocation(location.getLatitude(),location.getLongitude()));
    }

    @Override
    public void dataChange(DataSnapshot dataSnapshot) {

    }
}

package com.sspm.quickride;

import android.location.Location;
import android.util.Log;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
/**
 * Created by ashish on 9/22/2016.
 */
public class FireService {
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private static DatabaseReference mMyRef;
    private static String mMobile;
    private static String mIMEI;
    private static Location mLocation;
    DatabaseReference mUsersRef = FirebaseDatabase.getInstance().getReference("quickride/userlocations/geofire");
    GeoFire geoUsersFire = new GeoFire(mUsersRef);
    private static boolean active;
    public void setMobile(String mobile){
        this.mMobile = mobile;
        mMyRef = mDatabase.getReference("quickride/users/"+mobile);
    }
    public String getMobile(){
        if(this.mMobile != null)
            return this.mMobile;
        else
            return "UNKNOWN";
    }

    public void setIMEI(String imei){
        this.mIMEI = imei;
        if (mMobile != null){
            mMyRef.child("IMEI").setValue(imei);
        }
    }
    public String getIMEI(){
        if(this.mIMEI != null)
            return this.mIMEI;
        else
            return "UNKNOWN";
    }

    public void setLocation(Location location){
        this.mLocation = location;
        mMyRef.child("location").setValue(location);
        if(active)
            geoUsersFire.setLocation(this.getMobile(),new GeoLocation(location.getLatitude(),location.getLongitude()));
    }

    private void initLocation(Location location){
        this.mLocation = location;
    }
    public void getRideList(){
    };

    public Location getLocation(){
        if (this.mLocation != null){
            return this.mLocation;
        }
        else
            return null;
    }

    public void becomeActive(){
        this.active = true;
        mMyRef.child("isActive").setValue(true);
        geoUsersFire.setLocation(this.getMobile(),new GeoLocation(mLocation.getLatitude(),mLocation.getLongitude()));
    }
    public void becomeInActive(){
        this.active = false;
        mMyRef.child("isActive").setValue(false);
    }


}

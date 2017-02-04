package com.sspm.quickride.pojo;

import android.location.Location;

import java.io.Serializable;

/**
 * Created by Siddhesh on 25-01-2017.
 */

public class User implements Serializable {
    private String mIMEI, myMobile;
    private Location mLocation;
    private boolean active;

    public User(String mIMEI, String myMobile) {
        this.mIMEI = mIMEI;
        this.myMobile = myMobile;
    }

    public String getMyMobile() {
        return myMobile;
    }

    public void setMyMobile(String myMobile) {
        this.myMobile = myMobile;
    }

    public String getmIMEI() {
        return mIMEI;
    }

    public void setmIMEI(String mIMEI) {
        this.mIMEI = mIMEI;
    }

    public Location getmLocation() {
        return mLocation;
    }

    public void setmLocation(Location mLocation) {
        this.mLocation = mLocation;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}

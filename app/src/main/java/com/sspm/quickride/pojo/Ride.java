package com.sspm.quickride.pojo;

/**
 * Created by Siddhesh on 20-01-2017.
 */

public class Ride{

    String myMobile, rikMobile, sourceLat, sourceLng, destinationLat, destinationLng, rikLat, rikLng, srcText, destText;
    int status;

    /**
     * status
     * 0 searching
     * 1 waiting
     * 2 confirm
     * 3 discard
     */
    int SEARCHING = 0, WAITING = 1, CONFIRM = 2, COMPLETED = 3;

    public Ride(String myMobile, String sourceLat, String sourceLng, String destinationLat, String destinationLng, String srcText, String destText) {
        this.myMobile = myMobile;
        this.sourceLat = sourceLat;
        this.sourceLng = sourceLng;
        this.destinationLat = destinationLat;
        this.destinationLng = destinationLng;
        this.status = SEARCHING;
        this.srcText = srcText;
        this.destText = destText;
    }

    public Ride() {
    }

    public String getMyMobile() {
        return myMobile;
    }

    public void setMyMobile(String myMobile) {
        this.myMobile = myMobile;
    }

    public String getRikMobile() {
        return rikMobile;
    }

    public void setRikMobile(String rikMobile) {
        this.rikMobile = rikMobile;
    }

    public String getSourceLat() {
        return sourceLat;
    }

    public void setSourceLat(String sourceLat) {
        this.sourceLat = sourceLat;
    }

    public String getSourceLng() {
        return sourceLng;
    }

    public void setSourceLng(String sourceLng) {
        this.sourceLng = sourceLng;
    }

    public String getDestinationLat() {
        return destinationLat;
    }

    public void setDestinationLat(String destinationLat) {
        this.destinationLat = destinationLat;
    }

    public String getDestinationLng() {
        return destinationLng;
    }

    public void setDestinationLng(String destinationLng) {
        this.destinationLng = destinationLng;
    }

    public String getRikLat() {
        return rikLat;
    }

    public void setRikLat(String rikLat) {
        this.rikLat = rikLat;
    }

    public String getRikLng() {
        return rikLng;
    }

    public void setRikLng(String rikLng) {
        this.rikLng = rikLng;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

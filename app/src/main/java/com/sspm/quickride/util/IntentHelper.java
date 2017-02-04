package com.sspm.quickride.util;

import android.app.Activity;
import android.content.Intent;
import android.provider.Settings;

import com.sspm.quickride.app.App;

/**
 * Created by Siddhesh on 23-01-2017.
 */

public class IntentHelper {
    /**
     * open gps settings intent
     */
    public static void openGpsIntent() {

        Intent gpsOptionsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        gpsOptionsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        App.getInstance().startActivity(gpsOptionsIntent);
    }
}

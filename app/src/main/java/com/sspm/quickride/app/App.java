package com.sspm.quickride.app;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.sspm.quickride.util.foregroundDetector.ForegroundDetector;

/**
 * Created by Siddhesh on 23-01-2017.
 */

public class App extends Application{

    public static App context;

    public static Context getInstance(){
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        ForegroundDetector.init(this);
        ForegroundDetector.get().addListener(new ForegroundDetector.Listener() {
            @Override
            public void onBecameForeground() {
                Log.v("quick ride", "in foreground");
            }

            @Override
            public void onBecameBackground() {
                Log.v("quick ride", "in background");
            }
        });
    }

}

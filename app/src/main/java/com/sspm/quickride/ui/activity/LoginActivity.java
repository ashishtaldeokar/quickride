package com.sspm.quickride.ui.activity;
import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.DigitsAuthButton;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.digits.sdk.android.SessionListener;
import com.sspm.quickride.MapsActivity;
import com.sspm.quickride.R;
import com.sspm.quickride.pojo.User;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.digits.sdk.android.Digits;
import io.fabric.sdk.android.Fabric;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "dezTGFPENT8TDvGAny7H9rRFS";
    private static final String TWITTER_SECRET = "6CHehmHGTC5avcjmQK5KRf6XUeC0thrZhiM1qAjPlxdPXWa7ZM";
    public static TelephonyManager telephonyManager;
    private static String IMEI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onPause();
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig), new Digits.Builder().build());
        setContentView(R.layout.activity_login);
        DigitsAuthButton digitsButton = (DigitsAuthButton) findViewById(R.id.auth_button);
        digitsButton.setAuthTheme(R.style.AppTheme);
        Digits.addSessionListener(new SessionListener() {
            @Override
            public void changed(DigitsSession newSession) {
                Toast.makeText(LoginActivity.this, "Session phone was changed: " + newSession
                        .getPhoneNumber(), Toast.LENGTH_SHORT).show();
            }
        });
        if (digitsButton != null) {
            digitsButton.setCallback(new AuthCallback() {
                @Override
                public void success(DigitsSession session, String phoneNumber) {
                    // TODO: associate the session userID with your user model
                    User user = new User(IMEI, phoneNumber);
                    Intent home = new Intent(LoginActivity.this, MapsActivity.class);
                    home.putExtra("user", user);
                    startActivity(home);
                    //Toast.makeText(getApplicationContext(), "Authentication successful for " + phoneNumber, Toast.LENGTH_LONG).show();
                }
                @Override
                public void failure(DigitsException exception) {
                    Log.d("Digits", "Sign in with Digits failure", exception);
                }
            });
        }
        else{
            Toast.makeText(getApplicationContext(), "Null wala error ", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
//        fire.becameInActive();
        super.onDestroy();
    }
    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_PHONE_STATE}, 113);
            return;
        }
        else {
            telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            IMEI = telephonyManager.getDeviceId();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 113: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                    IMEI = telephonyManager.getDeviceId();
                    return;

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                onResume();
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
}}

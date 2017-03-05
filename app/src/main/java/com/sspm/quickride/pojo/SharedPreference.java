package com.sspm.quickride.pojo;

import android.content.Context;
import android.content.SharedPreferences;

import com.sspm.quickride.app.App;
import com.sspm.quickride.util.Constants;

/**
 * Created by Siddhesh on 05-03-2017.
 */

public class SharedPreference {

    public static SharedPreference preference;
    public static SharedPreferences pref;

    public static SharedPreference getPreference() {
       if(preference == null){
           preference = new SharedPreference();
           pref = App.getInstance().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
       }
        return preference;
    }

    public void setMyPhoneNumber(String phoneNumber){
        pref.edit().putString(Constants.MY_PHONE_NUMBER, phoneNumber).apply();
    }

    public String getMyPhoneNumber(){
        return pref.getString(Constants.MY_PHONE_NUMBER, null);
    }

}

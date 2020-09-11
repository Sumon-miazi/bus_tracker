package com.example.bus_tracker.db;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.bus_tracker.R;

public class CustomSharedPref {
    private static CustomSharedPref customSharedPref;
    private Context context;
    private SharedPreferences sharedPreferences;

    private CustomSharedPref(Context context) {
        this.context = context;
        sharedPreferences = this.context.getSharedPreferences(this.context.getResources().getString(R.string.sharedPrefName), Context.MODE_PRIVATE);
    }  //private constructor.

    public static CustomSharedPref getInstance(Context context) {
        if (customSharedPref == null) {
            customSharedPref = new CustomSharedPref(context);
        }
        return customSharedPref;
    }

    public void setIsItFirstTime(boolean b) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("IsItFirstTime", b);
        editor.apply();
    }

    public Boolean IsItFirstTime() {
        return sharedPreferences.getBoolean("IsItFirstTime", false);
    }
}

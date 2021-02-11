package com.dev_candra.moviedb.prefrence;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class SettingPreference {

    private static final String PREFS_NAME = "setting_pref";
    private static final String DAILY_REMINDER = "isDaily";
    private static final String RELEASE_REMINDER = "isRelease";
    private final SharedPreferences mSharedPreference;

    public SettingPreference(Context context){
        mSharedPreference = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
    }

    public void setDailyReminder(boolean isActive){
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.putBoolean(DAILY_REMINDER,isActive);
        editor.apply();
    }

    public void setReleaseReminder(boolean isActive){
        SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.putBoolean(RELEASE_REMINDER,isActive);
        editor.apply();
    }

  public boolean getDailyNumber(){
        return mSharedPreference.getBoolean(DAILY_REMINDER,false);
  }

  public boolean getReleaseReminder(){
        return mSharedPreference.getBoolean(RELEASE_REMINDER,false);
  }

}

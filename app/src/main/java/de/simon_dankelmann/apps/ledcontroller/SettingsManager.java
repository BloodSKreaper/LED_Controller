package de.simon_dankelmann.apps.ledcontroller;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by simon on 20.11.16.
 */

public class SettingsManager {
    public static Context context;
    private SharedPreferences preferences;

    public SettingsManager() {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(appContext.getAppContext());
    }

    public  String getString(String keyString, String String2){
        return preferences.getString(keyString,String2);
    }

    public Boolean getBoolean(String keyString, Boolean returnValue){
        return preferences.getBoolean(keyString, returnValue);
    }

}

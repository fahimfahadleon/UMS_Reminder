package com.unicornitsolutions.umsreminder.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {

    private static final String PREF_NAME = "MyAppPreferences";
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public SharedPreferencesHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Save data to SharedPreferences
    public void saveData(String key, String value) {
        editor.putString(key, value);
        editor.apply(); // or editor.commit(); for synchronous saving
    }

    // Retrieve data from SharedPreferences
    public String getData(String key) {
        return sharedPreferences.getString(key, null); // return null if key not found
    }

    // Remove data from SharedPreferences
    public void removeData(String key) {
        editor.remove(key);
        editor.apply();
    }

    // Clear all data from SharedPreferences
    public void clearAllData() {
        editor.clear();
        editor.apply();
    }
}
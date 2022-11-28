package com.example.woomansi.util;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;

public class SharedPreferencesUtil {

    private static final String PREF_NAME = "woomansi_pref";

    // get
    public static String getString(@NonNull Context context, String key) {
        return getSharedPref(context).getString(key, "");
    }

    public static boolean getBoolean(@NonNull Context context, String key) {
        return getSharedPref(context).getBoolean(key, false);
    }

    public static int getInt(@NonNull Context context, String key) {
        return getSharedPref(context).getInt(key, 0);
    }

    // set
    public static void putString(@NonNull Context context, String key, String value) {
        getSharedPref(context).edit().putString(key, value).apply();
    }

    public static void putBoolean(@NonNull Context context, String key, boolean value) {
        getSharedPref(context).edit().putBoolean(key, value).apply();
    }

    public static void putInt(@NonNull Context context, String key, int value) {
        getSharedPref(context).edit().putInt(key, value).apply();
    }

    private static SharedPreferences getSharedPref(@NonNull Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
}

package com.example.woomansi.util;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.woomansi.data.model.UserModel;
import com.google.gson.Gson;

public class UserCache {
    private static final String KEY_USER_CACHE = "user_cache";

    public static void setUser(@NonNull Context context, UserModel userModel) {
        String json = new Gson().toJson(userModel);
        SharedPreferencesUtil.putString(context, KEY_USER_CACHE, json);
    }

    public static UserModel getUser(@NonNull Context context) {
        String json = SharedPreferencesUtil.getString(context, KEY_USER_CACHE);
        if (json.isEmpty())
            return null;
        return new Gson().fromJson(json, UserModel.class);
    }
}

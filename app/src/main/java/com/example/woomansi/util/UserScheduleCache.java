package com.example.woomansi.util;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.woomansi.data.model.ScheduleDataWrapper;
import com.example.woomansi.data.model.ScheduleModel;
import com.example.woomansi.data.model.UserModel;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

public class UserScheduleCache {
    private static final String KEY_USER_SCHEDULE_CACHE = "user_schedule_cache";

    private static ScheduleDataWrapper cache;

    public static void setSchedule(@NonNull Context context, Map<String, List<ScheduleModel>> scheduleData) {
        cache.setSchedules(scheduleData);
        String json = new Gson().toJson(scheduleData);
        SharedPreferencesUtil.putString(context, KEY_USER_SCHEDULE_CACHE, json);
    }

    public static Map<String, List<ScheduleModel>> getSchedule(Context context) {
        if (cache != null)
            return cache.getSchedules();
        if (context == null)
            return null;
        String json = SharedPreferencesUtil.getString(context, KEY_USER_SCHEDULE_CACHE);
        if (json.isEmpty())
            return null;
        cache = new Gson().fromJson(json, ScheduleDataWrapper.class);
        return cache.getSchedules();
    }
}

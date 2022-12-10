package com.example.woomansi.util;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.woomansi.R;

import java.util.Arrays;
import java.util.List;

public class DayNameList {
    private static List<String> _dayNameList;

    public static List<String> get(@NonNull Context context) {
        if (_dayNameList == null) {
            String[] array = context.getResources().getStringArray(R.array.day_name);
            _dayNameList = Arrays.asList(array);
        }
        return _dayNameList;
    }

    public static List<String> get() {
        return _dayNameList;
    }
}

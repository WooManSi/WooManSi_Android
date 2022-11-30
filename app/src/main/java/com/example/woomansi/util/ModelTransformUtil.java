package com.example.woomansi.util;

import androidx.annotation.NonNull;

import com.cometj03.composetimetable.ScheduleDayData;
import com.cometj03.composetimetable.TimeTableData;
import com.example.woomansi.data.model.ScheduleModel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ModelTransformUtil {

    public static TimeTableData scheduleMapToTimeTableData(
            @NonNull Map<String, List<ScheduleModel>> scheduleMap
    ) {
        List<ScheduleDayData> dayDataList = new ArrayList<>();

        for (String key : scheduleMap.keySet()) {

            List<ScheduleModel> scheduleModels = scheduleMap.get(key);

            for (ScheduleModel schedule : scheduleMap.get(key)) {

            }
        }
        return null;
    }
}

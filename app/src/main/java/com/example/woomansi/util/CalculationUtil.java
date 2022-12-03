package com.example.woomansi.util;

import com.example.woomansi.data.model.ScheduleModel;

import java.util.List;

public class CalculationUtil {

    // origin에 scheduleModels의 값을 합치고 결과를 반환하는 함수
    public static List<Integer> unionLists(List<Integer> origin, List<ScheduleModel> models) {

        for (int i = 0; i < models.size(); i++) {
            int startIndex = calculateIndex(models.get(i).getStartTime());
            int endIndex = calculateIndex(models.get(i).getEndTime());

            for (int k = startIndex; k <= endIndex; k++)
                origin.set(k, origin.get(k) + 1);
        }
        return origin;
    }

    // origin에 scheduleModels의 값을 빼고 결과를 반환하는 함수
    public static List<Integer> minusLists(List<Integer> origin, List<ScheduleModel> models) {

        for (int i = 0; i < models.size(); i++) {
            int startIndex = calculateIndex(models.get(i).getStartTime());
            int endIndex = calculateIndex(models.get(i).getEndTime());

            for (int k = startIndex; k <= endIndex; k++)
                origin.set(k, origin.get(k) - 1);
        }
        return origin;
    }

    private static int calculateIndex(String time) {
        String[] array = time.split(":");
        int hour =  Integer.parseInt(array[0]);
        int minute = Integer.parseInt(array[1]);

        hour = (hour - 6) * 4 ;
        minute = (minute -1) / 15;

        return hour + minute;
    }
}

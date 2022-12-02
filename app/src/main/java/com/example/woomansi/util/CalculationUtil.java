package com.example.woomansi.util;

import com.example.woomansi.data.model.ScheduleModel;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CalculationUtil {

    //스케쥴 모듈을 불러와서 그룹 시간 int 배열을 갱신해주는 util
    public static List<Integer> change_ScheduleModel_ToIntArray(List<ScheduleModel> schedules) {
        int[] groupTimetable = new int[72];

        schedules.forEach(scheduleModel ->
        {
            String startTime = scheduleModel.getStartTime();
            String endTime = scheduleModel.getEndTime();

            int startIndex = calculateIndex(startTime);
            int endIndex = calculateIndex(endTime);
            for (int i = startIndex; i <= endIndex; i++) {
                groupTimetable[i]++;
            }
        });

        List<Integer> result = Arrays.stream(groupTimetable)
                .boxed()
                .collect(Collectors.toList());
        return result;
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

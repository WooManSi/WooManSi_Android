package com.example.woomansi.util;

import com.example.woomansi.data.model.ScheduleModel;

import com.example.woomansi.data.model.VoteScheduleModel;
import java.util.ArrayList;
import java.util.List;

public class CalculationUtil {

    // origin에 scheduleModels의 값을 합치고 결과를 반환하는 함수
    public static List<Integer> unionLists(List<Integer> origin, List<ScheduleModel> models) {

        for (int i = 0; i < models.size(); i++) {
            int startIndex = calculateTimeToIndex(models.get(i).getStartTime());
            int endIndex = calculateTimeToIndex(models.get(i).getEndTime());

            for (int k = startIndex; k <= endIndex; k++)
                origin.set(k, origin.get(k) + 1);
        }
        return origin;
    }

    // origin에 scheduleModels의 값을 빼고 결과를 반환하는 함수
    public static List<Integer> minusLists(List<Integer> origin, List<ScheduleModel> models) {

        for (int i = 0; i < models.size(); i++) {
            int startIndex = calculateTimeToIndex(models.get(i).getStartTime());
            int endIndex = calculateTimeToIndex(models.get(i).getEndTime());

            for (int k = startIndex; k <= endIndex; k++)
                origin.set(k, origin.get(k) - 1);
        }
        return origin;
    }

    // 개인 scheduleModels의 시작시간, 끝시간 -> 그룹 schedule int[72]에 넣을 index로 변환할 때 사용하는 함수
    private static int calculateTimeToIndex(String time) {
        String[] array = time.split(":");
        int hour =  Integer.parseInt(array[0]);
        int minute = Integer.parseInt(array[1]);

        hour = (hour - 6) * 4 ;
        minute = (minute -1) / 15;

        return hour + minute;
    }

    // 그룹 schedule int[72]에서 0이 들어있는 구간 -> 개인 scheduleModels의 시작시간, 끝시간으로 변환할 때 사용하는 함수
    public static List<VoteScheduleModel> calculateIndexToTime(List<Integer> origin) {

        List<VoteScheduleModel> result = new ArrayList<>();
        int overlapCount = 0; //최대 겹칠 수 있는 인원 수, 나중에 spinner를 이용해 사용자가 고르면 데이터를받아서 바꿔줄 예정
        boolean isStartTimeExist = false; // 시작 시간 or 끝시간 구분하기 위한 boolean 변수

        int startIndex = 0;
        int startHour = 0;
        int startMinute = 0;

        int endIndex = 0;
        int endHour = 0;
        int endMinute = 0;
        
        for (int i = 0; i < origin.size(); i++) {

            if (origin.get(i) == 0 && !isStartTimeExist) { //1. 배열값이 0이고 시작시간이 아직 셋팅되지 않았을 때
                startIndex = i;
                startHour = i / 4 + 6;
                startMinute = i % 4 * 15; //왜 +1을 안해주는가? 최종 투표결과에서 06:01~07:16보단 06:00~07:15가 더 좋아보여서
                isStartTimeExist = true; //시작 시간 세팅완료
                
            } else if (origin.get(i) > overlapCount && isStartTimeExist) { //2. 쭉 0이 나오다가 처음으로 0 이상의 값이 나온 순간
                endIndex = i;
                endHour = i / 4 + 6;
                endMinute = i % 4 * 15;

                String startTime = TimeFormatUtil.timeToString(startHour, startMinute);
                String endTime = TimeFormatUtil.timeToString(endHour, endMinute);

                if(endIndex - startIndex > 4) { // 너무 짧은 시간은 넣어주지 않기위함 (ex. 1시간 미만)
                    VoteScheduleModel newVoteSchedule = new VoteScheduleModel(startTime, endTime, 0);
                    result.add(newVoteSchedule);
                }

                isStartTimeExist = false;

            } else if (i == origin.size() - 1 && origin.get(i) == 0 && isStartTimeExist) { //3. 24:00까지 0값으로 차있을 때
                endIndex = i;
                endHour = i / 4 + 6;
                endMinute = 59;

                String startTime = TimeFormatUtil.timeToString(startHour, startMinute);
                String endTime = TimeFormatUtil.timeToString(endHour, endMinute);

                if(endIndex - startIndex > 4) {
                    VoteScheduleModel newVoteSchedule = new VoteScheduleModel(startTime, endTime, 0);
                    result.add(newVoteSchedule);
                }
                isStartTimeExist = false;
            }
        }
        return result;
    }
}

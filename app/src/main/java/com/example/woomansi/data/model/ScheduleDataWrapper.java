package com.example.woomansi.data.model;

import java.util.List;
import java.util.Map;

/**
 * Firestore, Gson에 데이터를 쓰고 받아오기 위한 클래스입니다.
 * 목적 이외의 사용은 삼가주세요.
 */
public class ScheduleDataWrapper {
    private Map<String, List<ScheduleModel>> schedules;

    public ScheduleDataWrapper() {}

    public Map<String, List<ScheduleModel>> getSchedules() {
        return schedules;
    }

    public void setSchedules(Map<String, List<ScheduleModel>> schedules) {
        this.schedules = schedules;
    }
}

package com.example.woomansi.data.repository;

import android.util.Log;

import com.example.woomansi.data.model.ScheduleModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseSchedules {

    private final static String COLLECTION_NAME = "schedules";

    public interface OnLoadSuccessListener {
        void onLoadSuccess(Map<String, List<ScheduleModel>> scheduleMap);
    }
    public interface OnLoadFailedListener {
        void onLoadFailed(String message);
    }

    public static void getSchedules(
            String scheduleId,
            OnLoadSuccessListener s,
            OnLoadFailedListener f
    ) {
        FirebaseFirestore
            .getInstance()
            .collection(COLLECTION_NAME)
            .document(scheduleId)
            .get()
            .addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    f.onLoadFailed("스케줄 데이터를 가져오는 과정에서 문제가 발생했습니다.\n" + task.getException());
                    return;
                }
                DocumentSnapshot document = task.getResult();
                if (!document.exists()) {
                    // firestore에 시간표 document가 존재하지 않을 때 초기화 해주는 과정
                    initializeSchedule(scheduleId, s);
                    s.onLoadSuccess(null);
                    return;
                }
                Log.d("TEST", "getScheduleList: " + document.getData());
                List<ScheduleModel> list = new ArrayList<>();

            });
    }

    public static void createScheduleList(
            Map<String, List<ScheduleModel>> schedule,
            String scheduleId,
            OnCompleteListener<Void> s
    ) {
        FirebaseFirestore
                .getInstance()
                .collection(COLLECTION_NAME)
                .document(scheduleId)
                .set(schedule, SetOptions.merge())
                .addOnCompleteListener(s);
    }

    private static void initializeSchedule(String scheduleId, OnLoadSuccessListener s) {
        // 기본: 월 ~ 금
        String[] dayNames = new String[] {"월", "화", "수", "목", "금"};
        Map<String, List<ScheduleModel>> data = new HashMap<>();
        for (String dayName : dayNames)
            data.put(dayName, null);

        FirebaseFirestore
                .getInstance()
                .collection(COLLECTION_NAME)
                .document(scheduleId)
                .set(data)
                .addOnCompleteListener(task -> s.onLoadSuccess(data));
    }
}

package com.example.woomansi.data.repository;

import android.util.Log;

import com.example.woomansi.data.model.ScheduleDataWrapper;
import com.example.woomansi.data.model.ScheduleModel;
import com.example.woomansi.util.UserScheduleCache;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

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
            List<String> dayNameList, // 기본: 월 ~ 일
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
                    initializeSchedule(scheduleId, dayNameList, s);
                    return;
                }

                ScheduleDataWrapper wrapper = document.toObject(ScheduleDataWrapper.class);
                if (wrapper != null) {
                    s.onLoadSuccess(wrapper.getSchedules());
                } else {
                    f.onLoadFailed("스케줄 데이터 형식이 잘못되어 불러올 수 없습니다.");
                }
            });
    }

    public static void addSchedule(
            String scheduleId,
            String dayName,
            ScheduleModel schedule,
            OnCompleteListener<Void> s
    ) {
        // schedules내의 특정 dayName의 경로를 가리킴. (schedules: ScheduleDataWrapper의 멤버 변수 이름)
        FieldPath path = FieldPath.of("schedules", dayName);

        // 특정 요일에 스케줄 추가
        FirebaseFirestore
                .getInstance()
                .collection(COLLECTION_NAME)
                .document(scheduleId)
                .update(path, FieldValue.arrayUnion(schedule))
                .addOnCompleteListener(s);
    }

    private static void initializeSchedule(
            String scheduleId,
            List<String> dayNameList,
            OnLoadSuccessListener s
    ) {
        Map<String, List<ScheduleModel>> scheduleData = new HashMap<>();
        for (String dayName : dayNameList)
            scheduleData.put(dayName, new ArrayList<>());

        ScheduleDataWrapper wrapper = new ScheduleDataWrapper();
        wrapper.setSchedules(scheduleData);

        FirebaseFirestore
                .getInstance()
                .collection(COLLECTION_NAME)
                .document(scheduleId)
                .set(wrapper)
                .addOnCompleteListener(task -> s.onLoadSuccess(scheduleData));
    }
}

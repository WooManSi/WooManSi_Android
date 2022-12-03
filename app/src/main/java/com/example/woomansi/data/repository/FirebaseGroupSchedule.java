package com.example.woomansi.data.repository;

import com.example.woomansi.data.model.GroupTimeTableWrapper;
import com.example.woomansi.data.model.ScheduleModel;
import com.example.woomansi.util.CalculationUtil;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseGroupSchedule {
    private static final String COLLECTION_NAME = "group_schedules";

    public interface OnFetchSuccessListener {
        void onSuccess(Map<String, List<Integer>> groupSchedule);
    }

    public interface OnUnionSuccessListener {
        void onSuccess();
    }

    // 기존의 그룹 시간표를 불러와서 scheduleData를 계산하여 합치고, 업데이트하는 함수
    public static void unionSchedules(
            String groupId,
            Map<String, List<ScheduleModel>> scheduleData,
            OnUnionSuccessListener s,
            OnFailedListener f
    ) {
        List<String> dayNames = new ArrayList<>(scheduleData.keySet());

        fetchGroupSchedule(groupId, dayNames, groupSchedule -> {
            Map<String, List<Integer>> result = calculateWith(groupSchedule, scheduleData);
            updateGroupSchedule(groupId, result, s, f);
        }, f);
    }

    // 그룹의 시간표를 가져오는 함수
    public static void fetchGroupSchedule(
            String groupId,
            List<String> dayNames,
            OnFetchSuccessListener s,
            OnFailedListener f
    ) {
        FirebaseFirestore
                .getInstance()
                .collection(COLLECTION_NAME)
                .document(groupId)
                .get()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        f.onFailed("그룹 시간표 정보를 가져오는 데 실패했습니다.");
                        return;
                    }
                    DocumentSnapshot document = task.getResult();
                    if (!document.exists()) {
                        // document가 존재하지 않을 때 초기화해주기
                        setEmptySchedule(groupId, dayNames, s);
                        return;
                    }
                    GroupTimeTableWrapper wrapper = document.toObject(GroupTimeTableWrapper.class);
                    if (wrapper != null) {
                        s.onSuccess(wrapper.getGroupTimeTable());
                    } else {
                        f.onFailed("데이터 형식이 잘못되어 불러올 수 없습니다.");
                    }
                });
    }

    // 데이터베이스에 빈 데이터로 초기화해주는 함수
    private static void setEmptySchedule(String groupId, List<String> dayNames, OnFetchSuccessListener s) {
        Map<String, List<Integer>> emptyData = new HashMap<>();
        for (String dayName : dayNames)
            emptyData.put(dayName, new ArrayList<>(Collections.nCopies(72, 0)));

        GroupTimeTableWrapper wrapper = new GroupTimeTableWrapper();
        wrapper.setGroupTimeTable(emptyData);

        FirebaseFirestore
                .getInstance()
                .collection(COLLECTION_NAME)
                .document(groupId)
                .set(wrapper)
                .addOnSuccessListener(a -> s.onSuccess(emptyData));
    }

    // 그룹 스케줄을 newValue로 업데이트 해주는 함수
    public static void updateGroupSchedule(
            String groupId,
            Map<String, List<Integer>> newValue,
            OnUnionSuccessListener s,
            OnFailedListener f
    ) {
        GroupTimeTableWrapper wrapper = new GroupTimeTableWrapper();
        wrapper.setGroupTimeTable(newValue);

        FirebaseFirestore
                .getInstance()
                .collection(COLLECTION_NAME)
                .document(groupId)
                .set(wrapper)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        s.onSuccess();
                    } else {
                        f.onFailed("새로운 데이터가 업데이트되지 않았습니다.");
                    }
                });
    }

    private static Map<String, List<Integer>> calculateWith(
            Map<String, List<Integer>> groupSchedule, Map<String,
            List<ScheduleModel>> scheduleData
    ) {
        Map<String, List<Integer>> result = new HashMap<>();

        scheduleData.forEach((key, value) -> {
            List<Integer> origin = groupSchedule.get(key); // 원래 있던 그룹 스케줄 데이터
            if (origin != null) {
                List<Integer> column = CalculationUtil.unionLists(origin, value);
                result.put(key, column);
            }
        });
        return result;
    }
}

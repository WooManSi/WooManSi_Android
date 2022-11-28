package com.example.woomansi.data.repository;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import com.example.woomansi.data.model.ScheduleModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FirebaseSchedules {

    private final static String COLLECTION_NAME = "schedules";

    public interface OnLoadSuccessListener {
        void onLoadSuccess(Map<String, List<ScheduleModel>> scheduleModels);
    }
    public interface OnLoadFailedListener {
        void onLoadFailed(String message, boolean isEmpty);
    }

    public static void getScheduleList(String scheduleId, OnLoadSuccessListener s, OnLoadFailedListener f) {
        FirebaseFirestore
            .getInstance()
            .collection(COLLECTION_NAME)
            .document(scheduleId)
            .get()
            .addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    f.onLoadFailed("스케줄 데이터를 가져오는 과정에서 문제가 발생했습니다.", false);
                    return;
                }
                DocumentSnapshot document = task.getResult();

                if (!document.exists()) {
                    f.onLoadFailed("일정을 등록해주세요!", true);
                    return;
                }
                Log.d("TEST", "getScheduleList: " + document.getData());
                List<ScheduleModel> list = new ArrayList<>();
                // Map<String, List<ScheduleModel>> scheduleMap = document.getData();

            })
            .addOnFailureListener(e -> {
                f.onLoadFailed(e.getMessage(), false);
            });
    }

    public static void createScheduleList(Map<String, List<ScheduleModel>> scheduleList, String userId) {
        FirebaseFirestore
            .getInstance()
            .collection(COLLECTION_NAME)
            .document(userId)
            .set(scheduleList);
    }
}

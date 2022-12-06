package com.example.woomansi.ui.viewmodel;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.cometj03.composetimetable.TimeTableData;
import com.example.woomansi.data.model.ScheduleModel;
import com.example.woomansi.data.model.UserModel;
import com.example.woomansi.data.repository.FirebaseGroupSchedule;
import com.example.woomansi.data.repository.FirebaseUserSchedule;
import com.example.woomansi.util.ScheduleTypeTransform;
import com.example.woomansi.util.TimeFormatUtil;
import com.example.woomansi.util.UserCache;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main1ViewModel extends ViewModel {

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private MutableLiveData<TimeTableData> timeTableData;

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<TimeTableData> getTimeTableData(List<String> dayNameList) {
        if (timeTableData == null) {
            timeTableData = new MutableLiveData<>();
            loadSchedules(dayNameList);
        }
        return timeTableData;
    }

    public void createSchedule(String title, String description, String dayOfWeekName, LocalTime startTime, LocalTime endTime, String color, List<String> dayNameList) {
        UserModel user = UserCache.getUser(null);
        if (user == null)
            return;
        ScheduleModel schedule = new ScheduleModel(
                title, description,
                TimeFormatUtil.timeToString(startTime),
                TimeFormatUtil.timeToString(endTime),
                color);

        FirebaseUserSchedule.addSchedule(
                user.getIdToken(),
                dayOfWeekName,
                schedule,
                task -> errorMessage.setValue(null),
                errorMsg -> errorMessage.setValue(errorMsg)
        );

        //현재 유저가 들어있는 그룹을 찾음
        FirebaseFirestore
            .getInstance()
            .collection("groups")
            .whereArrayContains("memberList", user.getIdToken())
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    //그룹이 존재할 시
                    if(!task.getResult().isEmpty()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            DocumentReference groupRef = document.getReference();

                            Map<String, List<ScheduleModel>> scheduleDate = new HashMap<>();
                            for (String dayName : dayNameList)
                                scheduleDate.put(dayName, new ArrayList<>());

                            scheduleDate.put(dayOfWeekName, List.of(schedule));

                            FirebaseGroupSchedule.unionSchedules(
                                groupRef.getId(),
                                scheduleDate,
                                () -> {
                                    Log.d(TAG, "개인시간표 추가 -> 그룹시간표 변경 성공");
                                },
                                errorMsg -> Log.d(TAG, errorMsg));
                        }
                    }
                    else {
                        Log.d(TAG, "해당 유저는 속한 그룹이 없음");
                    }
                }
            });
    }

    public void deleteSchedule(String dayName, ScheduleModel scheduleModel) {
        // FirebaseUserSchedule.deleteSchedule();
    }

    private void loadSchedules(List<String> dayNameList) {
        UserModel user = UserCache.getUser(null);
        if (user == null)
            return;

        isLoading.setValue(true);
        FirebaseUserSchedule.getSchedules(
            user.getIdToken(),
            dayNameList,
            scheduleMap -> {
                TimeTableData tableData
                        = ScheduleTypeTransform.scheduleMapToTimeTableData(dayNameList, scheduleMap);
                timeTableData.setValue(tableData);
                isLoading.setValue(false);
            },
            message -> {
                errorMessage.setValue(message);
                isLoading.setValue(false);
            });
    }
}

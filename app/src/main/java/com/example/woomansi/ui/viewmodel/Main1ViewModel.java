package com.example.woomansi.ui.viewmodel;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.cometj03.composetimetable.TimeTableData;
import com.example.woomansi.data.model.ScheduleModel;
import com.example.woomansi.data.model.UserModel;
import com.example.woomansi.data.repository.FirebaseGroup;
import com.example.woomansi.data.repository.FirebaseGroupSchedule;
import com.example.woomansi.data.repository.FirebaseUserSchedule;
import com.example.woomansi.util.DayNameList;
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

import javax.annotation.Nullable;

public class Main1ViewModel extends ViewModel {

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private MutableLiveData<TimeTableData> timeTableData;
    private Map<String, List<ScheduleModel>> scheduleMap;

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
    public Map<String, List<ScheduleModel>> getScheduleMap() {
        return scheduleMap;
    }

    public LiveData<TimeTableData> getTimeTableData(List<String> dayNameList) {
        if (timeTableData == null) {
            timeTableData = new MutableLiveData<>();
            loadSchedules(dayNameList);
        }
        return timeTableData;
    }


    public void createSchedule(String title, String description, String dayName,
                               LocalTime startTime, LocalTime endTime, String color, List<String> dayNameList) {
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
                dayName,
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
                            for (String day : dayNameList)
                                scheduleDate.put(day, new ArrayList<>());

                            scheduleDate.put(dayName, List.of(schedule));

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

    public void deleteSchedule(String dayName, ScheduleModel schedule) {
        UserModel user = UserCache.getUser(null);
        if (user == null)
            return;

        isLoading.setValue(true);
        FirebaseUserSchedule.deleteSchedule(
                user.getIdToken(),
                dayName,
                schedule,
                a -> {
                    FirebaseGroup.getGroups(
                            user.getIdToken(),
                            groupIdList -> {
                                // 현재 유저가 가입되어 있는 그룹이 여러 개일 수 있으므로
                                // 해당 일정을 몇 개의 그룹에서 빼는 과정을 몇 번 성공했는지 저장하는 변수입니다.
                                // int 변수는 익명함수 내에서 수정하려고 하면 오류가 생겨서 길이 1짜리 배열을 사용했습니다.
                                final int[] successCount = {0};
                                int groupCount = groupIdList.size();

                                for (String groupId : groupIdList) {
                                    FirebaseGroupSchedule.minusSchedule(
                                            groupId, dayName, schedule,
                                            () -> {
                                                if (successCount[0]++ >= groupCount)
                                                    setError(null);
                                            },
                                            errorMsg -> setError(errorMsg)
                                    );
                                }
                            },
                            errorMsg -> setError(errorMsg)
                    );
                },
                errorMsg -> setError(errorMsg)
        );
        // TODO: 그룹 스케줄에서 빼주기
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
                this.scheduleMap = scheduleMap;
                timeTableData.setValue(tableData);
                isLoading.setValue(false);
            },
            message -> setError(message));
    }

    private void setError(@Nullable String errorMsg) {
        // if errorMsg is null -> success
        errorMessage.setValue(errorMsg);
        isLoading.setValue(false);
    }
}

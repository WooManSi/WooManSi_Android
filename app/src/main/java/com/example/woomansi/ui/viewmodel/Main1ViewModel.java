package com.example.woomansi.ui.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cometj03.composetimetable.TimeTableData;
import com.example.woomansi.data.model.ScheduleModel;
import com.example.woomansi.data.model.UserModel;
import com.example.woomansi.data.repository.FirebaseSchedules;
import com.example.woomansi.util.ScheduleTypeTransform;
import com.example.woomansi.util.TimeFormatUtil;
import com.example.woomansi.util.UserCache;

import java.time.LocalTime;
import java.util.List;

public class Main1ViewModel extends ViewModel {

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> scheduleCreationErrorMsg = new MutableLiveData<>();
    private MutableLiveData<TimeTableData> timeTableData;

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    public LiveData<String> getScheduleCreationErrorMsg() {
        return scheduleCreationErrorMsg;
    }

    public LiveData<TimeTableData> getTimeTableData(List<String> dayNameList) {
        isLoading.setValue(true);
        if (timeTableData == null) {
            timeTableData = new MutableLiveData<>();
            loadSchedules(dayNameList);
        }
        return timeTableData;
    }

    public void createSchedule(String title, String dayOfWeekName, LocalTime startTime, LocalTime endTime) {
        UserModel user = UserCache.getUser(null);
        if (user == null)
            return;
        ScheduleModel schedule = new ScheduleModel(title, "",
                TimeFormatUtil.timeToString(startTime),
                TimeFormatUtil.timeToString(endTime),
                "#B3DCF5");

        FirebaseSchedules.addSchedule(
                user.getIdToken(),
                dayOfWeekName,
                schedule,
                task -> scheduleCreationErrorMsg.setValue(null)
        );
    }

    public void loadSchedules(List<String> dayNameList) {
        UserModel user = UserCache.getUser(null);
        if (user == null)
            return;
        FirebaseSchedules.getSchedules(
            user.getIdToken(),
            scheduleMap -> {
                TimeTableData tableData
                        = ScheduleTypeTransform.scheduleMapToTimeTableData(dayNameList, scheduleMap);
                timeTableData.setValue(tableData);
                isLoading.setValue(false);
            },
            message -> {
                isLoading.setValue(false);
            });
    }
}

package com.example.woomansi.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cometj03.composetimetable.TimeTableData;
import com.example.woomansi.data.model.UserModel;
import com.example.woomansi.data.repository.FirebaseSchedules;
import com.example.woomansi.util.UserCache;

public class Main1ViewModel extends ViewModel {

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(true);
    private MutableLiveData<TimeTableData> timeTableData;

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<TimeTableData> getTimeTableData() {
        if (timeTableData == null) {
            timeTableData = new MutableLiveData<>();
            loadSchedules();
        }
        return timeTableData;
    }

    public void createSchedule(String title, String startTime, String endTime) {
        // TODO: 스케줄 올리기
    }

    public void loadSchedules() {
        UserModel user = UserCache.getUser(null);
        if (user == null)
            return;
        FirebaseSchedules.getSchedules(
            user.getIdToken(),
            scheduleMap -> {
                // TODO scheduleMap을 timeTableData 형식으로 변환 후 setValue
                // timeTableData.setValue();
                isLoading.setValue(false);
            },
            message -> {
                isLoading.setValue(false);
            });
    }
}

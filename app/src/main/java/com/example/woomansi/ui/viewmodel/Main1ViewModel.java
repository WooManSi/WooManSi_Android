package com.example.woomansi.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.woomansi.data.model.ScheduleModel;
import com.example.woomansi.data.repository.FirebaseSchedules;
import java.util.List;

public class Main1ViewModel extends ViewModel {

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(true);
    private MutableLiveData<List<ScheduleModel>> schedules;

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<List<ScheduleModel>> getSchedules(String userId) {
        if (schedules == null) {
            schedules = new MutableLiveData<>();
            loadSchedules(userId);
        }
        return schedules;
    }

    public void createSchedule(String title, String startTime, String endTime) {
        // TODO: 스케줄 올리기
    }

    private void loadSchedules(String userId) {
        FirebaseSchedules.getScheduleList(
            userId,
            scheduleModels -> {
                isLoading.setValue(false);
            },
            (message, isEmpty) -> {
                isLoading.setValue(false);
            });
    }
}

package com.example.woomansi.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.cometj03.composetimetable.TimeTableData;
import com.example.woomansi.data.model.ScheduleModel;
import com.example.woomansi.data.model.UserModel;
import com.example.woomansi.data.repository.FirebaseGroup;
import com.example.woomansi.data.repository.FirebaseGroupSchedule;
import com.example.woomansi.data.repository.FirebaseUserSchedule;
import com.example.woomansi.util.ScheduleTypeTransform;
import com.example.woomansi.util.TimeFormatUtil;
import com.example.woomansi.util.UserCache;

import java.time.LocalTime;
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

        // ?????? ???????????? ????????? ?????? ?????????
        FirebaseUserSchedule.addSchedule(
                user.getIdToken(),
                dayName,
                schedule,
                a -> {
                    // ?????? ????????? ???????????? ?????? ????????? ?????? ?????? document id ???????????? ?????????
                    FirebaseGroup.getGroupIds(
                            user.getIdToken(),
                            groupIdList -> {
                                final int[] successCount = {0}; // ?????? deleteSchedule()?????? ?????? ??????
                                int groupCount = groupIdList.size();

                                for (String groupId : groupIdList) {
                                    // ????????? ?????? ??????????????? ????????? ?????????
                                    FirebaseGroupSchedule.unionSchedule(
                                            groupId, dayName, schedule,
                                            () -> {
                                                successCount[0]++;
                                                if (successCount[0] >= groupCount)
                                                    errorMessage.setValue(null);
                                            },
                                            errorMsg -> errorMessage.setValue(errorMsg)
                                    );
                                }
                                if (groupIdList.size() == 0) {
                                    // groupIdList??? ????????? 0??? ?????? ??? for ?????? ??? ?????? ?????????
                                    // ?????? ?????????????????? ???
                                    errorMessage.setValue(null);
                                }
                            },
                            errorMsg -> errorMessage.setValue(errorMsg)
                    );
                },
                errorMsg -> errorMessage.setValue(errorMsg)
        );
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
                    // ?????? ????????? ???????????? ?????? ????????? ?????? ?????? document id ???????????? ?????????
                    FirebaseGroup.getGroupIds(
                            user.getIdToken(),
                            groupIdList -> {
                                // ?????? ????????? ???????????? ?????? ????????? ?????? ?????? ??? ????????????
                                // ?????? ????????? ??? ?????? ???????????? ?????? ????????? ??? ??? ??????????????? ???????????? ???????????????.
                                // int ????????? ???????????? ????????? ??????????????? ?????? ????????? ????????? ?????? 1?????? ????????? ??????????????????.
                                final int[] successCount = {0};
                                int groupCount = groupIdList.size();

                                for (String groupId : groupIdList) {
                                    // ????????? ?????? ??????????????? ????????? ?????????
                                    FirebaseGroupSchedule.minusSchedule(
                                            groupId, dayName, schedule,
                                            () -> {
                                                successCount[0]++;
                                                if (successCount[0] >= groupCount)
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
    }

    private void loadSchedules(List<String> dayNameList) {
        UserModel user = UserCache.getUser(null);
        if (user == null)
            return;

        isLoading.setValue(true);
        FirebaseUserSchedule.getSchedulesWithChangeListener(
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

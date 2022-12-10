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

        // 개인 시간표에 일정을 먼저 추가함
        FirebaseUserSchedule.addSchedule(
                user.getIdToken(),
                dayName,
                schedule,
                a -> {
                    // 현재 유저가 가입되어 있는 그룹을 찾고 그룹 document id 리스트를 반환함
                    FirebaseGroup.getGroupIds(
                            user.getIdToken(),
                            groupIdList -> {
                                final int[] successCount = {0}; // 아래 deleteSchedule()에서 주석 확인
                                int groupCount = groupIdList.size();

                                for (String groupId : groupIdList) {
                                    // 각각의 그룹 시간표에서 일정을 제외함
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
                                    // groupIdList의 길이가 0일 때는 위 for 문을 안 돌기 때문에
                                    // 따로 처리해주어야 함
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
                    // 현재 유저가 가입되어 있는 그룹을 찾고 그룹 document id 리스트를 반환함
                    FirebaseGroup.getGroupIds(
                            user.getIdToken(),
                            groupIdList -> {
                                // 현재 유저가 가입되어 있는 그룹이 여러 개일 수 있으므로
                                // 해당 일정을 몇 개의 그룹에서 빼는 과정을 몇 번 성공했는지 저장하는 변수입니다.
                                // int 변수는 익명함수 내에서 수정하려고 하면 오류가 생겨서 길이 1짜리 배열을 사용했습니다.
                                final int[] successCount = {0};
                                int groupCount = groupIdList.size();

                                for (String groupId : groupIdList) {
                                    // 각각의 그룹 시간표에서 일정을 제외함
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

package com.example.woomansi.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cometj03.composetimetable.TimeTableData;
import com.example.woomansi.data.model.GroupModel;
import com.example.woomansi.data.model.VoteScheduleModel;
import com.example.woomansi.data.repository.FirebaseGroup;
import com.example.woomansi.data.repository.FirebaseGroupVote;
import com.example.woomansi.util.ScheduleTypeTransform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class VoteJoinViewModel extends ViewModel {
    private MutableLiveData<TimeTableData> timeTableData;

    private Map<String, List<VoteScheduleModel>> voteScheduleMap;
    private Map<String, List<Boolean>> selectedVoteSchedule;

    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<TimeTableData> getTimeTableData(List<String> dayNameList, GroupModel groupModel) {
        if (timeTableData == null) {
            timeTableData = new MutableLiveData<>();
            loadSchedules(dayNameList, groupModel);
        }
        return timeTableData;
    }

    public void castVote(String userId, GroupModel groupModel) {
        if (!isSelected(selectedVoteSchedule)) {
            setError("원하시는 시간을 하나 이상 선택해주세요");
            return;
        }

        FirebaseGroup.getSpecificGroupId(groupModel.getGroupName(), groupModel.getGroupPassword(), groupId -> {
            FirebaseGroupVote.castVote(userId, groupId, selectedVoteSchedule,
                    success -> setError(null),
                    errorMsg -> setError(errorMsg));
        }, errorMsg -> setError(errorMsg));
    }

    private void loadSchedules(List<String> dayNameList, GroupModel groupModel) {
        if (groupModel == null)
            return;

        FirebaseGroup.getSpecificGroupId(
                groupModel.getGroupName(),
                groupModel.getGroupPassword(),
                groupId -> {
                    FirebaseGroupVote.getVoteModel(
                            groupId,
                            voteModel -> {
                                voteScheduleMap = voteModel.getVoteScheduleList();
                                initialSelectedVote(dayNameList, voteModel.getVoteScheduleList());
                                updateTimeTable(dayNameList);
                            },
                            errorMsg -> {});
                },
                errorMsg -> {});
    }

    // row: 해당 행에서 몇 번째 원소인지
    public void clickVoteSchedule(String dayName, int row, List<String> dayNameList) {
        boolean isSelected = selectedVoteSchedule.get(dayName).get(row);
        selectedVoteSchedule.get(dayName).set(row, !isSelected);
        updateTimeTable(dayNameList);
    }

    private void updateTimeTable(List<String> dayNameList) {
        TimeTableData timeTable = ScheduleTypeTransform.voteScheduleMapToTimeTableData(
                dayNameList, voteScheduleMap, selectedVoteSchedule);
        timeTableData.setValue(timeTable);
    }

    private void initialSelectedVote(List<String> dayNameList, Map<String, List<VoteScheduleModel>> voteScheduleMap) {
        if (selectedVoteSchedule == null)
            selectedVoteSchedule = new HashMap<>();
        for (String dayName : dayNameList) {
            List<Boolean> tmp = new ArrayList<>();
            if (voteScheduleMap.get(dayName) != null) {
                for (VoteScheduleModel model : voteScheduleMap.get(dayName)) {
                    tmp.add(false);
                }
            }
            selectedVoteSchedule.put(dayName, tmp);
        }
    }

    // 선택된 게 하나라도 있으면 true, 없다면 false
    private boolean isSelected(Map<String, List<Boolean>> selectedMap) {
        for (String key : selectedMap.keySet()) {
            for (int i = 0; i < selectedMap.get(key).size(); i++)
                if (selectedMap.get(key).get(i))
                    return true;
        }
        return false;
    }

    private void setError(@Nullable String msg) {
        errorMessage.setValue(msg);
    }
}

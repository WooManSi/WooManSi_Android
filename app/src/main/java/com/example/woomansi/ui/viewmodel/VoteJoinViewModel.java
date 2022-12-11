package com.example.woomansi.ui.viewmodel;

import android.util.Log;

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

public class VoteJoinViewModel extends ViewModel {
    private MutableLiveData<TimeTableData> timeTableData;

    private Map<String, List<VoteScheduleModel>> voteScheduleMap;

    private Map<String, List<Boolean>> selectedVoteSchedule;
    public Map<String, List<Boolean>> getSelectedVoteSchedule() {
        return selectedVoteSchedule;
    }

    public LiveData<TimeTableData> getTimeTableData(List<String> dayNameList, GroupModel groupModel) {
        if (timeTableData == null) {
            timeTableData = new MutableLiveData<>();
            loadSchedules(dayNameList, groupModel);
        }
        return timeTableData;
    }

    public void loadSchedules(List<String> dayNameList, GroupModel groupModel) {
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
}

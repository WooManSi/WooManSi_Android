package com.example.woomansi.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cometj03.composetimetable.TimeTableData;
import com.example.woomansi.data.model.GroupModel;
import com.example.woomansi.data.model.VoteScheduleModel;
import com.example.woomansi.data.repository.FirebaseGroup;
import com.example.woomansi.data.repository.FirebaseGroupSchedule;
import com.example.woomansi.data.repository.FirebaseGroupVote;
import com.example.woomansi.util.ScheduleTypeTransform;

import java.util.List;
import java.util.Map;

public class VoteJoinViewModel extends ViewModel {
    private MutableLiveData<TimeTableData> timeTableData;

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
                                TimeTableData timeTable = ScheduleTypeTransform.voteScheduleMapToTimeTableData(
                                        dayNameList, voteModel.getVoteScheduleList());
                                timeTableData.setValue(timeTable);
                            },
                            errorMsg -> {});
                },
                errorMsg -> {});
    }
}

package com.example.woomansi.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.cometj03.composetimetable.TimeTableData;
import com.example.woomansi.data.model.GroupModel;
import com.example.woomansi.data.model.VoteModel;
import com.example.woomansi.data.repository.FirebaseGroup;
import com.example.woomansi.data.repository.FirebaseGroupSchedule;
import com.example.woomansi.data.repository.FirebaseGroupVote;
import com.example.woomansi.util.ScheduleTypeTransform;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class VoteCreateViewModel extends ViewModel {
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<TimeTableData> timeTableData;

    private Map<String, List<Integer>> groupScheduleMap;

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<TimeTableData> getTimeTableData(List<String> dayNameList, GroupModel groupModel, int initialLimit) {
        if (timeTableData == null) {
            timeTableData = new MutableLiveData<>();
            loadSchedules(dayNameList, groupModel, initialLimit);
        }
        return timeTableData;
    }

    public void loadSchedules(List<String> dayNameList, GroupModel groupModel, int initialLimit) {
        if (groupModel == null)
            return;

        isLoading.setValue(true);
        FirebaseGroup.getSpecificGroupId(
                groupModel.getGroupName(),
                groupModel.getGroupPassword(),
                groupId -> {
                    FirebaseGroupSchedule.fetchGroupSchedule(
                            groupId, null, // 어차피 그룹 스케줄 정의되어 있을테니 dayNames null이어도 됨
                            groupSchedule -> {
                                groupScheduleMap = groupSchedule;
                                updateOverlapedPeople(dayNameList, initialLimit);
                                setError(null);
                            },
                            errorMsg -> setError(errorMsg)
                    );
                },
                errorMsg -> setError(errorMsg));
    }

    public void saveScheduleEntity(
        List<String> dayNameList,
        GroupModel groupModel,
        int overlapPeople
    ) {
        if (groupModel == null)
            return;

        FirebaseFirestore
            .getInstance()
            .collection("groups")
            .whereEqualTo("groupName", groupModel.getGroupName())
            .whereEqualTo("groupPassword", groupModel.getGroupPassword())
            .get()
            .addOnCompleteListener(task -> {
                //그룹을 찾았을 경우
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult().getDocuments().get(0);
                    DocumentReference group = document.getReference();
                    String groupId = group.getId();

                    FirebaseGroupSchedule.fetchGroupSchedule(
                        groupId,
                        dayNameList,
                        groupSchedule-> {
                            VoteModel voteModel
                                = ScheduleTypeTransform.groupScheduleMapToVoteSchedule(dayNameList, groupSchedule, overlapPeople);
                            FirebaseGroupVote.createVote(
                                    groupId, voteModel,
                                    () -> {},
                                    errorMsg -> {}
                            );
                        },
                        message -> {
                            errorMessage.setValue(message);
                        });
                }
            });
    }

    public void updateOverlapedPeople(List<String> dayNameList, int peopleOverlapLimit) {
        if (groupScheduleMap == null)
            return;
        TimeTableData tableData = ScheduleTypeTransform
                .groupScheduleMapToTimeTableData(dayNameList, groupScheduleMap, peopleOverlapLimit);
        timeTableData.setValue(tableData);
    }

    private void setError(@Nullable String errorMsg) {
        errorMessage.setValue(errorMsg);
        isLoading.setValue(false);
    }
}

package com.example.woomansi.ui.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cometj03.composetimetable.TimeTableData;
import com.example.woomansi.data.model.GroupModel;
import com.example.woomansi.data.model.VoteModel;
import com.example.woomansi.data.repository.FirebaseGroup;
import com.example.woomansi.data.repository.FirebaseGroupSchedule;
import com.example.woomansi.util.ScheduleTypeTransform;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class GroupDetailViewModel extends ViewModel {

    private static final String CLASS_NAME = "GroupDetailViewModel";

    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<TimeTableData> timeTableData;

    private Map<String, List<Integer>> groupScheduleMap;

    private MutableLiveData<Boolean> canVoteJoin;
    private MutableLiveData<Boolean> canVoteResult;

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

    public Map<String, LiveData<Boolean>> getCanVoteJoinAndResult(GroupModel groupModel) {
        if (canVoteJoin == null) {
            canVoteJoin = new MutableLiveData<>();
        }
        if (canVoteResult == null) {
            canVoteResult = new MutableLiveData<>();
        }
        checkVoteDataExist(groupModel);
        return Map.of("canVoteJoin", canVoteJoin, "canVoteResult", canVoteResult);
    }

    private void loadSchedules(List<String> dayNameList, GroupModel groupModel, int initialLimit) {
        if (groupModel == null)
            return;

        FirebaseGroup.getSpecificGroupId(
                groupModel.getGroupName(),
                groupModel.getGroupPassword(),
                groupId -> {
                    FirebaseGroupSchedule.fetchGroupScheduleWithChangeListener(
                            groupId, null, // ????????? ?????? ????????? ???????????? ???????????? dayNames null????????? ???
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

    public void updateOverlapedPeople(List<String> dayNameList, int peopleOverlapLimit) {
        if (groupScheduleMap == null)
            return;
        TimeTableData tableData = ScheduleTypeTransform
                .groupScheduleMapToTimeTableData(dayNameList, groupScheduleMap, peopleOverlapLimit);
        timeTableData.setValue(tableData);
    }

    private void checkVoteDataExist(GroupModel groupModel) {
        String curFunctionName = "checkVoteDataExist";

        FirebaseFirestore
                .getInstance()
                .collection("groups")
                .whereEqualTo("groupName", groupModel.getGroupName())
                .whereEqualTo("groupPassword", groupModel.getGroupPassword())
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) {
                        Log.w(CLASS_NAME, curFunctionName + "-> ???????????? ??????????????? ???????????? ??????", e);
                        return;
                    }

                    if (snapshot != null && !snapshot.isEmpty()) {
                        setDebugLogMessage(curFunctionName, "????????? ?????? ????????? ??????");
                        DocumentSnapshot document = snapshot.getDocuments().get(0);
                        DocumentReference ref = document.getReference();
                        GroupModel currentGroupModel = document.toObject(GroupModel.class);

                        setBtnVisibility(ref.getId(), currentGroupModel.getMemberList());
                    } else {
                        setDebugLogMessage(curFunctionName, "????????? ?????? ???????????? ???????????? ??????");
                    }
                });
    }

    private void setBtnVisibility(String groupId, List<String> memberList) {
        String curFunctionName = "setBtnVisibility";

        canVoteJoin.setValue(false);
        canVoteResult.setValue(false);
        FirebaseFirestore
                .getInstance()
                .collection("group_votes")
                .document(groupId)
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) {
                        Log.w(CLASS_NAME, "setBtnVisibility-> ?????? VISIBLE ?????? ??????.", e);
                        return;
                    }

                    if (snapshot != null && snapshot.exists()) {
                        setDebugLogMessage(curFunctionName, "?????? ?????????: " + snapshot.getData());
                        VoteModel voteModel = snapshot.toObject(VoteModel.class);
                        //????????? ???????????? ????????? ?????? ??????????????? ??????
                        if (voteModel.getVoteFinishedMember().containsAll(memberList)) {
                            //?????????????????? ?????? ????????????
                            //?????????????????? ?????? ?????????
                            canVoteJoin.setValue(false);
                            canVoteResult.setValue(true);
                            setDebugLogMessage(curFunctionName, "?????? ??????");
                        } else { //????????? ???????????? ????????? ?????? ?????? ???????????? ?????? ??????
                            //?????????????????? ?????? ?????????
                            //?????????????????? ?????? ????????????
                            canVoteJoin.setValue(true);
                            canVoteResult.setValue(false);
                            setDebugLogMessage(curFunctionName, "?????? ?????????");
                        }
                    } else {
                        //?????????????????? ?????? ????????????
                        //?????????????????? ?????? ????????????
                        canVoteJoin.setValue(false);
                        canVoteResult.setValue(false);
                        setDebugLogMessage(curFunctionName, "?????? ????????? null");
                    }
                });
    }

    private void setDebugLogMessage(String curFunctionName, String message) {
        Log.d(CLASS_NAME, curFunctionName + "-> " + message);
    }

    private void setError(@Nullable String errorMsg) {
        errorMessage.setValue(errorMsg);
        isLoading.setValue(false);
    }
}

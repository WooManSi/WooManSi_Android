package com.example.woomansi.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.cometj03.composetimetable.TimeTableData;
import com.example.woomansi.data.model.GroupModel;
import com.example.woomansi.data.model.VoteModel;
import com.example.woomansi.data.repository.FirebaseGroupSchedule;
import com.example.woomansi.util.GroupScheduleTypeTransform;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

public class GroupDetailViewModel extends ViewModel {

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private MutableLiveData<TimeTableData> timeTableData;

    private MutableLiveData<Boolean> canVoteJoin;
    private MutableLiveData<Boolean> canVoteResult;

    public LiveData<TimeTableData> getTimeTableData(List<String> dayNameList, GroupModel groupModel) {
        if (timeTableData == null) {
            timeTableData = new MutableLiveData<>();
            loadSchedules(dayNameList, groupModel);
        }
        return timeTableData;
    }

    public LiveData<Boolean> getCanVoteJoin(GroupModel groupModel) {
        if (canVoteJoin == null) {
            canVoteJoin = new MutableLiveData<>();
        }
        checkVoteDataExist(groupModel);
        return canVoteJoin;
    }
    public LiveData<Boolean> getCanVoteResult(GroupModel groupModel) {
        if (canVoteResult == null) {
            canVoteResult = new MutableLiveData<>();
        }
        checkVoteDataExist(groupModel);
        return canVoteResult;
    }

    public void loadSchedules(List<String> dayNameList, GroupModel groupModel) {
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
                    group.getId();

                    isLoading.setValue(true);
                    FirebaseGroupSchedule.fetchGroupSchedule(
                        group.getId(),
                        dayNameList,
                        groupSchedule-> {
                            TimeTableData tableData
                                = GroupScheduleTypeTransform.groupScheduleMapToTimeTableData(dayNameList, groupSchedule, 0);
                            timeTableData.setValue(tableData);
                            isLoading.setValue(false);
                        },
                        message -> {
                            errorMessage.setValue(message);
                            isLoading.setValue(false);
                        });
                }
            });
    }

    public void checkVoteDataExist(GroupModel groupModel) {
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
                    DocumentReference ref = document.getReference();
                    GroupModel currnetGroupModel = document.toObject(GroupModel.class);

                    setBtnVisibility(ref.getId(), currnetGroupModel.getMemberList());
                }
            });
    }

    public void setBtnVisibility(String groupId, List<String> memberList) {
        canVoteJoin.setValue(false);
        canVoteResult.setValue(false);
        FirebaseFirestore
            .getInstance()
            .collection("group_votes")
            .document(groupId)
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    //투표 존재 시
                    if(document.exists()) {
                        VoteModel voteModel = document.toObject(VoteModel.class);
                        //투표가 존재하고 멤버가 전부 투표완료한 경우
                        if(voteModel.getVoteFinishedMember().containsAll(memberList)) {
                            //투표참여하기 버튼 비활성화
                            //투표결과보기 버튼 활성화
                            canVoteResult.setValue(true);
                        } else { //투표가 존재하나 멤버가 아직 전부 투표하지 않은 경우
                            //투표참여하기 버튼 활성화
                            //투표결과보기 버튼 비활성화
                            canVoteJoin.setValue(true);
                        }
                    } else { //투표가 존재하지 않을 시
                        //투표참여하기 버튼 비활성화
                        //투표결과보기 버튼 비활성화
                    }
                }
            });
    }
}

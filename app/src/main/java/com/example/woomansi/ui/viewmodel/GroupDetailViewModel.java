package com.example.woomansi.ui.viewmodel;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.cometj03.composetimetable.TimeTableData;
import com.example.woomansi.data.model.GroupModel;
import com.example.woomansi.data.model.VoteModel;
import com.example.woomansi.data.repository.FirebaseGroupSchedule;
import com.example.woomansi.util.ScheduleTypeTransform;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;
import java.util.Map;

public class GroupDetailViewModel extends ViewModel {

  private static final String CLASS_NAME = "GroupDetailViewModel";

  private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
  private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
  private MutableLiveData<TimeTableData> timeTableData;

  private MutableLiveData<Boolean> canVoteJoin;
  private MutableLiveData<Boolean> canVoteResult;

  public LiveData<TimeTableData> getTimeTableData(List<String> dayNameList, GroupModel groupModel) {
    if (timeTableData == null) {
      timeTableData = new MutableLiveData<>();
    }
    loadSchedules(dayNameList, groupModel);
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

  private void loadSchedules(List<String> dayNameList, GroupModel groupModel) {
    String curFunctionName = "loadSchedules";

    if (groupModel == null) {
      return;
    }

    FirebaseFirestore
        .getInstance()
        .collection("groups")
        .whereEqualTo("groupName", groupModel.getGroupName())
        .whereEqualTo("groupPassword", groupModel.getGroupPassword())
        .addSnapshotListener((snapshot, e) -> {
          if (e != null) {
            Log.w(CLASS_NAME, curFunctionName + "-> group 가져오기 실패.", e);
            return;
          }

          if (snapshot != null) {
            DocumentSnapshot document = snapshot.getDocuments().get(0);
            DocumentReference group = document.getReference();
            group.getId();

            isLoading.setValue(true);
            FirebaseGroupSchedule.fetchGroupSchedule(
                group.getId(),
                dayNameList,
                groupSchedule -> {
                  TimeTableData tableData
                      = ScheduleTypeTransform.groupScheduleMapToTimeTableData(dayNameList,
                      groupSchedule, 0);
                  timeTableData.setValue(tableData);
                  isLoading.setValue(false);
                },
                message -> {
                  errorMessage.setValue(message);
                  isLoading.setValue(false);
                });
          } else {
            setDebugLogMessage(curFunctionName, "그룹스케쥴 데이터: null");
          }
        });
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
            Log.w(CLASS_NAME, curFunctionName + "-> 서버에서 그룹데이터 가져오기 실패", e);
            return;
          }

          if (snapshot != null) {
            setDebugLogMessage(curFunctionName, "서버에 그룹 데이터 존재");
            DocumentSnapshot document = snapshot.getDocuments().get(0);
            DocumentReference ref = document.getReference();
            GroupModel currentGroupModel = document.toObject(GroupModel.class);

            setBtnVisibility(ref.getId(), currentGroupModel.getMemberList());
          } else {
            setDebugLogMessage(curFunctionName, "서버에 그룹 데이터가 존재하지 않음");
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
            Log.w(CLASS_NAME, "setBtnVisibility-> 버튼 VISIBLE 지정 실패.", e);
            return;
          }

          if (snapshot != null && snapshot.exists()) {
            setDebugLogMessage(curFunctionName, "투표 데이터: " + snapshot.getData());
            VoteModel voteModel = snapshot.toObject(VoteModel.class);
            //투표가 존재하고 멤버가 전부 투표완료한 경우
            if (voteModel.getVoteFinishedMember().containsAll(memberList)) {
              //투표참여하기 버튼 비활성화
              //투표결과보기 버튼 활성화
              canVoteJoin.setValue(false);
              canVoteResult.setValue(true);
              setDebugLogMessage(curFunctionName, "투표 완료");
            } else { //투표가 존재하나 멤버가 아직 전부 투표하지 않은 경우
              //투표참여하기 버튼 활성화
              //투표결과보기 버튼 비활성화
              canVoteJoin.setValue(true);
              canVoteResult.setValue(false);
              setDebugLogMessage(curFunctionName, "투표 미완료");
            }
          } else {
            //투표참여하기 버튼 비활성화
            //투표결과보기 버튼 비활성화
            canVoteJoin.setValue(false);
            canVoteResult.setValue(false);
            setDebugLogMessage(curFunctionName, "투표 데이터 null");
          }
        });
  }

  private void setDebugLogMessage(String curFunctionName, String message) {
    Log.d(CLASS_NAME, curFunctionName + "-> " + message);
  }
}

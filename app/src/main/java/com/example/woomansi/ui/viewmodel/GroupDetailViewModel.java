package com.example.woomansi.ui.viewmodel;

import android.util.Log;
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
import java.util.Map;

public class GroupDetailViewModel extends ViewModel {

  private static final String TAG = "GroupDetailViewModel";

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

  public Map<String ,LiveData<Boolean>> getCanVoteJoinAndResult(GroupModel groupModel) {
    if (canVoteJoin == null) {
      canVoteJoin = new MutableLiveData<>();
    }
    if (canVoteResult == null) {
      canVoteResult = new MutableLiveData<>();
    }
    checkVoteDataExist(groupModel);
    return Map.of("canVoteJoin", canVoteJoin, "canVoteResult", canVoteResult);
  }

    public void loadSchedules(List<String> dayNameList, GroupModel groupModel) {
        if (groupModel == null)
            return;

    FirebaseFirestore
        .getInstance()
        .collection("groups")
        .whereEqualTo("groupName", groupModel.getGroupName())
        .whereEqualTo("groupPassword", groupModel.getGroupPassword())
        .addSnapshotListener((snapshot, e) -> {
          if (e != null) {
            Log.w(TAG, "loadSchedule -> group 가져오기 실패.", e);
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
                      = GroupScheduleTypeTransform.groupScheduleMapToTimeTableData(dayNameList,
                      groupSchedule, 0);
                  timeTableData.setValue(tableData);
                  isLoading.setValue(false);
                },
                message -> {
                  errorMessage.setValue(message);
                  isLoading.setValue(false);
                });
          } else {
            Log.d(TAG, "그룹스케쥴 데이터: null");
          }
        });
  }

  public void checkVoteDataExist(GroupModel groupModel) {
    FirebaseFirestore
        .getInstance()
        .collection("groups")
        .whereEqualTo("groupName", groupModel.getGroupName())
        .whereEqualTo("groupPassword", groupModel.getGroupPassword())
        .addSnapshotListener((snapshot, e) -> {
          DocumentSnapshot document = snapshot.getDocuments().get(0);
          DocumentReference ref = document.getReference();
          GroupModel currentGroupModel = document.toObject(GroupModel.class);

          setBtnVisibility(ref.getId(), currentGroupModel.getMemberList());
        });
  }

  public void setBtnVisibility(String groupId, List<String> memberList) {
    canVoteJoin.setValue(false);
    canVoteResult.setValue(false);
    FirebaseFirestore
        .getInstance()
        .collection("group_votes")
        .document(groupId)
        .addSnapshotListener((snapshot, e) -> {
          if (e != null) {
            Log.w(TAG, "버튼 VISIBLE 지정 실패.", e);
            return;
          }

          if (snapshot != null && snapshot.exists()) {
            Log.d(TAG, "setBtnVisibility-> 투표 데이터: " + snapshot.getData());
            VoteModel voteModel = snapshot.toObject(VoteModel.class);
            //투표가 존재하고 멤버가 전부 투표완료한 경우
            if (voteModel.getVoteFinishedMember().containsAll(memberList)) {
              //투표참여하기 버튼 비활성화
              //투표결과보기 버튼 활성화
              canVoteJoin.setValue(false);
              canVoteResult.setValue(true);
              Log.d(TAG, "setBtnVisibility-> 투표 완료");
            } else { //투표가 존재하나 멤버가 아직 전부 투표하지 않은 경우
              //투표참여하기 버튼 활성화
              //투표결과보기 버튼 비활성화
              canVoteJoin.setValue(true);
              canVoteResult.setValue(false);
              Log.d(TAG, "setBtnVisibility-> 투표 미완료");
            }
          } else {
            //투표참여하기 버튼 비활성화
            //투표결과보기 버튼 비활성화
            canVoteJoin.setValue(false);
            canVoteResult.setValue(false);
            Log.d(TAG, "setBtnVisibility-> 투표 데이터 null");
          }
        });
  }
}

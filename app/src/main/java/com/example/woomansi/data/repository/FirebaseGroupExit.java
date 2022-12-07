package com.example.woomansi.data.repository;

import com.example.woomansi.data.model.GroupModel;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class FirebaseGroupExit {

  public interface OnExitSuccessListener {
    void onSuccess();
  }

  public static void groupLeaderExit(
      GroupModel groupModel,
      OnExitSuccessListener s,
      OnFailedListener f
  ) {
    //만약 그룹탈퇴를 요청한 사용자가 리더일 경우, 그룹 자체를 삭제
    FirebaseFirestore
        .getInstance()
        .collection("groups")
        .whereEqualTo("groupName", groupModel.getGroupName())
        .whereEqualTo("groupPassword", groupModel.getGroupPassword())
        .get()
        .addOnCompleteListener(task -> {
          if (task.isSuccessful()) {
            //그룹 삭제
            DocumentSnapshot document = task.getResult().getDocuments().get(0);
            DocumentReference group = document.getReference();
            group.delete();

            //그룹 시간표 삭제
            FirebaseGroupSchedule.deleteGroupSchedule(group.getId());
            s.onSuccess();
          }
          else {
            f.onFailed("그룹 탈퇴가 이루어지지 않았습니다.");
          }
        });
  }

  public static void groupMemberExit(
      GroupModel groupModel,
      ArrayList<String> memberList,
      String memberId,
      List<String> dayNameList,
      OnExitSuccessListener s,
      OnFailedListener f
  ) {
    //만약 그룹탈퇴를 요청한 사용자가 그룹원일 경우, 해당 그룹원을 멤버 리스트에서 제외
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

            //1. 그룹 스케쥴 업데이트
            //2. 먼저 멤버의 스케쥴을 불러옴
            FirebaseUserSchedule.getSchedules(
                memberId,
                dayNameList,
                scheduleMap -> {
                  // 2-1. 스케줄 불러오는 것을 성공했으면
                  // 3. 해당 그룹 스케쥴에서 멤버 스케쥴 제거
                  FirebaseGroupSchedule.minusSchedules(
                      group.getId(),
                      scheduleMap,
                      () -> {
                        //3-1. 제거를 성공했을 때 할 일
                      },
                      f //3-2. 제거를 실패했을 때 할 일
                  );
                },
                message -> {
                  //2-2. 멤버 스케쥴 불러오기를 실패했을 때 할 일
                });

            //그룹 멤버 리스트 업데이트
            group.update("memberList", memberList);
            s.onSuccess();
          }
        });
  }
}

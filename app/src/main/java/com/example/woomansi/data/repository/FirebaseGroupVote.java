package com.example.woomansi.data.repository;

import android.util.Log;
import com.example.woomansi.data.model.VoteModel;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseGroupVote {
  private static final String COLLECTION_NAME = "group_votes";
  private static final String TAG = "FirebaseGroupVote";

  // 투표데이터를 서버에 생성하는 함수
  public static void createVote(String groupId, VoteModel voteModel) {
    FirebaseFirestore
        .getInstance()
        .collection(COLLECTION_NAME)
        .document(groupId)
        .set(voteModel)
        .addOnCompleteListener(task -> {
          if (task.isSuccessful()) {
            Log.d(TAG, "그룹 스케쥴 데이터 생성완료");
          } else {
            Log.d(TAG, "그룹 스케쥴 데이터 생성을 실패하였습니다.");
          }
        });
  }
}

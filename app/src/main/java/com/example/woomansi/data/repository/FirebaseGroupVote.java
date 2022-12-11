package com.example.woomansi.data.repository;

import com.example.woomansi.data.model.VoteModel;
import com.example.woomansi.data.model.VoteScheduleModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Map;

public class FirebaseGroupVote {

    private static final String COLLECTION_NAME = "group_votes";

    public interface OnVoteCreateSuccessListener {
        void onSuccess();
    }

    public interface OnGetVoteModelSuccessListener {
        void onSuccess(VoteModel voteModel);
    }

    public interface OnCheckVoteExistListener {
        void onCheck(boolean exist);
    }

    // 투표데이터를 서버에 생성하는 함수
    public static void createVote(
            String groupId,
            VoteModel voteModel,
            OnVoteCreateSuccessListener s,
            OnFailedListener f
    ) {
        FirebaseFirestore
                .getInstance()
                .collection(COLLECTION_NAME)
                .document(groupId)
                .set(voteModel)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        f.onFailed("투표 생성 실패");
                        return;
                    }
                    s.onSuccess();
                });
    }

    // 진행중인 투표의 정보를 불러오는 함수
    public static void getVoteModel(String groupId, OnGetVoteModelSuccessListener s, OnFailedListener f) {
        FirebaseFirestore
                .getInstance()
                .collection(COLLECTION_NAME)
                .document(groupId)
                .get()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        f.onFailed("get vote schedule failed");
                        return;
                    }
                    if (!task.getResult().exists()) {
                        f.onFailed("진행중인 투표가 존재하지 않습니다.");
                        return;
                    }
                    VoteModel voteModel = task.getResult().toObject(VoteModel.class);
                    s.onSuccess(voteModel);
                });
    }

    // 진행중인 투표가 있는지 체크하는 함수
    public static void checkIfVoteExists(String groupId, OnCheckVoteExistListener l) {
        FirebaseFirestore
                .getInstance()
                .collection(COLLECTION_NAME)
                .document(groupId)
                .get()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful() || !task.getResult().exists()) {
                        l.onCheck(false);
                        return;
                    }
                    l.onCheck(true);
                });
    }
}

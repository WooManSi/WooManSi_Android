package com.example.woomansi.data.repository;

import com.example.woomansi.data.model.VoteModel;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseGroupVote {

    private static final String COLLECTION_NAME = "group_votes";

    public interface OnVoteCreateSuccessListener {
        void onSuccess();
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
}

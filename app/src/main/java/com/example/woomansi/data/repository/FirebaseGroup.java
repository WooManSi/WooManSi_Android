package com.example.woomansi.data.repository;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FirebaseGroup {
    private static final String COLLECTION_NAME = "groups";

    public interface OnFindGroupSuccess {
        void onSuccess(List<String> groupIdList);
    }

    // 현재 유저가 가입되어 있는 그룹을 찾는 함수
    public static void getGroups(String userId, OnFindGroupSuccess s, OnFailedListener f) {
        FirebaseFirestore
                .getInstance()
                .collection(COLLECTION_NAME)
                .whereArrayContains("memberList", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        f.onFailed("그룹 정보를 가져오는 도중 오류가 발생했습니다.");
                        return;
                    }
                    if (task.getResult().isEmpty()) {
                        s.onSuccess(List.of());
                        return;
                    }
                    List<String> groupIdList = new ArrayList<>();
                    for (DocumentSnapshot doc : task.getResult())
                        groupIdList.add(doc.getReference().getId());
                    s.onSuccess(groupIdList);
                });
    }
}

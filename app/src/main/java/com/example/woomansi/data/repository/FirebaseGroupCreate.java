package com.example.woomansi.data.repository;

import com.example.woomansi.data.model.GroupModel;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseGroupCreate {
    private static final String COLLECTION_NAME = "groups";

    private interface OnGroupValidationSuccessListener {
        void onSuccess();
    }

    public interface OnCreateSuccessListener {
        void onSuccess(String documentPath);
    }

    public static void checkAndCreateGroup(GroupModel groupModel, OnCreateSuccessListener s, OnFailedListener f) {
        checkIfGroupIsValid(groupModel, () ->
                createGroup(groupModel, s, f), f);
    }

    // 그룹이 이미 존재하는지 확인하는 함수
    private static void checkIfGroupIsValid(
            GroupModel groupModel,
            OnGroupValidationSuccessListener s,
            OnFailedListener f
    ) {
        FirebaseFirestore
                .getInstance()
                .collection(COLLECTION_NAME)
                .whereEqualTo("groupName", groupModel.getGroupName())
                .whereEqualTo("groupPassword", groupModel.getGroupPassword())
                .get()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        f.onFailed("그룹을 불러오는 데 실패하였습니다.");
                        return;
                    }
                    if (!task.getResult().isEmpty()) {
                        // 그룹명과 비밀번호가 둘 다 똑같으면 그룹생성 불가.
                        f.onFailed("그룹명과 비밀번호가 모두 일치하는 동일한 그룹이 이미 존재합니다.");
                        return;
                    }
                    s.onSuccess();
                });
    }

    // 그룹을 생성하는 함수
    private static void createGroup(GroupModel groupModel, OnCreateSuccessListener s, OnFailedListener f) {
        FirebaseFirestore
                .getInstance()
                .collection(COLLECTION_NAME)
                .add(groupModel)
                .addOnSuccessListener(docRef -> s.onSuccess(docRef.getId()))
                .addOnFailureListener(e ->
                        f.onFailed("그룹 추가 과정에서 에러 발생\n" + e.getMessage()));
    }
}

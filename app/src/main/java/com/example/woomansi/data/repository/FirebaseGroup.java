package com.example.woomansi.data.repository;

import com.example.woomansi.data.model.GroupModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FirebaseGroup {
    private static final String COLLECTION_NAME = "groups";

    public interface OnFindGroupIdSuccessListener {
        void onSuccess(List<String> groupIdList);
    }

    public interface OnFindGroupModelSuccessListener {
        void onSuccess(List<GroupModel> groupModelList);
    }

    private interface OnGetGroupQuerySuccessListener {
        void onSuccess(QuerySnapshot querySnapshot);
    }

    // 현재 유저가 가입되어 있는 그룹을 찾는 함수
    public static void getGroupIds(String userId, OnFindGroupIdSuccessListener s, OnFailedListener f) {
        getGroupQueries(userId, querySnapshot -> {
            List<String> groupIdList = new ArrayList<>();
            for (DocumentSnapshot doc : querySnapshot)
                groupIdList.add(doc.getId());
            s.onSuccess(groupIdList);
        }, f);
    }

    public static void getGroupIdsWithChangeListener(String userId, OnFindGroupIdSuccessListener s, OnFailedListener f) {
        getGroupIds(userId, s, f);

        // 리스너 달아주는 코드
        FirebaseFirestore
                .getInstance()
                .collection(COLLECTION_NAME)
                .whereArrayContains("memberList", userId)
                .addSnapshotListener((snapshot, error) -> {
                    if (error != null) {
                        f.onFailed("Data Listen Failed\n" + error.getMessage());
                        return;
                    }
                    if (snapshot != null && !snapshot.isEmpty()) {
                        List<String> groupIdList = new ArrayList<>();
                        for (DocumentSnapshot doc : snapshot.getDocuments())
                            groupIdList.add(doc.getId());
                        s.onSuccess(groupIdList);
                    }
                });
    }

    public static void getGroupModelsWithChangeListener(String userId, OnFindGroupModelSuccessListener s, OnFailedListener f) {
        getGroupQueries(userId, querySnapshot -> {
            List<GroupModel> groupModelList = new ArrayList<>();
            for (DocumentSnapshot doc : querySnapshot)
                groupModelList.add(doc.toObject(GroupModel.class));
            s.onSuccess(groupModelList);
        }, f);

        // 리스너 달아주는 코드
        FirebaseFirestore
                .getInstance()
                .collection(COLLECTION_NAME)
                .whereArrayContains("memberList", userId)
                .addSnapshotListener((snapshot, error) -> {
                    if (error != null) {
                        f.onFailed("Data Listen Failed\n" + error.getMessage());
                        return;
                    }
                    if (snapshot == null || snapshot.isEmpty()) {
                        s.onSuccess(new ArrayList<>());
                        return;
                    }

                    List<GroupModel> groupModelList = new ArrayList<>();
                    for (DocumentSnapshot doc : snapshot.getDocuments())
                        groupModelList.add(doc.toObject(GroupModel.class));
                    s.onSuccess(groupModelList);
                });
    }

    // groupId 그룹에 멤버 한 명 추가
    public static void addMember(String groupId, String memberId, OnSuccessListener<Void> s, OnFailedListener f) {
        FirebaseFirestore
                .getInstance()
                .collection(COLLECTION_NAME)
                .document(groupId)
                .update("memberList", FieldValue.arrayUnion(groupId))
                .addOnSuccessListener(s)
                .addOnFailureListener(e ->
                        f.onFailed("그룹에 추가되지 않았습니다.\n" + e.getMessage()));
    }

    // groupId 그룹에 멤버 한 명 탈퇴
    public static void removeMember(String groupId, String memberId, OnSuccessListener<Void> s, OnFailedListener f) {
        FirebaseFirestore
                .getInstance()
                .collection(COLLECTION_NAME)
                .document(groupId)
                .update("memberList", FieldValue.arrayRemove(groupId))
                .addOnSuccessListener(s)
                .addOnFailureListener(e ->
                        f.onFailed("그룹에 추가되지 않았습니다.\n" + e.getMessage()));
    }

    private static void getGroupQueries(String userId, OnGetGroupQuerySuccessListener s, OnFailedListener f) {
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
                    s.onSuccess(task.getResult());
                });
    }
}

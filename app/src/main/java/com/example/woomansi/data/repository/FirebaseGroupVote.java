package com.example.woomansi.data.repository;

import com.example.woomansi.data.model.VoteModel;
import com.example.woomansi.data.model.VoteScheduleModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    // 선택지를 선택하고 투표하는 함수 (VoteModel 갱신)
    public static void castVote(
            String userId,
            String groupId,
            Map<String, List<Boolean>> selectedMap,
            OnSuccessListener<Void> s,
            OnFailedListener f
    ) {
        // VoteModel 필드 이름
        String memberFieldPath = "voteFinishedMember";

        DocumentReference ref = FirebaseFirestore.getInstance()
                .collection(COLLECTION_NAME).document(groupId);

        getVoteModel(groupId, voteModel -> {
            Map<String, List<VoteScheduleModel>> updatedList = new HashMap<>();

            // 하나씩 투표 수 갱신
            for (String dayName : selectedMap.keySet()) {
                List<Boolean> selectedList = selectedMap.get(dayName);
                List<VoteScheduleModel> origin = voteModel.getVoteScheduleList().get(dayName);

                for (int i = 0; i < selectedList.size(); i++) {
                    if (selectedList.get(i)) { // 해당 스케줄이 선택되었을 때
                        VoteScheduleModel newSchedule = origin.get(i);
                        newSchedule.setVoteNum(newSchedule.getVoteNum() + 1);
                        origin.set(i, newSchedule);
                    }
                }
                updatedList.put(dayName, origin);
            }
            voteModel.setVoteScheduleList(updatedList);

            // 투표 데이터 갱신
            ref.set(voteModel).addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    f.onFailed("투표 데이터 갱신 실패");
                    return;
                }
                // 투표 완료 멤버 추가
                ref.update(memberFieldPath, FieldValue.arrayUnion(userId))
                        .addOnCompleteListener(t -> {
                            if (!t.isSuccessful()) {
                                f.onFailed("투표되지 않았습니다.");
                                return;
                            }
                            s.onSuccess(null);
                        });
            });
        }, f);

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

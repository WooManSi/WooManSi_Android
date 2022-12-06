package com.example.woomansi.ui.screen.vote;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.woomansi.R;
import com.example.woomansi.data.model.GroupModel;
import com.example.woomansi.data.model.VoteModel;
import com.example.woomansi.data.model.VoteResultModel;
import com.example.woomansi.data.model.VoteScheduleModel;
import com.example.woomansi.ui.adapter.VoteResultListAdapter;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;

public class VoteResultActivity extends AppCompatActivity {

  private Button returnGroupActivityBtn;
  private ListView listView;
  private ArrayList<VoteResultModel> voteResultModelArrayList;
  private VoteResultListAdapter voteResultListAdapter;
  private MaterialToolbar topAppBar;
  private GroupModel group;

  private int maxVoteNum;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_vote_result_with_appbar);

    topAppBar= findViewById(R.id.voteResult_topAppBar);
    topAppBar.setNavigationOnClickListener(view -> finish());

    group = (GroupModel) getIntent().getSerializableExtra("group");

    this.InitializeMemberData();

    //리스트 뷰 초기화
    listView = findViewById(R.id.voteResult_lv_listView);
    voteResultListAdapter = new VoteResultListAdapter(VoteResultActivity.this, voteResultModelArrayList);

    listView.setAdapter(voteResultListAdapter);

    returnGroupActivityBtn = findViewById(R.id.voteResult_btn_return);
    returnGroupActivityBtn.setOnClickListener(v -> finish());
  }

  public void InitializeMemberData()
  {
    maxVoteNum = 0;
    voteResultModelArrayList = new ArrayList<>();

    FirebaseFirestore
        .getInstance()
        .collection("groups")
        .whereEqualTo("groupName", group.getGroupName())
        .whereEqualTo("groupPassword", group.getGroupPassword())
        .get()
        .addOnCompleteListener(task -> {
          //그룹을 찾았을 경우
          if (task.isSuccessful()) {
            DocumentSnapshot document = task.getResult().getDocuments().get(0);
            DocumentReference ref = document.getReference();

            FirebaseFirestore
                .getInstance()
                .collection("group_votes")
                .document(ref.getId())
                .get()
                .addOnCompleteListener(task2 -> {
                  if (task2.isSuccessful()) {
                    DocumentSnapshot document2 = task2.getResult();

                    //투표 존재 시
                    if (document2.exists()) {
                      VoteModel voteModel = document2.toObject(VoteModel.class);

                      //1. 가장 큰 voteNum 값을 찾음
                      voteModel.getVoteScheduleList()
                          .forEach((key, value) -> {
                            for (VoteScheduleModel voteScheduleModel : value) {
                              if (maxVoteNum < voteScheduleModel.getVoteNum()) {
                                maxVoteNum = voteScheduleModel.getVoteNum();
                              }
                            }
                          });

                      //2.maxVoteNum과 일치하는 결과들만 불러옴
                      voteModel.getVoteScheduleList()
                          .forEach((key, value) -> {
                            for (VoteScheduleModel voteScheduleModel : value) {
                              if (maxVoteNum == voteScheduleModel.getVoteNum()) {
                                VoteResultModel voteResultModel = new VoteResultModel(key, voteScheduleModel.getStartTime(), voteScheduleModel.getEndTime());
                                voteResultModelArrayList.add(voteResultModel);
                              }
                            }
                          });
                      //어뎁터에게 데이터 변경됬다는 알림 전송
                      voteResultListAdapter.notifyDataSetChanged();
                    }
                  }
                });
          }
        });
  }
}

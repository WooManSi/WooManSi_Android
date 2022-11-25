package com.example.woomansi.ui.screen.group;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar.OnMenuItemClickListener;
import com.example.woomansi.R;
import com.example.woomansi.data.model.GroupModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class GroupDetailActivity extends AppCompatActivity {

  private BottomSheetDialog dialog_groupInfo;
  private BottomSheetDialog dialog_vote;
  private GroupModel group;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_group_detail_with_appbar);

      group = (GroupModel) getIntent().getSerializableExtra("group");

      MaterialToolbar topAppBar= findViewById(R.id.groupDetail_topAppBar);
      topAppBar.setTitle(group.getGroupName());
      topAppBar.setOnMenuItemClickListener(new OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
          if (item.getItemId() == R.id.appBar_groupInfo) {
            showGroupInfoDialog();
            return true;
          } else if (item.getItemId() == R.id.appBar_vote) {
            showVoteDialog();
            return true;
          }
          return true;
        }
      });

      dialog_groupInfo = new BottomSheetDialog(GroupDetailActivity.this);
      dialog_groupInfo.setContentView(R.layout.bottom_sheet_group_info);

      dialog_vote = new BottomSheetDialog(GroupDetailActivity.this);
      dialog_vote.setContentView(R.layout.bottom_sheet_vote);
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_appbar_for_group_detail, menu);

    return true;
  }

  public void showGroupInfoDialog(){
    dialog_groupInfo.show(); // 다이얼로그 띄우기

    //멤버 리스트 확인 버튼
    Button memberListBtn = dialog_groupInfo.findViewById(R.id.bottomSheet_groupInfo_btn_memberList);
    memberListBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        dialog_groupInfo.dismiss();
        Intent intent = new Intent(getApplicationContext(), MemberListActivity.class);
        intent.putExtra("group", group);
        startActivity(intent);
      }
    });

    //그룹 정보 및 탈퇴 버튼
    Button groupInfoBtn = dialog_groupInfo.findViewById(R.id.bottomSheet_groupInfo_btn_groupInfo);
    groupInfoBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        dialog_groupInfo.dismiss();

        Intent intent = new Intent(getApplicationContext(), GroupInfoActivity.class);
        intent.putExtra("group", group);
        startActivity(intent);
      }
    });
  }

  public void showVoteDialog(){
    dialog_vote.show(); // 다이얼로그 띄우기

    //투표 생성하기 버튼
    Button createVoteBtn = dialog_vote.findViewById(R.id.bottomSheet_vote_btn_createVote);
    createVoteBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        //TODO: 투표생성 화면으로 이동
      }
    });

    //투표 참여하기 버튼
    Button joinVoteBtn = dialog_vote.findViewById(R.id.bottomSheet_vote_btn_joinVote);
    joinVoteBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        //TODO: 투표참여 화면으로 이동
      }
    });

    //투표 결과확인 버튼
    Button voteResultBtn = dialog_vote.findViewById(R.id.bottomSheet_vote_btn_voteResult);
    voteResultBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        //TODO: 투표결과 화면으로 이동
      }
    });
  }
}
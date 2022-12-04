package com.example.woomansi.ui.screen.vote;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.woomansi.R;
import com.google.android.material.appbar.MaterialToolbar;

public class VoteJoinActivity extends AppCompatActivity {

  MaterialToolbar topAppBar;
  Button voteBtn;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_vote_join_with_appbar);

    topAppBar = findViewById(R.id.voteJoin_topAppBar);
    topAppBar.setNavigationOnClickListener(view -> finish());

    voteBtn = findViewById(R.id.voteJoin_btn_vote);
    voteBtn.setOnClickListener(v -> {
      //TODO : 사용자가 클릭한 시간의 투표수 +1, 투표참여자 리스트에 해당 유저 추가
      finish();
    });

    //만약 투표참여자 리스트에 해당 유저가 존재한다면
    /*if( ) {
      topAppBar.setTitle("투표를 이미 하셨습니다.");
      voteBtn.setVisibility(View.INVISIBLE);
    }*/
  }
}
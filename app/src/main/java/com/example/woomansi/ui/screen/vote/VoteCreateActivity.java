package com.example.woomansi.ui.screen.vote;

import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.woomansi.R;
import com.google.android.material.appbar.MaterialToolbar;

public class VoteCreateActivity extends AppCompatActivity {

    MaterialToolbar topAppBar;
    Button completeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_create_with_appbar);

        topAppBar = findViewById(R.id.voteCreate_topAppBar);
        topAppBar.setNavigationOnClickListener(view -> finish());

        completeBtn = findViewById(R.id.voteCreate_btn_complete);
        completeBtn.setOnClickListener(v -> {
            //TODO : 사용자가 클릭한 시간들을 vote 관련 데이터 모델 클래스에 담아 서버에 저장하는 코드 구현
            finish();
        });
    }
}
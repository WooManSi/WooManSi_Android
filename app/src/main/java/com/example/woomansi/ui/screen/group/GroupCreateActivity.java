package com.example.woomansi.ui.screen.group;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.woomansi.R;
import com.example.woomansi.ui.screen.main.MainActivity;


public class GroupCreateActivity extends AppCompatActivity {

    ImageButton cancelBtn;
    Button createGroupBtn;

    //onCreate 함수
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_create);

        //취소 버튼
        cancelBtn = findViewById(R.id.groupCreate_cancelButton);
        cancelBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //그룹생성 버튼
        createGroupBtn = findViewById(R.id.groupCreate_createGroupButton);
        createGroupBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                /*TODO: 그룹명, 비밀번호, 날짜데이터 들어왔는지 모두 검사하고,
                   정상이면 서버에 그룹데이터 넣고 사용자 그룹리스트에 그룹 추가해줌.
                 */
                finish();
            }
        });
    }
}


package com.example.woomansi.ui.screen.group;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.woomansi.R;
import com.example.woomansi.ui.screen.main.MainActivity;

public class GroupInfoActivity extends AppCompatActivity {

    private ImageButton backBtn;
    private Button exitGroupBtn;
    private Dialog dialog_exitGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);

        //뒤로가기 화살표 버튼
        backBtn = findViewById(R.id.groupInfo_ib_backButton);
        backBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //그룹 나가기 버튼
        exitGroupBtn = findViewById(R.id.groupInfo_btn_exitGroupButton);
        exitGroupBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showExitGroupDialog();
            }
        });

        dialog_exitGroup = new Dialog(GroupInfoActivity.this);
        dialog_exitGroup.setContentView(R.layout.dialog_exit_group);
        dialog_exitGroup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //TODO: 그룹명하고 비밀번호 서버에서 가져와서 띄워주기
        TextView groupName = findViewById(R.id.groupInfo_tv_groupName);
        groupName.setText(getIntent().getStringExtra("그룹명"));

        TextView password = findViewById(R.id.groupInfo_tv_Password);
        password.setText(getIntent().getStringExtra("비밀번호"));

    }

    public void showExitGroupDialog(){
        dialog_exitGroup.show(); // 다이얼로그 띄우기

        //취소 버튼
        Button cancelBtn = dialog_exitGroup.findViewById(R.id.dialog_exitGroup_btn_cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_exitGroup.dismiss();
            }
        });

        //그룹탈퇴 버튼
        Button joinBtn = dialog_exitGroup.findViewById(R.id.dialog_exitGroup_btn_acceptExit);
        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: 누르면 해당 사용자는 그룹에서 탈퇴되며, 사용자의 그룹리스트에서 해당그룹이 삭제됨.
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

}

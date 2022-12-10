package com.example.woomansi.ui.screen.group;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.woomansi.R;
import com.example.woomansi.data.model.GroupModel;
import com.example.woomansi.data.repository.FirebaseGroupExit;
import com.example.woomansi.ui.screen.main.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class GroupInfoActivity extends AppCompatActivity {

    private ImageButton backBtn;
    private Button exitGroupBtn;
    private Dialog dialog_exitGroup;
    private TextView groupName;
    private TextView password;
    private GroupModel group;

    private FirebaseAuth auth;
    private FirebaseFirestore fireStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);

        auth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();

        //뒤로가기 화살표 버튼
        backBtn = findViewById(R.id.groupInfo_ib_backButton);
        backBtn.setOnClickListener(v -> finish());

        //그룹 나가기 버튼
        exitGroupBtn = findViewById(R.id.groupInfo_btn_exitGroupButton);
        exitGroupBtn.setOnClickListener(v -> showExitGroupDialog());

        dialog_exitGroup = new Dialog(GroupInfoActivity.this);
        dialog_exitGroup.setContentView(R.layout.dialog_exit_group);
        dialog_exitGroup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        group = (GroupModel) getIntent().getSerializableExtra("group");

        groupName = findViewById(R.id.groupInfo_tv_groupName);
        groupName.setText(group.getGroupName());

        password = findViewById(R.id.groupInfo_tv_Password);
        password.setText(group.getGroupPassword());
    }

    public void showExitGroupDialog(){
        dialog_exitGroup.show(); // 다이얼로그 띄우기

        //취소 버튼
        Button cancelBtn = dialog_exitGroup.findViewById(R.id.dialog_exitGroup_btn_cancel);
        cancelBtn.setOnClickListener(view -> dialog_exitGroup.dismiss());

        //그룹탈퇴 버튼
        Button joinBtn = dialog_exitGroup.findViewById(R.id.dialog_exitGroup_btn_acceptExit);
        joinBtn.setOnClickListener(view -> {
            ArrayList<String> memberList = group.getMemberList();
            String currentUserUid = auth.getCurrentUser().getUid();
            memberList.remove(currentUserUid);

            dialog_exitGroup.dismiss();

            //만약 그룹탈퇴를 요청한 사용자가 리더일 경우, 그룹 자체를 삭제
            if(currentUserUid.equals(group.getLeaderUid())){
                FirebaseGroupExit.groupLeaderExit(
                    group,
                    this::startMainActivity,
                    errorMsg -> showToast(errorMsg)
                );

            } else {
                List<String> dayNameList = List.of(getResources().getStringArray(R.array.day_name));
                FirebaseGroupExit.groupMemberExit(
                    group,
                    memberList,
                    currentUserUid,
                    dayNameList,
                    this::startMainActivity,
                    errorMsg -> showToast(errorMsg)
                );
            }
        });
    }

    public void startMainActivity() {
        showToast("그룹 탈퇴 성공!");
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("group", "group");
        startActivity(intent);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

package com.example.woomansi.ui.screen.group;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.woomansi.R;
import com.example.woomansi.data.model.GroupModel;
import com.example.woomansi.data.repository.FirebaseGroupCreate;
import com.example.woomansi.data.repository.FirebaseGroupSchedule;
import com.example.woomansi.data.repository.FirebaseUserSchedule;
import com.example.woomansi.util.UserCache;
import com.google.android.material.appbar.MaterialToolbar;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class GroupCreateActivity extends AppCompatActivity {

    List<String> dayNameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_create_with_appbar);

        dayNameList = List.of(getResources().getStringArray(R.array.day_name));

        EditText editGroupName = findViewById(R.id.groupCreate_et_editGroupName);
        EditText editGroupPassword = findViewById(R.id.groupCreate_et_editGroupPassword);


        //그룹생성 버튼
        Button createGroupBtn = findViewById(R.id.groupCreate_btn_createGroup);
        createGroupBtn.setOnClickListener(v -> {
            String groupName = editGroupName.getText().toString();
            String groupPassword = editGroupPassword.getText().toString();
            String currentUserUid = UserCache.getUser(this).getIdToken();

            createGroup(groupName, groupPassword, currentUserUid);
        });

        //뒤로가기 버튼
        MaterialToolbar topAppBar = findViewById(R.id.groupCreate_topAppBar);
        topAppBar.setNavigationOnClickListener(view -> finish());
    }

    private void createGroup(String groupName, String groupPassword, String userId) {
        if (groupName.isEmpty()) {
            showToast("그룹 이름을 입력해주세요");
            return;
        }
        if (groupPassword.length() < 4) {
            showToast("비밀번호를 네자리 이상 입력해주세요");
            return;
        }
        ArrayList<String> memberList = new ArrayList<>();
        memberList.add(userId);

        GroupModel group = new GroupModel(
                groupName,
                groupPassword,
                getCurrentTime(),
                userId,
                memberList);

        // 중복되는 그룹이 있는지 체크하고, 없다면 새 그룹을 생성함
        FirebaseGroupCreate.checkAndCreateGroup(
                group,
                documentId -> {
                    // 방금 생성한 그룹의 documentId를 인자로 건네줌
                    // 먼저 그룹장(지금 사용자)의 개인 스케줄 데이터를 받아오기
                    FirebaseUserSchedule.getSchedules(
                            userId,
                            dayNameList,
                            scheduleMap -> {
                                // 스케줄 불러오는 것을 성공했으면
                                // 해당 documentId로 그룹 스케줄 데이터 생성하기
                                FirebaseGroupSchedule.unionSchedules(
                                        documentId,
                                        scheduleMap,
                                        () -> {
                                            // 새로운 그룹 스케줄 생성하는 것까지 성공.
                                            showToast(groupName + " 그룹을 생성하였습니다");
                                            finish();
                                        },
                                        errorMsg -> showToast(errorMsg));
                            },
                            errorMsg -> showToast(errorMsg));
                },
                errorMsg -> showToast(errorMsg));
    }

    private String getCurrentTime(){
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        return dateTime.format(formatter);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}


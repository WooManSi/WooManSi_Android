package com.example.woomansi.ui.screen.group;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.woomansi.R;
import com.example.woomansi.data.model.GroupModel;
import com.example.woomansi.data.model.GroupTimeTableWrapper;
import com.example.woomansi.data.repository.FirebaseSchedules;
import com.example.woomansi.util.CalculationUtil;
import com.example.woomansi.util.UserCache;
import com.google.android.material.appbar.MaterialToolbar;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;


public class GroupCreateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_create_with_appbar);

        List<String> dayNameList = List.of(getResources().getStringArray(R.array.day_name));

        EditText editGroupName = findViewById(R.id.groupCreate_et_editGroupName);
        EditText editGroupPassword = findViewById(R.id.groupCreate_et_editGroupPassword);


        //그룹생성 버튼
        Button createGroupBtn = findViewById(R.id.groupCreate_btn_createGroup);
        createGroupBtn.setOnClickListener(v -> {
            String groupName = editGroupName.getText().toString();
            String groupPassword = editGroupPassword.getText().toString();

            String currentUserUid = UserCache.getUser(this).getIdToken();
            ArrayList<String> memberList = new ArrayList<>();
            memberList.add(currentUserUid);

            GroupModel group = new GroupModel(
                    groupName,
                    groupPassword,
                    getCurrentTime(),
                    currentUserUid,
                    memberList);

//            GroupTimeTableWrapper wrapper = new GroupTimeTableWrapper();
//            Map<String, List<Integer>> timetableMap = new HashMap<>();
//
//            calculateGroupTimeTable(timetableMap);
//            wrapper.setGroupTimeTable(timetableMap);

//
//            fireStore.collection("groups")
//                .whereEqualTo("groupName", groupName)
//                .whereEqualTo("groupPassword", groupPassword)
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        //그룹명과 비밀번호가 둘 다 똑같으면 그룹생성 불가.
//                        if (!task.getResult().isEmpty()) {
//                            Toast.makeText(GroupCreateActivity.this, "그룹명과 비밀번호가 모두 일치하는 동일한 그룹이 이미 존재합니다.", Toast.LENGTH_SHORT).show();
//                        } else {
//                            fireStore.collection("groups")
//                                    .add(group)
//                                    .addOnSuccessListener(documentReference -> {
//                                        String[] path = documentReference.getPath().split("/");
//                                        addGroupTimeTable(path[1], timetable);
//
//                                        Log.d(TAG, "그룹이름 : " + group.getGroupName() + ", 그룹생성날짜 : " + group.getGroupCreateDate());
//                                        Toast.makeText(GroupCreateActivity.this, "그룹생성 완료!", Toast.LENGTH_SHORT).show();
//
//                                        //finish()로 연결할 시 에러가 나서, intent로 새 화면 연결해줌.
//                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                                        intent.putExtra("group", "group");
//                                        startActivity(intent);
//                                    })
//                                    .addOnFailureListener(e -> {
//                                        Log.w(TAG, "그룹 추가 과정에서 에러 발생", e);
//                                        Toast.makeText(GroupCreateActivity.this, "그룹생성 실패", Toast.LENGTH_SHORT).show();
//                                    });
//                        }
//                    }
//                });
        });

        //뒤로가기 버튼
        MaterialToolbar topAppBar = findViewById(R.id.groupCreate_topAppBar);
        topAppBar.setNavigationOnClickListener(view -> finish());
    }

    //날짜 구하는 함수. 나중에 여러곳에서 쓴다면 util로 빼도 괜찮을듯?
    private String getCurrentTime(){
//        TimeZone tz;
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREAN);
//        tz = TimeZone.getTimeZone("Asia/Seoul");
//        sdf.setTimeZone(tz);
//
//        long now = System.currentTimeMillis();
//        Date date = new Date(now);

        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        return dateTime.format(formatter);
    }

    //개인 시간표를 그룹 시간표에 넣는 함수
//    private void calculateGroupTimeTable(Map<String, List<Integer>> timetableMap) {
//        String curUserUid = auth.getCurrentUser().getUid();
//        FirebaseSchedules.getSchedules(
//                curUserUid,
//                dayNameList,
//                scheduleMap -> {
//                    scheduleMap.forEach((key, value) -> {
//                        List<Integer> groupTimeTable = CalculationUtil.change_ScheduleModel_ToIntArray(value);
//                        timetableMap.put(key, groupTimeTable);
//                    });
//                },
//                message -> {
//
//                }
//        );
//    }

//    private void addGroupTimeTable(String path, GroupTimeTableWrapper timetable) {
//        fireStore.collection("group_schedules").document(path)
//                .set(timetable);
//    }
}


package com.example.woomansi.ui.screen.group;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.woomansi.R;
import com.example.woomansi.data.model.GroupModel;
import com.example.woomansi.ui.screen.main.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class GroupCreateActivity extends AppCompatActivity {

    private ImageButton cancelBtn;
    private Button createGroupBtn;
    private EditText editGroupName;
    private EditText editGroupPassword;

    private FirebaseAuth auth;
    private FirebaseFirestore fireStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_create);

        auth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();

        editGroupName = findViewById(R.id.groupCreate_et_editGroupName);
        editGroupPassword = findViewById(R.id.groupCreate_et_editGroupPassword);

        //취소 버튼
        cancelBtn = findViewById(R.id.groupCreate_btn_cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //그룹생성 버튼
        createGroupBtn = findViewById(R.id.groupCreate_btn_createGroup);
        createGroupBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String groupName = editGroupName.getText().toString();
                String groupPassword = editGroupPassword.getText().toString();

                GroupModel group = new GroupModel();
                group.setGroupName(groupName);
                group.setGroupPassword(groupPassword);
                group.setGroupCreateDate(getTime());

                String currentUserUid = auth.getCurrentUser().getUid();
                group.setLeaderUid(currentUserUid);

                ArrayList<String> memberList = new ArrayList<>();
                memberList.add(currentUserUid);
                group.setMemberList(memberList);

                fireStore.collection("groups")
                    .whereEqualTo("groupName", groupName)
                    .whereEqualTo("groupPassword", groupPassword)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                //그룹명과 비밀번호가 둘 다 똑같으면 그룹생성 불가.
                                if(!task.getResult().isEmpty()) {
                                    Toast.makeText(GroupCreateActivity.this, "그룹명과 비밀번호가 모두 일치하는 동일한 그룹이 이미 존재합니다.", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    fireStore.collection("groups")
                                        .add(group)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Log.d(TAG, "그룹이름 : " + group.getGroupName() + ", 그룹생성날짜 : " + group.getGroupCreateDate());
                                                Toast.makeText(GroupCreateActivity.this, "그룹생성 완료!", Toast.LENGTH_SHORT).show();

                                                //finish()로 연결할 시 에러가 나서, intent로 새 화면 연결해줌.
                                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                intent.putExtra("group", "group");
                                                startActivity(intent);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "그룹 추가 과정에서 에러 발생", e);
                                                Toast.makeText(GroupCreateActivity.this, "그룹생성 실패", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                }
                            }
                        }
                    });
            }
        });
    }

    //날짜 구하는 함수. 나중에 여러곳에서 쓴다면 util로 빼도 괜찮을듯?
    private String getTime(){
        TimeZone tz;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREAN);
        tz = TimeZone.getTimeZone("Asia/Seoul");
        sdf.setTimeZone(tz);

        long now = System.currentTimeMillis();
        Date date = new Date(now);

        return sdf.format(date);
    }
}


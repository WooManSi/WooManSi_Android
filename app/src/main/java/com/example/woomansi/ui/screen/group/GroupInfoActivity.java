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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.woomansi.R;
import com.example.woomansi.data.model.GroupModel;
import com.example.woomansi.ui.screen.main.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

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
                ArrayList<String> memberList = group.getMemberList();
                //멤버리스트에서 리더 Uid를 빼주는 데이터 처리 작업
                String currentUserUid = auth.getCurrentUser().getUid();
                memberList.remove(currentUserUid);

                //만약 그룹탈퇴를 요청한 사용자가 리더일 경우, 그룹 자체를 삭제
                if(currentUserUid.equals(group.getLeaderUid())){
                    fireStore.collection("groups")
                        .whereEqualTo("groupName", group.getGroupName())
                        .whereEqualTo("groupPassword", group.getGroupPassword())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (DocumentSnapshot document : task.getResult()) {
                                        DocumentReference d = document.getReference();
                                        System.out.println(d);
                                        d.delete();
                                    }
                                }
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.putExtra("group", "group");
                                startActivity(intent);
                            }
                        });
                    //만약 그룹탈퇴를 요청한 사용자가 그룹원일 경우, 해당 그룹원을 멤버 리스트에서 제외
                } else {
                    fireStore.collection("groups")
                        .whereEqualTo("groupName", group.getGroupName())
                        .whereEqualTo("groupPassword", group.getGroupPassword())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (DocumentSnapshot document : task.getResult()) {
                                        DocumentReference d = document.getReference();
                                        System.out.println(d);
                                        d.update("memberList", memberList);
                                    }
                                }
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.putExtra("group", group);
                                startActivity(intent);
                            }
                        });
                }
            }
        });
    }

}

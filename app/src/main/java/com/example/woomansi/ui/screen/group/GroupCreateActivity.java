package com.example.woomansi.ui.screen.group;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.woomansi.R;
import com.example.woomansi.data.model.GroupModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.SimpleDateFormat;
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

    //onCreate 함수
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
                /*TODO: 그룹명, 비밀번호 들어왔는지 모두 검사하고,
                   정상이면 서버에 그룹데이터 넣고 사용자 그룹리스트에 그룹 추가해줌.
                 */
                String groupName = editGroupName.getText().toString();
                String groupPassword = editGroupPassword.getText().toString();

                GroupModel group = new GroupModel(groupName, groupPassword, getTime());

                System.out.println("그룹이름 : " + group.getGroupName() + ", 그룹생성날짜 : " + group.getGroupCreateDate());
/*                fireStore.collection("groups")
                        .add(group)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });*/

                finish();
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


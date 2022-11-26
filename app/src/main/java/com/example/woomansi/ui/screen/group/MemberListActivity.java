package com.example.woomansi.ui.screen.group;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.woomansi.R;
import com.example.woomansi.data.model.GroupModel;
import com.example.woomansi.data.model.UserModel;
import com.example.woomansi.ui.adapter.MemberListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

public class MemberListActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<String> memberUidArrayList;
    private ArrayList<UserModel> userModelArrayList;
    private MemberListAdapter memberListAdapter;
    private GroupModel group;
    MaterialToolbar topAppBar;

    private TextView memberCount;
    private TextView leaderName;

    private FirebaseAuth auth;
    private FirebaseFirestore fireStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_list_with_appbar);

        auth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();

        group = (GroupModel) getIntent().getSerializableExtra("group");

        topAppBar= findViewById(R.id.memberList_topAppBar);
        topAppBar.setNavigationOnClickListener(view -> finish());

        //멤버 리스트 데이터 초기화
        this.InitializeMemberData();

        //리스트 뷰 초기화
        listView = findViewById(R.id.memberList_lv_listView);
        memberListAdapter = new MemberListAdapter(MemberListActivity.this, userModelArrayList);

        listView.setAdapter(memberListAdapter);

        //멤버 수 세팅
        memberUidArrayList = group.getMemberList();
        memberUidArrayList.remove(group.getLeaderUid());
        memberCount = findViewById(R.id.memberList_tv_memberCount);
        memberCount.setText("멤버(" + memberUidArrayList.size() + ")");

        //리더 닉네임 셋팅
        leaderName = findViewById(R.id.memberList_tv_leaderName);
        fireStore.collection("users")
            .whereEqualTo("idToken", group.getLeaderUid())
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "MemberList - 그룹 가져오기 성공.", task.getException());
                        DocumentSnapshot document =  task.getResult().getDocuments().get(0);
                        UserModel userModel = document.toObject(UserModel.class);
                        leaderName.setText(userModel.getNickname());
                    }
                }
            });
    }

    public void InitializeMemberData()
    {
        memberUidArrayList = group.getMemberList();
        memberUidArrayList.remove(group.getLeaderUid());

        userModelArrayList = new ArrayList<UserModel>();

        //가져온 memberUidArray를 이용하여 유저 목록(UserModel) 가져오기
        //가져온 userModel을 가지고 유저의 프로필사진, 닉네임 등을 세팅해줌.
        if(!memberUidArrayList.isEmpty()) {
            fireStore.collection("users")
                .whereIn("idToken", memberUidArrayList)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "InitializeMemberData - 유저 멤버 가져오기 성공.", task.getException());
                            for (DocumentSnapshot document : task.getResult()) {
                                UserModel member = document.toObject(UserModel.class);
                                userModelArrayList.add(member);
                            }
                            memberListAdapter.notifyDataSetChanged();
                        }
                    }
                });
        }
    }
}
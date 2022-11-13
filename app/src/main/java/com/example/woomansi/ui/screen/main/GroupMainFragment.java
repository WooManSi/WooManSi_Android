package com.example.woomansi.ui.screen.main;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.woomansi.R;
import com.example.woomansi.data.model.GroupModel;
import com.example.woomansi.ui.adapter.GroupListAdapter;
import com.example.woomansi.ui.screen.group.GroupCreateActivity;
import com.example.woomansi.ui.screen.group.GroupDetailActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

public class GroupMainFragment extends Fragment {

    private ImageButton backBtn;
    private ImageButton plusBtn;
    private BottomSheetDialog dialog_addGroup;
    private Dialog dialog_joinGroup;
    private Dialog dialog_failToJoinGroup;

    private ListView listView;
    private ArrayList<GroupModel> groupModelArrayList;
    private GroupListAdapter groupListAdapter;

    private ImageView questionIcon;
    private TextView groupNotExist;
    private View v;

    private FirebaseAuth auth;
    private FirebaseFirestore fireStore;

    public static GroupMainFragment newInstance() { return new GroupMainFragment(); }

    public GroupMainFragment() {
        // Required empty public constructor
    }

    public interface ImageItemClickListener {
        void onImageItemClick(int a_imageResId) ;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_group_main, container, false);

        auth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();

        backBtn = v.findViewById(R.id.groupMain_ib_backButton);
        plusBtn = v.findViewById(R.id.groupMain_ib_plusButton);
        plusBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                showAddGroupDialog();
            }
        });

        //그룹 추가 다이얼로그
        dialog_addGroup = new BottomSheetDialog(v.getContext());
        dialog_addGroup.setContentView(R.layout.bottom_sheet_add_group);

        //그룹 참여 다이얼로그
        dialog_joinGroup = new Dialog(v.getContext());
        dialog_joinGroup.setContentView(R.layout.dialog_join_group);
        dialog_joinGroup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //그룹 참여 실패 다이얼로그
        dialog_failToJoinGroup = new Dialog(v.getContext());
        dialog_failToJoinGroup.setContentView(R.layout.dialog_join_group_fail);
        dialog_failToJoinGroup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //그룹 리스트 데이터 초기화
        this.InitializeGroupData();

        //리스트 뷰 초기화
        listView = v.findViewById(R.id.groupMain_lv_listView);
        groupListAdapter = new GroupListAdapter(v.getContext(), groupModelArrayList);
        groupListAdapter.setImageItemClickListener(new ImageItemClickListener() {
            @Override
            public void onImageItemClick(int position) {
                GroupModel groupModel = groupListAdapter.getItem(position);

                Intent intent = new Intent(getActivity().getApplicationContext(), GroupDetailActivity.class);
                intent.putExtra("group", groupModel);
                startActivity(intent);
            }
        });

        //리스트뷰에 Adapter 연결
        listView.setAdapter(groupListAdapter);

        //리스트 뷰 아이템 클릭시 그룹 상세로 이동하는 Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id){
                GroupModel groupModel = groupListAdapter.getItem(position);

                Intent intent = new Intent(getActivity().getApplicationContext(), GroupDetailActivity.class);
                intent.putExtra("group", groupModel);
                startActivity(intent);
            }
        });

        questionIcon = v.findViewById(R.id.groupMain_iv_questionIcon);
        groupNotExist = v.findViewById(R.id.groupMain_tv_groupNotExist);

        //그룹이 있을경우 그룹을 생성해달라는 경고창을 비활성화.
        if(!groupModelArrayList.isEmpty()) {
            questionIcon.setVisibility(View.INVISIBLE);
            groupNotExist.setVisibility(View.INVISIBLE);
        }

        return v;
    }

    public void showAddGroupDialog(){
        dialog_addGroup.show(); // 다이얼로그 띄우기

        //그룹 생성하기 버튼
        Button addGroupBtn = dialog_addGroup.findViewById(R.id.bottomSheet_addGroup_btn_createGroup);
        addGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), GroupCreateActivity.class);
                startActivity(intent);

                dialog_addGroup.dismiss();
            }
        });

        //그룹 참여하기 버튼
        Button joinGroupBtn = dialog_addGroup.findViewById(R.id.bottomSheet_addGroup_btn_joinGroup);
        joinGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_addGroup.dismiss();
                showJoinGroupDialog();
            }
        });
    }

    public void showJoinGroupDialog(){
        dialog_joinGroup.show(); // 다이얼로그 띄우기

        //취소 버튼
        Button cancelBtn = dialog_joinGroup.findViewById(R.id.dialog_joinGroup_btn_cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_joinGroup.dismiss();
            }
        });

        //참여하기 버튼
        Button joinBtn = dialog_joinGroup.findViewById(R.id.dialog_joinGroup_btn_acceptJoin);
        EditText editGroupName = dialog_joinGroup.findViewById(R.id.dialog_joinGroup_et_GroupName);
        EditText editGroupPassword = dialog_joinGroup.findViewById(R.id.dialog_joinGroup_et_GroupPassword);

        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String groupName = editGroupName.getText().toString();
                String groupPassword = editGroupPassword.getText().toString();
                Log.d(TAG, "그룹 네임 : " + groupName);
                Log.d(TAG, "그룹 패스워드 : " + groupPassword);

                fireStore.collection("groups")
                    .whereEqualTo("groupName", groupName)
                    .whereEqualTo("groupPassword", groupPassword)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if(!task.getResult().isEmpty()) {
                                    Log.d(TAG, "그룹 참여하기 성공.", task.getException());
                                    DocumentSnapshot document = task.getResult().getDocuments().get(0);
                                    DocumentReference ref = document.getReference();

                                    GroupModel groupModel = document.toObject(GroupModel.class);
                                    ArrayList<String> list = groupModel.getMemberList();
                                    list.add(auth.getCurrentUser().getUid());
                                    ref.update("memberList", list);

                                    Toast.makeText(v.getContext(), "그룹 참여하기 성공", Toast.LENGTH_SHORT).show();
                                    //어뎁터 새로고침
                                    refreshAdapter();
                                    dialog_joinGroup.dismiss();
                                }
                                else{
                                    Toast.makeText(v.getContext(), "해당 그룹이 존재하지 않습니다", Toast.LENGTH_SHORT).show();
                                    dialog_joinGroup.dismiss();
                                    showFailToJoinGroupDialog();
                                }
                            } else {
                                dialog_joinGroup.dismiss();
                                showFailToJoinGroupDialog();
                            }
                        }
                    });
            }
        });
    }

    public void showFailToJoinGroupDialog(){
        dialog_failToJoinGroup.show(); // 다이얼로그 띄우기

        //취소 버튼
        Button cancelBtn = dialog_failToJoinGroup.findViewById(R.id.dialog_joinGroupFail_btn_cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_failToJoinGroup.dismiss();
                dialog_joinGroup.dismiss();
            }
        });

        //다시하기 버튼
        Button retryBtn = dialog_failToJoinGroup.findViewById(R.id.dialog_joinGroupFail_btn_retry);
        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_failToJoinGroup.dismiss();
                showJoinGroupDialog();
            }
        });
    }

    public void InitializeGroupData()
    {
        groupModelArrayList = new ArrayList<GroupModel>();

        fireStore.collection("groups")
            .whereArrayContains("memberList", auth.getCurrentUser().getUid())
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "InitializeGroupData 성공.", task.getException());
                        for (DocumentSnapshot document : task.getResult()) {
                            GroupModel group = document.toObject(GroupModel.class);
                            groupModelArrayList.add(group);
                        }
                        groupListAdapter.notifyDataSetChanged();
                        //그룹이 없을 경우 그룹을 생성해달라는 경고창을 활성화.
                        if (groupModelArrayList.isEmpty()) {
                            questionIcon.setVisibility(View.VISIBLE);
                            groupNotExist.setVisibility(View.VISIBLE);
                        } else {
                            questionIcon.setVisibility(View.INVISIBLE);
                            groupNotExist.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        Log.w(TAG, "그룹 존재하지 않음.", task.getException());
                    }
                }
            });
    }

    //ListView에 새로고침한 결과 반영하는 코드
    public void refreshAdapter() {
        InitializeGroupData();
        groupListAdapter = new GroupListAdapter(v.getContext(), groupModelArrayList);
        groupListAdapter.notifyDataSetChanged();
        listView.setAdapter(groupListAdapter);
    }
}
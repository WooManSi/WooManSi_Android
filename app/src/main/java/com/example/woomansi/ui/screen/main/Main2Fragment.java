package com.example.woomansi.ui.screen.main;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.woomansi.R;
import com.example.woomansi.data.model.GroupModel;
import com.example.woomansi.data.repository.FirebaseGroup;
import com.example.woomansi.data.repository.FirebaseGroupSchedule;
import com.example.woomansi.data.repository.FirebaseUserSchedule;
import com.example.woomansi.ui.adapter.GroupListAdapter;
import com.example.woomansi.ui.screen.group.GroupCreateActivity;
import com.example.woomansi.ui.screen.group.GroupDetailActivity;
import com.example.woomansi.util.UserCache;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class Main2Fragment extends Fragment {

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

    public static Main2Fragment newInstance() { return new Main2Fragment(); }

    public Main2Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_main2, container, false);

        auth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();

        //?????? ?????? ???????????????
        dialog_addGroup = new BottomSheetDialog(v.getContext());
        dialog_addGroup.setContentView(R.layout.bottom_sheet_add_group);

        //?????? ?????? ???????????????
        dialog_joinGroup = new Dialog(v.getContext());
        dialog_joinGroup.setContentView(R.layout.dialog_join_group);
        dialog_joinGroup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //?????? ?????? ?????? ???????????????
        dialog_failToJoinGroup = new Dialog(v.getContext());
        dialog_failToJoinGroup.setContentView(R.layout.dialog_join_group_fail);
        dialog_failToJoinGroup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //?????? ????????? ????????? ?????????
        this.InitializeGroupData();

        //????????? ??? ?????????
        listView = v.findViewById(R.id.groupMain_lv_listView);
        groupListAdapter = new GroupListAdapter(v.getContext(), groupModelArrayList);
        groupListAdapter.setImageItemClickListener(position -> {
            GroupModel groupModel = groupListAdapter.getItem(position);

            Intent intent = new Intent(getActivity().getApplicationContext(), GroupDetailActivity.class);
            intent.putExtra("group", groupModel);
            startActivity(intent);
        });

        //??????????????? Adapter ??????
        groupListAdapter = new GroupListAdapter(v.getContext(), groupModelArrayList);
        listView.setAdapter(groupListAdapter);

        //????????? ??? ????????? ????????? ?????? ????????? ???????????? Listener
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

        //????????? ???????????? ????????? ?????????????????? ???????????? ????????????.
        if(!groupModelArrayList.isEmpty()) {
            questionIcon.setVisibility(View.INVISIBLE);
            groupNotExist.setVisibility(View.INVISIBLE);
        }

        //appBar??? fragment?????? ?????? ??????
        setHasOptionsMenu(true);

        return v;
    }

    public void InitializeGroupData() {
        groupModelArrayList = new ArrayList<>();


        String userId = UserCache.getUser(getContext()).getIdToken();
        FirebaseGroup.getGroupModelsWithChangeListener(
                userId,
                groupModelList -> {
                    groupModelArrayList = new ArrayList<>();
                    if (groupModelList.size() != 0)
                        groupModelArrayList.addAll(groupModelList);
                    refreshAdapter();
                },
                errorMsg -> showToast(errorMsg)
        );
    }

    //ListView??? ??????????????? ?????? ???????????? ??????
    public void refreshAdapter() {
        groupListAdapter.setGroupModelList(groupModelArrayList);

        //????????? ?????? ?????? ????????? ?????????????????? ???????????? ?????????.
        if (groupModelArrayList.isEmpty()) {
            questionIcon.setVisibility(View.VISIBLE);
            groupNotExist.setVisibility(View.VISIBLE);
        } else {
            questionIcon.setVisibility(View.INVISIBLE);
            groupNotExist.setVisibility(View.INVISIBLE);
        }
    }

    public void showAddGroupDialog(){
        dialog_addGroup.show(); // ??????????????? ?????????

        //?????? ???????????? ??????
        Button addGroupBtn = dialog_addGroup.findViewById(R.id.bottomSheet_addGroup_btn_createGroup);
        addGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), GroupCreateActivity.class);
                startActivity(intent);

                dialog_addGroup.dismiss();
            }
        });

        //?????? ???????????? ??????
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
        dialog_joinGroup.show(); // ??????????????? ?????????

        //?????? ??????
        Button cancelBtn = dialog_joinGroup.findViewById(R.id.dialog_joinGroup_btn_cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_joinGroup.dismiss();
            }
        });

        //???????????? ??????
        Button joinBtn = dialog_joinGroup.findViewById(R.id.dialog_joinGroup_btn_acceptJoin);
        EditText editGroupName = dialog_joinGroup.findViewById(R.id.dialog_joinGroup_et_GroupName);
        EditText editGroupPassword = dialog_joinGroup.findViewById(R.id.dialog_joinGroup_et_GroupPassword);

        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String groupName = editGroupName.getText().toString().trim();
                String groupPassword = editGroupPassword.getText().toString().trim();
                Log.d(TAG, "?????? ?????? : " + groupName);
                Log.d(TAG, "?????? ???????????? : " + groupPassword);

                fireStore.collection("groups")
                    .whereEqualTo("groupName", groupName)
                    .whereEqualTo("groupPassword", groupPassword)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if(!task.getResult().isEmpty()) {
                                Log.d(TAG, "?????? ???????????? ??????.", task.getException());
                                DocumentSnapshot document = task.getResult().getDocuments().get(0);
                                DocumentReference ref = document.getReference();
                                GroupModel groupModel = document.toObject(GroupModel.class);

                                String userId = auth.getCurrentUser().getUid();
                                List<String> dayNameList = List.of(getResources().getStringArray(R.array.day_name));

                                ArrayList<String> list = groupModel.getMemberList();
                                list.add(userId);


                                //?????? ????????? ????????????
                                FirebaseUserSchedule.getSchedules(
                                    userId,
                                    dayNameList,
                                    scheduleMap -> {
                                        // ????????? ???????????? ?????? ???????????????
                                        // ?????? documentId??? ?????? ????????? ????????? ????????? ??? ????????????
                                        FirebaseGroupSchedule.unionSchedules(
                                            ref.getId(),
                                            scheduleMap,
                                            () -> {
                                                //?????? ???
                                            },
                                            errorMsg -> Toast.makeText(v.getContext(), errorMsg, Toast.LENGTH_SHORT).show());
                                    },
                                    errorMsg -> Toast.makeText(v.getContext(), errorMsg, Toast.LENGTH_SHORT).show());

                                //??????????????? ??????
                                ref.update("memberList", list);
                                Toast.makeText(v.getContext(), "?????? ???????????? ??????", Toast.LENGTH_SHORT).show();

                                dialog_joinGroup.dismiss();
                            }
                            else{
                                Toast.makeText(v.getContext(), "?????? ????????? ???????????? ????????????", Toast.LENGTH_SHORT).show();
                                dialog_joinGroup.dismiss();
                                showFailToJoinGroupDialog();
                            }
                        } else {
                            dialog_joinGroup.dismiss();
                            showFailToJoinGroupDialog();
                        }
                    });
            }
        });
    }

    public void showFailToJoinGroupDialog(){
        dialog_failToJoinGroup.show(); // ??????????????? ?????????

        //?????? ??????
        Button cancelBtn = dialog_failToJoinGroup.findViewById(R.id.dialog_joinGroupFail_btn_cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_failToJoinGroup.dismiss();
                dialog_joinGroup.dismiss();
            }
        });

        //???????????? ??????
        Button retryBtn = dialog_failToJoinGroup.findViewById(R.id.dialog_joinGroupFail_btn_retry);
        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_failToJoinGroup.dismiss();
                showJoinGroupDialog();
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    //????????? ????????? menu.xml??? ??????????????? ??????
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_appbar_with_plus_btn, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    //topAppBar ??? icon?????? click ???????????? ???????????? ??????
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_plus) {
            showAddGroupDialog();
        }
        return true;
    }
}
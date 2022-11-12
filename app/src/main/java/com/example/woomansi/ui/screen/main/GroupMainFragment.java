package com.example.woomansi.ui.screen.main;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.example.woomansi.R;
import com.example.woomansi.data.model.GroupModel;
import com.example.woomansi.ui.adapter.GroupListAdapter;
import com.example.woomansi.ui.screen.group.GroupCreateActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import java.util.ArrayList;

public class GroupMainFragment extends Fragment {

    private ImageButton backBtn;
    private ImageButton plusBtn;
    private BottomSheetDialog dialog_addGroup;
    private Dialog dialog_joinGroup;

    private ArrayList<GroupModel> groupDataList;
    private ListView listView;

    public static GroupMainFragment newInstance() { return new GroupMainFragment(); }

    public GroupMainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_group_main, container, false);

        backBtn = v.findViewById(R.id.groupMain_ib_backButton);
        plusBtn = v.findViewById(R.id.groupMain_ib_plusButton);
        plusBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                showAddGroupDialog();
            }
        });

        dialog_addGroup = new BottomSheetDialog(v.getContext());
        dialog_addGroup.setContentView(R.layout.dialog_add_group);

        dialog_joinGroup = new Dialog(v.getContext());
        dialog_joinGroup.setContentView(R.layout.dialog_join_group);
        dialog_joinGroup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        this.InitializeGroupData();

        listView = v.findViewById(R.id.groupMain_lv_listView);
        final GroupListAdapter groupListAdapter = new GroupListAdapter(v.getContext(), groupDataList);

        listView.setAdapter(groupListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id){
                groupListAdapter.getItem(position).getGroupName();

            }
        });

        ImageView questionIcon = v.findViewById(R.id.groupMain_iv_questionIcon);
        TextView groupNotExist = v.findViewById(R.id.groupMain_tv_groupNotExist);

        //그룹이 있을경우 그룹을 생성해달라는 경고창을 비활성화.
        if(!groupDataList.isEmpty()) {
            questionIcon.setVisibility(View.INVISIBLE);
            groupNotExist.setVisibility(View.INVISIBLE);
        }

        return v;
    }


    public void showAddGroupDialog(){
        dialog_addGroup.show(); // 다이얼로그 띄우기

        //그룹 생성하기 버튼
        Button addGroupBtn = dialog_addGroup.findViewById(R.id.dialog_addGroup_createGroupButton);
        addGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), GroupCreateActivity.class);
                startActivity(intent);
            }
        });

        //그룹 참여하기 버튼
        Button joinGroupBtn = dialog_addGroup.findViewById(R.id.dialog_addGroup_joinGroupButton);
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
        Button cancelBtn = dialog_joinGroup.findViewById(R.id.dialog_joinGroup_cancelButton);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_joinGroup.dismiss();
            }
        });

        //참여하기 버튼
        Button joinBtn = dialog_joinGroup.findViewById(R.id.dialog_joinGroup_acceptJoinButton);
        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*TODO: 그룹명과 비밀번호를 서버의 데이터와 대조하여 찾아냄,
                   찾으면 그룹을 추가해주고, 못찾으면 dialog_join_group_fail 다이얼로그 띄우기
                 */
            }
        });
    }

    public void InitializeGroupData()
    {
        groupDataList = new ArrayList<GroupModel>();

        //TODO : 현재는 임시값, 나중에는 로그인 한 사용자의 그룹 리스트를 불러와 초기화해주기.
        for(int i = 1; i <= 10; i++) {
            groupDataList.add(new GroupModel("그룹" + i, "groupPassword", "2022.11.13"));
        }
    }
}
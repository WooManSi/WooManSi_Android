package com.example.woomansi.ui.screen.main;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.fragment.app.Fragment;
import com.example.woomansi.R;
import com.example.woomansi.ui.screen.group.GroupCreateActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class GroupMainFragment extends Fragment {

    ImageButton backBtn;
    ImageButton plusBtn;
    BottomSheetDialog dialog_addGroup;
    Dialog dialog_joinGroup;

    public static GroupMainFragment newInstance() { return new GroupMainFragment(); }

    public GroupMainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_group_main, container, false);

        backBtn = v.findViewById(R.id.groupMain_backButton);
        plusBtn = v.findViewById(R.id.groupMain_plusButton);
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
}
package com.example.woomansi.ui.screen.group;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.woomansi.R;
import com.example.woomansi.ui.adapter.MemberListAdapter;
import java.util.ArrayList;
import java.util.List;

public class MemberListActivity extends AppCompatActivity {

    private ListView listView;
    private List<String> list;
    private TextView memberCount;
    private ImageButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_list);

        list = new ArrayList<>();
        //TODO : 현재는 임시값, 나중에는 멤버 리스트를 불러와 초기화해주기.
        for(int i = 1; i <= 10; i++) {
            list.add("멤버" + i);
        }

        listView = findViewById(R.id.memberList_lv_listView);
        final MemberListAdapter memberListAdapter = new MemberListAdapter(MemberListActivity.this, list);

        listView.setAdapter(memberListAdapter);

        //멤버 수 세팅
        memberCount = findViewById(R.id.memberList_tv_memberCount);
        memberCount.setText("멤버(" + memberListAdapter.getCount() + ")");

        //뒤로가기 화살표 버튼
        backBtn = findViewById(R.id.memberList_ib_backButton);
        backBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
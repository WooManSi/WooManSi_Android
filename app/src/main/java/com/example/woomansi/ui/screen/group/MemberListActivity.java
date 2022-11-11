package com.example.woomansi.ui.screen.group;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.woomansi.ListView.MemberListAdapter;
import com.example.woomansi.R;

import java.util.ArrayList;
import java.util.List;

public class MemberListActivity extends AppCompatActivity {

    private RecyclerView recyclerView1;
    private List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_list);

        list = new ArrayList<>();
        list.add("김OO");
        list.add("이OO");
        list.add("박OO");
        list.add("최OO");
        list.add("유OO");
        list.add("강OO");

        recyclerView1 = (RecyclerView)findViewById(R.id.memberRecyclerView);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        recyclerView1.setAdapter(new MemberListAdapter(list));
    }
}
package com.example.woomansi.ui.screen.vote;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.woomansi.R;
import com.example.woomansi.data.model.VoteResultModel;
import com.example.woomansi.ui.adapter.VoteResultListAdapter;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;

public class VoteResultActivity extends AppCompatActivity {

  private Button returnGroupActivityBtn;
  private ListView listView;
  private ArrayList<VoteResultModel> voteResultModelArrayList;
  private VoteResultListAdapter voteResultListAdapter;
  MaterialToolbar topAppBar;

  private TextView dayOfWeek;
  private TextView Time;

  private FirebaseAuth auth;
  private FirebaseFirestore fireStore;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_vote_result_with_appbar);

    topAppBar= findViewById(R.id.voteResult_topAppBar);
    topAppBar.setNavigationOnClickListener(view -> finish());

    this.InitializeMemberData();

    //리스트 뷰 초기화
    listView = findViewById(R.id.voteResult_lv_listView);
    voteResultListAdapter = new VoteResultListAdapter(VoteResultActivity.this, voteResultModelArrayList);

    listView.setAdapter(voteResultListAdapter);

    returnGroupActivityBtn = findViewById(R.id.voteResult_btn_return);
    returnGroupActivityBtn.setOnClickListener(v -> finish());
  }

  public void InitializeMemberData()
  {
    voteResultModelArrayList = new ArrayList<>();
    voteResultModelArrayList.add(new VoteResultModel("월", "14:00", "17:00"));
    voteResultModelArrayList.add(new VoteResultModel("화", "14:00", "17:00"));
  }
}

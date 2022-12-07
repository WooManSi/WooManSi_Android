package com.example.woomansi.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.woomansi.R;
import com.example.woomansi.data.model.VoteResultModel;
import java.util.List;

public class VoteResultListAdapter extends BaseAdapter {

  Context mContext = null;
  LayoutInflater mLayoutInflater = null;
  private List<VoteResultModel> voteResultList;

  public VoteResultListAdapter(Context context, List<VoteResultModel> list) {
    mContext = context;
    mLayoutInflater = LayoutInflater.from(mContext);
    voteResultList = list;
  }

  @Override
  public int getCount() {
    return voteResultList.size();
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public VoteResultModel getItem(int position) {
    return voteResultList.get(position);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    convertView = mLayoutInflater.inflate(R.layout.list_item_vote_result, null);
    TextView dayOfWeek = convertView.findViewById(R.id.listItemVoteResult_tv_dayOfWeek);
    String dayOfWeekStr = voteResultList.get(position).getDayOfWeek() + "요일";
    dayOfWeek.setText(dayOfWeekStr);

    TextView time = convertView.findViewById(R.id.listItemVoteResult_tv_time);
    String timeStr = voteResultList.get(position).getStartTime() + " ~ " + voteResultList.get(position).getEndTime();
    time.setText(timeStr);

    //item 클릭 비활성화
    convertView.setOnTouchListener((v, event) -> true);
    return convertView;
  }
}


package com.example.woomansi.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.woomansi.R;
import com.example.woomansi.data.model.GroupModel;
import java.util.ArrayList;

public class GroupListAdapter extends BaseAdapter {

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<GroupModel> group;

    public GroupListAdapter(Context context, ArrayList<GroupModel> data) {
      mContext = context;
      group = data;
      mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
      return group.size();
    }

    @Override
    public long getItemId(int position) {
      return position;
    }

    @Override
    public GroupModel getItem(int position) {
      return group.get(position);
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
      View view = mLayoutInflater.inflate(R.layout.list_item_groups, null);

      TextView groupName = view.findViewById(R.id.listItemGroup_groupName);
      TextView groupCreateDate = view.findViewById(R.id.listItemGroup_groupCreatedDate);

      groupName.setText(group.get(position).getGroupName());
      groupCreateDate.setText(group.get(position).getGroupCreateDate());

      return view;
    }
}

package com.example.woomansi.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.woomansi.R;
import com.example.woomansi.data.model.GroupModel;

import java.util.ArrayList;

public class GroupListAdapter extends BaseAdapter {

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<GroupModel> group;
    private ImageItemClickListener mImageItemClickListener;

    public GroupListAdapter(Context context, ArrayList<GroupModel> data) {
      mContext = context;
      mLayoutInflater = LayoutInflater.from(mContext);
      group = data;
    }

    public interface ImageItemClickListener {
        void onImageItemClick(int a_imageResId) ;
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

      TextView groupName = view.findViewById(R.id.listItemGroup_tv_groupName);
      TextView groupCreateDate = view.findViewById(R.id.listItemGroup_tv_groupCreatedDate);

      groupName.setText(group.get(position).getGroupName());
      groupCreateDate.setText(group.get(position).getGroupCreateDate());

      ImageButton enterGroup = view.findViewById(R.id.listItemGroup_ib_enterGroup);
      enterGroup.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          if (mImageItemClickListener != null) {
            mImageItemClickListener.onImageItemClick(position);
          }
        }
      });

      return view;
    }

    public void setImageItemClickListener(ImageItemClickListener a_listener) {
      mImageItemClickListener = a_listener;
    }
}

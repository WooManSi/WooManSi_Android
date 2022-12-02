package com.example.woomansi.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.woomansi.R;
import com.example.woomansi.data.model.UserModel;
import com.example.woomansi.util.SetProfileImageUtil;

import java.util.List;

public class MemberListAdapter extends BaseAdapter {

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    private List<UserModel> memberList;

    public MemberListAdapter(Context context, List<UserModel> list) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        memberList = list;
    }

    @Override
    public int getCount() {
        return memberList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public UserModel getItem(int position) {
        return memberList.get(position);
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.list_item_member, null);
        TextView memberName = view.findViewById(R.id.listItemMember_tv_name);
        memberName.setText(memberList.get(position).getNickname());

        ImageView memberProfile = view.findViewById(R.id.listItemMember_iv_profile);
        memberProfile.setImageResource(SetProfileImageUtil.getImageFile(memberList.get(position).getProfile()));

        return view;
    }
}

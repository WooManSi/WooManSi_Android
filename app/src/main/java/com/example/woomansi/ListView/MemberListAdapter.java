package com.example.woomansi.ListView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.woomansi.R;

import java.util.List;

public class MemberListAdapter extends RecyclerView.Adapter<MemberListAdapter.MyViewHolder> {

    private List<String> list;

    public MemberListAdapter(List<String> list) {
        this.list = list;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String text = list.get(position);
        holder.textView1.setText(text);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_member,parent,false);
        return new MyViewHolder(view);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView textView1;

        MyViewHolder(View view){
            super(view);
            textView1 = (TextView)view.findViewById(R.id.member_info);
        }
    }
}

package com.example.woomansi.ui.screen.main;

import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.woomansi.R;
import com.example.woomansi.ui.screen.timetable.createTimeTableActivity;

public class Main1Fragment extends Fragment {

    public Main1Fragment() {
        // Required empty public constructor
    }

    public static Main1Fragment newInstance() {
        return new Main1Fragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //appBar의 fragment개별 맞춤 설정
        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main1, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_appbar_with_plus_btn, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.appBar_plusButton) {
            Intent intent = new Intent(getActivity().getApplicationContext(), createTimeTableActivity.class);
            startActivity(intent);
        }
        return true;
    }
}
package com.example.woomansi.ui.screen.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.woomansi.R;
import com.example.woomansi.ui.screen.timetable.createTimeTableActivity;

public class Main4Fragment extends Fragment {

    public Main4Fragment() {
        // Required empty public constructor
    }

    public static Main4Fragment newInstance() {
        return new Main4Fragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //appBar의 fragment개별 맞춤 설정
        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main4, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_appbar_for_profile_change, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.appBar_deleteAccount) {
            Intent intent = new Intent(getActivity().getApplicationContext(), createTimeTableActivity.class);
            startActivity(intent);
        }
        return true;
    }
}
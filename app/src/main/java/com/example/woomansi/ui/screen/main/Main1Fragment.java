package com.example.woomansi.ui.screen.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.woomansi.R;

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main1, container, false);
    }
}
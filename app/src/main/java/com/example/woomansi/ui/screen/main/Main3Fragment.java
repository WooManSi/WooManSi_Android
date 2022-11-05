package com.example.woomansi.ui.screen.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.woomansi.R;

public class Main3Fragment extends Fragment {

    public Main3Fragment() {
        // Required empty public constructor
    }

    public static Main3Fragment newInstance() {
        return new Main3Fragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main3, container, false);
    }
}
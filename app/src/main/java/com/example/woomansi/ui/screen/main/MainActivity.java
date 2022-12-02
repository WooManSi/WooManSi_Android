package com.example.woomansi.ui.screen.main;

import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.woomansi.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;
    AppBarLayout appBarLayout;
    MaterialToolbar topAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appBarLayout = findViewById(R.id.main_appBar);
        topAppBar = findViewById(R.id.main_topAppBar);
        setSupportActionBar(topAppBar);

        bottomNav = findViewById(R.id.bottom_nav);

        bottomNav.setOnItemSelectedListener(item -> {
            changeFragment(item.getItemId());
            return true;
        });

        //그룹 탈퇴 시 바로 그룹 메인화면으로 보여주기 위함
        if(getIntent().getStringExtra("group") != null)
            bottomNav.setSelectedItemId(R.id.item_group);
        else
            bottomNav.setSelectedItemId(R.id.item_home);
    }

    private void changeFragment(int itemId) {
        Fragment fragment = getFragmentByItemId(itemId);

        if (fragment == null) {
            return;
        }

        setAppBarTitleAndBackGroundColorByItemId(itemId);

        getSupportFragmentManager().beginTransaction()
            .replace(R.id.content_main, fragment)
            .commit();
    }

    private Fragment getFragmentByItemId(int itemId) {
        switch (itemId) {
            case R.id.item_home:
                return Main1Fragment.newInstance();
            case R.id.item_group:
                return Main2Fragment.newInstance();
            case R.id.item_account:
                return Main4Fragment.newInstance();
        }
        return null;
    }

    //appBar별로 타이틀 및 배경색 변경
    private boolean setAppBarTitleAndBackGroundColorByItemId(int itemId) {
        switch (itemId) {
            case R.id.item_home: {
                topAppBar.setTitle("내 시간표");
                topAppBar.setTitleTextAppearance(topAppBar.getContext(), R.style.black_bold_title);

                appBarLayout.setBackgroundColor(Color.parseColor("#F3F2F2"));
                return true;
            }
            case R.id.item_group: {
                topAppBar.setTitle("나의 그룹들");
                topAppBar.setTitleTextAppearance(topAppBar.getContext(), R.style.text_white_bold_title);

                appBarLayout.setBackgroundColor(Color.parseColor("#FF8E2B"));
                return true;
            }

            case R.id.item_account: {
                topAppBar.setTitle("프로필 편집");
                topAppBar.setTitleTextAppearance(topAppBar.getContext(), R.style.black_bold_title);

                appBarLayout.setBackgroundColor(Color.parseColor("#F3F2F2"));
                return true;
            }
        }
        return true;
    }
}
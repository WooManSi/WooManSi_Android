package com.example.woomansi.ui.screen.main;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.woomansi.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        getSupportFragmentManager().beginTransaction()
            .replace(R.id.content_main, fragment)
            .commit();
    }

    private Fragment getFragmentByItemId(int itemId) {
        switch (itemId) {
            case R.id.item_home:
                return Main1Fragment.newInstance();
            case R.id.item_group:
                return GroupMainFragment.newInstance();
            case R.id.item_notification:
                return Main3Fragment.newInstance();
            case R.id.item_account:
                return Main4Fragment.newInstance();
        }
        return null;
    }
}
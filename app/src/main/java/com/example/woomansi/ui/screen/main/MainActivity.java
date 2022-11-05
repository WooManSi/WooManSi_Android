package com.example.woomansi.ui.screen.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

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
                return Main2Fragment.newInstance();
            case R.id.item_notification:
                return Main3Fragment.newInstance();
            case R.id.item_account:
                return Main4Fragment.newInstance();
        }
        return null;
    }
}
package com.example.woomansi.ui.screen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.woomansi.R;
import com.example.woomansi.data.model.UserModel;
import com.example.woomansi.ui.screen.login.LoginActivity;
import com.example.woomansi.ui.screen.main.MainActivity;
import com.example.woomansi.util.UserCache;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            UserModel userCache = UserCache.getUser(this);
            Intent intent;

            if (user != null && userCache != null) {
                // 로그인 되어 있을 때
                intent = new Intent(SplashActivity.this, MainActivity.class);
            } else {
                intent = new Intent(SplashActivity.this, LoginActivity.class);
            }
            startActivity(intent);
            finish();
        },1000);
    }
}
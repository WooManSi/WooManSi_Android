package com.example.woomansi.ui.screen.login;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.woomansi.R;
import com.example.woomansi.data.model.GroupModel;
import com.example.woomansi.data.model.UserModel;
import com.example.woomansi.data.repository.FirebaseGroupExit;
import com.example.woomansi.ui.screen.main.MainActivity;
import com.example.woomansi.util.UserCache;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private TextView join;
    private Button login;
    private EditText email_login, pwd_login;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseFirestore mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.LoginActivity_btn_login);
        mFirebaseAuth = FirebaseAuth.getInstance();

        email_login = findViewById(R.id.LoginActivity_et_Id_Text);
        pwd_login = findViewById(R.id.LoginActivity_et_Pw_Text);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabaseRef = FirebaseFirestore.getInstance();
                //로그인 요청
                String strEmail = email_login.getText().toString();        //문자열로 변환된 입력값을 변수안에 저장
                String strPwd = pwd_login.getText().toString();


                mFirebaseAuth.signInWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            //로그인성공
                            String uid = task.getResult().getUser().getUid();
                            setUserCache(uid);
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });


        join = findViewById(R.id.LoginActivity_tv_goto_signup);
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //회원가입 화면으로 이동
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setUserCache(String uid) {
        FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(uid)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().exists()) {
                            Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        UserModel user = task.getResult().toObject(UserModel.class);
                        UserCache.setUser(this, user);

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish(); //현재 액티비티 파괴
                    }
                });
    }
}

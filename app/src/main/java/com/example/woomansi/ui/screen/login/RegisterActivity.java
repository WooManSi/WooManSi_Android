package com.example.woomansi.ui.screen.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.woomansi.R;
import com.example.woomansi.data.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;          //파이어베이스 인증
    private DatabaseReference mDatabaseRef;     //실시간 데이터베이스
    private EditText mEtEmail, mEtPwd, mEtname;          //회원가입 입력필드
    private Button mBtnRegister;            //회원가입 버튼
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("woomansi");

        mEtEmail = findViewById(R.id.RegisterActivity_et_Signup_Id_Text);
        mEtPwd = findViewById(R.id.RegisterActivity_et_Signup_Pw_Text);
        mEtname = findViewById(R.id.RegisterActivity_et_Nickname_Text);
        mBtnRegister = findViewById(R.id.RegisterActivity_et_Signup_button);

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //회원가입 처리 시작
                String strEmail = mEtEmail.getText().toString();        //문자열로 변환된 입력값을 변수안에 저장
                String strPwd = mEtPwd.getText().toString();
                String strName = mEtname.getText().toString();
                //FirebaseAuth 진행
                mFirebaseAuth.createUserWithEmailAndPassword(strEmail,strPwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();         //로그인이 성공한 경우 firebaseuser객체에 현재 로그인된 유저를 가져온다.
                            UserModel account = new UserModel();
                            account.setIdToken(firebaseUser.getUid());
                            account.setEmailId(firebaseUser.getEmail());
                            account.setPassword(strPwd);
                            account.setNickname(strName);

                            //setvalue : database에 insert 하는 행위
                            mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);

                                    //회원가입 화면으로 이동
                                    finish();

                            Toast.makeText(RegisterActivity.this, "회원가입에 성공 하셨습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegisterActivity.this, "회원가입에 실패 하셨습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
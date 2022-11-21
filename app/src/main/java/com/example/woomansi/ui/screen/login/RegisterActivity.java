package com.example.woomansi.ui.screen.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.woomansi.R;
import com.example.woomansi.data.model.UserModel;
import com.example.woomansi.ui.screen.main.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;          //파이어베이스 인증
    private FirebaseFirestore db;     //실시간 데이터베이스
    private EditText et_emailId, et_password, et_name;          //회원가입 입력필드
    private Button mBtnRegister;            //회원가입 버튼
    private String strName, strEmail, strPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //초기화. 연동
        mFirebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        et_name = findViewById(R.id.RegisterActivity_et_Nickname_Text); //이름
        et_emailId = findViewById(R.id.RegisterActivity_et_Signup_Id_Text); //이메일 아이디
        et_password = findViewById(R.id.RegisterActivity_et_Signup_Pw_Text); //비밀번호
        mBtnRegister = findViewById(R.id.RegisterActivity_et_Signup_button); //회원가입 완료 버튼


        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //회원가입 처리 시작
                //각각의 텍스트에 입력되어 있는 정보를 변수에 저장
                strName = et_name.getText().toString();
                strEmail = et_emailId.getText().toString();
                strPwd = et_password.getText().toString();

                Log.i("name", strName);
                Log.i("id", strEmail);
                Log.i("pwd", strPwd);

                if (strName != null && strEmail != null && strPwd != null && !strEmail.isEmpty() && !strName.isEmpty() && !strPwd.isEmpty()) { //모든 칸을 채워야 함
                    //Firebase Auth 진행
                    mFirebaseAuth.createUserWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //회원가입이 이루어졌을 때의 처리
                            //task는 회원가입 처리 후의 결과값
                            if (task.isSuccessful()) {
                                //현재 로그인 된 유저를 가지고 오는 변수
                                FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                                UserModel account = new UserModel(firebaseUser.getUid(), firebaseUser.getEmail(), strPwd, strName, "");
                                //로그인 된 정보를 저장
                                //데이터 베이스 TravelRecord에 데이터 삽입
                                //set은 database에 삽입
                                //Uid를 키값으로 UserAccount의 데이터를 set
                                //collection-document
                                db.collection("users").document(firebaseUser.getUid()).set(account);

                                Toast.makeText(RegisterActivity.this, "회원가입 완료", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, ProfileActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(RegisterActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(RegisterActivity.this, "모두 입력하세요", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
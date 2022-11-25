package com.example.woomansi.ui.screen.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.woomansi.R;
import com.example.woomansi.ui.screen.main.MainActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    private ImageView imageView;
    private Button btn;
    private int i = 0;
    private int count = 0;
    private int result = 0;
    //private FirebaseStorage mStorage;
    //private FirebaseDatabase mDatabase;
    private FirebaseFirestore db;
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth mFirebaseAuth;

    private void customDialog() {
        Dialog dialog = new Dialog(ProfileActivity.this);
        dialog.setContentView(R.layout.dialog_profile);
        dialog.setTitle("custom dialog !!");
        GridLayout g1 = dialog.findViewById(R.id.g_view);

        count = g1.getChildCount();

        dialog.show();

        for(i = 0; i< count; i++) {
            ImageView iv = (ImageView) g1.getChildAt(i);

            StringBuilder fileName = new StringBuilder("ic_profile");
            fileName.append(i+1);

            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imageView.setImageResource(getResources().getIdentifier(fileName.toString(), "drawable", getPackageName()));
                    result = fileName.toString().compareTo("ic_profile0");
                    dialog.dismiss();
                }
            });
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        db = FirebaseFirestore.getInstance();
        btn = findViewById(R.id.ProfileActivity_bt_profilebtn);
        imageView = findViewById(R.id.ProfileActivity_iv_image);
        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
        //FirebaseStorage storage = FirebaseStorage.getInstance();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference profileRef = db.collection("users").document(firebaseUser.getUid());
                profileRef
                        .update("profile", getString(R.string.profile,result))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(ProfileActivity.this,"프로필 설정 완료",Toast.LENGTH_SHORT).show();
                            }
                        });


               // mDatabaseRef = FirebaseDatabase.getInstance().getReference("woomansi/UserAccount").child(firebaseUser.getUid());
               // mDatabaseRef.child("profile").setValue(getString(R.string.profile,result));
                //DatabaseReference conditionRef = mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).child("profile");
                /*StorageReference storageReference = storage.getReference();
                StorageReference pathReference = storageReference.child(getString(R.string.profile,result));
                pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Toast.makeText(ProfileActivity.this, "다운로드 성공", Toast.LENGTH_SHORT).show();
                        UserModel account = new UserModel();
                        String url = uri.toString();
                        Toast.makeText(ProfileActivity.this, url, Toast.LENGTH_SHORT).show();
                        UploadTask uploadTask=pathReference.putFile(uri);
                    }
                });*/
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);


            }
        });
    }
}


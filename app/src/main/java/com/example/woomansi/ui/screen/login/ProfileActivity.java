package com.example.woomansi.ui.screen.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.woomansi.R;
import com.example.woomansi.data.model.UserModel;
import com.example.woomansi.ui.screen.main.Main1Fragment;
import com.example.woomansi.ui.screen.main.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    private ImageView imageView;
    private Button button;
    private int result = 0;
    //private FirebaseStorage mStorage;
    //private FirebaseDatabase mDatabase;
    private FirebaseFirestore db;
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth mFirebaseAuth;

    private void customDialog() {
        Dialog dialog = new Dialog(ProfileActivity.this);
        dialog.setContentView(R.layout.fragment_avatar);
        dialog.setTitle("custom dialog !!");
        ImageView iv1 = dialog.findViewById(R.id.image1);
        ImageView iv2 = dialog.findViewById(R.id.image2);
        ImageView iv3 = dialog.findViewById(R.id.image3);
        ImageView iv4 = dialog.findViewById(R.id.image4);
        ImageView iv5 = dialog.findViewById(R.id.image5);
        ImageView iv6 = dialog.findViewById(R.id.image6);
        Button button_choose_iv = dialog.findViewById(R.id.btn_ok);

        dialog.show();
        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setImageResource(R.drawable.ic_profile1);
                result = 1;
            }
        });
        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setImageResource(R.drawable.ic_profile2);
                result = 2;
            }
        });
        iv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setImageResource(R.drawable.ic_profile3);
                result = 3;
            }
        });
        iv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setImageResource(R.drawable.ic_profile4);
                result = 4;
            }
        });
        iv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setImageResource(R.drawable.ic_profile5);
                result = 5;
            }
        });
        iv6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setImageResource(R.drawable.ic_profile6);
                result = 6;
            }
        });
        button_choose_iv.setOnClickListener(view -> dialog.dismiss());

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        db = FirebaseFirestore.getInstance();
        button = findViewById(R.id.ProfileActivity_bt_profilebtn);
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

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ProfileActivity.this, getString(R.string.example, result), Toast.LENGTH_SHORT).show();

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


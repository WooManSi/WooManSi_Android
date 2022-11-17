package com.example.woomansi.ui.screen.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.woomansi.R;
import com.example.woomansi.ui.screen.main.Main1Fragment;
import com.example.woomansi.ui.screen.main.MainActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProfileActivity extends AppCompatActivity {
    private ImageView imageView;
    private Button button;

    private ImageView load;

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
        Button button = dialog.findViewById(R.id.btn_ok);
        Glide.with(this).load(R.drawable.ic_profile1).into(iv1);
        Glide.with(this).load(R.drawable.ic_profile2).into(iv2);
        Glide.with(this).load(R.drawable.ic_profile3).into(iv3);
        Glide.with(this).load(R.drawable.ic_profile4).into(iv4);
        Glide.with(this).load(R.drawable.ic_profile5).into(iv5);
        Glide.with(this).load(R.drawable.ic_profile6).into(iv6);
        dialog.show();

        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setImageResource(R.drawable.ic_profile1);

            }
        });
        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setImageResource(R.drawable.ic_profile2);
            }
        });
        iv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setImageResource(R.drawable.ic_profile3);
            }
        });
        iv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setImageResource(R.drawable.ic_profile4);
            }
        });
        iv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setImageResource(R.drawable.ic_profile5);
            }
        });
        iv6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setImageResource(R.drawable.ic_profile6);
            }
        });


        button.setOnClickListener(view -> dialog.dismiss());
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        button = findViewById(R.id.ProfileActivity_bt_profilebtn);
        imageView = findViewById(R.id.ProfileActivity_iv_image);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog();
            }
        });

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        StorageReference pathReference = storageReference.child("ic_profile6");
        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(ProfileActivity.this).load(uri).into(load);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
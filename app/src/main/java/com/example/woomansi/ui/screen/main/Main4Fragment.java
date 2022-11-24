package com.example.woomansi.ui.screen.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.woomansi.R;

import android.app.Dialog;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.woomansi.ui.screen.login.ProfileActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class Main4Fragment extends Fragment{

    public Main4Fragment() {
        // Required empty public constructor
    }


    public static Main4Fragment newInstance() {
        return new Main4Fragment();
    }

    private ImageView imageView;
    private Button button;
    private int num = 0;
    private int i = 0;
    private int j = 0;
    private int count = 0;
    private int result = 0;
    //private FirebaseStorage mStorage;
    //private FirebaseDatabase mDatabase;
    private FirebaseFirestore db;
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth mFirebaseAuth;
    private View view;
    ArrayList idList = new ArrayList();

    private void customDialog() {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_profile);
        dialog.setTitle("custom dialog !!");
        GridLayout g1 = dialog.findViewById(R.id.g_view);

        count = g1.getChildCount();

        dialog.show();

        for(i = 0; i< count ; i++) {
            ImageView iv = (ImageView) g1.getChildAt(i);
            StringBuilder fileName = new StringBuilder("ic_profile");
            fileName.append(i+1);

            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imageView.setImageResource(getResources().getIdentifier(fileName.toString(), "drawable", getActivity().getPackageName()));
                    result = fileName.toString().compareTo("ic_profile0");
                    dialog.dismiss();
                }
            });
        }

}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_main4, container, false);

        db = FirebaseFirestore.getInstance();
        button = view.findViewById(R.id.ProfileActivity_bt_profilebtn);
        imageView = view.findViewById(R.id.ProfileActivity_iv_image);
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

                DocumentReference profileRef = db.collection("users").document(firebaseUser.getUid());
                profileRef
                        .update("profile", getString(R.string.profile, result))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(view.getContext(), "프로필 설정 완료", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        return view;
    }

}
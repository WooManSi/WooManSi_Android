package com.example.woomansi.ui.screen.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.woomansi.R;
import com.example.woomansi.ui.screen.timetable.createTimeTableActivity;

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

    private ImageView ivProfile;
    private Button btn;

    private int i = 0;

    private int count = 0;
    private int result = 0;
    //private FirebaseStorage mStorage;
    //private FirebaseDatabase mDatabase;
    private FirebaseFirestore db;
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth mFirebaseAuth;
    private View view;

    private final int[] profiles = new int[] {
            R.drawable.ic_profile1,
            R.drawable.ic_profile2,
            R.drawable.ic_profile3,
            R.drawable.ic_profile4,
            R.drawable.ic_profile5,
            R.drawable.ic_profile6
    };



    private void customDialog() {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_profile);
        dialog.setTitle("custom dialog !!");
        GridLayout g1 = dialog.findViewById(R.id.g_view);

        count = g1.getChildCount();

        dialog.show();

        for(i = 0; i< count ; i++) {
            int j = i;
            ImageView iv = (ImageView) g1.getChildAt(i);
            StringBuilder fileName = new StringBuilder("ic_profile");
            fileName.append(j+1);

            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ivProfile.setImageResource(profiles[j]);
                    result = j;
                    dialog.dismiss();
                }
            });
        }

}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //appBar의 fragment개별 맞춤 설정
        setHasOptionsMenu(true);

        view = inflater.inflate(R.layout.fragment_main4, container, false);

        db = FirebaseFirestore.getInstance();
        btn = view.findViewById(R.id.ProfileActivity_bt_profilebtn);
        ivProfile = view.findViewById(R.id.ProfileActivity_iv_image);
        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
        //FirebaseStorage storage = FirebaseStorage.getInstance();


        ivProfile.setOnClickListener(new View.OnClickListener() {
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


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_appbar_for_profile_change, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.appBar_deleteAccount) {
            //TODO : 탈퇴 및 계정 삭제 클릭 시 탈퇴화면으로 이동하는 코드. 탈퇴 화면 작성 후에 갈아끼워주기
            Intent intent = new Intent(getActivity().getApplicationContext(), createTimeTableActivity.class);
            startActivity(intent);
        }
        return true;
    }
}
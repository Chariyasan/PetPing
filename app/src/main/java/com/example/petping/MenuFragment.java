package com.example.petping;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MenuFragment extends Fragment {
    private Button btnEditUser, btnLikeList, btnPetStory, btnHistory, btnFAQ, btnRule, btnLogOut;
    private ImageView image;
    private TextView name;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private AlertDialog dialog;
    private AlertDialog.Builder builder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_menu, container, false);

        name = view.findViewById(R.id.textView);
        btnEditUser = view.findViewById(R.id.btn_user);
        btnLikeList = view.findViewById(R.id.btn_like);
        btnPetStory = view.findViewById(R.id.btn_story);
        btnHistory = view.findViewById(R.id.btn_history);
        btnRule = view.findViewById(R.id.btn_rule);
        btnFAQ = view.findViewById(R.id.btn_faq);
        btnLogOut = view.findViewById(R.id.btn_log_out);
        image = view.findViewById(R.id.user_profile);

        db.collection("User")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("Information")
                .document("Information")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            Glide.with(getContext())
                                    .load(documentSnapshot.get("Image").toString())
                                    .into(image);
                            name.setText(documentSnapshot.get("UserName").toString());
                        }
                    }
                });
        btnEditUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(getId(), new UserEditFragment());
                ft.addToBackStack(null).commit();
            }
        });

        btnLikeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(getId(), new UserLikeFragment());
                ft.addToBackStack(null).commit();
            }
        });

        btnPetStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(getId(), new UserHistoryFragment());
                ft.addToBackStack(null).commit();
            }
        });

        btnRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(getId(), new UserRegulationFragment());
                ft.addToBackStack(null).commit();
            }
        });

        btnFAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(getId(), new UserFAQFragment());
                ft.addToBackStack(null).commit();
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new AlertDialog.Builder(getContext());
                builder.setTitle("คุณต้องการออกจากระบบใช่หรือไม่");
                builder.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), SelectTypeUserActivity.class);
                        getActivity().startActivity(intent);
                    }
                });
                builder.setNegativeButton("ไม่ใช่", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog = builder.create();
                dialog.show();
            }
        });

        return view;
    }
}
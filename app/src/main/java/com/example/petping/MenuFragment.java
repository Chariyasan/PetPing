package com.example.petping;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MenuFragment extends Fragment {
    private Button btnEditUser, btnLikeList, btnPetStory, btnHistory, btnFAQ, btnRule;
    private TextView name;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
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
        db.collection("User")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("Information")
                .document("Information")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        name.setText(documentSnapshot.get("UserName").toString());
                    }
                });
        btnEditUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(getId(), new UserEditFragment());
                ft.commit();
            }
        });

        btnLikeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(getId(), new UserLikeFragment());
                ft.commit();
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
                ft.commit();
            }
        });

        btnRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(getId(), new UserRegulationFragment());
                ft.commit();
            }
        });

        btnFAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(getId(), new UserFAQFragment());
                ft.commit();
            }
        });

        return view;
    }


//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        MenuInflater inflater = getLayoutInflater();
//        inflater.inflate(R.layout.fragment_menu,menu);
//        return true;
//
//    }

    //    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
////        final View temp = inflater.inflate(R.layout.fragment_search, null);
//        final View view = inflater.inflate(R.layout.fragment_menu, null);
//        btnUser = view.findViewById(R.id.btn_user);
//        btnHistory = view.findViewById(R.id.btn_history);
//
////        btnUser.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                FragmentTransaction ft = getFragmentManager().beginTransaction();
////                ft.replace(getId(), new UserFragment());
////                ft.commit();
////            }
////        });
//
//        btnHistory.setOnClickListener(listener);
//
//
//
//
//
//        return view;
//    }

//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        final View view = inflater.inflate(R.layout.fragment_menu_history,container,false);
//
//        btnHistory = view.findViewById(R.id.btn_history);
//        btnHistory.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.replace(getId(), new UserHistoryFragment());
//                ft.commit();
//            }
//        });
//
//        return view;
//    }

}
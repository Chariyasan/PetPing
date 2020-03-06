package com.example.petping;

import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class UserEditFragment extends Fragment {
    private TextView userName, name, tel, job, addr;
    private ImageView btnUserName, btnName, btnTel, btnJob, btnAddr;
    private ImageButton edit_back_btn;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_edit, container, false);
        userName = view.findViewById(R.id.txt_user_name);
        name = view.findViewById(R.id.txt_name);
        tel = view.findViewById(R.id.txt_tel);
        job = view.findViewById(R.id.txt_job);
        addr = view.findViewById(R.id.txt_addr);

        btnUserName = view.findViewById(R.id.btn_edit_user_name);
        btnName = view.findViewById(R.id.btn_edit_name);
        btnTel = view.findViewById(R.id.btn_edit_tel);
        btnJob = view.findViewById(R.id.btn_edit_job);
        btnAddr = view.findViewById(R.id.btn_edit_addr);
        edit_back_btn = view.findViewById(R.id.edit_back_btn);

        db.collection("User")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("Information")
                .document("Information")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        userName.setText(documentSnapshot.get("UserName").toString());
                        name.setText(documentSnapshot.get("Name").toString());
                        tel.setText(documentSnapshot.get("TelNo").toString());
                        job.setText(documentSnapshot.get("Job").toString());
                        addr.setText(documentSnapshot.get("Address").toString());
                    }
                });

        btnUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PopUpEditUserNameActivity.class);
                startActivity(intent);
            }
        });

        btnName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PopUpEditNameActivity.class);
                startActivity(intent);
            }
        });

        btnTel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PopUpEditTelActivity.class);
                startActivity(intent);
            }
        });

        btnJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PopUpEditJobActivity.class);
                startActivity(intent);
            }
        });

        btnAddr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PopUpEditAddressActivity.class);
                startActivity(intent);
            }
        });

        edit_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(getId(), new MenuFragment());
                ft.commit();
            }
        });
        return view;
    }
}

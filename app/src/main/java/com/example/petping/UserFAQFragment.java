package com.example.petping;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserFAQFragment extends Fragment {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView help;
    private ImageButton help_back_btn;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_faq, null);

        help = view.findViewById(R.id.help);
        help_back_btn = view.findViewById(R.id.help_back_btn);

        db.collection("Information")
                .document("Help")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.d("็Help", String.valueOf(documentSnapshot.get("consent")));
                        help.setText(String.valueOf(documentSnapshot.get("consent")));
                    }
                });

        help_back_btn.setOnClickListener(new View.OnClickListener() {
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

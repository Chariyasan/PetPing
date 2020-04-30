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
    private TextView what, shelter, adopter, how;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_faq, null);

        what = view.findViewById(R.id.what);
        shelter = view.findViewById(R.id.shelter);
        adopter = view.findViewById(R.id.adopter);
        how = view.findViewById(R.id.how);
        db.collection("Information")
                .document("Help")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.d("็Help", String.valueOf(documentSnapshot.get("what")));
                        what.setText(String.valueOf(documentSnapshot.get("what")));
                        shelter.setText(String.valueOf(documentSnapshot.get("shelter")));
                        adopter.setText(String.valueOf(documentSnapshot.get("adopter")));
                        how.setText(String.valueOf(documentSnapshot.get("how")));
                    }
                });

        return view;
    }
}

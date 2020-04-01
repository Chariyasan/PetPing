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

public class UserRegulationFragment extends Fragment {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView one, two, three, four, five, six, seven, eight;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_regulation, null);

        one = view.findViewById(R.id.reg_one);
        two = view.findViewById(R.id.reg_two);
        three = view.findViewById(R.id.reg_three);
        four = view.findViewById(R.id.reg_four);
        five = view.findViewById(R.id.reg_five);
        six = view.findViewById(R.id.reg_six);
        seven = view.findViewById(R.id.reg_seven);
        eight = view.findViewById(R.id.reg_eight);

        db.collection("Information")
                .document("Regulation")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.d("Regulation", String.valueOf(documentSnapshot.get("one")));
                        one.setText(String.valueOf(documentSnapshot.get("one")));
                        two.setText(String.valueOf(documentSnapshot.get("two")));
                        three.setText(String.valueOf(documentSnapshot.get("three")));
                        four.setText(String.valueOf(documentSnapshot.get("four")));
                        five.setText(String.valueOf(documentSnapshot.get("five")));
                        six.setText(String.valueOf(documentSnapshot.get("six")));
                        seven.setText(String.valueOf(documentSnapshot.get("seven")));
                        eight.setText(String.valueOf(documentSnapshot.get("eight")));
                    }
                });

        return view;
    }
}

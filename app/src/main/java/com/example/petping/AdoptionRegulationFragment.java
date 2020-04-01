package com.example.petping;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class AdoptionRegulationFragment extends Fragment {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView one, two, three, four, five, six, seven, eight;
    private CheckBox cbBox;
    private Button regBox;
    private ArrayList<PetSearch> petProfileList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_adoption_reg_process, null);
        if(getArguments() != null){
            petProfileList = getArguments().getParcelableArrayList("petProfile");
        }
       one = view.findViewById(R.id.reg_one);
       two = view.findViewById(R.id.reg_two);
       three = view.findViewById(R.id.reg_three);
       four = view.findViewById(R.id.reg_four);
       five = view.findViewById(R.id.reg_five);
       six = view.findViewById(R.id.reg_six);
       seven = view.findViewById(R.id.reg_seven);
       eight = view.findViewById(R.id.reg_eight);

       cbBox = view.findViewById(R.id.reg_cb);
       regBox = view.findViewById(R.id.reg_btn);
       regBox.setEnabled(false);
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

       cbBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if(isChecked){
                   regBox.setEnabled(true);
                   regBox.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           AdoptionInfoFragment adoptionInfo = new AdoptionInfoFragment();
                           Bundle bundle = new Bundle();
                           bundle.putParcelableArrayList("petProfile", petProfileList);

                           adoptionInfo.setArguments(bundle);
                           FragmentTransaction ft = getFragmentManager().beginTransaction();
                           ft.replace(getId(), adoptionInfo);
                           ft.addToBackStack(null).commit();
//                           FragmentTransaction ft = getFragmentManager().beginTransaction();
//                           ft.replace(getId(), new AdoptionInfoFragment());
//                           ft.commit();
                       }
                   });
               }
               else {
                   regBox.setEnabled(false);
               }
           }
       });
       return view;
    }


}

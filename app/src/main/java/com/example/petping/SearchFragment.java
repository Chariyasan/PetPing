package com.example.petping;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


public class SearchFragment extends Fragment {
    private ImageButton btnDog;
    private ImageButton btnCat;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private PetSearch petSearch;
    private ArrayList<PetSearch> petList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_search, null);
        btnDog = view.findViewById(R.id.btn_dog);
        btnCat = view.findViewById(R.id.btn_cat);
//        btnDog.setImageResource(R.mipmap.dog_background);
//        btnCat.setImageResource(R.mipmap.cat_background);
        //Show Pet that match with each user instead of random
        btnDog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Pet")
                .whereEqualTo("Type", "สุนัข")
                .whereEqualTo("Status", "กำลังหาบ้าน")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            setValue(task);
                        } else {
                            Log.d("Error", "Error getting documents: ", task.getException());
                        }
                    }

                });
            }
        });

        btnCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Pet")
                        .whereEqualTo("Type", "แมว")
                        .whereEqualTo("Status", "กำลังหาบ้าน")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    setValue(task);
                                } else {
                                    Log.d("Error", "Error getting documents: ", task.getException());
                                }
                            }

                        });
            }
        });
        return view;
    }


    private void setValue(Task<QuerySnapshot> task) {
        if(petList != null){
            petList.clear();
        }
        for (QueryDocumentSnapshot document : task.getResult()) {
            petSearch = new PetSearch(document.getId(), document.get("Name").toString(), document.get("Type").toString(),
                    document.get("Color").toString(), document.get("Sex").toString(), document.get("Age").toString(),
                    document.get("Breed").toString(), document.get("Size").toString(), document.get("Image").toString(),
                    document.get("Weight").toString(), document.get("Character").toString(), document.get("Marking").toString(),
                    document.get("Health").toString(), document.get("OriginalLocation").toString(), document.get("Status").toString(),
                    document.get("Story").toString());
            petList.add(petSearch);
        }

        PetSearchResult petSearchResult = new PetSearchResult();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("petL", petList);
        petSearchResult.setArguments(bundle);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(getId(), petSearchResult);
        ft.addToBackStack(null).commit();
    }

}
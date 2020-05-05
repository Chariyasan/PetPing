package com.example.petping;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment extends Fragment {
    private ViewFlipper flipper;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<PetSearch> petList = new ArrayList<>();
    private ArrayList<Content> contentList = new ArrayList<>();
    private HomeAdapter homeAdapter;
    private ContentHomeAdapter contentAdapter;
    private RecyclerView pet_rec, contentView;
    private TextView seePet;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, null);
        int image[]= {R.drawable.flip1, R.drawable.flip2, R.drawable.flip3};
        flipper = view.findViewById(R.id.flipper_home);
        seePet = view.findViewById(R.id.see_pet);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        pet_rec = view.findViewById(R.id.pet_rec);
        pet_rec.setLayoutManager(layoutManager);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        contentView = view.findViewById(R.id.content);
        contentView.setLayoutManager(layoutManager1);

        for(int i=0; i<image.length; i++){
            flipperImages(image[i]);
        }

        if(petList != null){
            petList.clear();
        }
        if(contentList != null){
            contentList.clear();
        }


        db.collection("Content")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Content content = new Content(document.getId(), document.get("Topic").toString(),
                                        document.get("Story").toString(), document.get("URL").toString(),
                                        document.get("Tag").toString(), document.get("ShelterID").toString());
                                contentList.add(content);
//                                Log.d("Content",document.get("Topic").toString());
                            }
                            contentAdapter = new ContentHomeAdapter(getFragmentManager(), getId(), getContext(), contentList);
                            contentView.setAdapter(contentAdapter);
                        }
                    }
                });

        db.collection("Pet")
                .whereEqualTo("Status", "กำลังหาบ้าน")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                PetSearch petSearch = new PetSearch(document.getId(), document.get("Name").toString(), document.get("Type").toString(),
                                        document.get("Color").toString(), document.get("Sex").toString(), document.get("Age").toString(),
                                        document.get("Breed").toString(), document.get("Size").toString(), document.get("Image").toString(),
                                        document.get("Weight").toString(), document.get("Character").toString(), document.get("Marking").toString(),
                                        document.get("Health").toString(), document.get("OriginalLocation").toString(), document.get("Status").toString(),
                                        document.get("Story").toString(), document.get("ShelterID").toString());
                                petList.add(petSearch);
                            }
                            homeAdapter = new HomeAdapter(getFragmentManager(), getId(), getContext(), petList);
                            pet_rec.setAdapter(homeAdapter);
                        }
                    }
                });

        seePet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecommendPetFragment pet = new RecommendPetFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("recommendPet", petList);
                pet.setArguments(bundle);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(getId(), pet);
                ft.addToBackStack(null).commit();
            }
        });

        return view;
    }

    public void flipperImages(int image){
        ImageView img = new ImageView(getContext());
        img.setBackgroundResource(image);
        flipper.addView(img);
        flipper.setFlipInterval(4000);
        flipper.setAutoStart(true);
    }
}

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

public class HomeFragment extends Fragment {
    private ViewFlipper flipper, flipperPet;
    private Button dogBtn, catBtn;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<PetSearch> petListDog = new ArrayList<>();
    private ArrayList<PetSearch> petListCat = new ArrayList<>();
    private ArrayList<PetSearch> petList = new ArrayList<>();
    private HomeAdapter homeAdapter;
    private GridView gridDog, gridCat, gridAll;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, null);
        int image[]= {R.drawable.flip1, R.drawable.flip2, R.drawable.flip3};
        flipper = view.findViewById(R.id.flipper_home);
        flipperPet = view.findViewById(R.id.flipper_home_pet);

        dogBtn = view.findViewById(R.id.home_dog_btn);
        catBtn = view.findViewById(R.id.home_cat_btn);

        gridAll = view.findViewById(R.id.grid_all);
        gridDog = view.findViewById(R.id.grid_dog);
        gridCat = view.findViewById(R.id.grid_cat);

        for(int i=0; i<image.length; i++){
            flipperImages(image[i]);
        }

        if(petList != null){
            petList.clear();
        }
        if(petListDog != null){
            petListDog.clear();
        }
        if(petListCat != null){
            petListCat.clear();
        }
        db.collection("Pet")
                .whereEqualTo("Type", "สุนัข")
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
                                        document.get("Story").toString());
                                petListDog.add(petSearch);
                                petList.add(petSearch);
                            }
                            Set<PetSearch> set = new HashSet<PetSearch>(petListDog);
                            petListDog.clear();
                            petListDog.addAll(set);


                            Set<PetSearch> setA = new HashSet<PetSearch>(petList);
                            petList.clear();
                            petList.addAll(setA);
                            flipperPet.setDisplayedChild(flipperPet.indexOfChild(view.findViewById(R.id.grid_all)));
                            homeAdapter = new HomeAdapter(getContext(), petList);
                            gridAll.setAdapter(homeAdapter);
                            homeAdapter.notifyDataSetChanged();

                            gridAll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    PetProfileGeneralFragment petProfile = new PetProfileGeneralFragment();
                                    Bundle bundle = new Bundle();
                                    ArrayList<PetSearch> list = new ArrayList<>();
                                    list.add(petList.get(position));
                                    bundle.putParcelableArrayList("petProfile", list);
                                    petProfile.setArguments(bundle);
                                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                                    ft.replace(getId(), petProfile);
                                    ft.addToBackStack(null).commit();
                                }
                            });
                        } else {
                            Log.d("Error", "Error getting documents: ", task.getException());
                        }
                    }

                });


        db.collection("Pet")
                .whereEqualTo("Type", "แมว")
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
                                        document.get("Story").toString());
                                petListCat.add(petSearch);
                            }
                            Set<PetSearch> set = new HashSet<PetSearch>(petListCat);
                            petListCat.clear();
                            petListCat.addAll(set);
                        } else {
                            Log.d("Error", "Error getting documents: ", task.getException());
                        }
                    }

                });

        dogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipperPet.setDisplayedChild(flipperPet.indexOfChild(view.findViewById(R.id.grid_dog)));
                homeAdapter = new HomeAdapter(getContext(), petListDog);
                gridDog.setAdapter(homeAdapter);
                homeAdapter.notifyDataSetChanged();

                gridDog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        PetProfileGeneralFragment petProfile = new PetProfileGeneralFragment();
                        Bundle bundle = new Bundle();
                        ArrayList<PetSearch> list = new ArrayList<>();
                        list.add(petListDog.get(position));
                        bundle.putParcelableArrayList("petProfile", list);
                        petProfile.setArguments(bundle);
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(getId(), petProfile);
                        ft.addToBackStack(null).commit();
                    }
                });
            }
        });

        catBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipperPet.setDisplayedChild(flipperPet.indexOfChild(view.findViewById(R.id.grid_cat)));
                homeAdapter = new HomeAdapter(getContext(), petListCat);
                gridCat.setAdapter(homeAdapter);
                homeAdapter.notifyDataSetChanged();
                gridCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        PetProfileGeneralFragment petProfile = new PetProfileGeneralFragment();
                        Bundle bundle = new Bundle();
                        ArrayList<PetSearch> list = new ArrayList<>();
                        list.add(petListCat.get(position));
                        bundle.putParcelableArrayList("petProfile", list);
                        petProfile.setArguments(bundle);
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(getId(), petProfile);
                        ft.addToBackStack(null).commit();
                    }
                });
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

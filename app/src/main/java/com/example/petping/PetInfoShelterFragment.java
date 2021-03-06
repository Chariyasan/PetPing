package com.example.petping;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class PetInfoShelterFragment extends Fragment {
    private ArrayList<PetSearch> petInfoList;
    private ViewFlipper viewFlipper;
    private Button btnInfo, btnStory;
    private ImageView image;
    private TextView name, sex, breed, age, colour, marking, weight;
    private TextView size, character, foundLoc, status, story;
    private Button btnEditInfo, btnDeleteInfo;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private int i;
    private String ID;
    private ArrayList<PetSearch> petItem = new ArrayList<>();
    private AlertDialog dialog;
    private AlertDialog.Builder builder;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_pet_info_shelter, null);
        if(getArguments() != null){
            petInfoList = getArguments().getParcelableArrayList("petInfo");
        }

        image = view.findViewById(R.id.image);
        name = view.findViewById(R.id.name);
        sex = view.findViewById(R.id.sex);
        breed = view.findViewById(R.id.breed);
        colour = view.findViewById(R.id.colour);
        age = view.findViewById(R.id.age);
        marking = view.findViewById(R.id.marking);
        weight = view.findViewById(R.id.weight);
        size = view.findViewById(R.id.size);
        character = view.findViewById(R.id.character);
        foundLoc = view.findViewById(R.id.found_location);
        status = view.findViewById(R.id.status);
        story = view.findViewById(R.id.story);

        btnEditInfo = view.findViewById(R.id.edit_info);
        btnDeleteInfo = view.findViewById(R.id.delete_info);

        for (i = 0; i < petInfoList.size(); i++) {
            ID = petInfoList.get(i).getID();
        }

        db.collection("Pet")
                .document(ID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Glide.with(getContext())
                                .load(documentSnapshot.get("Image"))
                                .into(image);
                        name.setText(documentSnapshot.get("Name").toString());
                        sex.setText(documentSnapshot.get("Sex").toString());
                        breed.setText(documentSnapshot.get("Breed").toString());
                        colour.setText(documentSnapshot.get("Color").toString());
                        age.setText(documentSnapshot.get("Age").toString());
                        marking.setText(documentSnapshot.get("Marking").toString());
                        weight.setText(documentSnapshot.get("Weight").toString());
                        size.setText(documentSnapshot.get("Size").toString());
                        character.setText(documentSnapshot.get("Character").toString());
                        foundLoc.setText(documentSnapshot.get("OriginalLocation").toString());
                        status.setText(documentSnapshot.get("Status").toString());
                        story.setText(documentSnapshot.get("Story").toString());

                        PetSearch petSearch = new PetSearch(documentSnapshot.getId(), documentSnapshot.get("Name").toString(), documentSnapshot.get("Type").toString(),
                                documentSnapshot.get("Color").toString(), documentSnapshot.get("Sex").toString(), documentSnapshot.get("Age").toString(),
                                documentSnapshot.get("Breed").toString(), documentSnapshot.get("Size").toString(), documentSnapshot.get("Image").toString(),
                                documentSnapshot.get("Weight").toString(), documentSnapshot.get("Character").toString(), documentSnapshot.get("Marking").toString(),
                                documentSnapshot.get("Health").toString(), documentSnapshot.get("OriginalLocation").toString(), documentSnapshot.get("Status").toString(),
                                documentSnapshot.get("Story").toString(), documentSnapshot.get("ShelterID").toString(), documentSnapshot.get("Rec").toString(), documentSnapshot.get("AddDateTime").toString());
                        petItem.add(petSearch);
                    }
                });

        btnEditInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditPetInfoShelterFragment petInfoShelterFragment = new  EditPetInfoShelterFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("petEditInfo", petItem);
                petInfoShelterFragment.setArguments(bundle);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(getId(),  petInfoShelterFragment);
                ft.addToBackStack(null).commit();
            }
        });
        btnDeleteInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new AlertDialog.Builder(getContext());
                builder.setTitle("คุณต้องการลบสัตว์นี้ใช่หรือไม่");

                builder.setNegativeButton("ไม่ใช่", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.collection("Pet")
                                .document(ID)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                                        ft.replace(getId(), new ManagePetInfoShelterFragment());
                                        ft.commit();
                                    }
                                });
                    }
                });
                dialog = builder.create();
                dialog.show();
            }
        });
        return view;
    }
}

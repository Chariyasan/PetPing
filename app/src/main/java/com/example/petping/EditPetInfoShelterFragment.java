package com.example.petping;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import static android.app.Activity.RESULT_OK;

public class EditPetInfoShelterFragment extends Fragment {
    private ArrayList<PetSearch> petInfoList;
    private ArrayList<PetSearch> petList = new ArrayList<>();
    private TextView changeImage;
    private EditText name, sex, breed, age, colour, marking, weight;
    private EditText size, character, foundLoc, status, story;
    private RadioGroup sexRdGroup;
    private RadioButton sexRd, maleRd, femaleRd;
    private ImageView image;
    private ViewFlipper viewFlipper;
    private Button btnInfo, btnStory, btnSaveInfo;
    private String ID, type, size1, url, health;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Map<String, Object> data = new HashMap<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_edit_pet_info_shelter, null);

        if(getArguments() != null){
            petInfoList = (ArrayList<PetSearch>)getArguments().getSerializable("petEditInfo");
        }

        changeImage = view.findViewById(R.id.change_img);
        image = view.findViewById(R.id.image);
        name = view.findViewById(R.id.name);
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

        sexRdGroup = view.findViewById(R.id.rd_sex);
        maleRd = view.findViewById(R.id.rd_male);
        femaleRd = view.findViewById(R.id.rd_female);

        viewFlipper = view.findViewById(R.id.view_flipper_shelter);
        btnInfo = view.findViewById(R.id.btn_info);
        btnStory = view.findViewById(R.id.btn_story);
        btnSaveInfo = view.findViewById(R.id.save_info);

        btnInfo.setTypeface(null, Typeface.BOLD);
        btnStory.setTypeface(null, Typeface.NORMAL);

        for (int i=0; i<petInfoList.size(); i++){
            ID = petInfoList.get(i).getID();
            type = petInfoList.get(i).getType();
            size1 = petInfoList.get(i).getSize();
            url = petInfoList.get(i).getUrl();
            health = petInfoList.get(i).getHealth();
            Glide.with(getContext())
                    .load(petInfoList.get(i).getUrl())
                    .into(image);
            name.setText(petInfoList.get(i).getName());
            breed.setText(petInfoList.get(i).getBreed());
            colour.setText(petInfoList.get(i).getColour());
            age.setText(petInfoList.get(i).getAge());
            marking.setText(petInfoList.get(i).getMarking());
            weight.setText(petInfoList.get(i).getWeight());
            size.setText(petInfoList.get(i).getSize());
            character.setText(petInfoList.get(i).getCharacter());
            foundLoc.setText(petInfoList.get(i).getFoundLoc());
            status.setText(petInfoList.get(i).getStatus());
            story.setText(petInfoList.get(i).getStory());

            int radioSex = sexRdGroup.getCheckedRadioButtonId();
            sexRd = view.findViewById(radioSex);
            if(petInfoList.get(i).getSex().equals("ผู้")){
                maleRd.setChecked(true);
                femaleRd.setChecked(false);
            }
            if(petInfoList.get(i).getSex().equals("เมีย")){
                maleRd.setChecked(false);
                femaleRd.setChecked(true);
            }

//            int radioSex = sexRdGroup.getCheckedRadioButtonId();
//            sexRd = view.findViewById(radioSex);
//            if(sexRd == view.findViewById(R.id.rd_male)){
//                sex = maleRd.getText().toString();
//            }
//            else if(sexRd == view.findViewById(R.id.rd_female)){
//                sex = femaleRd.getText().toString();
//            }
        }

        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(view.findViewById(R.id.view_info)));
                btnInfo.setTypeface(null, Typeface.BOLD);
                btnStory.setTypeface(null, Typeface.NORMAL);
            }
        });

        btnStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(view.findViewById(R.id.story)));
                btnStory.setTypeface(null, Typeface.BOLD);
                btnInfo.setTypeface(null, Typeface.NORMAL);
            }
        });

        btnSaveInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sex = null;
                int radioSex = sexRdGroup.getCheckedRadioButtonId();
                sexRd = view.findViewById(radioSex);
                if(sexRd == view.findViewById(R.id.rd_male)){
                    sex = maleRd.getText().toString();
                }
                else if(sexRd == view.findViewById(R.id.rd_female)){
                    sex = femaleRd.getText().toString();
                }
                
                data.put("Name", name.getText().toString());
                data.put("Breed", breed.getText().toString());
                data.put("Color", colour.getText().toString());
                data.put("Sex", sex);
                data.put("Age", age.getText().toString());
                data.put("Marking", marking.getText().toString());
                data.put("Weight", weight.getText().toString());
                data.put("Character", character.getText().toString());
                data.put("OriginalLocation", foundLoc.getText().toString());
                data.put("Status", status.getText().toString());
                data.put("Story", story.getText().toString());
                
//                sex.setText(documentSnapshot.get("Sex").toString());
//                size.setText(documentSnapshot.get("Size").toString());


                final String finalSex = sex;
                db.collection("Pet")
                        .document(ID)
                        .update(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("Update", "Success");
                                PetSearch pet = new PetSearch(ID, name.getText().toString(), type,
                                        colour.getText().toString(), finalSex, age.getText().toString(),
                                        breed.getText().toString(), size1, url, weight.getText().toString(),
                                        character.getText().toString(), marking.getText().toString(),
                                        health, foundLoc.getText().toString(), status.getText().toString(),
                                        story.getText().toString());
                                petList.add(pet);
                                PetInfoShelterFragment petInfoShelterFragment = new PetInfoShelterFragment();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("petInfo", petList);
                                petInfoShelterFragment.setArguments(bundle);
                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.replace(getId(), petInfoShelterFragment);
                                ft.commit();
                            }
                        });
            }
        });
        return view;
    }


}

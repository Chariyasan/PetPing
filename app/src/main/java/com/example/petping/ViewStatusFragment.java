package com.example.petping;

import android.graphics.Color;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class ViewStatusFragment extends Fragment {
    private ArrayList<PetSearch> statusList = new ArrayList<>();
    private TextView petName, petBreed, petAge, petSex, petColour, petMarking;
    private TextView petHealth, petWeight, petSize, petCharacter, petFoundLoc;
    private TextView qOne, qTwo, qThree, qFour, qFive;
    private TextView aOne, aTwo, aThree, aFour, aFive;
    private TextView qSix, qSeven, qEight, qNine, qTen, qEleven;
    private TextView aSix, aSeven, aEight, aNine, aTen, aEleven;
    private TextView shelterName, shelterOwner, shelterAddress, shelterEmail;
    private TextView shelterTelNo, shelterFB, shelterIG, shelterLine, infoMap;
    private Button btnShelter, btnQuestion, btnPet;
    private ViewFlipper flipper;
    private ImageView petImage, adoptImage;
    private String petID, shelterID;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_view_status, null);
        if(getArguments() != null){
            statusList = getArguments().getParcelableArrayList("viewStatus");
        }
        petImage = view.findViewById(R.id.pet_image_qa);
        adoptImage = view.findViewById(R.id.adopt_image_qa);

        infoMap = view.findViewById(R.id.shelter_map);
        petName = view.findViewById(R.id.pet_name);
        petBreed = view.findViewById(R.id.pet_breed);
        petAge = view.findViewById(R.id.pet_age);
        petSex = view.findViewById(R.id.pet_sex);
        petColour = view.findViewById(R.id.pet_colour);
        petMarking = view.findViewById(R.id.pet_marking);
        petWeight = view.findViewById(R.id.pet_weight);
        petHealth = view.findViewById(R.id.pet_health);
        petSize = view.findViewById(R.id.pet_size);
        petCharacter = view.findViewById(R.id.pet_character);
        petFoundLoc = view.findViewById(R.id.pet_found_location);

        qOne = view.findViewById(R.id.q_one);
        qTwo = view.findViewById(R.id.q_two);
        qThree = view.findViewById(R.id.q_three);
        qFour = view.findViewById(R.id.q_four);
        qFive = view.findViewById(R.id.q_five);
        qSix = view.findViewById(R.id.q_six);
        qSeven = view.findViewById(R.id.q_seven);
        qEight = view.findViewById(R.id.q_eight);
        qNine = view.findViewById(R.id.q_nine);
        qTen = view.findViewById(R.id.q_ten);
        qEleven = view.findViewById(R.id.q_eleven);

        aOne = view.findViewById(R.id.a_one);
        aTwo = view.findViewById(R.id.a_two);
        aThree = view.findViewById(R.id.a_three);
        aFour = view.findViewById(R.id.a_four);
        aFive = view.findViewById(R.id.a_five);
        aSix = view.findViewById(R.id.a_six);
        aSeven = view.findViewById(R.id.a_seven);
        aEight = view.findViewById(R.id.a_eight);
        aNine = view.findViewById(R.id.a_nine);
        aTen = view.findViewById(R.id.a_ten);
        aEleven = view.findViewById(R.id.a_eleven);

        shelterName = view.findViewById(R.id.shelter_name);
        shelterAddress = view.findViewById(R.id.shelter_address);
        shelterEmail = view.findViewById(R.id.shelter_email);
        shelterTelNo = view.findViewById(R.id.shelter_telno);
        shelterFB = view.findViewById(R.id.shelter_fb);
        shelterIG = view.findViewById(R.id.shelter_ig);
        shelterLine = view.findViewById(R.id.shelter_line);
        shelterOwner = view.findViewById(R.id.shelter_owner);

        btnShelter = view.findViewById(R.id.btn_shelter_info);
        btnQuestion = view.findViewById(R.id.btn_basic_q);
        btnPet = view. findViewById(R.id.btn_pet_info);
        flipper = view.findViewById(R.id.view_flipper_status);

        btnShelter.setTypeface(null, Typeface.BOLD);
        btnShelter.setTextColor(Color.parseColor("#808080"));
        btnQuestion.setTypeface(null, Typeface.NORMAL);
        btnQuestion.setTextColor(Color.parseColor("#FFAFAFAF"));
        btnPet.setTypeface(null, Typeface.NORMAL);
        btnPet.setTextColor(Color.parseColor("#FFAFAFAF"));

        for(int i=0; i<statusList.size(); i++){
            Glide.with(getContext())
                    .load(statusList.get(i).getUrl())
                    .into(petImage);
            petName.setText(statusList.get(i).getName());
            petBreed.setText(statusList.get(i).getBreed());
            petAge.setText(statusList.get(i).getAge());
            petSex.setText(statusList.get(i).getSex());
            petColour.setText(statusList.get(i).getColour());
            petMarking.setText(statusList.get(i).getMarking());
            petWeight.setText(statusList.get(i).getWeight());
            petHealth.setText(statusList.get(i).getHealth());
            petSize.setText(statusList.get(i).getSize());
            petCharacter.setText(statusList.get(i).getCharacter());
            petFoundLoc.setText(statusList.get(i).getFoundLoc());

            petID = statusList.get(i).getID();
            shelterID = statusList.get(i).getShelterID();
        }

        db.collection("RequestAdoption")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("BasicQ")
                .document(petID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot document) {
                        aOne.setText(document.get("one").toString());
                        aTwo.setText(document.get("two").toString());
                        aThree.setText(document.get("three").toString());
                        aFour.setText(document.get("four").toString());
                        aFive.setText(document.get("five").toString());

                        aSix.setText(document.get("six").toString());
                        aSeven.setText(document.get("seven").toString());
                        aEight.setText(document.get("eight").toString());
                        aNine.setText(document.get("nine").toString());
                        aTen.setText(document.get("ten").toString());
                        aEleven.setText(document.get("eleven").toString());
                    }
                });
        db.collection("Information")
                .document("BasicQ")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        qOne.setText(String.valueOf(documentSnapshot.get("one")));
                        qTwo.setText(String.valueOf(documentSnapshot.get("two")));
                        qThree.setText(String.valueOf(documentSnapshot.get("three")));
                        qFour.setText(String.valueOf(documentSnapshot.get("four")));
                        qFive.setText(String.valueOf(documentSnapshot.get("five")));

                        qSix.setText(String.valueOf(documentSnapshot.get("six")));
                        qSeven.setText(String.valueOf(documentSnapshot.get("seven")));
                        qEight.setText(String.valueOf(documentSnapshot.get("eight")));
                        qNine.setText(String.valueOf(documentSnapshot.get("nine")));
                        qTen.setText(String.valueOf(documentSnapshot.get("ten")));
                        qEleven.setText(String.valueOf(documentSnapshot.get("eleven")));
                    }
                });
        db.collection("Shelter")
                .document(shelterID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        shelterName.setText(documentSnapshot.get("Name").toString());
                        shelterOwner.setText(documentSnapshot.get("Owner").toString());
                        shelterAddress.setText(documentSnapshot.get("Address").toString());
                        shelterEmail.setText(documentSnapshot.get("Email").toString());
                        shelterTelNo.setText(documentSnapshot.get("TelNo").toString());
                        shelterFB.setText(documentSnapshot.get("Facebook").toString());
                        shelterIG.setText(documentSnapshot.get("Instagram").toString());
                        shelterLine.setText(documentSnapshot.get("LineID").toString());
                        String address = documentSnapshot.get("Name").toString();
                        String latitude = documentSnapshot.get("Latitude").toString();
                        String longitude = documentSnapshot.get("Longitude").toString();

                        InfoMap(latitude, longitude, address);
                    }
                });
        btnShelter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipper.setDisplayedChild(flipper.indexOfChild(view.findViewById(R.id.shelter_info)));
                btnShelter.setTypeface(null, Typeface.BOLD);
                btnShelter.setTextColor(Color.parseColor("#808080"));
                btnQuestion.setTypeface(null, Typeface.NORMAL);
                btnQuestion.setTextColor(Color.parseColor("#FFAFAFAF"));
                btnPet.setTypeface(null, Typeface.NORMAL);
                btnPet.setTextColor(Color.parseColor("#FFAFAFAF"));
            }
        });

        btnQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipper.setDisplayedChild(flipper.indexOfChild(view.findViewById(R.id.basic_q)));
                btnShelter.setTypeface(null, Typeface.NORMAL);
                btnShelter.setTextColor(Color.parseColor("#FFAFAFAF"));
                btnQuestion.setTypeface(null, Typeface.BOLD);
                btnQuestion.setTextColor(Color.parseColor("#808080"));
                btnPet.setTypeface(null, Typeface.NORMAL);
                btnPet.setTextColor(Color.parseColor("#FFAFAFAF"));
            }
        });

        btnPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipper.setDisplayedChild(flipper.indexOfChild(view.findViewById(R.id.pet_info)));
                btnShelter.setTypeface(null, Typeface.NORMAL);
                btnShelter.setTextColor(Color.parseColor("#FFAFAFAF"));
                btnQuestion.setTypeface(null, Typeface.NORMAL);
                btnQuestion.setTextColor(Color.parseColor("#FFAFAFAF"));
                btnPet.setTypeface(null, Typeface.BOLD);
                btnPet.setTextColor(Color.parseColor("#808080"));
            }
        });
        return view;
    }

    private void InfoMap(final String latitude, final String longitude, final String address) {
        infoMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapsActivity mapsActivity = new MapsActivity();
                Bundle bundle = new Bundle();
                String location = address+","+latitude+","+longitude;
                bundle.putString("location", location);
                mapsActivity.setArguments(bundle);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(getId(), mapsActivity);
                ft.addToBackStack(null).commit();
//                Intent intent = new Intent(getActivity(), MapsActivity);
//                startActivity(intent);
            }
        });
    }
}

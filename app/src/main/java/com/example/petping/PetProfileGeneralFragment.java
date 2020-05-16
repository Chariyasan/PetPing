package com.example.petping;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.ViewFlipper;
import ru.dimorinny.floatingtextbutton.FloatingTextButton;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PetProfileGeneralFragment extends Fragment {
    private ArrayList<PetSearch> petProfileList;
    private ImageView imageView, imageSex, imageFav;
    private TextView infoName, infoAge, infoBreed,infoStory;
    private TextView infoColor, infoSize, infoMarking, infoChar;
    private TextView infoWeight, infoFoundLoc, infoStatus, infoMap, infohealth;
    private ViewFlipper viewFlipper;
    private Button btnGeneral, btnStory, btnShelter;
    //    private FloatingActionButton btnAdopt;
    private FloatingActionButton btnAdopt;
    //    private ExtendedFloatingActionButton btnAdopt;
    private ArrayList<PetSearch> petItem;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<PetSearch> petFavList = new ArrayList<>();
    private UserLikeAdapter likeAdapter;
    private TextView shelterName;
    private TextView shelterOwner;
    private TextView shelterAddress;
    private TextView shelterEmail;
    private TextView shelterTelNo;
    private TextView shelterFB;
    private TextView shelterIG;
    private TextView shelterLine;
    private String shelterID;
    private Map<String, Object> dataToSave = new HashMap<String, Object>();
    private AlertDialog dialog;
    private AlertDialog.Builder builder;
    private ToggleButton toggleButtonFav;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_pet_profile_general, null);
        if(getArguments() != null){
            petProfileList = getArguments().getParcelableArrayList("petProfile");
        }

        viewFlipper = view.findViewById(R.id.view_flipper_info);
        btnGeneral = view.findViewById(R.id.button_general);
        btnStory = view.findViewById(R.id.button_story);
        btnShelter = view.findViewById(R.id.button_shelter);
        btnAdopt = view.findViewById(R.id.btn_adopt);

        btnGeneral.setTypeface(null, Typeface.BOLD);
        btnGeneral.setTextColor(Color.parseColor("#808080"));
        btnStory.setTypeface(null, Typeface.NORMAL);
        btnStory.setTextColor(Color.parseColor("#FFAFAFAF"));
        btnShelter.setTypeface(null, Typeface.NORMAL);
        btnShelter.setTextColor(Color.parseColor("#FFAFAFAF"));

        btnGeneral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //viewFlipper.showPrevious();
                btnGeneral.setTypeface(null, Typeface.BOLD);
                btnGeneral.setTextColor(Color.parseColor("#808080"));
                btnStory.setTypeface(null, Typeface.NORMAL);
                btnStory.setTextColor(Color.parseColor("#FFAFAFAF"));
                btnShelter.setTypeface(null, Typeface.NORMAL);
                btnShelter.setTextColor(Color.parseColor("#FFAFAFAF"));
                viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(view.findViewById(R.id.scrollView_pet_general)));
            }
        });

        btnStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //viewFlipper.showNext();
                btnGeneral.setTypeface(null, Typeface.NORMAL);
                btnGeneral.setTextColor(Color.parseColor("#FFAFAFAF"));
                btnStory.setTypeface(null, Typeface.BOLD);
                btnStory.setTextColor(Color.parseColor("#808080"));
                btnShelter.setTypeface(null, Typeface.NORMAL);
                btnShelter.setTextColor(Color.parseColor("#FFAFAFAF"));
                viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(view.findViewById(R.id.scrollView_pet_story)));
            }
        });

        btnShelter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //viewFlipper.showNext();
                btnGeneral.setTypeface(null, Typeface.NORMAL);
                btnGeneral.setTextColor(Color.parseColor("#FFAFAFAF"));
                btnStory.setTypeface(null, Typeface.NORMAL);
                btnStory.setTextColor(Color.parseColor("#FFAFAFAF"));
                btnShelter.setTypeface(null, Typeface.BOLD);
                btnShelter.setTextColor(Color.parseColor("#808080"));
                viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(view.findViewById(R.id.scrollView_pet_shelter)));
            }
        });

//        imageFav = view.findViewById(R.id.fav);
        imageView = view.findViewById(R.id.img_pet_profile);
        infoName = view.findViewById(R.id.info_name);
        infoAge = view.findViewById(R.id.info_age);
        infoBreed = view.findViewById(R.id.info_breed);
        infoColor = view.findViewById(R.id.info_colour);
        infoSize = view.findViewById(R.id.info_size);
        infoMarking = view.findViewById(R.id.info_marking);
        infoChar = view.findViewById(R.id.info_character);
        infoWeight = view.findViewById(R.id.info_weight);
        infoFoundLoc = view.findViewById(R.id.info_location);
        infoStatus = view.findViewById(R.id.info_status);
        imageSex = view.findViewById(R.id.img_info_sex);
        toggleButtonFav = view.findViewById(R.id.toggle_favorite);
        infoMap = view.findViewById(R.id.shelter_map);
        infoStory = view.findViewById(R.id.info_story);
        infohealth = view.findViewById(R.id.info_health);

        shelterName = view.findViewById(R.id.shelter_name);
        shelterAddress = view.findViewById(R.id.shelter_address);
        shelterEmail = view.findViewById(R.id.shelter_email);
        shelterTelNo = view.findViewById(R.id.shelter_telno);
        shelterFB = view.findViewById(R.id.shelter_fb);
        shelterIG = view.findViewById(R.id.shelter_ig);
        shelterLine = view.findViewById(R.id.shelter_line);
        shelterOwner = view.findViewById(R.id.shelter_owner);

        petItem = new ArrayList<>();
        for(int i=0; i<petProfileList.size(); i++){
            Glide.with(getContext())
                    .load(petProfileList.get(i).getUrl())
                    .into(imageView);
            infoName.setText(petProfileList.get(i).getName());
            infoAge.setText(petProfileList.get(i).getAge());
            infoBreed.setText(petProfileList.get(i).getBreed());
            infoColor.setText(petProfileList.get(i).getColour());
            infoSize.setText(petProfileList.get(i).getSize());
            infoMarking.setText(petProfileList.get(i).getMarking());
            infoChar.setText(petProfileList.get(i).getCharacter());
            infoWeight.setText(petProfileList.get(i).getWeight());
            infoFoundLoc.setText(petProfileList.get(i).getFoundLoc());
            infoStatus.setText(petProfileList.get(i).getStatus());
            infoStory.setText(petProfileList.get(i).getStory());
            shelterID = petProfileList.get(i).getShelterID();
            infohealth.setText(petProfileList.get(i).getHealth());

            if(petProfileList.get(i).getSex().equals("ผู้")){
                imageSex.setImageResource(R.drawable.sex_male);
            }
            else {
                imageSex.setImageResource(R.drawable.sex_female);
            }

            toggleButtonFav.setButtonDrawable(R.drawable.ic_favorite_border_black_24dp);
            final int finalI1 = i;
            toggleButtonFav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if(isChecked){
                        toggleButtonFav.setChecked(true);
                        toggleButtonFav.setButtonDrawable(R.drawable.ic_favorite_red_24dp);
                        saveIntoLike(petProfileList.get(finalI1).getID(), petProfileList.get(finalI1).getRecommend());

                    }
                    else if (!isChecked){
                        toggleButtonFav.setChecked(false);
                        toggleButtonFav.setButtonDrawable(R.drawable.ic_favorite_border_black_24dp);
                        deleteFromLike(petProfileList.get(finalI1).getID());
                    }

                }
            });
            PetSearch petProfile = new PetSearch(petProfileList.get(i).getID(), petProfileList.get(i).getName(),
                    petProfileList.get(i).getType(), petProfileList.get(i).getColour(), petProfileList.get(i).getSex(),
                    petProfileList.get(i).getAge(), petProfileList.get(i).getBreed(), petProfileList.get(i).getSize(), petProfileList.get(i).getUrl(),
                    petProfileList.get(i).getWeight(), petProfileList.get(i).getCharacter(), petProfileList.get(i).getMarking(),
                    petProfileList.get(i).getHealth(), petProfileList.get(i).getFoundLoc(), petProfileList.get(i).getStatus(),
                    petProfileList.get(i).getStory(), petProfileList.get(i).getShelterID(), petProfileList.get(i).getRecommend());
            petItem.add(petProfile);

            final int finalI = i;
            db.collection("User")
                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .collection("Like")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(final QueryDocumentSnapshot document : task.getResult()){
                                    final String ID = document.getId();
                                    Log.d("ID", document.getId());
                                    if(petProfileList.get(finalI).getID().equals(ID)){
//                                        imageFav.setImageResource(R.drawable.ic_favorite_red_24dp);
                                        toggleButtonFav.setButtonDrawable(R.drawable.ic_favorite_red_24dp);
                                    }

                                    toggleButtonFav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                                            if(isChecked){
                                                toggleButtonFav.setChecked(true);
                                                toggleButtonFav.setButtonDrawable(R.drawable.ic_favorite_red_24dp);
                                                saveIntoLike(petProfileList.get(finalI).getID(), petProfileList.get(finalI).getRecommend());

                                            }
                                            else if (!isChecked){
                                                toggleButtonFav.setChecked(false);
                                                toggleButtonFav.setButtonDrawable(R.drawable.ic_favorite_border_black_24dp);
                                                deleteFromLike(petProfileList.get(finalI).getID());
                                            }

                                        }
                                    });
                                }
                            }
                        }
                    });
        }



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

        btnAdopt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new AlertDialog.Builder(getContext());
                builder.setTitle("คุณต้องการอุปการะน้องตัวนี้ใช่หรือไม่");

                builder.setNegativeButton("ไม่ใช่", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AdoptionRegulationFragment adoptionRegulation = new AdoptionRegulationFragment();
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList("petProfile", petItem);

                        adoptionRegulation.setArguments(bundle);
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(getId(), adoptionRegulation);
                        ft.addToBackStack(null).commit();
                    }
//                        });
//                    }
                });
                dialog = builder.create();
                dialog.show();


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
    private void saveIntoLike(String ID, String recommend){
        dataToSave.put("petID", ID);
        dataToSave.put("Rec", recommend);
        db.collection("User")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("Like")
                .document(ID)
                .set(dataToSave);
    }

    private void deleteFromLike(String id) {
        db.collection("User")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("Like")
                .document(id)
                .delete();
    }


}

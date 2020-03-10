package com.example.petping;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ViewRequestShelterFragment extends Fragment {
    private ArrayList<HomeShelter> adoptionList = new ArrayList<>();
    private Button btnAdopter, btnBasicQ, btnPet;
    private ViewFlipper viewFlipper;
    private String uID, petID;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView adoptName, adoptNid, adoptDOB, adoptTel, adoptAddr, adoptJob, adoptSalary;
    private TextView petName, petBreed, petAge, petSex, petColour, petMarking;
    private TextView petWeight, petSize, petCharacter, petFoundLoc;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_view_request_shelter, null);
        if(getArguments() != null){
            adoptionList = (ArrayList<HomeShelter>)getArguments().getSerializable("homeShelter");
        }
        for(int i=0; i<adoptionList.size(); i++){
            uID = adoptionList.get(i).getuID();
            petID = adoptionList.get(i).getPetID();
//            Log.d("AdoptionList", adoptionList.get(i).getuID());
        }
        btnAdopter = view.findViewById(R.id.btn_adopter_info);
        btnBasicQ = view.findViewById(R.id.btn_basic_q);
        btnPet = view.findViewById(R.id.btn_pet_info);
        viewFlipper = view.findViewById(R.id.view_flipper_request);

        adoptName = view.findViewById(R.id.adopter_name);
        adoptNid = view.findViewById(R.id.adopter_nid);
        adoptDOB = view.findViewById(R.id.adopter_dob);
        adoptTel = view.findViewById(R.id.adopter_tel);
        adoptAddr = view.findViewById(R.id.adopter_address);
        adoptJob = view.findViewById(R.id.adopter_job);
        adoptSalary = view.findViewById(R.id.adopter_salary);

        petName = view.findViewById(R.id.pet_name);
        petBreed = view.findViewById(R.id.pet_breed);
        petAge = view.findViewById(R.id.pet_age);
        petSex = view.findViewById(R.id.pet_sex);
        petColour = view.findViewById(R.id.pet_colour);
        petMarking = view.findViewById(R.id.pet_marking);
        petWeight = view.findViewById(R.id.pet_weight);
        petSize = view.findViewById(R.id.pet_size);
        petCharacter = view.findViewById(R.id.pet_character);
        petFoundLoc = view.findViewById(R.id.pet_found_location);

        btnAdopter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(view.findViewById(R.id.adopter_info)));
                db.collection("User")
                        .document(uID)
                        .collection("Information")
                        .document("Information")
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Log.d("Flipper", documentSnapshot.getData().toString());
                                adoptName.setText(documentSnapshot.get("Name").toString());
                                adoptNid.setText(documentSnapshot.get("NID").toString());
                                adoptDOB.setText(documentSnapshot.get("DOB").toString());
                                adoptTel.setText(documentSnapshot.get("TelNo").toString());
                                adoptAddr.setText(documentSnapshot.get("Address").toString());
                                adoptJob.setText(documentSnapshot.get("Job").toString());
                                adoptSalary.setText(documentSnapshot.get("Salary").toString());
                            }
                        });
            }
        });

        btnBasicQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(view.findViewById(R.id.basic_q)));
            }
        });

        btnPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(view.findViewById(R.id.pet_info)));
                db.collection("Pet")
                        .document(petID)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                petName.setText(documentSnapshot.get("Name").toString());
                                petBreed.setText(documentSnapshot.get("Breed").toString());
                                petAge.setText(documentSnapshot.get("Age").toString());
                                petSex.setText(documentSnapshot.get("Sex").toString());
                                petColour.setText(documentSnapshot.get("Color").toString());
                                petMarking.setText(documentSnapshot.get("Marking").toString());
                                petWeight.setText(documentSnapshot.get("Weight").toString());
                                petSize.setText(documentSnapshot.get("Size").toString());
                                petCharacter.setText(documentSnapshot.get("Character").toString());
                                petFoundLoc.setText(documentSnapshot.get("OriginalLocation").toString());
                            }
                        });
            }
        });

        return view;
    }
}

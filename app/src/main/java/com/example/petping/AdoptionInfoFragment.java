package com.example.petping;

import android.content.ContentResolver;
import android.content.Intent;
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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import static android.app.Activity.RESULT_OK;

public class AdoptionInfoFragment extends Fragment {
    private EditText eName, eNID, eDOB, eTel, eAddr, eJob, eSalary;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Button button, btnAdoptImg;
    private ArrayList<PetSearch> petProfileList;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private ImageView imageView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_adoption_info_process, null);
        eName = view.findViewById(R.id.edit_info_name);
        eNID = view.findViewById(R.id.edit_info_nid);
        eDOB = view.findViewById(R.id.edit_info_dob);
        eTel = view.findViewById(R.id.edit_info_tel);
        eAddr = view.findViewById(R.id.edit_info_addr);
        eJob = view.findViewById(R.id.edit_info_job);
        eSalary = view.findViewById(R.id.edit_info_salary);
        button = view.findViewById(R.id.edit_info_btn);

        btnAdoptImg = view.findViewById(R.id.btn_adopt_image);
        imageView = view.findViewById(R.id.image_info);
        if(getArguments() != null){
            petProfileList = (ArrayList<PetSearch>)getArguments().getSerializable("petProfile");

        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = eName.getText().toString();
                String nid = eNID.getText().toString();
                String DOB = eDOB.getText().toString();
                String tel = eTel.getText().toString();
                String addr = eAddr.getText().toString();
                String job = eJob.getText().toString();
                String  salary = eSalary.getText().toString();

                Map<String, Object> data = new HashMap<>();
                data.put("Name", name);
                data.put("NID", nid);
                data.put("DOB", DOB);
                data.put("TelNo", tel);
                data.put("Address", addr);
                data.put("Job", job);
                data.put("Salary", salary);

                db.collection("User")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .collection("Information")
                        .document("Information")
                        .update(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                AdoptionQAFragment adoptionQA = new AdoptionQAFragment();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("petProfile", petProfileList);

                                adoptionQA.setArguments(bundle);
                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.replace(getId(), adoptionQA);
                                ft.commit();
                            }
                        });
            }
        });

        btnAdoptImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                openFileChoose();
            }
        });
        return view;
    }

//    private void openFileChoose() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent, PICK_IMAGE_REQUEST);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
//                && data != null && data.getData() != null){
//            imageUri = data.getData();
//            Picasso.with(getContext()).load(imageUri).into(imageView);
//        }
//    }
//
//    private String getFileExtension(Uri uri) {
//        ContentResolver cR = getActivity().getContentResolver();
//        MimeTypeMap mime = MimeTypeMap.getSingleton();
//        return mime.getExtensionFromMimeType(cR.getType(uri));
//    }

}

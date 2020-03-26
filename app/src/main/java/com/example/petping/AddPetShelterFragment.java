package com.example.petping;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import static android.app.Activity.RESULT_OK;

public class AddPetShelterFragment extends Fragment {

    private RadioGroup typeRdGroup, sexRdGroup, statusRdGroup;
    private RadioButton typeRd, dogRd, catRd, sexRd, maleRd, femaleRd, statusRd, homeRd, waitRd;
    private EditText name, breed, color, marking, age, weight, foundLoc;
    private EditText character, size, story, health;
    private Button btn;
    private String sex, type, status, size1;
    private Map<String, Object> data = new HashMap<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageRef;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageButton btnChooseImg;
    private ImageView imgView;
    private Uri imageUri;
    private String url;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_add_pet_shelter, null);
        btn = view.findViewById(R.id.button);
        name = view.findViewById(R.id.name);
        breed = view.findViewById(R.id.breed);
        color = view.findViewById(R.id.color);
        marking = view.findViewById(R.id.marking);
        age = view.findViewById(R.id.age);
        weight = view.findViewById(R.id.weight);
        foundLoc = view.findViewById(R.id.found_loc);
        character =  view.findViewById(R.id.character);
        size = view.findViewById(R.id.size);
        story = view.findViewById(R.id.story);
        health = view.findViewById(R.id.health);

        sexRdGroup = view.findViewById(R.id.rd_sex);
        maleRd = view.findViewById(R.id.rd_male);
        femaleRd = view.findViewById(R.id.rd_female);

        typeRdGroup = view.findViewById(R.id.rd_type);
        dogRd = view.findViewById(R.id.rd_dog);
        catRd = view.findViewById(R.id.rd_cat);

        statusRdGroup = view.findViewById(R.id.rd_status);
        homeRd = view.findViewById(R.id.rd_find_home);
        waitRd = view.findViewById(R.id.rd_waiting);

        btnChooseImg = view.findViewById(R.id.btn_choose_image);
        imgView = view.findViewById(R.id.img_view);
        storageRef = FirebaseStorage.getInstance().getReference();

        if(weight.getText().toString().equals("1") || weight.getText().toString().equals("2") ||
                weight.getText().toString().equals("3") || weight.getText().toString().equals("4") ||
                weight.getText().toString().equals("5")){
            size.setText("S");
            size1 = "S";
        }
        if(weight.getText().toString().equals("6") || weight.getText().toString().equals("7") ||
                weight.getText().toString().equals("8") || weight.getText().toString().equals("9") ||
                weight.getText().toString().equals("10")){
            size.setText("M");
            size1 = "M";
        }
        if(weight.getText().toString().compareTo("11") > 0){
            size.setText("L");
            size1 = "L";
        }

        btnChooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChoose();
            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioSex = sexRdGroup.getCheckedRadioButtonId();
                sexRd = view.findViewById(radioSex);
                if(sexRd == view.findViewById(R.id.rd_male)){
                    sex = maleRd.getText().toString();
                }
                else if(sexRd == view.findViewById(R.id.rd_female)){
                    sex = femaleRd.getText().toString();
                }

                int radioType = typeRdGroup.getCheckedRadioButtonId();
                typeRd = view.findViewById(radioType);
                if(typeRd == view.findViewById(R.id.rd_dog)){
                    type = dogRd.getText().toString();
                }
                else if(typeRd == view.findViewById(R.id.rd_cat)){
                    type = catRd.getText().toString();
                }

                int radioStatus = statusRdGroup.getCheckedRadioButtonId();
                statusRd = view.findViewById(radioStatus);
                if(statusRd == view.findViewById(R.id.rd_find_home)){
                    status = homeRd.getText().toString();
                }
                else if(statusRd == view.findViewById(R.id.rd_waiting)){
                    status = waitRd.getText().toString();
                }

                if(age.getText().toString().isEmpty() || breed.getText().toString().isEmpty() || character.getText().toString().isEmpty() ||
                        color.getText().toString().isEmpty() ||  health.getText().toString().isEmpty() || marking.getText().toString().isEmpty() ||
                        name.getText().toString().isEmpty() || foundLoc.getText().toString().isEmpty() || sex.isEmpty() || status.isEmpty() ||
                        story.getText().toString().isEmpty() || type.isEmpty() || imageUri == null || weight.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "กรุณากรอกข้อมูลให้ครบถ้วนค่ะ", Toast.LENGTH_LONG).show();
                }
                if(imageUri != null){
                    final StorageReference fileReference = storageRef.child(name.getText().toString() + "." + getFileExtension(imageUri));
//                    Log.d("URI", fileReference.getPath());
                    fileReference.putFile(imageUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            data.put("Age", age.getText().toString());
                                            data.put("Breed", breed.getText().toString());
                                            data.put("Character", character.getText().toString());
                                            data.put("Color", color.getText().toString());
                                            data.put("Health", health.getText().toString());
                                            data.put("Marking", marking.getText().toString());
                                            data.put("Name", name.getText().toString());
                                            data.put("OriginalLocation", foundLoc.getText().toString());
                                            data.put("Sex", sex);
                                            data.put("Size", size.getText().toString());
                                            data.put("Status", status);
                                            data.put("Story", story.getText().toString());
                                            data.put("Type", type);
                                            data.put("Weight", weight.getText().toString());
                                            data.put("Image", uri.toString());
                                            db.collection("Pet").document().set(data);
                                        }
                                    });
                                }
                            });
                }
            }
        });
        return view;
    }

    private void openFileChoose() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            imageUri = data.getData();
            Picasso.with(getContext()).load(imageUri).resize(500, 500).centerCrop().into(imgView);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

}

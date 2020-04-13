package com.example.petping;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import static android.app.Activity.RESULT_OK;

public class AddPetShelterFragment extends Fragment {

    private RadioGroup typeRdGroup, sexRdGroup, statusRdGroup;
    private RadioButton typeRd, dogRd, catRd, sexRd, maleRd, femaleRd, statusRd, homeRd, waitRd;
    private EditText name, breed, marking, age, weight, foundLoc;
    private EditText character, size, story, health;
    private Spinner spinColor;
    private Button btn;
    private String sex, type, color, size1;
    private Map<String, Object> data = new HashMap<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageRef;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageButton btnChooseImg;
    private ImageView imgView;
    private Uri imageUri;

    private AlertDialog dialog;
    private AlertDialog.Builder builder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_add_pet_shelter, null);
        btn = view.findViewById(R.id.button);
        name = view.findViewById(R.id.name);
        breed = view.findViewById(R.id.breed);
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
//
//        statusRdGroup = view.findViewById(R.id.rd_status);
//        homeRd = view.findViewById(R.id.rd_find_home);
//        waitRd = view.findViewById(R.id.rd_waiting);

        btnChooseImg = view.findViewById(R.id.btn_choose_image);
        imgView = view.findViewById(R.id.img_view);
        storageRef = FirebaseStorage.getInstance().getReference();

//        spinColor = view.findViewById(R.id.color);
//        ArrayAdapter<CharSequence> colorAdapter = ArrayAdapter.createFromResource(getContext(), R.array.color_array, android.R.layout.simple_spinner_item);
//        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinColor.setAdapter(colorAdapter);

        spinColor = view.findViewById(R.id.color);
        final CollectionReference collection = db.collection("Pet");
        final List<String> colorList = new ArrayList<>();
        final ArrayAdapter<String> colorAdapter =  new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, colorList );
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinColor.setAdapter(colorAdapter);
        collection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String color = document.getString("Color");
                    colorList.add(color);
                }
                Set<String> set = new HashSet<>(colorList);
                colorList.clear();
                colorList.addAll(set);
                colorList.add(0,"เลือกสีของน้อง");
                colorAdapter.notifyDataSetChanged();
            }
        });

        btnChooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChoose();
            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                color = spinColor.getSelectedItem().toString();
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

//                final CollectionReference collection = db.collection("Pet");
//                final List<String> colorList = new ArrayList<>();
//                final ArrayAdapter<String> colorAdapter =  new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, colorList );
//                colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                color.setAdapter(colorAdapter);
//                collection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        for (QueryDocumentSnapshot document : task.getResult()) {
//                            String color = document.getString("Color");
//                            colorList.add(color);
//                        }
//                        Set<String> set = new HashSet<>(colorList);
//                        colorList.clear();
//                        colorList.addAll(set);
////                        colorList.add(0,"ไม่ระบุ");
//                        colorAdapter.notifyDataSetChanged();
//                    }
//                });

//                int radioStatus = statusRdGroup.getCheckedRadioButtonId();
//                statusRd = view.findViewById(radioStatus);
//                if(statusRd == view.findViewById(R.id.rd_find_home)){
//                    status = homeRd.getText().toString();
//                }
//                else if(statusRd == view.findViewById(R.id.rd_waiting)){
//                    status = waitRd.getText().toString();
//                }

//                color.getText().toString().isEmpty()
                if(age.getText().toString().isEmpty() && breed.getText().toString().isEmpty() && character.getText().toString().isEmpty() &&
                        health.getText().toString().isEmpty() && marking.getText().toString().isEmpty() &&
                        name.getText().toString().isEmpty() && foundLoc.getText().toString().isEmpty() && sex == null &&
                        story.getText().toString().isEmpty() && type == null && imageUri == null && weight.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "กรุณากรอกข้อมูลให้ครบถ้วนค่ะ", Toast.LENGTH_LONG).show();
                }
                else if(imageUri == null){
                    Toast.makeText(getContext(), "กรุณาเลือกรูปภาพสัตว์ค่ะ", Toast.LENGTH_LONG).show();
                }
                else if(name.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "กรุณาระบุชื่อสัตว์ค่ะ", Toast.LENGTH_LONG).show();
                }
                else if(type == null){
                    Toast.makeText(getContext(), "กรุณาเลือกประเภทสัตว์ค่ะ", Toast.LENGTH_LONG).show();
                }
                else if(sex == null){
                    Toast.makeText(getContext(), "กรุณาเลือกเพศสัตว์ค่ะ", Toast.LENGTH_LONG).show();
                }
                else if(breed.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "กรุณาระบุสายพันธุ์สัตว์ค่ะ", Toast.LENGTH_LONG).show();
                }
                else if(color == null){
                    Toast.makeText(getContext(), "กรุณาระบุลสีสัตว์ค่ะ", Toast.LENGTH_LONG).show();
                }
                else if(marking.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "กรุณาระบุจุดเด่นสัตว์", Toast.LENGTH_LONG).show();
                }
                else if(age.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "กรุณาระบุอายุสัตว์ค่ะ", Toast.LENGTH_LONG).show();
                }
                else if(health.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "กรุณาระบุสุขภาพสัตว์ค่ะ", Toast.LENGTH_LONG).show();
                }
                else if(foundLoc.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "กรุณาระบุสถานที่พบสัตว์ค่ะ", Toast.LENGTH_LONG).show();
                }
                else if(character.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "กรุณาระบุลักษณะเฉพาะสัตว์ค่ะ", Toast.LENGTH_LONG).show();
                }
                else if(story.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "กรุณาระบุเรื่องราวสัตว์ค่ะ", Toast.LENGTH_LONG).show();
                }
                else{
                    final StorageReference fileReference = storageRef.child(name.getText().toString() + "." + getFileExtension(imageUri));
//                    Log.d("URI", fileReference.getPath());
                    fileReference.putFile(imageUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            if(type.equals("สุนัข")){
                                                if(weight.getText().toString().equals("1") || weight.getText().toString().equals("2") ||
                                                        weight.getText().toString().equals("3") || weight.getText().toString().equals("4") ||
                                                        weight.getText().toString().equals("5")){
                                                    size.setText("S");
                                                    size1 = "S";
                                                }
                                                else if(weight.getText().toString().equals("6") || weight.getText().toString().equals("7") ||
                                                        weight.getText().toString().equals("8") || weight.getText().toString().equals("9") ||
                                                        weight.getText().toString().equals("10")){
                                                    size.setText("M");
                                                    size1 = "M";
                                                }
                                                else if(weight.getText().toString().compareTo("11") > 0){
                                                    size.setText("L");
                                                    size1 = "L";
                                                }
                                            }
                                            else if(type.equals("แมว")){
                                                if(weight.getText().toString().equals("1") || weight.getText().toString().equals("2") || weight.getText().toString().equals("3") ){
                                                    size.setText("S");
                                                    size1 = "S";
                                                }
                                                else if(weight.getText().toString().equals("4") || weight.getText().toString().equals("5") || weight.getText().toString().equals("6")){
                                                    size.setText("M");
                                                    size1 = "M";
                                                }
                                                else if(weight.getText().toString().compareTo("6") > 0){
                                                    size.setText("L");
                                                    size1 = "L";
                                                }
                                            }

                                            data.put("Age", age.getText().toString());
                                            data.put("Breed", breed.getText().toString());
                                            data.put("Character", character.getText().toString());
//                                            data.put("Color", color.getText().toString());
                                            data.put("Health", health.getText().toString());
                                            data.put("Marking", marking.getText().toString());
                                            data.put("Name", name.getText().toString());
                                            data.put("OriginalLocation", foundLoc.getText().toString());
                                            data.put("Sex", sex);
                                            data.put("Size", size1);
                                            data.put("Status", "กำลังหาบ้าน");
                                            data.put("Story", story.getText().toString());
                                            data.put("Type", type);
                                            data.put("Weight", weight.getText().toString());
                                            data.put("Image", uri.toString());
                                            data.put("ShelterID", FirebaseAuth.getInstance().getCurrentUser().getUid());

                                            builder = new AlertDialog.Builder(getContext());
                                            builder.setTitle("คุณต้องการเพิ่มข้อมูลสัตว์ใช่หรือไม่");

                                            builder.setNegativeButton("ไม่ใช่", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            });

                                            builder.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    db.collection("Pet")
                                                            .document()
                                                            .set(data)
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

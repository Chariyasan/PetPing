package com.example.petping;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import static android.app.Activity.RESULT_OK;

public class EditMenuShelterFragment extends Fragment {
    private ImageView image;
    private Button btnSave;
    private ImageButton btnImage;
    private TextView email;
    private EditText name, owner, license, address, telNo, facebook, ig, lineID;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri Uri;
    private StorageReference storageRef;
    private Map<String, Object> data = new HashMap<>();
    private Map<String, Object> data1 = new HashMap<>();

    private AlertDialog dialog;
    private AlertDialog.Builder builder;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_edit_shelter, null);
        image = view.findViewById(R.id.shelter_img_profile);
        btnImage = view.findViewById(R.id.btn_img);
        btnSave = view.findViewById(R.id.btn_save);
        storageRef = FirebaseStorage.getInstance().getReference();

        email = view.findViewById(R.id.edit_shelter_email);
        name = view.findViewById(R.id.edit_shelter_name);
        owner = view.findViewById(R.id.edit_shelter_owner);
        license = view.findViewById(R.id.edit_shelter_id);
        address = view.findViewById(R.id.edit_shelter_addr);
        telNo = view.findViewById(R.id.edit_shelter_tel);
        facebook = view.findViewById(R.id.edit_shelter_fb);
        ig = view.findViewById(R.id.edit_shelter_ig);
        lineID = view.findViewById(R.id.edit_shelter_line);

        db.collection("Shelter")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Glide.with(getContext())
                                .load(documentSnapshot.get("Image").toString())
                                .into(image);
                        name.setText(documentSnapshot.get("Name").toString());
                        owner.setText(documentSnapshot.get("Owner").toString());
                        license.setText(documentSnapshot.get("License").toString());
                        address.setText(documentSnapshot.get("Address").toString());
                        email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                        telNo.setText(documentSnapshot.get("TelNo").toString());
                        facebook.setText(documentSnapshot.get("Facebook").toString());
                        ig.setText(documentSnapshot.get("Instagram").toString());
                        lineID.setText(documentSnapshot.get("LineID").toString());
                    }
                });

        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Uri != null){
                    final StorageReference fileReference = storageRef.child(name.getText().toString() + "." + getFileExtension(Uri));
                    fileReference.putFile(Uri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<android.net.Uri>() {
                                        @Override
                                        public void onSuccess(android.net.Uri uri) {
                                            data.put("Name", name.getText().toString());
                                            data.put("Owner", owner.getText().toString());
                                            data.put("Address", address.getText().toString());
                                            data.put("TelNo", telNo.getText().toString());
                                            data.put("License", license.getText().toString());
                                            data.put("Facebook", facebook.getText().toString());
                                            data.put("Instagram", ig.getText().toString());
                                            data.put("LineID", lineID.getText().toString());
                                            data.put("Image", uri.toString());

                                            builder = new AlertDialog.Builder(getContext());
                                            builder.setTitle("คุณต้องการแก้ไขข้อมูลใช่หรือไม่");
                                            builder.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                db.collection("Shelter")
                                                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                        .update(data)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                                                ft.replace(getId(), new MenuShelterFragment());
                                                                ft.commit();
                                                            }
                                                        });
                                                }
                                            });
                                            builder.setNegativeButton("ไม่ใช่", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            });
                                            dialog = builder.create();
                                            dialog.show();

                                        }
                                    });
                                }
                            });

                }
                else {
                    data1.put("Name", name.getText().toString());
                    data1.put("Owner", owner.getText().toString());
                    data1.put("Address", address.getText().toString());
                    data1.put("TelNo", telNo.getText().toString());
                    data1.put("License", license.getText().toString());
                    data1.put("Facebook", facebook.getText().toString());
                    data1.put("Instagram", ig.getText().toString());
                    data1.put("LineID", lineID.getText().toString());

                    builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("คุณต้องการแก้ไขข้อมูลใช่หรือไม่");
                    builder.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            db.collection("Shelter")
                                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .update(data1)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                                            ft.replace(getId(), new MenuShelterFragment());
                                            ft.commit();
                                        }
                                    });

                        }
                    });
                    builder.setNegativeButton("ไม่ใช่", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dialog = builder.create();
                    dialog.show();


                }

            }
        });
        return view;
    }

    private void chooseImage() {
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
            Uri = data.getData();
            Picasso.with(getContext()).load(Uri).into(image);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
}

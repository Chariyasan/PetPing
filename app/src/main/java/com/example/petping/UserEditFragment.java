package com.example.petping;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

public class UserEditFragment extends Fragment {
    private EditText userName, telNo, password;
    private Button btnSave;
    private ImageButton btnImage;
    private ImageView image;
    private String name;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final int PICK_IMAGE_REQUEST = 1;
    private android.net.Uri Uri;
    private StorageReference storageRef;
    private Map<String, Object> data = new HashMap<>();
    private Map<String, Object> data1 = new HashMap<>();
    private AlertDialog dialog;
    private AlertDialog.Builder builder;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_edit, container, false);
        userName = view.findViewById(R.id.userName);
        telNo = view.findViewById(R.id.telNO);
        password = view.findViewById(R.id.password);
        image = view.findViewById(R.id.image);
        storageRef = FirebaseStorage.getInstance().getReference();
        btnSave = view.findViewById(R.id.btn_save);
        btnImage = view.findViewById(R.id.btn_img);

        db.collection("User")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("Information")
                .document("Information")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Glide.with(getContext())
                                .load(documentSnapshot.get("Image"))
                                .into(image);
                        userName.setText(documentSnapshot.get("UserName").toString());
                        telNo.setText(documentSnapshot.get("TelNo").toString());
                        name = documentSnapshot.get("Name").toString();
                    }
                });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Uri != null){
                    final StorageReference fileReference = storageRef.child(name + "." + getFileExtension(Uri));
                    fileReference.putFile(Uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<android.net.Uri>() {
                                @Override
                                public void onSuccess(android.net.Uri uri) {
                                    data.put("TelNo", telNo.getText().toString());
                                    data.put("UserName", userName.getText().toString());
                                    data.put("Image", uri.toString());
                                    db.collection("User")
                                            .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .collection("Information")
                                            .document("Information")
                                            .update(data)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    builder = new AlertDialog.Builder(getContext());
                                                    builder.setTitle("คุณต้องการแก้ไขข้อมูลใช่หรือไม่");

                                                    builder.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                                                            ft.replace(getId(),  new MenuFragment());
                                                            ft.commit();
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
                    });

                }
                else{
                    data1.put("TelNo", telNo.getText().toString());
                    data1.put("UserName", userName.getText().toString());
                    db.collection("User")
                            .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .collection("Information")
                            .document("Information")
                            .update(data1)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    builder = new AlertDialog.Builder(getContext());
                                    builder.setTitle("คุณต้องการแก้ไขข้อมูลใช่หรือไม่");

                                    builder.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                                            ft.replace(getId(),  new MenuFragment());
                                            ft.commit();
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
            }
        });

        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

//        btnUserName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), PopUpEditUserNameActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        btnName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), PopUpEditNameActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        btnTel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), PopUpEditTelActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        btnJob.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), PopUpEditJobActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        btnAddr.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), PopUpEditAddressActivity.class);
//                startActivity(intent);
//            }
//        });

//        edit_back_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.replace(getId(), new MenuFragment());
//                ft.commit();
//            }
//        });
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

    private String getFileExtension(android.net.Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

}
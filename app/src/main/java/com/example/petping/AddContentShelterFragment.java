package com.example.petping;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
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

public class AddContentShelterFragment extends Fragment {
    //    private EditText topic, story, tag;
    private EditText topic, story;
    private Button btnSave;
    private ImageButton btnImage;
    private ImageView image;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageRef;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Map<String, Object> data = new HashMap<>();
    private Uri Uri;

    private AlertDialog dialog;
    private AlertDialog.Builder builder;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_content_shelter, null);
        topic = view.findViewById(R.id.topic);
        story = view.findViewById(R.id.story);
        image = view.findViewById(R.id.img_view);
//        tag = view.findViewById(R.id.tag);
        btnSave = view.findViewById(R.id.button);
        btnImage = view.findViewById(R.id.btn_choose_image);

        storageRef = FirebaseStorage.getInstance().getReference();

        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(topic.getText().toString().isEmpty() && story.getText().toString().isEmpty() && Uri == null){
                    Toast.makeText(getContext(), "กรุณากรอกข้อมูลให้ครบถ้วนค่ะ", Toast.LENGTH_LONG).show();
                }
                if(topic.getText().toString().isEmpty() ){
                    Toast.makeText(getContext(), "กรุณาระบุหัวข้อบทความค่ะ", Toast.LENGTH_LONG).show();
                }
                if(story.getText().toString().isEmpty() ){
                    Toast.makeText(getContext(), "กรุณาระบุเนื้อหาบทความค่ะ", Toast.LENGTH_LONG).show();
                }
                if(Uri == null){
                    Toast.makeText(getContext(), "กรุณาใส่รูปบทความค่ะ", Toast.LENGTH_LONG).show();
                }
                if(Uri != null){
                    final StorageReference fileReference = storageRef.child(System.currentTimeMillis() + "." + getFileExtension(Uri));
                    fileReference.putFile(Uri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<android.net.Uri>() {
                                        @Override
                                        public void onSuccess(android.net.Uri uri) {
                                            data.put("Topic", topic.getText().toString());
                                            data.put("Story", story.getText().toString());
                                            data.put("URL", uri.toString());
                                            data.put("ShelterID", FirebaseAuth.getInstance().getCurrentUser().getUid());
//                                            data.put("Tag", tag.getText().toString());

                                            builder = new AlertDialog.Builder(getContext());
                                            builder.setTitle("คุณต้องการเพิ่มข้อมูลบทความใช่หรือไม่");

                                            builder.setNegativeButton("ไม่ใช่", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            });
                                            builder.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    db.collection("Content")
                                                            .document()
                                                            .set(data)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                                                                    ft.replace(getId(), new ManageContentShelterFragment());
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
            Picasso.with(getContext()).load(Uri).resize(500, 500).centerCrop().into(image);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
}

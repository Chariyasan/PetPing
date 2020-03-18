package com.example.petping;

import android.content.ContentResolver;
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
import android.widget.ImageView;

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

import static android.app.Activity.RESULT_OK;

public class AddContentShelterFragment extends Fragment {
    private EditText topic, story;
    private Button btnSave, btnImage;
    private ImageView image;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageRef;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Map<String, Object> data = new HashMap<>();
    private Uri Uri;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_content_shelter, null);
        topic = view.findViewById(R.id.topic);
        story = view.findViewById(R.id.story);
        image = view.findViewById(R.id.img_view);
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
                final StorageReference fileReference = storageRef.child(System.currentTimeMillis() + "." + getFileExtension(Uri));

                fileReference.putFile(Uri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<android.net.Uri>() {
                                    @Override
                                    public void onSuccess(android.net.Uri uri) {
                                        data.put("Topic", topic);
                                        data.put("Story", story);
                                        data.put("Image", uri.toString());
                                        data.put("AuthorID", FirebaseAuth.getInstance().getCurrentUser().getUid());

                                    }
                                });
                            }
                        });
                Log.d("URI", fileReference.getPath());
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

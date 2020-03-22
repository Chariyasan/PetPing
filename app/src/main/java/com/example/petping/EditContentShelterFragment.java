package com.example.petping;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import static android.app.Activity.RESULT_OK;

public class EditContentShelterFragment extends Fragment{
    private ArrayList<Content> contentList = new ArrayList<>();
    private Button btnImage, btn;
    private ImageView image;
    private EditText topic, story;
    private static final int PICK_IMAGE_REQUEST = 1;
    private android.net.Uri Uri;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageRef;
    private Map<String, Object> data = new HashMap<>();
    private String ID;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_content_shelter, null);
//        if(getArguments() != null){
//            contentList = <ArrayList<? extends Parcelable> getArguments().getParcelableArrayList("contentEdit");
//        }
//        Bundle bundle = getActivity().getIntent().getExtras();

        if (getArguments() != null) {
            contentList  = getArguments().getParcelableArrayList("contentEdit");
        }


        btnImage = view.findViewById(R.id.btn_img);
        btn = view.findViewById(R.id.button);
        topic = view.findViewById(R.id.topic);
        story = view.findViewById(R.id.story);
        storageRef = FirebaseStorage.getInstance().getReference();
        image = view.findViewById(R.id.image);
        for(int i=0; i<contentList.size(); i++){
            Glide.with(getContext())
                    .load(contentList.get(i))
                    .into(image);
            topic.setText(contentList.get(i).getTopic());
            story.setText(contentList.get(i).getStory());
            ID = contentList.get(i).getID();
        }

        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
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
                                        data.put("Topic", topic.getText().toString());
                                        data.put("Story", story.getText().toString());
                                        data.put("URL", uri.toString());
                                        db.collection("Content")
                                                .document(ID)
                                                .update(data)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                                                        ft.replace(getId(),  new ContentShelterFragment());
                                                        ft.commit();
                                                    }
                                                });
                                    }
                                });
                            }
                        });
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

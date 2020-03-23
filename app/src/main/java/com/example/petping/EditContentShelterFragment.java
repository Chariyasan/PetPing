package com.example.petping;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
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
    private ArrayList<Content> contentL = new ArrayList<>();
    private Button btnImage, btn;
    private ImageView image;
    private EditText topic, story;
    private static final int PICK_IMAGE_REQUEST = 1;
    private android.net.Uri Uri;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageRef;
    private Map<String, Object> data = new HashMap<>();
    private Map<String, Object> data1 = new HashMap<>();
    private String ID, tag, author, authorName, imageUrl;

    private AlertDialog dialog;
    private AlertDialog.Builder builder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_content_shelter, null);
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
                    .load(contentList.get(i).getUrl())
                    .into(image);
            topic.setText(contentList.get(i).getTopic());
            story.setText(contentList.get(i).getStory());
            ID = contentList.get(i).getID();
            tag = contentList.get(i).getTag();
            author = contentList.get(i).getAuthorID();
            authorName = contentList.get(i).getAuthorName();
            imageUrl = contentList.get(i).getUrl();
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
                if(Uri != null){
                    final StorageReference fileReference = storageRef.child(System.currentTimeMillis() + "." + getFileExtension(Uri));
                    fileReference.putFile(Uri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<android.net.Uri>() {
                                        @Override
                                        public void onSuccess(final android.net.Uri uri) {
                                            data.put("Topic", topic.getText().toString());
                                            data.put("Story", story.getText().toString());
                                            data.put("URL", uri.toString());
                                            db.collection("Content")
                                                    .document(ID)
                                                    .update(data)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Content content = new Content(ID, topic.getText().toString(),
                                                                    story.getText().toString(), uri.toString(), tag, author, authorName);
                                                            contentL.add(content);

                                                            builder = new AlertDialog.Builder(getContext());
                                                            builder.setTitle("คุณต้องการแก้ไขข้อมูลใช่หรือไม่");

                                                            builder.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    ContentShelterFragment contentShelterFragment = new  ContentShelterFragment();
                                                                    Bundle bundle = new Bundle();
                                                                    bundle.putParcelableArrayList("contentInfo", contentL);
                                                                    contentShelterFragment.setArguments(bundle);
                                                                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                                                                    ft.replace(getId(),  contentShelterFragment);
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
                    data1.put("Topic", topic.getText().toString());
                    data1.put("Story", story.getText().toString());

                    db.collection("Content")
                            .document(ID)
                            .update(data1)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Content content = new Content(ID, topic.getText().toString(),
                                            story.getText().toString(), imageUrl, tag, author, authorName);
                                    contentL.add(content);

                                    builder = new AlertDialog.Builder(getContext());
                                    builder.setTitle("คุณต้องการแก้ไขข้อมูลใช่หรือไม่");

                                    builder.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ContentShelterFragment contentShelterFragment = new  ContentShelterFragment();
                                            Bundle bundle = new Bundle();
                                            bundle.putParcelableArrayList("contentInfo", contentL);
                                            contentShelterFragment.setArguments(bundle);
                                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                                            ft.replace(getId(),  contentShelterFragment);
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

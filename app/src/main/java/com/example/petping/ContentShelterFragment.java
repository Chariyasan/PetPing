package com.example.petping;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class ContentShelterFragment extends Fragment {
    private ArrayList<Content> contentList;
    private ArrayList<Content> contentL = new ArrayList<>();
    private ImageView image;
    private String ID;
    private TextView topic, story;
    private Button btnEdit, btnDelete;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private AlertDialog dialog;
    private AlertDialog.Builder builder;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_content_shelter, null);
        if(getArguments() != null){
            contentList = getArguments().getParcelableArrayList("contentInfo");
        }

        image = view.findViewById(R.id.image);
        topic = view.findViewById(R.id.topic);
        story = view.findViewById(R.id.story);
        btnEdit = view.findViewById(R.id.edit_info);
        btnDelete = view.findViewById(R.id.delete_info);
        for (int i=0; i<contentList.size(); i++){
            ID = contentList.get(i).getID();
        }

        db.collection("Content")
                .document(ID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Glide.with(getContext())
                                .load(documentSnapshot.get("URL"))
                                .into(image);
                        topic.setText(documentSnapshot.get("Topic").toString());
                        story.setText(documentSnapshot.get("Story").toString());
                        Content content = new Content(documentSnapshot.getId(), documentSnapshot.get("Topic").toString(),
                                documentSnapshot.get("Story").toString(), documentSnapshot.get("URL").toString(),
                                documentSnapshot.get("Tag").toString(), documentSnapshot.get("AuthorID").toString(),
                                documentSnapshot.get("AuthorName").toString());
                        contentL.add(content);
                    }
                });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("content1", contentL.toString());
                EditContentShelterFragment editContentShelterFragment = new  EditContentShelterFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("contentEdit", contentL);
                editContentShelterFragment.setArguments(bundle);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(getId(),  editContentShelterFragment);
                ft.addToBackStack(null).commit();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new AlertDialog.Builder(getContext());
                builder.setTitle("คุณต้องการลบบทความนี้ใช่หรือไม่");

                builder.setNegativeButton("ไม่ใช่", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.collection("Content")
                                .document(ID)
                                .delete()
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
        return view;
    }
}

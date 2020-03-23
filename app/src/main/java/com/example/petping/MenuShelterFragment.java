package com.example.petping;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MenuShelterFragment extends Fragment {
    private TextView name, owner, license, address, email, telNo;
    private TextView facebook, instagram, lineID;
    private ImageView image;
    private Button btn;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_shelter, null);
        name = view.findViewById(R.id.name);
        owner = view.findViewById(R.id.owner);
        license = view.findViewById(R.id.license);
        address = view.findViewById(R.id.address);
        email = view.findViewById(R.id.email);
        telNo = view.findViewById(R.id.tel_no);
        facebook = view.findViewById(R.id.facebook);
        instagram = view.findViewById(R.id.instagram);
        lineID = view.findViewById(R.id.line);
        image = view.findViewById(R.id.image);
        btn = view.findViewById(R.id.btn_edit);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(getId(), new EditMenuShelterFragment());
                ft.commit();
            }
        });

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
                        instagram.setText(documentSnapshot.get("Instagram").toString());
                        lineID.setText(documentSnapshot.get("LineID").toString());
                    }
                });
        return view;
    }
}

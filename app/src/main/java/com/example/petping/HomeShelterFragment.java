package com.example.petping;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeShelterFragment extends Fragment {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String ID;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_shelter, null);
        final List<String> petIDList = new ArrayList<>();
        db.collection("Pet")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ID = document.getId();
//                                Log.d("IdTest", ID);
                                petIDList.add(ID);
                            }
                            showDetail(petIDList);
                        } else {
                            Log.d("Error", "Error getting documents: ", task.getException());
                        }
                    }

                });



//        db.collection("Pet")
//                .whereEqualTo("Status", "อยู่ระหว่างดำเนินการ")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        db.collection("User")
//                                .document()
//                                .collection("Information")
//                                .document("Adoption")
//                                .collection("PetList")
//                                .get()
//                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                        for (QueryDocumentSnapshot document : task.getResult()) {
//                                            Log.d("Name", document.get("petName").toString());
//                                        }
//                                    }
//                                });
//                    }
//                });
        return view;
    }

    private void showDetail(List<String> petIDList) {
//        UserRecord userRecord = FirebaseAuth.getInstance().getUser(uid);
        for (int i=0; i<petIDList.size(); i++){
//            Log.d("pet", petIDList.get(i));
            db.collection("RequestAdoption")
                .document()
                .collection("Adoption")
                .whereEqualTo("petID", petIDList.get(i))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("Name", document.get("petName").toString());
                            }
                        }
                    }
                });
        }
    }
}

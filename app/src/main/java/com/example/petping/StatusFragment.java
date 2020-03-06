package com.example.petping;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class StatusFragment extends Fragment {
    private ListView listView;
    private PetStatusAdapter petAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<PetSearch> petList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_status, null);
        listView = view.findViewById(R.id.listView_status);

        db.collection("RequestAdoption")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("Adoption")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if(task.isSuccessful()){
                                PetSearch petSearch = new PetSearch(document.get("petID").toString(), document.get("petName").toString(), document.get("petType").toString(),
                                        document.get("petColor").toString(), document.get("petSex").toString(), document.get("petAge").toString(),
                                        document.get("petBreed").toString(), document.get("petSize").toString(), document.get("petURL").toString(),
                                        document.get("petWeight").toString(), document.get("petCharacter").toString(), document.get("petMarking").toString(),
                                        document.get("petHealth").toString(), document.get("petFoundLoc").toString(), document.get("petStatus").toString(),
                                        document.get("petStory").toString());
                                petList.add(petSearch);
                                petAdapter = new PetStatusAdapter(getContext(), petList);
                                listView.setAdapter(petAdapter);
                            }
                        }
                    }
                });

       return view;
    }

}

package com.example.petping;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class StatusFragment extends Fragment {
    private ListView listView;
    private PetStatusAdapter petAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<Status> petList = new ArrayList<>();
    private ArrayList<Status> statusList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_status, null);
        listView = view.findViewById(R.id.listView_status);
        if(!petList.isEmpty()){
            petList.clear();
        }
        db.collection("RequestAdoption")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("Adoption")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if(task.isSuccessful()){
                                Status petSearch = new Status(document.get("petID").toString(), document.get("petName").toString(), document.get("petType").toString(),
                                        document.get("petColor").toString(), document.get("petSex").toString(), document.get("petAge").toString(),
                                        document.get("petBreed").toString(), document.get("petSize").toString(), document.get("petURL").toString(),
                                        document.get("petWeight").toString(), document.get("petCharacter").toString(), document.get("petMarking").toString(),
                                        document.get("petHealth").toString(), document.get("petFoundLoc").toString(), document.get("petStatus").toString(),
                                        document.get("petStory").toString(), document.get("ShelterID").toString(), document.get("DateTime").toString());
                                petList.add(petSearch);

                                petAdapter = new PetStatusAdapter(getContext(), petList);
                                petAdapter.sortStatus();
                                listView.setAdapter(petAdapter);

                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        ViewStatusFragment viewStatus = new ViewStatusFragment();
                                        Bundle bundle = new Bundle();
                                        statusList.add(petList.get(position));
                                        bundle.putParcelableArrayList("viewStatus", statusList);

                                        viewStatus.setArguments(bundle);
                                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                                        ft.replace(getId(), viewStatus);
                                        ft.addToBackStack(null).commit();
                                    }
                                });
                            }
                        }
                    }
                });

       return view;
    }

}

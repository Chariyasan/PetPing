package com.example.petping;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
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
import java.util.List;

public class HomeShelterFragment extends Fragment {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ListView listView;
    private HomeShelterAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_shelter, null);
        listView = view.findViewById(R.id.listView);

        final List<String> value = new ArrayList<>();
        db.collection("User1")
                .document("userID")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        for ( Object key : documentSnapshot.getData().values() ) {
                            value.add(key.toString());

                        }
                      showDetail(value);
                    }
                });

        return view;
    }

    private void showDetail(final List<String> value) {
        final List<HomeShelter> homeList = new ArrayList<>();
        for (int i=0; i<value.size(); i++){
            Log.d("uid", value.get(i));
            final int finalI1 = i;
            db.collection("RequestAdoption")
                    .document(value.get(i))
                    .collection("Adoption")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("name", document.get("UserName").toString());
                                HomeShelter homeShelter = new HomeShelter(value.get(finalI1), document.getId(), document.get("UserName").toString(),
                                        document.get("petName").toString(), document.get("petStatus").toString());
                                homeList.add(homeShelter);
                                adapter = new HomeShelterAdapter(getContext(), homeList);
                                listView.setAdapter(adapter);
                            }
                        }
                    });
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ArrayList<HomeShelter> homeShelter = new ArrayList<>();
                    ViewRequestShelterFragment viewRequest = new ViewRequestShelterFragment();
                    Bundle bundle = new Bundle();
                    homeShelter.add(homeList.get(position));
                    bundle.putSerializable("homeShelter", homeShelter);

                    viewRequest.setArguments(bundle);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(getId(), viewRequest);
                    ft.commit();
                }
            });
        }

//        db.collection("Pet")
//        .get()
//        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        ID = document.getId();
//                        Log.d("IdTest", ID);
//                        petIDList.add(ID);
//                    }
//                    for(int j=0; j<value.size(); j++){
//                        for (int i=0; i<petIDList.size(); i++){
//
//                            db.collection("RequestAdoption")
//                                    .document()
//                                    .collection("Adoption")
//                                    .document()
//                                    .get()
//                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                                        @Override
//                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
//                                            if(documentSnapshot.exists()){
//                                                Log.d("Test", documentSnapshot.get("petName").toString());
//                                            }
//
//                                        }
//                                    });
//                            db.collection("RequestAdoption")
//                                    .document(value.get(j))
//                                    .collection("Adoption")
//                                    .get()
//                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                                        @Override
//                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                                            if(!queryDocumentSnapshots.isEmpty()){
//                                                Log.d("Test", queryDocumentSnapshots.getDocuments().toString());
//                                            }
//
//                                        }
//                                    });
//                        }
//                        Log.d("Key", value.get(j));
//
////           }
//
//                    }
//                } else {
//                    Log.d("Error", "Error getting documents: ", task.getException());
//                }
//            }
//        });

    }
}

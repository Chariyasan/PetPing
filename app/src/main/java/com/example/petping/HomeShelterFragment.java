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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HomeShelterFragment extends Fragment {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ListView listView;
    private HomeShelterAdapter adapter;
    private ArrayList<HomeShelter> homeShelter = new ArrayList<>();
    private List<HomeShelter> homeList = new ArrayList<>();
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
                        Set<String> set = new HashSet<String>(value);
                        value.clear();
                        value.addAll(set);
                        showDetail(value);
                    }
                });

        return view;
    }

    private void showDetail(final List<String> value) {
        for (int i=0; i<value.size(); i++){
//            Log.d("uid", value.get(i));
            final int finalI1 = i;
            final int finalI = i;
            db.collection("RequestAdoption")
                    .document(value.get(i))
                    .collection("Adoption")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d("name", document.get("UserName").toString());
                                    HomeShelter homeShelter = new HomeShelter(document.getId(), value.get(finalI1), document.get("UserName").toString(), document.get("UserImage").toString(),
                                            document.get("petName").toString(), document.get("petStatus").toString(), document.get("petURL").toString());
                                   homeList.add(homeShelter);
                                }
                                adapter = new HomeShelterAdapter(getContext(), homeList);
                                listView.setAdapter(adapter);
                            }

                        }
                    });

        }



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewRequestShelterFragment viewRequest = new ViewRequestShelterFragment();
                Bundle bundle = new Bundle();
                homeShelter.add(homeList.get(position));
//                for(int i=0; i<homeShelter.size(); i++){
//                    Log.d("homeShelter", homeShelter.get(i).getuID());
//                }
                bundle.putParcelableArrayList("homeShelter", homeShelter);
                viewRequest.setArguments(bundle);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(getId(), viewRequest);
                ft.commit();
            }
        });

    }
}
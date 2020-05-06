package com.example.petping;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class ManageContentShelterFragment extends Fragment {
    private ListView listView;
    private Button btnAdd;
    private ManageContentShelterAdapter adapter;
    private ArrayList<Content> contentList = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView result;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_content_shelter, null);

        listView = view.findViewById(R.id.listView_content_shelter);
        btnAdd = view.findViewById(R.id.btn_add_content);
        result = view.findViewById(R.id.result);
        if(!contentList.isEmpty()){
            contentList.clear();
        }
        db.collection("Content")
                .whereEqualTo("ShelterID", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            int count = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Content content = new Content(document.getId(), document.get("Topic").toString(),
                                        document.get("Story").toString(), document.get("URL").toString(), document.get("ShelterID").toString(),
                                        document.get("Date").toString(), document.get("Time").toString());
                                contentList.add(content);
                                count++;
                            }

                            adapter = new ManageContentShelterAdapter(getFragmentManager(),getId(), getContext(), contentList);
                            adapter.sortDate();
                            listView.setAdapter(adapter);
                            result.setText(String.valueOf(count));
                        }
                    }
                });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(getId(), new AddContentShelterFragment());
                ft.addToBackStack(null).commit();
            }
        });

        return view;
    }
}

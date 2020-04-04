package com.example.petping;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UserLikeFragment extends Fragment {

    private ListView listView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<PetSearch> petFavList = new ArrayList<>();
    private ArrayList<PetSearch> petList = new ArrayList<>();
    private UserLikeAdapter likeAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_menu_like, container, false);
        listView = view.findViewById(R.id.listView_like);
        db.collection("User")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("Like")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(final QueryDocumentSnapshot document : task.getResult()){
//                        historyList.add(document.getId());
                        String ID = document.getId();
                        Log.d("ID", document.getId());
                        db.collection("Pet")
                                .document(ID)
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot document) {
                                        PetSearch petHist = new PetSearch(document.getId(), document.get("Name").toString(), document.get("Type").toString(),
                                                document.get("Color").toString(), document.get("Sex").toString(), document.get("Age").toString(),
                                                document.get("Breed").toString(), document.get("Size").toString(), document.get("Image").toString(),
                                                document.get("Weight").toString(), document.get("Character").toString(), document.get("Marking").toString(),
                                                document.get("Health").toString(), document.get("OriginalLocation").toString(), document.get("Status").toString(),
                                                document.get("Story").toString(), document.get("ShelterID").toString());
                                        petFavList.add(petHist);
                                        likeAdapter = new UserLikeAdapter(getContext(),petFavList);
                                        listView.setAdapter(likeAdapter);

                                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                PetProfileGeneralFragment petProfile = new PetProfileGeneralFragment();
                                                Bundle bundle = new Bundle();
                                                petList.add(petFavList.get(position));
                                                bundle.putParcelableArrayList("petProfile", petList);
                                                petProfile.setArguments(bundle);
                                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                                ft.replace(getId(), petProfile);
                                                ft.commit();
                                            }
                                        });
                                    }
                                });
                    }
                }
            }
        });

        return view;
    }
}
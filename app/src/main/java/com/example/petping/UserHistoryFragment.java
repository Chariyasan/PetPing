package com.example.petping;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UserHistoryFragment extends Fragment {

    private ArrayList<PetSearch> petList = new ArrayList<>();
    private ArrayList<PetSearch> petSearchList;

    private PetSearch petHistory;

    private ArrayList<PetSearch> historyList = new ArrayList<>();

    private ListView listView;
    private UserHisLikeAdapter historyAdapter;
    public TextView resultFound;
    private ArrayList<PetHistory> petHistoryItem;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private DocumentReference userDocRef = FirebaseFirestore.getInstance().document("/User/RPF67EzLXyEJlOk1Yzm6/History/PetHistory");
//    private CollectionReference userColRef = FirebaseFirestore.getInstance().collection("/User/RPF67EzLXyEJlOk1Yzm6/History");


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_menu_history, container, false);

        listView = view.findViewById(R.id.listView_history);

        db.collection("User")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("History")
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
                                                    document.get("Story").toString());
                                            historyList.add(petHist);
                                        historyAdapter = new UserHisLikeAdapter(getContext(),historyList);
                                        listView.setAdapter(historyAdapter);
                                    }
                                });
//                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                        for(final QueryDocumentSnapshot document : task.getResult()) {
//                                            PetSearch petHist = new PetSearch(document.getId(), document.get("Name").toString(), document.get("Type").toString(),
//                                                    document.get("Color").toString(), document.get("Sex").toString(), document.get("Age").toString(),
//                                                    document.get("Breed").toString(), document.get("Size").toString(), document.get("Image").toString(),
//                                                    document.get("Weight").toString(), document.get("Character").toString(), document.get("Marking").toString(),
//                                                    document.get("Health").toString(), document.get("OriginalLocation").toString(), document.get("Status").toString(),
//                                                    document.get("Story").toString());
//                                            historyList.add(petHist);
//                                            Log.d("ID", document.get("Name").toString());
//                                        }
//
//                                        historyAdapter = new UserHisLikeAdapter(getContext(),historyList);
//                                        listView.setAdapter(historyAdapter);
//                                    }
//                                });
//                        Log.d("YY",task.toString().);
//                        Log.d("Y",document.getId().toString());
//                        Log.d("Y2",task.getResult().getQuery().get().toString());
//                        if(document.getId().toString().equals(task.getResult().toString())){
////                            historyAdapter = new PetListViewAdapter(getContext(), historyList);
////                            listView.setAdapter(historyAdapter);
//                        }else {
//                            Log.d("Error", "Error getting documents: ", task.getException());
//                        }

                    }
                }
            }
        });


//
////        final Task<DocumentSnapshot> his = db.collection("User")
////                .document("RPF67EzLXyEJlOk1Yzm6")
////                .collection("History")
////                .document()
////                .get();
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                petHistoryItem = new ArrayList<>();
//                PetProfileGeneralFragment petProfile = new PetProfileGeneralFragment();
//                Bundle bundle = new Bundle();
//                petHistoryItem.add(petSearchList.get(i));
//                bundle.putSerializable("petProfile", petHistoryItem);
//
//                petProfile.setArguments(bundle);
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.replace(getId(), petProfile);
//                ft.commit();
//            }
//        });
//        int i=0;
////
//        db.collection("User")
//                .document("RPF67EzLXyEJlOk1Yzm6")
//                .collection("History")
//                .document()
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if(task.isSuccessful()){
//                            Log.d("Y", task.getResult().toString());
//                            historyList.add(task.getResult());
//                            historyAdapter = new UserHistoryAdapter(getContext(), historyList);
//                            listView.setAdapter(historyAdapter);
//                            }
//                        else {
//                            Log.d("Error", "Error getting documents: ", task.getException());
//                     }
//                    }
//                });


        return view;

    }
}
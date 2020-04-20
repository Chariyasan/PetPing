package com.example.petping;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;


import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HomeShelterFragment extends Fragment {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ListView listView;
    private HomeShelterAdapter adapter;
    private ArrayList<HomeShelter> homeShelter = new ArrayList<>();
    private ArrayList<HomeShelter> homeList = new ArrayList<>();
    private Button btnReject, btnWaiting, btnSuccess;
    private TextView count;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_shelter, null);
        listView = view.findViewById(R.id.listView);
        btnReject = view.findViewById(R.id.reject);
        btnWaiting = view.findViewById(R.id.waiting);
        btnSuccess = view.findViewById(R.id.success);
        count = view.findViewById(R.id.count);

        btnWaiting.setTypeface(null, Typeface.BOLD);
        btnWaiting.setTextColor(Color.parseColor("#808080"));
        btnSuccess.setTypeface(null, Typeface.NORMAL);
        btnSuccess.setTextColor(Color.parseColor("#FFAFAFAF"));
        btnReject.setTypeface(null, Typeface.NORMAL);
        btnReject.setTextColor(Color.parseColor("#FFAFAFAF"));

        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.filterReject();
                int num = 0;
                for(int i=0; i < adapter.getCount(); i++){
                    num++;
                }
                count.setText(String.valueOf(num));
                btnWaiting.setTypeface(null, Typeface.NORMAL);
                btnWaiting.setTextColor(Color.parseColor("#FFAFAFAF"));
                btnSuccess.setTypeface(null, Typeface.NORMAL);
                btnSuccess.setTextColor(Color.parseColor("#FFAFAFAF"));
                btnReject.setTypeface(null, Typeface.BOLD);
                btnReject.setTextColor(Color.parseColor("#808080"));
            }
        });

        btnWaiting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.filterWaiting();
                int num = 0;
                for(int i=0; i < adapter.getCount(); i++){
                    num++;
                }
                count.setText(String.valueOf(num));
                btnWaiting.setTypeface(null, Typeface.BOLD);
                btnWaiting.setTextColor(Color.parseColor("#808080"));
                btnSuccess.setTypeface(null, Typeface.NORMAL);
                btnSuccess.setTextColor(Color.parseColor("#FFAFAFAF"));
                btnReject.setTypeface(null, Typeface.NORMAL);
                btnReject.setTextColor(Color.parseColor("#FFAFAFAF"));
            }
        });

        btnSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.filterSuccess();
                int num = 0;
                for(int i=0; i < adapter.getCount(); i++){
                    num++;
                }
                count.setText(String.valueOf(num));
                btnWaiting.setTypeface(null, Typeface.NORMAL);
                btnWaiting.setTextColor(Color.parseColor("#FFAFAFAF"));
                btnSuccess.setTypeface(null, Typeface.BOLD);
                btnSuccess.setTextColor(Color.parseColor("#808080"));
                btnReject.setTypeface(null, Typeface.NORMAL);
                btnReject.setTextColor(Color.parseColor("#FFAFAFAF"));
            }
        });

        final List<String> value = new ArrayList<>();
        db.collection("User1")
                .document("userID")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            for (String key : documentSnapshot.getData().keySet() ) {
                                value.add(key);
                                Log.d("key1", key);
                            }
                            Set<String> set = new HashSet<String>(value);
                            value.clear();
                            value.addAll(set);
                            showDetail(value);
                        }
                    }
                });

        return view;
    }

    private void showDetail(final List<String> value) {
        for (int i=0; i<value.size(); i++){
//            Log.d("uid", value.get(i));
            final int finalI1 = i;
            final int finalI = i;
            if(homeList != null){
                homeList.clear();
            }
            db.collection("RequestAdoption")
                    .document(value.get(i))
                    .collection("Adoption")
                    .whereEqualTo("ShelterID", FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d("name", document.get("UserName").toString());
                                    HomeShelter homeShelter = new HomeShelter(document.getId(), value.get(finalI1), document.get("UserName").toString(), document.get("UserImage").toString(),
                                            document.get("petName").toString(), document.get("petStatus").toString(), document.get("petURL").toString(), document.get("DateTime").toString());
                                    homeList.add(homeShelter);
                                }
                                int num = 0;
                                for(int i=0; i<homeList.size(); i++){
                                    if(homeList.get(i).getPetStatus().equals("กำลังดำเนินการ")){
                                        num++;
                                        count.setText(String.valueOf(num));
                                    }
                                }
                                adapter = new HomeShelterAdapter(getContext(), homeList);
                                adapter.filterWaiting();
                                listView.setAdapter(adapter);
                            }
                        }
                    });
        }

//        btnReject.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int num = 0;
//                for(int i=0; i < adapter.getCount(); i++){
//                    num++;
//                }
//                count.setText(String.valueOf(num));
//            }
//        });
//
//        btnWaiting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                adapter.filterWaiting();
//                int num = 0;
//                for(int i=0; i < adapter.getCount(); i++){
//                    num++;
//                }
//                count.setText(String.valueOf(num));
//            }
//        });
//
//        btnSuccess.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                adapter.filterSuccess();
//                int num = 0;
//                for(int i=0; i < adapter.getCount(); i++){
//                    num++;
//                }
//                count.setText(String.valueOf(num));
//            }
//        });

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
                ft.addToBackStack(null).commit();
            }
        });

    }
}
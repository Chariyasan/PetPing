package com.example.petping;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class ManagePetInfoShelterFragment extends Fragment {
    private ArrayList<PetSearch> petList = new ArrayList<>();
    private ManagePetInfoShelterAdapter adapter;
    private ListView listView;
    private PetSearch petSearch;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Button btnAddPet;
    private TextView result;
    private EditText search;
    private Button btnFinding, btnWaiting, btnSuccess;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_pet_info_shelter, null);
        listView = view.findViewById(R.id.listView_pet_info);
        btnAddPet = view.findViewById(R.id.btn_add_pet);
        result = view.findViewById(R.id.result);
        search = view.findViewById(R.id.search);
        btnFinding = view.findViewById(R.id.finding);
        btnWaiting = view.findViewById(R.id.waiting);
        btnSuccess = view.findViewById(R.id.success);
        btnFinding.setTypeface(null, Typeface.BOLD);
        btnFinding.setTextColor(Color.parseColor("#808080"));
        btnWaiting.setTypeface(null, Typeface.NORMAL);
        btnWaiting.setTextColor(Color.parseColor("#FFAFAFAF"));
        btnSuccess.setTypeface(null, Typeface.NORMAL);
        btnSuccess.setTextColor(Color.parseColor("#FFAFAFAF"));

        db.collection("Pet")
                .orderBy("Status")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            setValue(task);
                        } else {
                            Log.d("Error", "Error getting documents: ", task.getException());
                        }
                    }

                });

        btnAddPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(getId(), new AddPetShelterFragment());
                ft.addToBackStack(null).commit();
            }
        });

        btnFinding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.filterFinding();
                int num = 0;
                for(int i=0; i < adapter.getCount(); i++){
                    num++;
                }
                result.setText(String.valueOf(num));
                btnWaiting.setTypeface(null, Typeface.NORMAL);
                btnWaiting.setTextColor(Color.parseColor("#FFAFAFAF"));
                btnSuccess.setTypeface(null, Typeface.NORMAL);
                btnSuccess.setTextColor(Color.parseColor("#FFAFAFAF"));
                btnFinding.setTypeface(null, Typeface.BOLD);
                btnFinding.setTextColor(Color.parseColor("#808080"));

                searchFilter();
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
                result.setText(String.valueOf(num));
                btnWaiting.setTypeface(null, Typeface.BOLD);
                btnWaiting.setTextColor(Color.parseColor("#808080"));
                btnSuccess.setTypeface(null, Typeface.NORMAL);
                btnSuccess.setTextColor(Color.parseColor("#FFAFAFAF"));
                btnFinding.setTypeface(null, Typeface.NORMAL);
                btnFinding.setTextColor(Color.parseColor("#FFAFAFAF"));
                searchFilter();
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
                result.setText(String.valueOf(num));
                btnWaiting.setTypeface(null, Typeface.NORMAL);
                btnWaiting.setTextColor(Color.parseColor("#FFAFAFAF"));
                btnSuccess.setTypeface(null, Typeface.BOLD);
                btnSuccess.setTextColor(Color.parseColor("#808080"));
                btnFinding.setTypeface(null, Typeface.NORMAL);
                btnFinding.setTextColor(Color.parseColor("#FFAFAFAF"));
                searchFilter();
            }
        });

        return view;
    }

    private void searchFilter() {
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
                adapter.getFilter().filter(s, new Filter.FilterListener() {
                    public void onFilterComplete(int count) {
                        result.setText(String.valueOf(count));
                    }
                });
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setValue(Task<QuerySnapshot> task) {
        int count=0;
        if(petList != null){
            petList.clear();
        }
        for (QueryDocumentSnapshot document : task.getResult()) {
            petSearch = new PetSearch(document.getId(), document.get("Name").toString(), document.get("Type").toString(),
                    document.get("Color").toString(), document.get("Sex").toString(), document.get("Age").toString(),
                    document.get("Breed").toString(), document.get("Size").toString(), document.get("Image").toString(),
                    document.get("Weight").toString(), document.get("Character").toString(), document.get("Marking").toString(),
                    document.get("Health").toString(), document.get("OriginalLocation").toString(), document.get("Status").toString(),
                    document.get("Story").toString(), document.get("ShelterID").toString());
            petList.add(petSearch);

        }

        for(int i=0; i<petList.size(); i++){
            if(petList.get(i).getStatus().equals("กำลังหาบ้าน")){
                adapter = new ManagePetInfoShelterAdapter(getFragmentManager(),getId(), getContext(), petList);
                adapter.filterFinding();
                listView.setAdapter(adapter);
                count++;
                result.setText(String.valueOf(count));
            }
        }

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                petItem = new ArrayList<>();
//                PetInfoShelterFragment petInfo = new PetInfoShelterFragment();
//                Bundle bundle = new Bundle();
//                petItem.add(petList.get(position));
//                bundle.putSerializable("petInfo", petItem);
//
//                petInfo.setArguments(bundle);
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.replace(getId(), petInfo);
//                ft.commit();
//            }
//        });
    }
}

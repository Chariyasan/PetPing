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
import android.widget.LinearLayout;
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
    private ArrayList<PetSearch> tempList = new ArrayList<>();
    private ManagePetInfoShelterAdapter adapter;
    private ListView listView;
    private PetSearch petSearch;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Button btnAddPet;
    private TextView result;
    private EditText search;
    private Button btnFinding, btnWaiting, btnSuccess, btnPrevious, btnNext;
    private View.OnClickListener clickListener;

    public int ITEMS_IN_PAGE = 5;
    private int noOfBtns;
    private Button[] btns;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) { 
        view = inflater.inflate(R.layout.fragment_manage_pet_info_shelter, null);
        listView = view.findViewById(R.id.listView_pet_info);
        btnAddPet = view.findViewById(R.id.btn_add_pet);
        result = view.findViewById(R.id.result);
        search = view.findViewById(R.id.search);
        btnFinding = view.findViewById(R.id.finding);
        btnWaiting = view.findViewById(R.id.waiting);
        btnSuccess = view.findViewById(R.id.success);
//        btnPrevious = view.findViewById(R.id.btn_left);
//        btnNext = view.findViewById(R.id.btn_right);

        btnFinding.setTypeface(null, Typeface.BOLD);
        btnFinding.setTextColor(Color.parseColor("#808080"));
        btnWaiting.setTypeface(null, Typeface.NORMAL);
        btnWaiting.setTextColor(Color.parseColor("#FFAFAFAF"));
        btnSuccess.setTypeface(null, Typeface.NORMAL);
        btnSuccess.setTextColor(Color.parseColor("#FFAFAFAF"));

        if(petList != null){
            petList.clear();
        }
        db.collection("Pet")
                .orderBy("Status")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int total=0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                petSearch = new PetSearch(document.getId(), document.get("Name").toString(), document.get("Type").toString(),
                                        document.get("Color").toString(), document.get("Sex").toString(), document.get("Age").toString(),
                                        document.get("Breed").toString(), document.get("Size").toString(), document.get("Image").toString(),
                                        document.get("Weight").toString(), document.get("Character").toString(), document.get("Marking").toString(),
                                        document.get("Health").toString(), document.get("OriginalLocation").toString(), document.get("Status").toString(),
                                        document.get("Story").toString(), document.get("ShelterID").toString());
                                petList.add(petSearch);
                                total++;
                            }
                            setButtonsForPagination(total);
                            result.setText(String.valueOf(total));
                            int count=0;
                            for(int i=0; i<ITEMS_IN_PAGE; i++) {
//                                if (petList.get(i).getStatus().equals("กำลังหาบ้าน")) {
                                    tempList.add(petList.get(i));

//                                }
                            }
                            adapter = new ManagePetInfoShelterAdapter(getFragmentManager(),getId(), getContext(), tempList);
//                            adapter.filterFinding();
                            listView.setAdapter(adapter);
                            searchFilter();

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
//                setButtonsForPagination(num);
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
//                setButtonsForPagination(num);

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
//                setButtonsForPagination(num);
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

//    private void setValue(Task<QuerySnapshot> task) {
//
//
//    }
    private void setButtonsForPagination(int total) {

        int val = total % ITEMS_IN_PAGE;
        //val = val == 0 ? 0 : 1;
        if(val==0){
            val = 0;
        }
        else {
            val = 1;
        }
        noOfBtns = total / ITEMS_IN_PAGE + val;

        LinearLayout layout = (LinearLayout) view.findViewById(R.id.btn_page);

        btns = new Button[noOfBtns];


        for (int i = 0; i < noOfBtns; i++) {
            btns[i] = new Button(getContext());
            btns[i].setBackgroundColor(getResources().getColor(
                    android.R.color.transparent));
            btns[i].setText("" + (i + 1));

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layout.addView(btns[i], lp);

            final int j = i;

            //code to perform on click of each button
            btns[j].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    loadList(j);
                    CheckBtnBackGroud(j);
                }
            });
        }

    }// end of setButtonsForPagination()

    private void CheckBtnBackGroud(int j) {
        for (int i = 0; i < noOfBtns; i++) {
            if (i == j) {
                // btns[index].setBackgroundDrawable(getResources().getDrawable(R.drawable.box_green));
                btns[j].setBackgroundColor(getResources().getColor(
                        android.R.color.darker_gray));
                btns[i].setTextColor(getResources().getColor(
                        android.R.color.white));
            } else {
                btns[i].setBackgroundColor(getResources().getColor(
                        android.R.color.transparent));
                btns[i].setTextColor(getResources().getColor(
                        android.R.color.black));
            }
        }
    }

    private void loadList(int j) {

        int start = j * ITEMS_IN_PAGE;
        tempList.clear();
        for (int i = start; i < (start) + ITEMS_IN_PAGE; i++) {
            if (i < petList.size()) {
                // sort.add(data.get(i));
                tempList.add(petList.get(i));
//                if (btnFinding.isSelected()){
//                    tempList.add(petList.get(i));
//                    adapter = new ManagePetInfoShelterAdapter(getFragmentManager(),getId(), getContext(), tempList);
////                    adapter.filterFinding();
//                    listView.setAdapter(adapter);
//                    int num = 0;
//                    for(int k=0; k < adapter.getCount(); k++){
//                        num++;
//                    }
//                    result.setText(String.valueOf(num));
//                }
//                if (petList.get(i).getStatus().equals("กำลังดำเนินการ")){
//                    tempList.add(petList.get(i));
//                    adapter = new ManagePetInfoShelterAdapter(getFragmentManager(),getId(), getContext(), tempList);
//                    adapter.filterWaiting();
//                    listView.setAdapter(adapter);
//                    int num = 0;
//                    for(int k=0; k < adapter.getCount(); k++){
//                        num++;
//                    }
//                    result.setText(String.valueOf(num));
//                }
//                if (petList.get(i).getStatus().equals("ดำเนินการสำเร็จ")){
//                    tempList.add(petList.get(i));
//                    adapter = new ManagePetInfoShelterAdapter(getFragmentManager(),getId(), getContext(), tempList);
//                    adapter.filterSuccess();
//                    listView.setAdapter(adapter);
//                    int num = 0;
//                    for(int k=0; k < adapter.getCount(); k++){
//                        num++;
//                    }
//                    result.setText(String.valueOf(num));
//                }
            } else {
                break;
            }
            adapter = new ManagePetInfoShelterAdapter(getFragmentManager(),getId(), getContext(), tempList);
            listView.setAdapter(adapter);
        }
    }

}

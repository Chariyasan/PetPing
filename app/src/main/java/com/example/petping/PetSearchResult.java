package com.example.petping;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PetSearchResult extends Fragment implements DialogFiltering.filterSelected{
    private ArrayList<PetSearch> petSearchList;
    private ArrayList<PetSearch> petSearchFilter = new ArrayList<>();;
    private GridView listView;
    private PetListViewAdapter petAdapter;
    private ArrayList<PetSearch> petItem;
    private ImageButton btnFiltering;
    private String type;

    private Map<String, Object> dataToSave = new HashMap<String, Object>();
    private String KEY_PETID = "petID";
    private String KEY_COUNT = "count";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public TextView resultFound;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View temp = inflater.inflate(R.layout.fragment_pet_search_result, null);

        if(getArguments() != null){
            petSearchList = getArguments().getParcelableArrayList("petL");
        }

        resultFound = temp.findViewById(R.id.result_found);
        int count = 0;
        for(int i=0; i<petSearchList.size(); i++){
            count++;
        }
        resultFound.setText(String.valueOf(count));

        //Show pet list after searching
        listView = (GridView) temp.findViewById(R.id.listView_pet);
        petAdapter = new PetListViewAdapter(getContext(), petSearchList);
        listView.setAdapter(petAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<PetSearch> petSearchArrayList = new ArrayList<>();
                PetSearch pet = (PetSearch) parent.getItemAtPosition(position);

                PetSearch petSearch = new PetSearch(pet.getID(), pet.getName(), pet.getType(), pet.getColour(), pet.getSex(),
                        pet.getAge(), pet.getBreed(), pet.getSize(), pet.getUrl(), pet.getWeight(), pet.getCharacter(), pet.getMarking(),
                        pet.getHealth(), pet.getFoundLoc(), pet.getStatus(), pet.getStory(), pet.getShelterID());

                petSearchArrayList.add(petSearch);

                PetProfileGeneralFragment petProfile = new PetProfileGeneralFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("petProfile", petSearchArrayList);

                for (int i = 0; i < petSearchArrayList.size(); i++) {
                    saveIntoHistory("petID", petSearchArrayList.get(i).getID());
                }

                petProfile.setArguments(bundle);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(getId(), petProfile);
                ft.addToBackStack(null).commit();
            }
        });

        //Filtering part
        btnFiltering = temp.findViewById(R.id.adapter_filter);
        btnFiltering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                petFilterList = new ArrayList<>();
                for(int i=0; i<petSearchList.size(); i++){
                    if(petSearchList.get(i).getType().equals("สุนัข")){
                        type = "สุนัข";
                    }
                }
                for(int i=0; i<petSearchList.size(); i++){
                    if(petSearchList.get(i).getType().equals("แมว")){
                        type = "แมว";
                    }
                }
                DialogFiltering dialog = new DialogFiltering();
                Bundle bundle = new Bundle();
                bundle.putString("type", type);
                dialog.setArguments(bundle);
                dialog.setTargetFragment(PetSearchResult.this, 1);
                dialog.show(getFragmentManager(), "MyCustomDialog");
            }
        });

        return temp;
    }

    @Override
    public void sendFiltering(String color, String sex, ArrayList<String> petSearchAge, ArrayList<String> petSearchSize, String breed) {
        if(petSearchFilter != null){
            petSearchFilter.clear();
        }
        //Select all filter
        if(!color.equals("ไม่ระบุ") && sex != null && !petSearchAge.isEmpty() && !petSearchSize.isEmpty() && !breed.isEmpty()){
            int count = 0;
            for(int i=0; i<petSearchList.size(); i++){
                for(int j=0; j<petSearchAge.size(); j++){
                    for (int k=0; k<petSearchSize.size(); k++){
                        if(petSearchList.get(i).getColour().equals(color)
                                && petSearchList.get(i).getBreed().equals(breed)
                                && petSearchList.get(i).getSex().equals(sex)
                                && petSearchList.get(i).getAge().equals(petSearchAge.get(j))
                                && petSearchList.get(i).getSize().equals(petSearchSize.get(k))){
                            petFilter(i);
                            count++;
                        }
                    }
                }
            }
            resultFound.setText(String.valueOf(count));
            petAdapter = new PetListViewAdapter(getContext(), petSearchFilter);
            listView.setAdapter(petAdapter);
        }
        //Not Choose Color
        if(color.equals("ไม่ระบุ") && sex != null && !petSearchAge.isEmpty() && !petSearchSize.isEmpty() && breed != null) {
            int count = 0;
            for (int i = 0; i < petSearchList.size(); i++) {
                for (int j = 0; j < petSearchAge.size(); j++) {
                    for (int k=0; k<petSearchSize.size(); k++){
                        if (petSearchList.get(i).getSex().equals(sex)
                                && petSearchList.get(i).getBreed().equals(breed)
                                && petSearchList.get(i).getAge().equals(petSearchAge.get(j))
                                && petSearchList.get(i).getSize().equals(petSearchSize.get(k))) {
                            petFilter(i);
                            count++;
                        }
                    }
                }
            }

            resultFound.setText(String.valueOf(count));
            petAdapter = new PetListViewAdapter(getContext(), petSearchFilter);
            listView.setAdapter(petAdapter);
        }
        //Not Choose Sex
        if(!color.equals("ไม่ระบุ") && sex == null && !petSearchAge.isEmpty() && !petSearchSize.isEmpty() && breed != null) {
            int count = 0;
            for (int i = 0; i < petSearchList.size(); i++) {
                for (int j = 0; j < petSearchAge.size(); j++) {
                    for (int k=0; k<petSearchSize.size(); k++){
                        if (petSearchList.get(i).getColour().equals(color)
                                && petSearchList.get(i).getBreed().equals(breed)
                                && petSearchList.get(i).getAge().equals(petSearchAge.get(j))
                                && petSearchList.get(i).getSize().equals(petSearchSize.get(k))) {
                            petFilter(i);
                            count++;
                        }
                    }
                }
            }

            resultFound.setText(String.valueOf(count));
            petAdapter = new PetListViewAdapter(getContext(), petSearchFilter);
            listView.setAdapter(petAdapter);
        }
        //Not Choose Age
        if(!color.equals("ไม่ระบุ") && sex != null && petSearchAge.isEmpty() && !petSearchSize.isEmpty() && breed != null) {
            int count = 0;
            for (int i = 0; i < petSearchList.size(); i++) {
                for (int k=0; k<petSearchSize.size(); k++){
                    if (petSearchList.get(i).getColour().equals(color)
                            && petSearchList.get(i).getBreed().equals(breed)
                            && petSearchList.get(i).getSex().equals(sex)
                            && petSearchList.get(i).getSize().equals(petSearchSize.get(k))) {
                        petFilter(i);
                        count++;
                    }
                }
            }

            resultFound.setText(String.valueOf(count));
            petAdapter = new PetListViewAdapter(getContext(), petSearchFilter);
            listView.setAdapter(petAdapter);
        }
        //Not Choose Size
        if(!color.equals("ไม่ระบุ") && sex != null && !petSearchAge.isEmpty() && petSearchSize.isEmpty() && breed != null) {
            int count = 0;
            for (int i = 0; i < petSearchList.size(); i++) {
                for (int j = 0; j < petSearchAge.size(); j++){
                    if (petSearchList.get(i).getColour().equals(color)
                            && petSearchList.get(i).getBreed().equals(breed)
                            && petSearchList.get(i).getSex().equals(sex)
                            && petSearchList.get(i).getAge().equals(petSearchAge.get(j))) {
                        petFilter(i);
                        count++;
                    }
                }
            }
            resultFound.setText(String.valueOf(count));
            petAdapter = new PetListViewAdapter(getContext(), petSearchFilter);
            listView.setAdapter(petAdapter);
        }
        //Not Choose Breed
        if(!color.equals("ไม่ระบุ") && sex != null && !petSearchAge.isEmpty() && !petSearchSize.isEmpty() && breed.isEmpty()) {
            int count = 0;
            for (int i = 0; i < petSearchList.size(); i++) {
                for (int j = 0; j < petSearchAge.size(); j++){
                    for (int k=0; k < petSearchSize.size(); k++){
                        if (petSearchList.get(i).getColour().equals(color)
                                && petSearchList.get(i).getSex().equals(sex)
                                && petSearchList.get(i).getAge().equals(petSearchAge.get(j))
                                && petSearchList.get(i).getSize().equals(petSearchSize.get(k))) {
                            petFilter(i);
                            count++;
                        }
                    }
                }
            }
            resultFound.setText(String.valueOf(count));
            petAdapter = new PetListViewAdapter(getContext(), petSearchFilter);
            listView.setAdapter(petAdapter);
        }
        //Not Choose Color & Age
        if(color.equals("ไม่ระบุ") && sex != null && petSearchAge.isEmpty() && !petSearchSize.isEmpty() && breed != null) {
            int count = 0;
            for (int i = 0; i < petSearchList.size(); i++) {
                for (int k=0; k<petSearchSize.size(); k++){
                    if (petSearchList.get(i).getSex().equals(sex) &&
                            petSearchList.get(i).getBreed().equals(breed) &&
                            petSearchList.get(i).getSize().equals(petSearchSize.get(k))) {
                        petFilter(i);
                        count++;
                    }
                }
            }
            resultFound.setText(String.valueOf(count));
            petAdapter = new PetListViewAdapter(getContext(), petSearchFilter);
            listView.setAdapter(petAdapter);
        }
        //Not Choose Color & Size
        if(color.equals("ไม่ระบุ") && sex != null && !petSearchAge.isEmpty() && petSearchSize.isEmpty() && breed!=null) {
            int count = 0;
            for (int i = 0; i < petSearchList.size(); i++) {
                for (int j = 0; j < petSearchAge.size(); j++){
                    if (petSearchList.get(i).getSex().equals(sex) &&
                            petSearchList.get(i).getBreed().equals(breed) &&
                            petSearchList.get(i).getAge().equals(petSearchAge.get(j))) {
                        petFilter(i);
                        count++;
                    }
                }
            }
            resultFound.setText(String.valueOf(count));
            petAdapter = new PetListViewAdapter(getContext(), petSearchFilter);
            listView.setAdapter(petAdapter);
        }

        //Not Choose Color & Sex
        if(color.equals("ไม่ระบุ") && sex == null && !petSearchAge.isEmpty() && !petSearchSize.isEmpty() && breed!=null) {
            int count = 0;
            for (int i = 0; i < petSearchList.size(); i++) {
                for (int j = 0; j < petSearchAge.size(); j++){
                    for (int k = 0; k < petSearchSize.size(); k++) {
                        if (petSearchList.get(i).getBreed().equals(breed) &&
                                petSearchList.get(i).getAge().equals(petSearchAge.get(j))
                                && petSearchList.get(i).getSize().equals(petSearchSize.get(k))) {
                            petFilter(i);
                            count++;
                        }
                    }
                }
            }
            resultFound.setText(String.valueOf(count));
            petAdapter = new PetListViewAdapter(getContext(), petSearchFilter);
            listView.setAdapter(petAdapter);
        }

        //Not Choose Color & Breed
        if(color.equals("ไม่ระบุ") && sex != null && !petSearchAge.isEmpty() && !petSearchSize.isEmpty() && breed.isEmpty()) {
            int count = 0;
            for (int i = 0; i < petSearchList.size(); i++) {
                for (int j=0; j<petSearchAge.size(); j++){
                    for(int k=0; k<petSearchSize.size(); k++){
                        if (petSearchList.get(i).getSex().equals(sex)
                                && petSearchList.get(i).getAge().equals(petSearchAge.get(j))
                                && petSearchList.get(i).getSize().equals(petSearchSize.get(k))) {
                            petFilter(i);
                            count++;
                        }
                    }
                }
            }
            resultFound.setText(String.valueOf(count));
            petAdapter = new PetListViewAdapter(getContext(), petSearchFilter);
            listView.setAdapter(petAdapter);
        }

        //Not Choose Breed & Sex
        if(!color.equals("ไม่ระบุ") && sex == null && !petSearchAge.isEmpty() && !petSearchSize.isEmpty() && breed.isEmpty()) {
            int count = 0;
            for (int i = 0; i < petSearchList.size(); i++) {
                for (int j=0; j<petSearchAge.size(); j++) {
                    for (int k = 0; k < petSearchSize.size(); k++) {
                        if (petSearchList.get(i).getColour().equals(color)
                                && petSearchList.get(i).getAge().equals(petSearchAge.get(j))
                                && petSearchList.get(i).getSize().equals(petSearchSize.get(k))) {
                            petFilter(i);
                            count++;
                        }
                    }
                }
            }
            resultFound.setText(String.valueOf(count));
            petAdapter = new PetListViewAdapter(getContext(), petSearchFilter);
            listView.setAdapter(petAdapter);
        }

        //Not Choose Breed & Age
        if(!color.equals("ไม่ระบุ") && sex != null && petSearchAge.isEmpty() && !petSearchSize.isEmpty() && breed.isEmpty()) {
            int count = 0;
            for (int i = 0; i < petSearchList.size(); i++) {
                for (int j=0; j<petSearchSize.size(); j++){
                    if (petSearchList.get(i).getColour().equals(color)
                            && petSearchList.get(i).getSex().equals(sex)
                            && petSearchList.get(i).getSize().equals(petSearchSize.get(j))) {
                        petFilter(i);
                        count++;
                    }
                }
            }
            resultFound.setText(String.valueOf(count));
            petAdapter = new PetListViewAdapter(getContext(), petSearchFilter);
            listView.setAdapter(petAdapter);
        }
        //Not Choose Breed & Size
        if(!color.equals("ไม่ระบุ") && sex != null && !petSearchAge.isEmpty() && petSearchSize.isEmpty() && breed.isEmpty()) {
            int count = 0;
            for (int i = 0; i < petSearchList.size(); i++) {
                for (int j=0; j<petSearchAge.size(); j++){
                    if (petSearchList.get(i).getColour().equals(color)
                            && petSearchList.get(i).getSex().equals(sex)
                            && petSearchList.get(i).getAge().equals(petSearchAge.get(j))) {
                        petFilter(i);
                        count++;
                    }
                }
            }
            resultFound.setText(String.valueOf(count));
            petAdapter = new PetListViewAdapter(getContext(), petSearchFilter);
            listView.setAdapter(petAdapter);
        }


////////////////////////////////////////////////
        //Not Choose Age & Size
        if(!color.equals("ไม่ระบุ") && sex != null && petSearchAge.isEmpty() && petSearchSize.isEmpty() && breed!=null) {
            int count = 0;
            for (int i = 0; i < petSearchList.size(); i++) {
                if (petSearchList.get(i).getColour().equals(color)
                        && petSearchList.get(i).getBreed().equals(breed)
                        && petSearchList.get(i).getSex().equals(sex)) {
                    petFilter(i);
                    count++;
                }
            }
            resultFound.setText(String.valueOf(count));
            petAdapter = new PetListViewAdapter(getContext(), petSearchFilter);
            listView.setAdapter(petAdapter);
        }

        //Not Choose sex & Size
        if(!color.equals("ไม่ระบุ") && sex == null && !petSearchAge.isEmpty() && petSearchSize.isEmpty() && breed!=null) {
            int count = 0;
            for (int i = 0; i < petSearchList.size(); i++) {
                for (int j = 0; j < petSearchAge.size(); j++) {
                    if (petSearchList.get(i).getColour().equals(color)
                            && petSearchList.get(i).getBreed().equals(breed)
                            && petSearchList.get(i).getAge().equals(petSearchAge.get(j))) {
                        petFilter(i);
                        count++;
                    }
                }
            }
            resultFound.setText(String.valueOf(count));
            petAdapter = new PetListViewAdapter(getContext(), petSearchFilter);
            listView.setAdapter(petAdapter);
        }

        //Not Choose sex & age
        if(!color.equals("ไม่ระบุ") && sex == null && petSearchAge.isEmpty() && !petSearchSize.isEmpty() && breed!=null) {
            int count = 0;
            for (int i = 0; i < petSearchList.size(); i++) {
                for (int j = 0; j < petSearchSize.size(); j++) {
                    if (petSearchList.get(i).getColour().equals(color)
                            && petSearchList.get(i).getBreed().equals(breed)
                            && petSearchList.get(i).getSize().equals(petSearchSize.get(j))) {
                        petFilter(i);
                        count++;
                    }
                }
            }
            resultFound.setText(String.valueOf(count));
            petAdapter = new PetListViewAdapter(getContext(), petSearchFilter);
            listView.setAdapter(petAdapter);
        }

        //Choose Color & Breed
        if(!color.equals("ไม่ระบุ") && breed !=null && sex==null && petSearchAge.isEmpty() && petSearchSize.isEmpty()) {
            int count = 0;
            for (int i = 0; i < petSearchList.size(); i++) {
                if (petSearchList.get(i).getColour().equals(color)
                        && petSearchList.get(i).getBreed().equals(breed)) {
                    petFilter(i);
                    count++;
                }
            }
            resultFound.setText(String.valueOf(count));
            petAdapter = new PetListViewAdapter(getContext(), petSearchFilter);
            listView.setAdapter(petAdapter);
        }

        //Choose Color & Sex
        if(!color.equals("ไม่ระบุ") && sex !=null && breed.isEmpty() && petSearchAge.isEmpty() && petSearchSize.isEmpty()) {
            int count = 0;
            for (int i = 0; i < petSearchList.size(); i++) {
                if (petSearchList.get(i).getColour().equals(color)
                        && petSearchList.get(i).getSex().equals(sex)) {
                    petFilter(i);
                    count++;
                }
            }
            resultFound.setText(String.valueOf(count));
            petAdapter = new PetListViewAdapter(getContext(), petSearchFilter);
            listView.setAdapter(petAdapter);
        }

        //Choose Color & Age
        if(!color.equals("ไม่ระบุ") && !petSearchAge.isEmpty() && sex==null && breed.isEmpty() && petSearchSize.isEmpty()) {
            int count = 0;
            for (int i = 0; i < petSearchList.size(); i++) {
                for (int j = 0; j < petSearchAge.size(); j++) {
                    if (petSearchList.get(i).getColour().equals(color)
                            && petSearchList.get(i).getAge().equals(petSearchAge.get(j))) {
                        petFilter(i);
                        count++;
                    }
                }
            }
            resultFound.setText(String.valueOf(count));
            petAdapter = new PetListViewAdapter(getContext(), petSearchFilter);
            listView.setAdapter(petAdapter);
        }

        //Choose Color & Size
        if(!color.equals("ไม่ระบุ") && !petSearchSize.isEmpty() && breed.isEmpty() && sex==null && petSearchAge.isEmpty()) {
            int count = 0;
            for (int i = 0; i < petSearchList.size(); i++) {
                for (int j = 0; j < petSearchSize.size(); j++) {
                    if (petSearchList.get(i).getColour().equals(color)
                            && petSearchList.get(i).getSize().equals(petSearchSize.get(j))) {
                        petFilter(i);
                        count++;
                    }
                }
            }
            resultFound.setText(String.valueOf(count));
            petAdapter = new PetListViewAdapter(getContext(), petSearchFilter);
            listView.setAdapter(petAdapter);
        }

        //Choose Breed && Sex
        if(sex!=null && breed!=null && color.equals("ไม่ระบุ") && petSearchAge.isEmpty() && petSearchSize.isEmpty()) {
            int count = 0;
            for (int i = 0; i < petSearchList.size(); i++) {
                if (petSearchList.get(i).getBreed().equals(breed)
                        && petSearchList.get(i).getSize().equals(sex)) {
                    petFilter(i);
                    count++;
                }
            }
            resultFound.setText(String.valueOf(count));
            petAdapter = new PetListViewAdapter(getContext(), petSearchFilter);
            listView.setAdapter(petAdapter);
        }

        //Choose Breed & Age
        if(breed!=null && !petSearchAge.isEmpty() && color.equals("ไม่ระบุ") && sex==null && petSearchSize.isEmpty()) {
            int count = 0;
            for (int i = 0; i < petSearchList.size(); i++) {
                for (int j = 0; j < petSearchAge.size(); j++) {
                    if (petSearchList.get(i).getBreed().equals(breed)
                            && petSearchList.get(i).getAge().equals(petSearchAge.get(j))) {
                        petFilter(i);
                        count++;
                    }
                }
            }
            resultFound.setText(String.valueOf(count));
            petAdapter = new PetListViewAdapter(getContext(), petSearchFilter);
            listView.setAdapter(petAdapter);
        }

        //Choose Breed & Size
        if(breed!=null && !petSearchSize.isEmpty() && color.equals("ไม่ระบุ") && sex==null && petSearchAge.isEmpty()) {
            int count = 0;
            for (int i = 0; i < petSearchList.size(); i++) {
                for (int j = 0; j < petSearchSize.size(); j++) {
                    if (petSearchList.get(i).getBreed().equals(breed)
                            && petSearchList.get(i).getSize().equals(petSearchSize.get(j))) {
                        petFilter(i);
                        count++;
                    }
                }
            }
            resultFound.setText(String.valueOf(count));
            petAdapter = new PetListViewAdapter(getContext(), petSearchFilter);
            listView.setAdapter(petAdapter);
        }

        //Choose Sex & Age
        if(sex!=null && !petSearchSize.isEmpty() && color.equals("ไม่ระบุ") && breed.isEmpty() && petSearchAge.isEmpty()) {
            int count = 0;
            for (int i = 0; i < petSearchList.size(); i++) {
                for (int j = 0; j < petSearchSize.size(); j++) {
                    if (petSearchList.get(i).getAge().equals(sex)
                            && petSearchList.get(i).getSize().equals(petSearchSize.get(j))) {
                        petFilter(i);
                        count++;
                    }
                }
            }
            resultFound.setText(String.valueOf(count));
            petAdapter = new PetListViewAdapter(getContext(), petSearchFilter);
            listView.setAdapter(petAdapter);
        }

        //Choose Sex & Size
        if(sex!=null && !petSearchSize.isEmpty() && color.equals("ไม่ระบุ") && breed.isEmpty() && petSearchAge.isEmpty()) {
            int count = 0;
            for (int i = 0; i < petSearchList.size(); i++) {
                for (int j = 0; j < petSearchSize.size(); j++) {
                    if (petSearchList.get(i).getSex().equals(sex)
                            && petSearchList.get(i).getSize().equals(petSearchSize.get(j))) {
                        petFilter(i);
                        count++;
                    }
                }
            }
            resultFound.setText(String.valueOf(count));
            petAdapter = new PetListViewAdapter(getContext(), petSearchFilter);
            listView.setAdapter(petAdapter);
        }

        //Choose Age & Size
        if(!petSearchAge.isEmpty() && !petSearchSize.isEmpty() && color.equals("ไม่ระบุ") && sex==null && breed.isEmpty()) {
            int count = 0;
            for (int i = 0; i < petSearchList.size(); i++) {
                for (int j = 0; j < petSearchSize.size(); j++) {
                    for (int k = 0; k < petSearchAge.size(); k++) {
                        if (petSearchList.get(i).getSize().equals(petSearchSize.get(j))
                            && petSearchList.get(i).getAge().equals(petSearchAge.get(k))) {
                            petFilter(i);
                            count++;
                        }
                    }

                }
            }
            resultFound.setText(String.valueOf(count));
            petAdapter = new PetListViewAdapter(getContext(), petSearchFilter);
            listView.setAdapter(petAdapter);
        }


        //Choose colour
        if(!color.equals("ไม่ระบุ") && sex == null && petSearchAge.isEmpty() && petSearchSize.isEmpty() && breed.isEmpty()) {
            int count = 0;
            for (int i = 0; i < petSearchList.size(); i++) {
                if (petSearchList.get(i).getColour().equals(color)) {
                    petFilter(i);
                    count++;
                }

            }
            resultFound.setText(String.valueOf(count));
            petAdapter = new PetListViewAdapter(getContext(), petSearchFilter);
            listView.setAdapter(petAdapter);
        }

        //Choose sex
        if(color.equals("ไม่ระบุ") && sex != null && petSearchAge.isEmpty() && petSearchSize.isEmpty() && breed.isEmpty()) {
            int count = 0;
            for (int i = 0; i < petSearchList.size(); i++) {
                if (petSearchList.get(i).getSex().equals(sex)) {
                    petFilter(i);
                    count++;

                }
            }
            resultFound.setText(String.valueOf(count));
            petAdapter = new PetListViewAdapter(getContext(), petSearchFilter);
            listView.setAdapter(petAdapter);
        }

        //Choose age
        if(color.equals("ไม่ระบุ") && sex == null && !petSearchAge.isEmpty() && petSearchSize.isEmpty() && breed.isEmpty()) {
            int count = 0;
            for (int i = 0; i < petSearchList.size(); i++) {
                for (int j = 0; j < petSearchAge.size(); j++) {
                    if (petSearchList.get(i).getAge().equals(petSearchAge.get(j))) {
                        petFilter(i);
                        count++;
                    }
                }
            }
            resultFound.setText(String.valueOf(count));
            petAdapter = new PetListViewAdapter(getContext(), petSearchFilter);
            listView.setAdapter(petAdapter);
        }

        //Choose size
        if(color.equals("ไม่ระบุ") && sex == null && petSearchAge.isEmpty() && !petSearchSize.isEmpty() && breed.isEmpty()) {
            int count = 0;
            for (int i = 0; i < petSearchList.size(); i++) {
                for (int j = 0; j < petSearchSize.size(); j++) {
                    if (petSearchList.get(i).getSize().equals(petSearchSize.get(j))) {
                        petFilter(i);
                        count++;

                    }
                }
            }
            resultFound.setText(String.valueOf(count));
            petAdapter = new PetListViewAdapter(getContext(), petSearchFilter);
            listView.setAdapter(petAdapter);
        }

        //Choose Breed
        if(color.equals("ไม่ระบุ") && sex == null && petSearchAge.isEmpty() && petSearchSize.isEmpty() && breed != null) {
            int count = 0;
            for (int i = 0; i < petSearchList.size(); i++) {
                if (petSearchList.get(i).getBreed().equals(breed)) {
                    petFilter(i);
                    count++;

                }
            }
            resultFound.setText(String.valueOf(count));
            petAdapter = new PetListViewAdapter(getContext(), petSearchFilter);
            listView.setAdapter(petAdapter);
        }

        //Not Choose
        if(color.equals("ไม่ระบุ") && sex == null && petSearchAge.isEmpty() && petSearchSize.isEmpty() && breed.isEmpty()) {
            int count = 0;
            for (int i = 0; i < petSearchList.size(); i++) {
                petFilter(i);
                count++;
            }
            resultFound.setText(String.valueOf(count));
            petAdapter = new PetListViewAdapter(getContext(), petSearchFilter);
            listView.setAdapter(petAdapter);
        }
    }

    private void petFilter(int i) {
        PetSearch petFilter = new PetSearch(petSearchList.get(i).getID(), petSearchList.get(i).getName(),
                petSearchList.get(i).getType(), petSearchList.get(i).getColour(), petSearchList.get(i).getSex(),
                petSearchList.get(i).getAge(), petSearchList.get(i).getBreed(), petSearchList.get(i).getSize(),petSearchList.get(i).getUrl(),
                petSearchList.get(i).getWeight(), petSearchList.get(i).getCharacter(), petSearchList.get(i).getMarking(),
                petSearchList.get(i).getHealth(), petSearchList.get(i).getFoundLoc(), petSearchList.get(i).getStatus(),
                petSearchList.get(i).getStory(), petSearchList.get(i).getShelterID());
        petSearchFilter.add(petFilter);
    }

    private void saveIntoHistory(String KEY_PETID, final String petID) {
        dataToSave.put(KEY_COUNT, 3);
        dataToSave.put(KEY_PETID,petID);
        db.collection("User")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("History")
                .document(petID)
                .set(dataToSave).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("Y", "saved_1");
            }
        });
    }
}

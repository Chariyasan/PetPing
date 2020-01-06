package com.example.petping;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SearchFragment extends Fragment {

    private CheckBox dogBox;
    private CheckBox catBox;
    private CheckBox rabbitBox;
    private CheckBox otherBox;
    private Spinner spinColor;
    private RadioButton maleBtn;
    private RadioButton femaleBtn;
    private Button searchButton;
    private RadioGroup radioGroupSex;
    private RadioButton radioButton;
    private CheckBox ageLeastOne;
    private CheckBox ageOnetoFive;
    private CheckBox ageFivetoTen;
    private CheckBox ageTentoFiveteen;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String dog;
    private String cat;
    private String rabbit;
    private String color;
    private String sex;
    private List<String> petSearchType = new ArrayList<>();
    private List<String> searchResult = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View temp = inflater.inflate(R.layout.fragment_search, null);

        //Type
        dogBox = temp.findViewById(R.id.cb_dog);
        catBox = temp.findViewById(R.id.cb_cat);
        rabbitBox = temp.findViewById(R.id.cb_rabbit);
        otherBox = temp.findViewById(R.id.cb_other);

        //Colour
        spinColor = temp.findViewById(R.id.color_spin);
        ArrayAdapter<CharSequence> colorAdapter = ArrayAdapter.createFromResource(getContext(), R.array.color_array, android.R.layout.simple_spinner_item);
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinColor.setAdapter(colorAdapter);

        //Sex
        maleBtn = temp.findViewById(R.id.rd_male);
        femaleBtn = temp.findViewById(R.id.rd_female);
        radioGroupSex = temp.findViewById(R.id.rd_sex);

        //Age
        ageLeastOne = temp.findViewById(R.id.cb_age_least1y);
        ageOnetoFive = temp.findViewById(R.id.cb_age_1to5y);
        ageFivetoTen = temp.findViewById(R.id.cb_age_5to10y);
        ageTentoFiveteen = temp.findViewById(R.id.cb_age_10to15y);

        //Search
        searchButton = temp.findViewById(R.id.pet_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dog = dogBox.getText().toString();
                cat = catBox.getText().toString();
                rabbit = rabbitBox.getText().toString();

                petTypeChoose(dog, cat, rabbit);
                petColorChoose();
                petAgeChoose();

                int radioSex = radioGroupSex.getCheckedRadioButtonId();
                radioButton = temp.findViewById(radioSex);
                if(radioButton == temp.findViewById(R.id.rd_male)){
                    sex = maleBtn.getText().toString();
                    searchResult.add(sex);
                    Log.d("Sex", sex);
                }
                if(radioButton == temp.findViewById(R.id.rd_female)){
                    sex = femaleBtn.getText().toString();
                    searchResult.add(sex);
                    Log.d("Sex", sex);
                }
                searchPetResult();
                searchResult.clear();
            }
        });

        return temp;
    }

    private void petAgeChoose() {
        if (ageLeastOne.isChecked()){

        }
    }


    private void petColorChoose() {
        color = spinColor.getSelectedItem().toString();
        if(color != "อื่นๆ"){
            searchResult.add(color);
        }
        else searchResult.remove(color);
    }

    private void petTypeChoose(String dog, String cat, String rabbit) {
        if(dogBox.isChecked()){
            petSearchType.add(dog);
            searchResult.add(dog);
        }
        else{
            petSearchType.remove(dog);
        }

        if(catBox.isChecked()){
            petSearchType.add(cat);
            searchResult.add(cat);
        }
        else{
            petSearchType.remove(cat);
        }

        if(rabbitBox.isChecked()) {
            petSearchType.add(rabbit);
            searchResult.add(rabbit);
        }
        else {
            petSearchType.remove(rabbit);
        }

    }

    private void searchPetResult(){
        if(!petSearchType.isEmpty() && !color.equals("เลือกสี") && sex != null){
            for(int i = 0; i < searchResult.size(); i++){
                db.collection("Pet")
                        .whereEqualTo("Color", color)
                        .whereEqualTo("Sex", sex)
                        .whereIn("Type", Arrays.asList(petSearchType.get(i)))
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d("DataTest", document.getId() + " => " + document.getData());
                                    }
                                } else {
                                    Log.d("Error", "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }
        }
        if(sex == null){
           for(int i = 0; i < searchResult.size(); i++){
               db.collection("Pet")
                       .whereEqualTo("Color", color)
                       .whereIn("Type", Arrays.asList(petSearchType.get(i)))
                       .get()
                       .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                           @Override
                           public void onComplete(@NonNull Task<QuerySnapshot> task) {
                               if (task.isSuccessful()) {
                                   for (QueryDocumentSnapshot document : task.getResult()) {
                                       Log.d("DataTest", document.getId() + " => " + document.getData());
                                   }
                               } else {
                                   Log.d("Error", "Error getting documents: ", task.getException());
                               }
                           }
                       });
           }
       }

      if(sex == null && color.equals("เลือกสี")) {
          for (int i = 0; i < searchResult.size(); i++) {
              db.collection("Pet")
                      .whereIn("Type", Arrays.asList(petSearchType.get(i)))
                      .get()
                      .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                          @Override
                          public void onComplete(@NonNull Task<QuerySnapshot> task) {
                              if (task.isSuccessful()) {
                                  for (QueryDocumentSnapshot document : task.getResult()) {
                                      Log.d("DataTest", document.getId() + " => " + document.getData());
                                  }
                              } else {
                                  Log.d("Error", "Error getting documents: ", task.getException());
                              }
                          }
                      });
          }
      }

    }

}

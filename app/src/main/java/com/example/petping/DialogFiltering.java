package com.example.petping;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


public class DialogFiltering extends DialogFragment {
    private CheckBox ageLeastOne;
    private CheckBox ageOnetoFive;
    private CheckBox ageFivetoTen;
    private CheckBox ageTenUp;
    private CheckBox sizeS;
    private CheckBox sizeM;
    private CheckBox sizeL;
    private Spinner spinColor, spinBreed;
    private RadioGroup radioGroupSex;
    private RadioButton radioButton;
    private RadioButton maleBtn;
    private RadioButton femaleBtn;
    public  filterSelected mOnInputSelected;
    private ArrayList<String> petSearchAge = new ArrayList<>();
    private ArrayList<String> petSearchSize = new ArrayList<>();
    private String type;
    private String sex;
    private String color;
    private String breed;
    private TextView textS, textM, textL;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.layout_dialog_filtering, null);
        if(getArguments() != null){
            type = (String) getArguments().getString("type");
        }
        //Age
        ageLeastOne = view.findViewById(R.id.cb_age_least1y);
        ageOnetoFive = view.findViewById(R.id.cb_age_1to5y);
        ageFivetoTen = view.findViewById(R.id.cb_age_5to10y);
        ageTenUp = view.findViewById(R.id.cb_age_10_up);

//        spinColor = view.findViewById(R.id.color_spinner);
//        ArrayAdapter<CharSequence> colorAdapter = ArrayAdapter.createFromResource(getContext(), R.array.color_array, android.R.layout.simple_spinner_item);
//        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinColor.setAdapter(colorAdapter);

        //Color
        final CollectionReference collection = db.collection("PetColor");
        spinColor = view.findViewById(R.id.color_spinner);
        final List<String> colorList = new ArrayList<>();
        final ArrayAdapter<String> colorAdapter =  new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, colorList );
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinColor.setAdapter(colorAdapter);

        //Breed
//        spinBreed = view.findViewById(R.id.breed_spinner);
//        final List<String> breedList = new ArrayList<>();
//        final ArrayAdapter<String> breedAdapter =  new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, breedList );
//        breedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinBreed.setAdapter(breedAdapter);

        final AutoCompleteTextView breedSelected = view.findViewById(R.id.select_breed);
        final List<String> breedList = new ArrayList<>();
        final ArrayAdapter<String> breedAdapter =  new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, breedList);
        breedSelected.setAdapter(breedAdapter);

//        breedSelected.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                breedSelected.showDropDown();
//            }
//        });
        //Sex
        radioGroupSex = view.findViewById(R.id.rd_sex);
        maleBtn = view.findViewById(R.id.rd_male);
        femaleBtn = view.findViewById(R.id.rd_female);

        //Size
        textS = view.findViewById(R.id.text_size_s);
        textM = view.findViewById(R.id.text_size_m);
        textL = view.findViewById(R.id.text_size_l);
        sizeS = view.findViewById(R.id.cb_size_s);
        sizeM = view.findViewById(R.id.cb_size_m);
        sizeL = view.findViewById(R.id.cb_size_l);
        if(type.equals("สุนัข")){
            textS.setText("1-5 กิโลกรัม");
            textM.setText("5-10 กิโลกรัม");
            textL.setText("10 กิโลกรัมชึ้นไป");
            textS.setVisibility(View.VISIBLE);
            textM.setVisibility(View.VISIBLE);
            textL.setVisibility(View.VISIBLE);

            collection.document("Dog").get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            for(Object key: documentSnapshot.getData().values()){
                                colorList.add(key.toString());
                            }
                            Collections.sort(colorList);
                            colorList.add(0,"ไม่ระบุ");
                            colorAdapter.notifyDataSetChanged();
                        }
                    });

            db.collection("Pet")
                    .whereEqualTo("Type", "สุนัข")
                    .whereEqualTo("Status", "กำลังหาบ้าน")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String breed = document.getString("Breed");
                                    breedList.add(breed);
                                }
                                Set<String> set = new HashSet<>(breedList);
                                breedList.clear();
                                breedList.addAll(set);
                                Collections.sort(breedList);
//                                breedAdapter.notifyDataSetChanged();

                        }
                    });
        }
        if(type.equals("แมว")){
            textS.setText("1-3 กิโลกรัม");
            textM.setText("3-6 กิโลกรัม");
            textL.setText("6 กิโลกรัมชึ้นไป");
            textS.setVisibility(View.VISIBLE);
            textM.setVisibility(View.VISIBLE);
            textL.setVisibility(View.VISIBLE);

            collection.document("Cat").get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            for(Object key: documentSnapshot.getData().values()){
                                colorList.add(key.toString());
                            }
                            Collections.sort(colorList);
                            colorList.add(0,"ไม่ระบุ");
                            colorAdapter.notifyDataSetChanged();
                        }
                    });

            db.collection("Pet")
                    .whereEqualTo("Type", "แมว")
                    .whereEqualTo("Status", "กำลังหาบ้าน")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String breed = document.getString("Breed");
                                    breedList.add(breed);
                                }
                                Set<String> set = new HashSet<>(breedList);
                                breedList.clear();
                                breedList.addAll(set);
                                Collections.sort(breedList);
//                                breedAdapter.notifyDataSetChanged();
                        }
                    });
        }

        builder.setView(view)
                .setTitle("ค้นหาน้องที่ต้องการ")
                .setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getDialog().dismiss();
                    }
                })
                .setPositiveButton("ค้นหา", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(ageLeastOne.isChecked()){
                            petSearchAge.add("0 ปี");
                        }
                        if(ageOnetoFive.isChecked()){
                            petSearchAge.add("1 ปี");
                            petSearchAge.add("2 ปี");
                            petSearchAge.add("3 ปี");
                            petSearchAge.add("4 ปี");
                            petSearchAge.add("5 ปี");
                        }
                        if(ageFivetoTen.isChecked()){
                            petSearchAge.add("6 ปี");
                            petSearchAge.add("7 ปี");
                            petSearchAge.add("8 ปี");
                            petSearchAge.add("9 ปี");
                            petSearchAge.add("10 ปี");
                        }
                        if(ageTenUp.isChecked()){
                            petSearchAge.add("10 ปี");
                        }
                        if(sizeS.isChecked()){
                            petSearchSize.add("S");
                            petSearchSize.add("s");
                        }
                        if(sizeM.isChecked()){
                            petSearchSize.add("M");
                            petSearchSize.add("m");
                        }
                        if(sizeL.isChecked()){
                            petSearchSize.add("L");
                            petSearchSize.add("l");
                        }
                        int radioSex = radioGroupSex.getCheckedRadioButtonId();
                        radioButton = view.findViewById(radioSex);
                        if(radioButton == view.findViewById(R.id.rd_male)){
                            sex = maleBtn.getText().toString();
                            radioGroupSex.clearCheck();
                        }
                        else if(radioButton == view.findViewById(R.id.rd_female)){
                            sex = femaleBtn.getText().toString();
                            radioGroupSex.clearCheck();
                        }
                        color = spinColor.getSelectedItem().toString();
                        breed = breedSelected.getText().toString();
                        Log.d("Breed", breed);
                        mOnInputSelected.sendFiltering(color, sex, petSearchAge, petSearchSize, breed);
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnInputSelected = (filterSelected) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }

    public interface filterSelected{
        void sendFiltering(String color, String sex, ArrayList<String> petSearchAge, ArrayList<String> petSearchSize, String breed);
    }
}

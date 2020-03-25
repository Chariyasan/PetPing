package com.example.petping;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class AdoptionQAFragment extends Fragment {
    private ViewFlipper flipper;
    private ImageButton btnOne, btnTwoB,btnTwoA;
    private ImageButton btnThreeB,btnThreeA;
    private ImageButton btnFourB,btnFourA;
    private ImageButton btnFiveB,btnFiveA;
    private ImageButton btnSixB,btnSixA;
    private ImageButton btnSevenB,btnSevenA;
    private ImageButton btnEightB,btnEightA;
    private ImageButton btnNineB,btnNineA;
    private ImageButton btnTenB,btnTenA;
    private ImageButton btnEleven;
    private Button btnNext;
    private TextView oneQ, twoQ, threeQ, fourQ, fiveQ, sixQ, sevenQ;
    private TextView eightQ, nineQ, tenQ, elevenQ;
    private EditText oneA, twoA, threeA, fourA, fiveA, sixA, sevenA;
    private EditText eightA, nineA, tenA, elevenA;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<PetSearch> petProfileList;
    private String ID;
    private Map<String, Object> adop = new HashMap<>();
    private String one, two, three, four, five, six, seven, eight, nine, ten, eleven;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_adoption_qa_process, null);
        if(getArguments() != null){
            petProfileList = getArguments().getParcelableArrayList("petProfile");
        }
        flipper = view.findViewById(R.id.flipper_qa_process);
        oneQ = view.findViewById(R.id.qa_one_q);
        twoQ = view.findViewById(R.id.qa_two_q);
        threeQ = view.findViewById(R.id.qa_three_q);
        fourQ = view.findViewById(R.id.qa_four_q);
        fiveQ = view.findViewById(R.id.qa_five_q);
        sixQ = view.findViewById(R.id.qa_six_q);
        sevenQ = view.findViewById(R.id.qa_seven_q);
        eightQ = view.findViewById(R.id.qa_eight_q);
        nineQ = view.findViewById(R.id.qa_nine_q);
        tenQ = view.findViewById(R.id.qa_ten_q);
        elevenQ = view.findViewById(R.id.qa_eleven_q);

        oneA = view.findViewById(R.id.qa_one_a);
        twoA = view.findViewById(R.id.qa_two_a);
        threeA = view.findViewById(R.id.qa_three_a);
        fourA = view.findViewById(R.id.qa_four_a);
        fiveA = view.findViewById(R.id.qa_five_a);
        sixA = view.findViewById(R.id.qa_six_a);
        sevenA = view.findViewById(R.id.qa_seven_a);
        eightA = view.findViewById(R.id.qa_eight_a);
        nineA = view.findViewById(R.id.qa_nine_a);
        tenA = view.findViewById(R.id.qa_ten_a);
        elevenA = view.findViewById(R.id.qa_eleven_a);
        db.collection("Information")
                .document("BasicQ")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        oneQ.setText(String.valueOf(documentSnapshot.get("one")));
                        twoQ.setText(String.valueOf(documentSnapshot.get("two")));
                        threeQ.setText(String.valueOf(documentSnapshot.get("three")));
                        fourQ.setText(String.valueOf(documentSnapshot.get("four")));
                        fiveQ.setText(String.valueOf(documentSnapshot.get("five")));
                        sixQ.setText(String.valueOf(documentSnapshot.get("six")));
                        sevenQ.setText(String.valueOf(documentSnapshot.get("seven")));
                        eightQ.setText(String.valueOf(documentSnapshot.get("eight")));
                        nineQ.setText(String.valueOf(documentSnapshot.get("nine")));
                        tenQ.setText(String.valueOf(documentSnapshot.get("ten")));
                        elevenQ.setText(String.valueOf(documentSnapshot.get("eleven")));
                    }
                });


        btnOne = view.findViewById(R.id.qa_one_btn);
        btnOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                db.collection("Information")
//                        .document("BasicQ")
//                        .get()
//                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                            @Override
//                            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                                twoQ.setText(String.valueOf(documentSnapshot.get("two")));
//                            }
//                        });
                one = oneA.getText().toString();
                if(one.isEmpty()){
                    showMessage();
                }
                else {
                    flipper.setDisplayedChild(flipper.indexOfChild(view.findViewById(R.id.qa_two)));
                }
            }
        });

        btnTwoB = view.findViewById(R.id.qa_two_btnB);
        btnTwoA = view.findViewById(R.id.qa_two_btnA);
        btnTwoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipper.setDisplayedChild(flipper.indexOfChild(view.findViewById(R.id.qa_one)));
            }
        });
        btnTwoA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                two = twoA.getText().toString();
                if(two.isEmpty()){
                    showMessage();
                }
                else {
                    flipper.setDisplayedChild(flipper.indexOfChild(view.findViewById(R.id.qa_three)));
                }

            }
        });

        btnThreeB = view.findViewById(R.id.qa_three_btnB);
        btnThreeA = view.findViewById(R.id.qa_three_btnA);
        btnThreeB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipper.setDisplayedChild(flipper.indexOfChild(view.findViewById(R.id.qa_two)));
            }
        });
        btnThreeA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                three = threeA.getText().toString();
                if(three.isEmpty()){
                    showMessage();
                }
                else {
                    flipper.setDisplayedChild(flipper.indexOfChild(view.findViewById(R.id.qa_four)));
                }
            }
        });

        btnFourB = view.findViewById(R.id.qa_four_btnB);
        btnFourA = view.findViewById(R.id.qa_four_btnA);
        btnFourB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipper.setDisplayedChild(flipper.indexOfChild(view.findViewById(R.id.qa_three)));
            }
        });
        btnFourA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                four = fourA.getText().toString();
                if(four.isEmpty()){
                    showMessage();
                }
                else {
                    flipper.setDisplayedChild(flipper.indexOfChild(view.findViewById(R.id.qa_five)));
                }
            }
        });

        btnFiveB = view.findViewById(R.id.qa_five_btnB);
        btnFiveA = view.findViewById(R.id.qa_five_btnA);
        btnFiveB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipper.setDisplayedChild(flipper.indexOfChild(view.findViewById(R.id.qa_four)));
            }
        });
        btnFiveA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipper.setDisplayedChild(flipper.indexOfChild(view.findViewById(R.id.qa_six)));
            }
        });

        btnSixB = view.findViewById(R.id.qa_six_btnB);
        btnSixA = view.findViewById(R.id.qa_six_btnA);
        btnSixB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipper.setDisplayedChild(flipper.indexOfChild(view.findViewById(R.id.qa_five)));
            }
        });
        btnSixA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipper.setDisplayedChild(flipper.indexOfChild(view.findViewById(R.id.qa_seven)));
            }
        });

        btnSevenB = view.findViewById(R.id.qa_seven_btnB);
        btnSevenA = view.findViewById(R.id.qa_seven_btnA);
        btnSevenB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipper.setDisplayedChild(flipper.indexOfChild(view.findViewById(R.id.qa_six)));
            }
        });
        btnSevenA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seven = sevenA.getText().toString();
                if(seven.isEmpty()){
                    showMessage();
                }
                else {
                    flipper.setDisplayedChild(flipper.indexOfChild(view.findViewById(R.id.qa_eight)));
                }
            }
        });

        btnEightB = view.findViewById(R.id.qa_eight_btnB);
        btnEightA = view.findViewById(R.id.qa_eight_btnA);
        btnEightB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipper.setDisplayedChild(flipper.indexOfChild(view.findViewById(R.id.qa_seven)));
            }
        });
        btnEightA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eight = eightA.getText().toString();
                if(eight.isEmpty()){
                    showMessage();
                }
                else {
                    flipper.setDisplayedChild(flipper.indexOfChild(view.findViewById(R.id.qa_nine)));
                }
            }
        });

        btnNineB = view.findViewById(R.id.qa_nine_btnB);
        btnNineA = view.findViewById(R.id.qa_nine_btnA);
        btnNineB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipper.setDisplayedChild(flipper.indexOfChild(view.findViewById(R.id.qa_eight)));
            }
        });
        btnNineA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nine = nineA.getText().toString();
                if(nine.isEmpty()){
                    showMessage();
                }
                else {
                    flipper.setDisplayedChild(flipper.indexOfChild(view.findViewById(R.id.qa_ten)));
                }
            }
        });

        btnTenB = view.findViewById(R.id.qa_ten_btnB);
        btnTenA = view.findViewById(R.id.qa_ten_btnA);
        btnTenB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipper.setDisplayedChild(flipper.indexOfChild(view.findViewById(R.id.qa_nine)));
            }
        });
        btnTenA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ten = tenA.getText().toString();
                if(ten.isEmpty()){
                    showMessage();
                }
                else {
                    flipper.setDisplayedChild(flipper.indexOfChild(view.findViewById(R.id.qa_eleven)));
                }
            }
        });

        btnEleven = view.findViewById(R.id.qa_eleven_btn);
        btnEleven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                flipper.setDisplayedChild(flipper.indexOfChild(view.findViewById(R.id.qa_ten)));
            }
        });

        btnNext = view.findViewById(R.id.qa_next_button);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                flipper.setDisplayedChild(flipper.indexOfChild(view.findViewById(R.id.qa_waiting)));

                Map<String, Object> data = new HashMap<>();
                one = oneA.getText().toString();
                two = twoA.getText().toString();
                three = threeA.getText().toString();
                four = fourA.getText().toString();
                five = fiveA.getText().toString();
                six = sixA.getText().toString();
                seven = sevenA.getText().toString();
                eight = eightA.getText().toString();
                nine = nineA.getText().toString();
                ten = tenA.getText().toString();
                eleven = elevenA.getText().toString();
                if (eleven.isEmpty()) {
                    showMessage();
                }


                data.put("one", one);
                data.put("two", two);
                data.put("three", three);
                data.put("four", four);
                data.put("five", five);
                data.put("six", six);
                data.put("seven", seven);
                data.put("eight", eight);
                data.put("nine", nine);
                data.put("ten", ten);
                data.put("eleven", eleven);
                for(int i=0; i<petProfileList.size(); i++) {
                    ID = petProfileList.get(i).getID();
                }
                db.collection("RequestAdoption")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .collection("BasicQ")
                        .document(ID)
                        .set(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("Writing", "DocumentSnapshot successfully written!");
                            }
                        });

                db.collection("User")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .collection("Information")
                        .document("Information")
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                adopProcess(documentSnapshot);
                            }
                        });
            }
        });
        return view;
    }

    private void adopProcess(DocumentSnapshot documentSnapshot) {
        for(int i=0; i<petProfileList.size(); i++) {
            db.collection("Pet")
                    .document(petProfileList.get(i).getID())
                    .update("Status", "อยู่ระหว่างดำเนินการ")
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("update status", "DocumentSnapshot successfully updated!");
                        }
                    });
            adop.put("UserName", documentSnapshot.get("UserName").toString());
            adop.put("UserImage", documentSnapshot.get("Image").toString());
            adop.put("petID", petProfileList.get(i).getID());
            adop.put("petName", petProfileList.get(i).getName());
            adop.put("petType", petProfileList.get(i).getType());
            adop.put("petColor", petProfileList.get(i).getColour());
            adop.put("petSex", petProfileList.get(i).getSex());
            adop.put("petAge", petProfileList.get(i).getAge());
            adop.put("petBreed", petProfileList.get(i).getBreed());
            adop.put("petSize", petProfileList.get(i).getSize());
            adop.put("petURL", petProfileList.get(i).getUrl());
            adop.put("petWeight", petProfileList.get(i).getWeight());
            adop.put("petCharacter", petProfileList.get(i).getCharacter());
            adop.put("petMarking", petProfileList.get(i).getMarking());
            adop.put("petHealth", petProfileList.get(i).getHealth());
            adop.put("petFoundLoc", petProfileList.get(i).getFoundLoc());
            adop.put("petStatus", "อยู่ระหว่างดำเนินการ");
            adop.put("petStory", petProfileList.get(i).getStory());

            db.collection("RequestAdoption")
                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .collection("Adoption")
                    .document(petProfileList.get(i).getID())
                    .set(adop)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("Writing", "DocumentSnapshot successfully written!");
                        }
                    });
        }

        HashMap<String, Object> data1 = new HashMap<>();
        String ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        data1.put(documentSnapshot.get("UserName").toString(), ID);
        db.collection("User1")
                .document("userID")
                .update(data1)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Writing", "DocumentSnapshot successfully written!");
                    }
                });

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(getId(), new AdoptionWaitingFragment());
        ft.commit();
    }

    private void showMessage() {
        Toast.makeText(getContext(), "กรุณาตอบคำถามให้ครบถ้วนค่ะ", Toast.LENGTH_LONG).show();
    }
}

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

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_adoption_qa_process, null);
        if(getArguments() != null){
            petProfileList = (ArrayList<PetSearch>)getArguments().getSerializable("petProfile");
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
                    }
                });


        btnOne = view.findViewById(R.id.qa_one_btn);
        btnOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Information")
                        .document("BasicQ")
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                twoQ.setText(String.valueOf(documentSnapshot.get("two")));
                            }
                        });
                flipper.setDisplayedChild(flipper.indexOfChild(view.findViewById(R.id.qa_two)));
            }
        });

        btnTwoB = view.findViewById(R.id.qa_two_btnB);
        btnTwoA = view.findViewById(R.id.qa_two_btnA);
        btnTwoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipper.setDisplayedChild(flipper.indexOfChild(view.findViewById(R.id.qa_one)));
                db.collection("Information")
                        .document("BasicQ")
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                oneQ.setText(String.valueOf(documentSnapshot.get("one")));
                            }
                        });
            }
        });
        btnTwoA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Information")
                        .document("BasicQ")
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                threeQ.setText(String.valueOf(documentSnapshot.get("three")));
                            }
                        });
                flipper.setDisplayedChild(flipper.indexOfChild(view.findViewById(R.id.qa_three)));
            }
        });

        btnThreeB = view.findViewById(R.id.qa_three_btnB);
        btnThreeA = view.findViewById(R.id.qa_three_btnA);
        btnThreeB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Information")
                        .document("BasicQ")
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                twoQ.setText(String.valueOf(documentSnapshot.get("two")));
                            }
                        });
                flipper.setDisplayedChild(flipper.indexOfChild(view.findViewById(R.id.qa_two)));
            }
        });
        btnThreeA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Information")
                        .document("BasicQ")
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                fourQ.setText(String.valueOf(documentSnapshot.get("four")));
                            }
                        });
                flipper.setDisplayedChild(flipper.indexOfChild(view.findViewById(R.id.qa_four)));
            }
        });

        btnFourB = view.findViewById(R.id.qa_four_btnB);
        btnFourA = view.findViewById(R.id.qa_four_btnA);
        btnFourB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Information")
                        .document("BasicQ")
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                threeQ.setText(String.valueOf(documentSnapshot.get("three")));
                            }
                        });
                flipper.setDisplayedChild(flipper.indexOfChild(view.findViewById(R.id.qa_three)));
            }
        });
        btnFourA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Information")
                        .document("BasicQ")
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                fiveQ.setText(String.valueOf(documentSnapshot.get("five")));
                            }
                        });
                flipper.setDisplayedChild(flipper.indexOfChild(view.findViewById(R.id.qa_five)));
            }
        });

        btnFiveB = view.findViewById(R.id.qa_five_btnB);
        btnFiveA = view.findViewById(R.id.qa_five_btnA);
        btnFiveB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Information")
                        .document("BasicQ")
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                fourQ.setText(String.valueOf(documentSnapshot.get("four")));
                            }
                        });
                flipper.setDisplayedChild(flipper.indexOfChild(view.findViewById(R.id.qa_four)));
            }
        });
        btnFiveA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Information")
                        .document("BasicQ")
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                sixQ.setText(String.valueOf(documentSnapshot.get("six")));
                            }
                        });
                flipper.setDisplayedChild(flipper.indexOfChild(view.findViewById(R.id.qa_six)));
            }
        });

        btnSixB = view.findViewById(R.id.qa_six_btnB);
        btnSixA = view.findViewById(R.id.qa_six_btnA);
        btnSixB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Information")
                        .document("BasicQ")
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                fiveQ.setText(String.valueOf(documentSnapshot.get("five")));
                            }
                        });
                flipper.setDisplayedChild(flipper.indexOfChild(view.findViewById(R.id.qa_five)));
            }
        });
        btnSixA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Information")
                        .document("BasicQ")
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                sevenQ.setText(String.valueOf(documentSnapshot.get("seven")));
                            }
                        });
                flipper.setDisplayedChild(flipper.indexOfChild(view.findViewById(R.id.qa_seven)));
            }
        });

        btnSevenB = view.findViewById(R.id.qa_seven_btnB);
        btnSevenA = view.findViewById(R.id.qa_seven_btnA);
        btnSevenB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Information")
                        .document("BasicQ")
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                sixQ.setText(String.valueOf(documentSnapshot.get("six")));
                            }
                        });
                flipper.setDisplayedChild(flipper.indexOfChild(view.findViewById(R.id.qa_six)));
            }
        });
        btnSevenA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Information")
                        .document("BasicQ")
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                eightQ.setText(String.valueOf(documentSnapshot.get("eight")));
                            }
                        });
                flipper.setDisplayedChild(flipper.indexOfChild(view.findViewById(R.id.qa_eight)));
            }
        });

        btnEightB = view.findViewById(R.id.qa_eight_btnB);
        btnEightA = view.findViewById(R.id.qa_eight_btnA);
        btnEightB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Information")
                        .document("BasicQ")
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                sevenQ.setText(String.valueOf(documentSnapshot.get("seven")));
                            }
                        });
                flipper.setDisplayedChild(flipper.indexOfChild(view.findViewById(R.id.qa_seven)));
            }
        });
        btnEightA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Information")
                        .document("BasicQ")
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                nineQ.setText(String.valueOf(documentSnapshot.get("nine")));
                            }
                        });
                flipper.setDisplayedChild(flipper.indexOfChild(view.findViewById(R.id.qa_nine)));
            }
        });

        btnNineB = view.findViewById(R.id.qa_nine_btnB);
        btnNineA = view.findViewById(R.id.qa_nine_btnA);
        btnNineB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Information")
                        .document("BasicQ")
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                eightQ.setText(String.valueOf(documentSnapshot.get("eight")));
                            }
                        });
                flipper.setDisplayedChild(flipper.indexOfChild(view.findViewById(R.id.qa_eight)));
            }
        });
        btnNineA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Information")
                        .document("BasicQ")
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                tenQ.setText(String.valueOf(documentSnapshot.get("ten")));
                            }
                        });
                flipper.setDisplayedChild(flipper.indexOfChild(view.findViewById(R.id.qa_ten)));
            }
        });

        btnTenB = view.findViewById(R.id.qa_ten_btnB);
        btnTenA = view.findViewById(R.id.qa_ten_btnA);
        btnTenB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Information")
                        .document("BasicQ")
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                nineQ.setText(String.valueOf(documentSnapshot.get("nine")));
                            }
                        });
                flipper.setDisplayedChild(flipper.indexOfChild(view.findViewById(R.id.qa_nine)));
            }
        });
        btnTenA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Information")
                        .document("BasicQ")
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                elevenQ.setText(String.valueOf(documentSnapshot.get("eleven")));
                            }
                        });
                flipper.setDisplayedChild(flipper.indexOfChild(view.findViewById(R.id.qa_eleven)));
            }
        });

        btnEleven = view.findViewById(R.id.qa_eleven_btn);
        btnEleven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Information")
                        .document("BasicQ")
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                tenQ.setText(String.valueOf(documentSnapshot.get("ten")));
                            }
                        });

                flipper.setDisplayedChild(flipper.indexOfChild(view.findViewById(R.id.qa_ten)));
            }
        });

        btnNext = view.findViewById(R.id.qa_next_button);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                flipper.setDisplayedChild(flipper.indexOfChild(view.findViewById(R.id.qa_waiting)));

                Map<String, Object> data = new HashMap<>();
                String one = oneA.getText().toString();
                String two = twoA.getText().toString();
                String three = threeA.getText().toString();
                String four = fourA.getText().toString();
                String five = fiveA.getText().toString();
                String six = sixA.getText().toString();
                String seven = sevenA.getText().toString();
                String eight = eightA.getText().toString();
                String nine = nineA.getText().toString();
                String ten = tenA.getText().toString();
                String eleven = elevenA.getText().toString();

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

                db.collection("RequestAdoption")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .collection("Information")
                        .document("BasicQ")
                        .set(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("Writing", "DocumentSnapshot successfully written!");
                            }
                        });

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
                    Map<String, Object> adop = new HashMap<>();
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
                            .collection("Information")
                            .document("Adoption")
                            .set(adop)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("Writing", "DocumentSnapshot successfully written!");
                                }
                            });
                }

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(getId(), new AdoptionWaitingFragment());
                ft.commit();
            }
        });
        return view;
    }
}

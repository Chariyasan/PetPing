package com.example.petping;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.tensorflow.lite.Interpreter;

public class HomeFragment extends Fragment {
    private ViewFlipper flipper;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<PetSearch> petList = new ArrayList<>();
    private ArrayList<PetSearch> petRec = new ArrayList<>();
    private ArrayList<Content> contentList = new ArrayList<>();
    private HomeAdapter homeAdapter;
    private ContentHomeAdapter contentAdapter;
    private RecyclerView pet_rec, contentView;
    private TextView seePet;
    private String modelFile="C:/Users/User/Downloads/graph.tflite";
    private Interpreter tflite;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, null);
        int image[]= {R.drawable.flip1, R.drawable.flip2, R.drawable.flip3};
        flipper = view.findViewById(R.id.flipper_home);
        seePet = view.findViewById(R.id.see_pet);

        try {
            tflite = new Interpreter(loadModelFile());
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        pet_rec = view.findViewById(R.id.pet_rec);
        pet_rec.setLayoutManager(layoutManager);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        contentView = view.findViewById(R.id.content);
        contentView.setLayoutManager(layoutManager1);

        for(int i=0; i<image.length; i++){
            flipperImages(image[i]);
        }
        Log.d("TFFF", String.valueOf(tflite));
        if(!petList.isEmpty()){
            petList.clear();
        }
        if(!contentList.isEmpty()){
            contentList.clear();
        }

//        db.collection("User")
//                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                .collection("Like")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if(task.isSuccessful()){
//                            for(final QueryDocumentSnapshot document : task.getResult()){
//                                String petID = document.getId();
//                                String[][] in = new String[][]{{petID,petID}};
//                                String[][] out =new String[][]{{"0"}};
//                                tflite.run(in, out);
//                            }
//                        }
//                    }
//                });
        db.collection("Content")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Content content = new Content(document.getId(), document.get("Topic").toString(),
                                        document.get("Story").toString(), document.get("URL").toString(), document.get("ShelterID").toString(),
                                        document.get("Date").toString(), document.get("Time").toString());
                                contentList.add(content);
//                                Log.d("Content",document.get("Topic").toString());
                            }
                            contentAdapter = new ContentHomeAdapter(getFragmentManager(), getId(), getContext(), contentList);
                            contentView.setAdapter(contentAdapter);
                        }
                    }
                });


        db.collection("Pet")
                .whereEqualTo("Status", "กำลังหาบ้าน")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                PetSearch petSearch = new PetSearch(document.getId(), document.get("Name").toString(), document.get("Type").toString(),
                                        document.get("Color").toString(), document.get("Sex").toString(), document.get("Age").toString(),
                                        document.get("Breed").toString(), document.get("Size").toString(), document.get("Image").toString(),
                                        document.get("Weight").toString(), document.get("Character").toString(), document.get("Marking").toString(),
                                        document.get("Health").toString(), document.get("OriginalLocation").toString(), document.get("Status").toString(),
                                        document.get("Story").toString(), document.get("ShelterID").toString());
                                petList.add(petSearch);
                            }
                            homeAdapter = new HomeAdapter(getFragmentManager(), getId(), getContext(), petList);
                            pet_rec.setAdapter(homeAdapter);
                        }
                    }
                });

        seePet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecommendPetFragment pet = new RecommendPetFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("recommendPet", petList);
                pet.setArguments(bundle);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(getId(), pet);
                ft.addToBackStack(null).commit();
            }
        });

        return view;
    }

    public void flipperImages(int image){
        ImageView img = new ImageView(getContext());
        img.setBackgroundResource(image);
        flipper.addView(img);
        flipper.setFlipInterval(4000);
        flipper.setAutoStart(true);
    }

    private MappedByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor = getActivity().getAssets().openFd(modelFile);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);

    }
}

package com.example.petping;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.io.InputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

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
    private String modelFile="converted_model_test.tflite";

    private Interpreter tflite;
    private String modelFile2 = "ranking_v1.xls";
    private int rows ;
    private int cols ;
    private String[] pet;
    private int index;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, null);
        int image[]= {R.drawable.flip1, R.drawable.flip2, R.drawable.flip3};
        flipper = view.findViewById(R.id.flipper_home);
        seePet = view.findViewById(R.id.see_pet);
        try {
            tflite = new Interpreter(loadModelFile(modelFile));
        }  catch (IOException e) {
            e.printStackTrace();
        }
        final float[][][][] inp = new float[1][224][224][3];
        final float[][] out = new float[1][1000];

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        pet_rec = view.findViewById(R.id.pet_rec);
        pet_rec.setLayoutManager(layoutManager);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        contentView = view.findViewById(R.id.content);
        contentView.setLayoutManager(layoutManager1);

        for(int i=0; i<image.length; i++){
            flipperImages(image[i]);
        }

        if(!petList.isEmpty()){
            petList.clear();
        }
        if(!contentList.isEmpty()){
            contentList.clear();
        }

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


        db.collection("User")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("Like")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){

                            ArrayList<String> recommend =  new ArrayList<>();
                            for(final QueryDocumentSnapshot document : task.getResult()){
                                String recID =  document.get("Rec").toString();
                                recommend.add(recID);
                            }

                            if(recommend.isEmpty()){
                                randomWeightPet();
                            }
                            else{
                                recommendPet(recommend);
//                                int index = 0;
//                                for (Map.Entry<Integer, ArrayList<String>> entry : map.entrySet()) {
//                                    if(entry.getValue().equals(recommend)){
//                                        index = entry.getKey();
//                                        recommendPet(index);
//                                        Log.d("Index1", String.valueOf(index)+""+entry.getValue());
////                                        break;
//                                    }


//                                }

                            }

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


//
//    private void recommend(final float[][] out) {
//                db.collection("Pet")
//                .whereEqualTo("Status", "กำลังหาบ้าน")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            int[] output = new int[1000];
//                            float total =0;
//                            for(int i=0; i<1 ;i++){
//                                for(int j=0; j<1000; j++){
//                                    BigDecimal bigNum = new BigDecimal(out[i][j]);
//                                    String num = bigNum.toPlainString();
////                                    Log.d("Number", num);
//                                    total += Float.valueOf(num);
////                                    String num = String.valueOf(out[i][j]);
////                                    String[] parts = num.split(Pattern.quote("."));
////                                    String part = parts[0]; // 004
////                                    output[j] = Integer.parseInt(num);
////                                    Log.d("Num", String.valueOf(output[j] ));
//                                }
//                            }
////                            Log.d("Total", String.valueOf(total));
//
//                            Map<Integer,Integer> countMap = new HashMap<>();
//                            for(int i=0;i<output.length;i++){
//                                if(countMap.containsKey(output[i])){
//                                    countMap.put(output[i], countMap.get(output[i])+1 );
//                                }else{
//                                    countMap.put(output[i], 1);
//                                }
//                            }
//
//                            String key = null;
//                            String value=null;
//                            for (Integer in: countMap.keySet()){
//                                key = in.toString();
//                                value = countMap.get(in).toString();
////                                Log.d("key", key);
////                                Log.d("value", value);
//                                for (QueryDocumentSnapshot document : task.getResult()) {
//                                    if(document.get("Rec").toString().equals(key)){
//                                        PetSearch petSearch = new PetSearch(document.getId(), document.get("Name").toString(), document.get("Type").toString(),
//                                        document.get("Color").toString(), document.get("Sex").toString(), document.get("Age").toString(),
//                                        document.get("Breed").toString(), document.get("Size").toString(), document.get("Image").toString(),
//                                        document.get("Weight").toString(), document.get("Character").toString(), document.get("Marking").toString(),
//                                        document.get("Health").toString(), document.get("OriginalLocation").toString(), document.get("Status").toString(),
//                                        document.get("Story").toString(), document.get("ShelterID").toString());
//                                        petList.add(petSearch);
////                                        Log.d("PetName", document.get("Name").toString());
//                                    }
//                                }
//                            }
//
////                            homeAdapter = new HomeAdapter(getFragmentManager(), getId(), getContext(), petList);
////                            pet_rec.setAdapter(homeAdapter);
//                        }
//                    }
//                });
//
//    }


    public void flipperImages(int image){
        ImageView img = new ImageView(getContext());
        img.setBackgroundResource(image);
        flipper.addView(img);
        flipper.setFlipInterval(4000);
        flipper.setAutoStart(true);
    }

    private MappedByteBuffer loadModelFile(String modelFile) throws IOException {
        AssetFileDescriptor assetFileDescriptor = getContext().getAssets().openFd(modelFile) ;
        FileInputStream fileInputStream = new FileInputStream( assetFileDescriptor.getFileDescriptor() ) ;
        FileChannel fileChannel = fileInputStream.getChannel() ;
        long startoffset = assetFileDescriptor.getStartOffset() ;
        long declaredLength = assetFileDescriptor.getDeclaredLength() ;
        return fileChannel.map( FileChannel.MapMode.READ_ONLY , startoffset , declaredLength) ;
    }

    private Map<Integer, ArrayList<String>> readfile(){
        String[][] item = new String[0][];
        Map<Integer, ArrayList<String>> map = new HashMap<>();
        try {
            AssetManager assetManager = getActivity().getAssets();
            InputStream inputStream = assetManager.open(modelFile2);
            Workbook workbook = Workbook.getWorkbook(inputStream);
            Sheet sheet = workbook.getSheet(0);
            rows = sheet.getRows();
            cols = sheet.getColumns();
            item = new String[rows][cols];
            String str = "";

            for(int i=0; i<rows; i++){
                ArrayList<String> value =  new ArrayList<>();
//               Log.d("RowL", String.valueOf(sheet.getRow(i).length));
               for(int j=0; j<cols; j++){
                   Cell cell = sheet.getCell(j,i);
                   str = cell.getContents();
                   item[i][j] = str;
                   if(!item[i][j] .isEmpty()){
                       value.add(item[i][j]);
                   }
               }
                map.put(i, value);
            }



//            double item_item[][] = new double[cols][cols];
//            int count=0;
//            for(int i=0; i<cols; i++){
//                for(int j=0; j<cols; j++){
//                    if (i == j) {
//                        item_item[i][j] = 0.0;
//                    } else {
//                        item_item[i][j] =  CosineSimilarity(item, item);
//                        Log.d("Cosine", String.valueOf(item_item[i][j]));
//                        count++;
//                    }
//                }
//            }
//            Log.d("Count", String.valueOf(count));

        }  catch (
                IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }

        for (Map.Entry<Integer, ArrayList<String>> entry : map.entrySet()) {
            Log.d("value",String.valueOf(entry.getKey())+" "+entry.getValue());
        }

        return map;

    }


    private void recommendPet(ArrayList<String> likeList) {
//        int index = 0;
//        for (Map.Entry<Integer, ArrayList<String>> entry : map.entrySet()) {
//            if(entry.getValue().equals(likeList)){
//                index = 201;
//                Log.d("Ind", String.valueOf(index)+" "+entry.getValue());
//            }
//            Log.d("Inde", String.valueOf(index)+" "+entry.getValue());
//        }


        String str = "";
        try {
            AssetManager assetManager = getActivity().getAssets();
            InputStream inputStream = assetManager.open(modelFile2);
            Workbook workbook = Workbook.getWorkbook(inputStream);
            Sheet sheet = workbook.getSheet(1);
            rows = sheet.getRows();
            cols = sheet.getColumns();
            pet = new String[cols];

            boolean matched;
            int i;
            Map<Integer, ArrayList<String>> map = readfile();
            for (int key : map.keySet()) {
                matched = true;
                ArrayList<String> list = map.get(key);

                for(i = 0; i < list.size() && i < likeList.size(); i++) {
                    if(!list.get(i).equals(likeList.get(i))){
                        matched = false;
                        break;
                    }
                    else{
                        index = key;
                    }
                }
//                if (matched && i == list.size() && i == likeList.size()) {
//
//                    Log.d("IndexN", String.valueOf(key));
//                }
            }

//            Map<Integer, ArrayList<String>> map = readfile();
//            List<Integer> indices = new ArrayList<>();
//            for (int key : map.keySet()) {
//                if (map.get(key).equals(likeList)) {
//                    indices.add(key);
//                }
//            }


            Log.d("Indenn", String.valueOf(index));


            for(int j=0; j<pet.length; j++){
                Cell[] cell1 = sheet.getRow(index);
                str = cell1[j].getContents();
                pet[j] = str;
            }

        }  catch (
                IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }

        db.collection("Pet")
                .whereEqualTo("Status", "กำลังหาบ้าน")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                    Log.d("PetID", document.getId());
                                PetSearch petSearch = new PetSearch(document.getId(), document.get("Name").toString(), document.get("Type").toString(),
                                        document.get("Color").toString(), document.get("Sex").toString(), document.get("Age").toString(),
                                        document.get("Breed").toString(), document.get("Size").toString(), document.get("Image").toString(),
                                        document.get("Weight").toString(), document.get("Character").toString(), document.get("Marking").toString(),
                                        document.get("Health").toString(), document.get("OriginalLocation").toString(), document.get("Status").toString(),
                                        document.get("Story").toString(), document.get("ShelterID").toString(), document.get("Rec").toString(), document.get("AddDateTime").toString());
                                petList.add(petSearch);
                            }
                            showRecommendPet(pet, petList);
                        }
                    }
                });


//        for(int i=0; i<pet.length; i++){
//            str = cell1[i].getContents();
//            pet[i] = str;
//            Log.d("Pet1", pet[i]);
//            final String[] finalPet = pet;
//            final Cell[] finalCell = cell1;
//            final String[] finalPet2 = pet;
//            final int finalI = i;
//            count++;


//            db.collection("Pet")
//                    .whereEqualTo("Rec", pet[i])
//                    .whereEqualTo("Status", "กำลังหาบ้าน")
//                    .get()
//                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            if(task.isSuccessful()){
//                                Log.d("Pet2", finalPet2[finalI]);
//                                for (QueryDocumentSnapshot document : task.getResult()) {
//                                    PetSearch petSearch = new PetSearch(document.getId(), document.get("Name").toString(), document.get("Type").toString(),
//                                            document.get("Color").toString(), document.get("Sex").toString(), document.get("Age").toString(),
//                                            document.get("Breed").toString(), document.get("Size").toString(), document.get("Image").toString(),
//                                            document.get("Weight").toString(), document.get("Character").toString(), document.get("Marking").toString(),
//                                            document.get("Health").toString(), document.get("OriginalLocation").toString(), document.get("Status").toString(),
//                                            document.get("Story").toString(), document.get("ShelterID").toString(),document.get("Rec").toString());
//                                    petList.add(petSearch);
//                                }
//                                if(count == finalCell.length){
//                                    Log.d("CountLe", String.valueOf(count));
//                                    homeAdapter = new HomeAdapter(getFragmentManager(), getId(), getContext(), petList, finalPet);
//                                    pet_rec.setAdapter(homeAdapter);
//                                }
//                            }
//                        }
//                    });
//        }

    }
    private void randomWeightPet(){
        Log.d("IndexP", "Not Match");
        db.collection("Pet")
                .whereEqualTo("Status", "กำลังหาบ้าน")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            Map<String, Integer> map =  new HashMap<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                map.put(document.getId(), Integer.parseInt(document.get("LikeCount").toString()));
                            }
                            Set<Map.Entry<String, Integer>> set = map.entrySet();
                            List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(set);

                            Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
                                public int compare(Map.Entry<String, Integer> o1,
                                                   Map.Entry<String, Integer> o2) {
                                    return o2.getValue().compareTo(o1.getValue());
                                }
                            });

                            list = list.subList(0,20);
                            for (Map.Entry<String, Integer> entry : list) {
//                                Log.d("Count", entry.getKey()+" "+entry.getValue());
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if(document.getId().equals(entry.getKey())){
                                        PetSearch petSearch = new PetSearch(document.getId(), document.get("Name").toString(), document.get("Type").toString(),
                                                document.get("Color").toString(), document.get("Sex").toString(), document.get("Age").toString(),
                                                document.get("Breed").toString(), document.get("Size").toString(), document.get("Image").toString(),
                                                document.get("Weight").toString(), document.get("Character").toString(), document.get("Marking").toString(),
                                                document.get("Health").toString(), document.get("OriginalLocation").toString(), document.get("Status").toString(),
                                                document.get("Story").toString(), document.get("ShelterID").toString(), document.get("Rec").toString(), document.get("AddDateTime").toString());
                                        petList.add(petSearch);
                                    }
                                }
                                homeAdapter = new HomeAdapter(getFragmentManager(), getId(), getContext(), petList);
                                homeAdapter.sortWeightPet();
                                pet_rec.setAdapter(homeAdapter);
                            }
                        }
                    }
                });
    }

    private void showRecommendPet(String[] pet, ArrayList<PetSearch> petList) {
        Log.d("petLength", String.valueOf(pet.length));
        for(int i=0; i<pet.length; i++){
            Log.d("Pet[i]", pet[i]);
            for(int j=0; j<petList.size(); j++){
                if(pet[i].equals(petList.get(j).getRecommend())){
                    petRec.add(petList.get(j));
                }
            }
        }
        homeAdapter = new HomeAdapter(getFragmentManager(), getId(), getContext(), petRec);
        pet_rec.setAdapter(homeAdapter);
    }


//    public double CosineSimilarity(int[][] vec1, int[][] vec2) {
//
//        double cosineSimilarity = 0;
//
//        for (int i = 0; i < rows; i++) {
//            for (int j = 0; j < cols; j++) {
//                dotProduct += vec1[i][j] * vec2[i][j];
//                magnitude1 += Math.pow(vec1[i][j], 2);
//                magnitude2 += Math.pow(vec2[i][j], 2);
//            }
//
//        }
//
//        magnitude1 = Math.sqrt(magnitude1);//sqrt(a^2)
//        magnitude2 = Math.sqrt(magnitude2);//sqrt(b^2)
//
//        if (magnitude1 != 0.0 | magnitude2 != 0.0)
//        {
//            cosineSimilarity = dotProduct / (magnitude1 * magnitude2);
//        }
//        else
//        {
//            cosineSimilarity = 0.0;
//        }
//
//        return cosineSimilarity;
//    }

}

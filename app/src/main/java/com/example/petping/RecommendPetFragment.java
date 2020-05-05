package com.example.petping;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class RecommendPetFragment extends Fragment {
    private ArrayList<PetSearch> petList = new ArrayList<>();
    private ListView listView;
    private RecommendPetAdapter recommendPetAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_recommend_pet, null);
        if(getArguments() != null){
            petList = getArguments().getParcelableArrayList("recommendPet");
        }

        listView = view.findViewById(R.id.listView);
        recommendPetAdapter = new RecommendPetAdapter(getFragmentManager(), getId(), getContext(), petList);
        listView.setAdapter(recommendPetAdapter);

        return  view;
    }
}

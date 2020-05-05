package com.example.petping;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class RecommendPetAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<PetSearch> petList;
    private FragmentManager fragmentManager;
    private int id;
    public RecommendPetAdapter(FragmentManager fragmentManager, int id, Context context, ArrayList<PetSearch> petList) {
        this.context = context;
        this.petList = petList;
        this.fragmentManager = fragmentManager;
        this.id = id;
    }
    @Override
    public int getCount() {
        return petList.size();
    }

    @Override
    public Object getItem(int position) {
        return petList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(context, R.layout.recommend_pet_adapter, null);
        TextView name, breed, age, rank;
        ImageView imageView, sex;

        imageView = convertView.findViewById(R.id.image);
        name = convertView.findViewById(R.id.name);
        breed = convertView.findViewById(R.id.breed);
        age = convertView.findViewById(R.id.age);
        sex = convertView.findViewById(R.id.sex);
        rank = convertView.findViewById(R.id.rank);

        rank.setText(String.valueOf(position+1));

        Glide.with(context)
                .load(petList.get(position).getUrl())
                .into((ImageView) imageView);
        if(petList.get(position).getSex().equals("ผู้")){
            sex.setImageResource(R.drawable.sex_male_white);
        }
        else {
            sex.setImageResource(R.drawable.sex_female_white);
        }
        name.setText(petList.get(position).getName());
        breed.setText(petList.get(position).getBreed());
        age.setText(petList.get(position).getAge());

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PetProfileGeneralFragment petProfile = new PetProfileGeneralFragment();
                Bundle bundle = new Bundle();
                ArrayList<PetSearch> list = new ArrayList<>();
                list.add(petList.get(position));
                bundle.putParcelableArrayList("petProfile", list);
                petProfile.setArguments(bundle);
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(id, petProfile);
                ft.addToBackStack(null).commit();
            }
        });
        return convertView;
    }
}

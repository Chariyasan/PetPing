package com.example.petping;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class ManagePetInfoShelterAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<PetSearch> petList;
    private int id;
    private FragmentManager fragment;
    public ManagePetInfoShelterAdapter(FragmentManager fragment, int id, Context context, ArrayList<PetSearch> petList) {
        this.fragment = fragment;
        this.id = id;
        this.context = context;
        this.petList = petList;
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
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.manage_pet_info_adapter, null);
        TextView textName, textType, textBreed;
        ImageButton btn;
        textName = view.findViewById(R.id.name);
        textType = view.findViewById(R.id.type);
        textBreed = view.findViewById(R.id.breed);
        btn = view.findViewById(R.id.button);

        textName.setText(petList.get(position).getName());
        textType.setText(petList.get(position).getType());
        textBreed.setText(petList.get(position).getBreed());

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<PetSearch> petItem = new ArrayList<>();
                PetInfoShelterFragment petInfo = new PetInfoShelterFragment();
                Bundle bundle = new Bundle();
                petItem.add(petList.get(position));
                bundle.putParcelableArrayList("petInfo", petItem);
                petInfo.setArguments(bundle);

                FragmentTransaction ft = fragment.beginTransaction();
                ft.replace(id, petInfo);
                ft.commit();


            }
        });

        return view;
    }
}

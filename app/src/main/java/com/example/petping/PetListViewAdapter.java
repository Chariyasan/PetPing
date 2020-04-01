package com.example.petping;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.FragmentTransaction;


class PetListViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<PetSearch> petSearchList;

    public PetListViewAdapter(Context context, ArrayList<PetSearch> petSearchList) {
        this.context = context;
        this.petSearchList = petSearchList;
    }

    @Override
    public int getCount() {
        return petSearchList.size();
    }

    @Override
    public Object getItem(int position) {
        return petSearchList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
       // return Long.parseLong(petSearchList.get(position).getID());
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.pet_listview_adapter, null);
        TextView textViewName, textViewAge, textViewBreed;
        ImageView imgView = (ImageView) view.findViewById(R.id.adapter_img);

        Glide.with(context)
                .load(petSearchList.get(position).getUrl())
                .into((ImageView) imgView);

        textViewName = view.findViewById(R.id.adapter_name);
        textViewAge = view.findViewById(R.id.adapter_age);
        textViewBreed = view.findViewById(R.id.adapter_breed);

        String sexMale = "ผู้";
        ImageView ImageViewSex = view.findViewById(R.id.adapter_sex);
        if(petSearchList.get(position).getSex().equals(sexMale)){
            ImageViewSex.setImageResource(R.drawable.sex_male_white);
        }
        else {
            ImageViewSex.setImageResource(R.drawable.sex_female_white);
        }

        textViewName.setText(petSearchList.get(position).getName());
        textViewAge.setText(petSearchList.get(position).getAge());
        textViewBreed.setText(petSearchList.get(position).getBreed());

        return view;
    }
}

package com.example.petping;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HomeShelterAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<HomeShelter> homeList;
    private ArrayList<HomeShelter> filterList;
    private LayoutInflater mLayoutInflater;

    public HomeShelterAdapter(Context context, ArrayList<HomeShelter> homeList) {
        this.context = context;
        this.homeList = homeList;
        this.filterList = homeList;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return filterList.size();
    }

    @Override
    public Object getItem(int position) {
        return filterList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void noFilter() {
        filterList = homeList;
        notifyDataSetChanged();
    }

    public void filterWaiting() {
        filterStatus("รอพิจารณาคุณสมบัติ");
    }

    public void filterSuccess() {
        filterStatus("รับอุปการะแล้ว");
    }

    private void filterStatus(String status) {
        filterList = new ArrayList<>();
        for(HomeShelter h: homeList){
            if(h.getPetStatus().equals(status)){
//                HomeShelter homeShelter = new HomeShelter();
                filterList.add(h);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
//        View view = View.inflate(context, R.layout.home_shelter_adapter, null);
        convertView = mLayoutInflater.inflate(R.layout.home_shelter_adapter, null);
        final TextView user, petName, petStatus, petDate;
        ImageView imgPet, imgUser;
        user = convertView.findViewById(R.id.user);
        petName = convertView.findViewById(R.id.pet_name);
        petStatus = convertView.findViewById(R.id.pet_status);
        imgPet = convertView.findViewById(R.id.img_pet);
        imgUser = convertView.findViewById(R.id.img_user);
        petDate = convertView.findViewById(R.id.pet_date);


        user.setText(filterList.get(position).getUserName());
        petName.setText(filterList.get(position).getPetName());
        petStatus.setText(filterList.get(position).getPetStatus());
        petDate.setText(filterList.get(position).getPetDate());
        Glide.with(context)
                .load(filterList.get(position).getURL())
                .into(imgPet);

        Glide.with(context)
                .load(filterList.get(position).getUserImage())
                .into(imgUser);

        return convertView;
    }
}

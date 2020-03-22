package com.example.petping;

import android.content.Context;
import android.util.Log;
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
    private List<HomeShelter> homeList;

    public HomeShelterAdapter(Context context, List<HomeShelter> homeList) {
        this.context = context;
        this.homeList = homeList;
    }

    @Override
    public int getCount() {
        return homeList.size();
    }

    @Override
    public Object getItem(int position) {
        return homeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.home_shelter_adapter, null);
        final TextView user, petName, petStatus;
        ImageView imgPet, imgUser;
        user = view.findViewById(R.id.user);
        petName = view.findViewById(R.id.pet_name);
        petStatus = view.findViewById(R.id.pet_status);
        imgPet = view.findViewById(R.id.img_pet);
        imgUser = view.findViewById(R.id.img_user);

        user.setText(homeList.get(position).getUserName());
        petName.setText(homeList.get(position).getPetName());
        petStatus.setText(homeList.get(position).getPetStatus());
        Glide.with(context)
                .load(homeList.get(position).getURL())
                .into(imgPet);

        return view;
    }
}

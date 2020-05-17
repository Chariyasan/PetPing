package com.example.petping;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import jxl.Cell;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    private Context context;
    private ArrayList<PetSearch> petList;
    private FragmentManager fragmentManager;
    private int id;
    public HomeAdapter(FragmentManager fragmentManager, int id, Context context, ArrayList<PetSearch> petList) {
        this.context = context;
        this.petList = petList;
        this.fragmentManager = fragmentManager;
        this.id = id;
    }

    public void sortWeightPet(){
        Collections.sort(petList, new Comparator<PetSearch>() {
            @Override
            public int compare(PetSearch o1, PetSearch o2) {
                try {
                    return new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(o1.getAddDataTime()).compareTo(new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(o2.getAddDataTime()));
                } catch (ParseException e) {
                    return 0;
                }
            }
        });
        notifyDataSetChanged();
    }
//    @Override
//    public int getCount() {
//        return petList.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return petList.get(position);
//    }

    @NonNull
    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAdapter.ViewHolder holder, final int position) {
//        View view = View.inflate(context, R.layout.home_adapter, null);
        Glide.with(context)
                .load(petList.get(position).getUrl())
                .into(holder.image);
        holder.name.setText(petList.get(position).getName());
        holder.age.setText(petList.get(position).getAge());
        holder.mView.setOnClickListener(new View.OnClickListener() {
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
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return petList.size();
    }

    public void sortReccomendPet(){

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View mView;
        ImageView image;
        TextView name;
        TextView age;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.pet_img);
            name = itemView.findViewById(R.id.pet_name);
            age = itemView.findViewById(R.id.pet_age);
            mView = itemView;
        }
    }
}

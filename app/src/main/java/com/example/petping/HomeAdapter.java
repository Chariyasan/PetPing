package com.example.petping;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    private Context context;
    private List<PetSearch> petList;
    public HomeAdapter(Context context, ArrayList<PetSearch> petList) {
        this.context = context;
        this.petList = petList;
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
    public void onBindViewHolder(@NonNull HomeAdapter.ViewHolder holder, int position) {
        View view = View.inflate(context, R.layout.home_adapter, null);
        Glide.with(context)
                .load(petList.get(position).getUrl())
                .into(holder.image);
        holder.name.setText(petList.get(position).getName());
        holder.age.setText(petList.get(position).getAge());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return petList.size();
    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View view = View.inflate(context, R.layout.home_adapter, null);
//        ImageView imgView = (ImageView) view.findViewById(R.id.home_img);
//        TextView textViewName, textViewBreed, textViewAge;
//        Glide.with(context)
//                .load(petList.get(position).getUrl())
//                .into((ImageView) imgView);
//
//
//        textViewName = view.findViewById(R.id.home_name);
//        textViewAge = view.findViewById(R.id.home_age);
//        textViewBreed = view.findViewById(R.id.home_breed);
//
//        textViewName.setText(petList.get(position).getName());
//        textViewAge.setText(petList.get(position).getAge());
//        textViewBreed.setText(petList.get(position).getBreed());
//        return view;
//    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name;
        TextView age;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.pet_img);
            name = itemView.findViewById(R.id.pet_name);
            age = itemView.findViewById(R.id.pet_age);
        }
    }
}

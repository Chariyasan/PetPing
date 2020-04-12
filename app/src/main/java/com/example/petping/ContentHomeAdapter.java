package com.example.petping;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ContentHomeAdapter extends RecyclerView.Adapter<ContentHomeAdapter.ViewHolder> {
    private Context context;
    private List<Content> contentList;
    private FragmentManager fragmentManager;
    private int id;

    public ContentHomeAdapter(FragmentManager fragmentManager, int id, Context context, ArrayList<Content> contentList) {
        this.context = context;
        this.contentList = contentList;
        this.fragmentManager = fragmentManager;
        this.id = id;
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
    public ContentHomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_home_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context)
                .load(contentList.get(position).getUrl())
                .into(holder.image);
        holder.topic.setText(contentList.get(position).getTopic());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return contentList.size();
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
        View mView;
        ImageView image;
        TextView topic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            topic = itemView.findViewById(R.id.topic);
            mView = itemView;
        }
    }
}

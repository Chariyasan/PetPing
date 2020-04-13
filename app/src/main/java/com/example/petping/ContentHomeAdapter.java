package com.example.petping;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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


    @NonNull
    @Override
    public ContentHomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_home_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Glide.with(context)
                .load(contentList.get(position).getUrl())
                .into(holder.image);
        holder.topic.setText(contentList.get(position).getTopic());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentHomeFragment content = new ContentHomeFragment();
                Bundle bundle = new Bundle();
                ArrayList<Content> list = new ArrayList<>();
                list.add(contentList.get(position));
                bundle.putParcelableArrayList("contentHome", list);
                content.setArguments(bundle);
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(id, content);
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
        return contentList.size();
    }

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

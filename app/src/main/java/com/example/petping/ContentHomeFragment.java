package com.example.petping;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ContentHomeFragment extends Fragment {
    private ArrayList<Content> contentList;
    private TextView topic, story;
    private ImageView image;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content, null);
        if(getArguments() != null){
            contentList = getArguments().getParcelableArrayList("contentHome");
        }
        image = view.findViewById(R.id.image);
        topic = view.findViewById(R.id.topic);
        story = view.findViewById(R.id.story);


        for(int i=0; i<contentList.size(); i++){
            Glide.with(getContext())
                    .load(contentList.get(i).getUrl())
                    .into(image);
            topic.setText(contentList.get(i).getTopic());
            story.setText(contentList.get(i).getStory());
        }

        return view;
    }
}

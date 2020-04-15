package com.example.petping;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class ManageContentShelterAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Content> contentList;
    private int id;
    private FragmentManager fragment;
    public ManageContentShelterAdapter(FragmentManager fragment, int id, Context context, ArrayList<Content> contentList) {
        this.fragment = fragment;
        this.id = id;
        this.context = context;
        this.contentList = contentList;
    }

    @Override
    public int getCount() {
        return contentList.size();
    }

    @Override
    public Object getItem(int position) {
        return contentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.manage_content_shelter_adapter, null);
        TextView contentId, author, topic;
        ImageButton btn;

        contentId = view.findViewById(R.id.contentID);
        author = view.findViewById(R.id.author);
        topic = view.findViewById(R.id.topic);
        btn = view.findViewById(R.id.button);

        contentId.setText(contentList.get(position).getID());
//        author.setText(contentList.get(position).getShelterID());
        topic.setText(contentList.get(position).getTopic());

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Content> contentList1 = new ArrayList<>();
                ContentShelterFragment content = new ContentShelterFragment();
                Bundle bundle = new Bundle();
                contentList1.add(contentList.get(position));
                bundle.putParcelableArrayList("contentInfo", contentList1);
                content.setArguments(bundle);

                FragmentTransaction ft = fragment.beginTransaction();
                ft.replace(id, content);
                ft.commit();
            }
        });
        return view;
    }
}

package com.example.petping;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
        return position;
    }

    public void sortDate(){
        Collections.sort(contentList, new Comparator<Content>() {
            @Override
            public int compare(Content o1, Content o2) {
                try {
                    return new SimpleDateFormat("dd/MM/yyyy").parse(o2.getDate()).compareTo(new SimpleDateFormat("dd/MM/yyyy").parse(o1.getDate()));
                } catch (ParseException e) {
                    return 0;
                }
            }
        });
        notifyDataSetChanged();
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(context, R.layout.manage_content_shelter_adapter, null);
        TextView date, topic;
        ImageButton btn;

        topic = convertView.findViewById(R.id.topic);
        date = convertView.findViewById(R.id.date);
        btn = convertView.findViewById(R.id.button);

        topic.setText(contentList.get(position).getTopic());
        date.setText(contentList.get(position).getDate());

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
                ft.addToBackStack(null).commit();
            }
        });
        return convertView;
    }
}

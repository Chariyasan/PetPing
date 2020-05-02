package com.example.petping;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class PetStatusAdapter extends BaseAdapter {
    private Context context;
    private List<Status> petList;
    public PetStatusAdapter(Context context, ArrayList<Status> petList) {
        this.context = context;
        this.petList = petList;
    }

    @Override
    public int getCount() {
        return petList.size();
    }

    @Override
    public Object getItem(int position) {
        return petList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.pet_listview_status, null);
        ImageView imgView = (ImageView) view.findViewById(R.id.status_img);

        Glide.with(context)
                .load(petList.get(position).getUrl())
                .into((ImageView) imgView);

        TextView name, breed, status, date;
        name = view.findViewById(R.id.status_name);
        breed = view.findViewById(R.id.status_breed);
        status = view.findViewById(R.id.status_status);
        date = view.findViewById(R.id.status_date);

        name.setText(petList.get(position).getName());
        breed.setText(petList.get(position).getBreed());
        status.setText(petList.get(position).getStatus());
        date.setText(petList.get(position).getDateTime());

        String status1 = petList.get(position).getStatus();
        if(status1.equals("กำลังดำเนินการ")){
            status.setTextColor(Color.parseColor("#FFCC00"));
        }
        if(status1.equals("ดำเนินการสำเร็จ")){
            status.setTextColor(Color.parseColor("#00574B"));
        }
        if(status1.equals("ไม่ผ่านการพิจารณา")){
            status.setTextColor(Color.parseColor("#a30203"));
        }

        return view;
    }
}

package com.example.petping;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class ManagePetInfoShelterAdapter extends BaseAdapter  implements Filterable {
    private Context context;
    private ArrayList<PetSearch> petList;
    private int id;
    private FragmentManager fragment;
    private ValueFilter valueFilter;
    private ArrayList<PetSearch> filterList;
    private LayoutInflater mLayoutInflater;


    public ManagePetInfoShelterAdapter(FragmentManager fragment, int id, Context context, ArrayList<PetSearch> petList) {
        this.fragment = fragment;
        this.id = id;
        this.context = context;
        this.petList = petList;
        this.filterList = petList;
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


    @Override
    public Filter getFilter() {
        if(valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private class ValueFilter extends Filter{
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if(constraint == null || constraint.length() == 0) {
                //no constraint given, just return all the data. (no search)
                results.count = petList.size();
                results.values = petList;
                filterList = petList;
            }else{
                filterList = new ArrayList<PetSearch>();
                for(int i=0;i<petList.size();i++){
                    if((petList.get(i).getName().toUpperCase()).contains(constraint.toString().toUpperCase())) {
                        PetSearch petSearch = new PetSearch();
                        petSearch.setName(petList.get(i).getName());
                        petSearch.setType(petList.get(i).getType());
                        petSearch.setBreed(petList.get(i).getBreed());
                        petSearch.setID(petList.get(i).getID());
                        filterList.add(petSearch);
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filterList = (ArrayList<PetSearch>) results.values;

            notifyDataSetChanged();
        }
    }
    public void filterWaiting() {
        filterStatus("กำลังดำเนินการ");
    }

    public void filterSuccess() {
        filterStatus("ดำเนินการสำเร็จ");
    }

    public void filterFinding() {
        filterStatus("กำลังหาบ้าน");
    }

    private void filterStatus(String status) {
        filterList = new ArrayList<>();
        for(PetSearch pet: petList){
            if(pet.getStatus().equals(status)){
//                HomeShelter homeShelter = new HomeShelter();
                filterList.add(pet);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
//        View view = View.inflate(context, R.layout.manage_pet_info_adapter, null);
        convertView = mLayoutInflater.inflate(R.layout.manage_pet_info_adapter, null);
        TextView textName, textType, textBreed;
        ImageButton btn;
        textName = convertView.findViewById(R.id.name);
        textType = convertView.findViewById(R.id.type);
        textBreed = convertView.findViewById(R.id.breed);
        btn = convertView.findViewById(R.id.button);

        textName.setText(filterList.get(position).getName());
        textType.setText(filterList.get(position).getType());
        textBreed.setText(filterList.get(position).getBreed());

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<PetSearch> petItem = new ArrayList<>();
                PetInfoShelterFragment petInfo = new PetInfoShelterFragment();
                Bundle bundle = new Bundle();
                petItem.add(filterList.get(position));
                bundle.putParcelableArrayList("petInfo", petItem);
                petInfo.setArguments(bundle);

                FragmentTransaction ft = fragment.beginTransaction();
                ft.replace(id, petInfo);
                ft.commit();

            }
        });

        return convertView;
    }

}

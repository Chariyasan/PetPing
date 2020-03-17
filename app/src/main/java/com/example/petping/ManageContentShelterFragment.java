package com.example.petping;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class ManageContentShelterFragment extends Fragment {
    private ListView listView;
    private Button btnAdd;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_content_shelter, null);

        listView = view.findViewById(R.id.listView_content_shelter);
        btnAdd = view.findViewById(R.id.btn_add_content);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(getId(), new AddContentShelterFragment());
                ft.commit();
            }
        });
        return view;
    }
}

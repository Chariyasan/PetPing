package com.example.petping;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MapsActivity extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private Button btn;
    private String location, address, shelterName;
    private String[] loc;
    private Double latitude, longitude;
    private Spinner spinner;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_maps, null);
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btn = view.findViewById(R.id.btn);
        if (getArguments() != null){
            location = getArguments().getString("location");
            loc = location.split(",");
            address = loc[0];
            latitude = Double.parseDouble(loc[1]);
            longitude = Double.parseDouble(loc[2]);
            Log.d("address", address);
        }

            final CollectionReference collection = db.collection("Shelter");
            spinner = view.findViewById(R.id.map_spinner);
            final List<String> mapList = new ArrayList<>();
            final ArrayAdapter<String> mapAdapter =  new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, mapList);
            mapAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(mapAdapter);
            collection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String name = document.get("Name").toString();
                        mapList.add(name);
                    }
                    mapAdapter.notifyDataSetChanged();

                }
            });
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Log.d("test", spinner.getSelectedItem().toString());
                    shelterName = spinner.getSelectedItem().toString();
                    db.collection("Shelter")
                            .whereEqualTo("Name", shelterName)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            latitude = Double.parseDouble(document.get("Latitude").toString());
                                            longitude = Double.parseDouble(document.get("Longitude").toString());
//                                            Log.d("Latitude", String.valueOf(latitude));
//                                            Log.d("Longitude", String.valueOf(longitude));

                                            LatLng thai = new LatLng(latitude, longitude);
                                            mMap.addMarker(new MarkerOptions().position(thai).title(document.get("Name").toString()));
                                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(thai, 17));
                                        }
                                    }
                                }
                            });
                }
            });




        return view;
    }

    //    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_maps);
//        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        if(latitude == null && longitude == null){
            LatLng loc = new LatLng(13.794404,100.3247247);
            mMap.addMarker(new MarkerOptions().position(loc).title("Faculty of ICT"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));
        }else{
            LatLng thai = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(thai).title(address));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(thai, 17));
        }

    }
}

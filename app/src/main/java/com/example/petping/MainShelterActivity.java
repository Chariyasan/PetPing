package com.example.petping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainShelterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_shelter);

        BottomNavigationView btnNav = findViewById(R.id.bottom_shelter_navigation);
        btnNav.setOnNavigationItemSelectedListener(navListener);

        //start with Home page
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeShelterFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment fragment = null;
                    switch(menuItem.getItemId()){
                        case R.id.nav_home:
                            fragment = new HomeShelterFragment();
                            break;
                        case R.id.nav_manage:
                            fragment = new ManageInfoShelterFragment();
                            break;
                        case R.id.nav_menu:
                            fragment = new MenuShelterFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            fragment).addToBackStack(null).commit();
                    return true;
                }
            };

}

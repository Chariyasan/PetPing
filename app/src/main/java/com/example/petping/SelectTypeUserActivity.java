package com.example.petping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class SelectTypeUserActivity extends AppCompatActivity {
    private ImageButton btnShelter, btnAdopter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_type_user);
        btnShelter = findViewById(R.id.type_shelter);
        btnAdopter =  findViewById(R.id.type_adopter);

        btnShelter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ใช้อันที่ comment
                Intent intent = new Intent(SelectTypeUserActivity.this, LogInShelterActivity.class);
//                Intent intent = new Intent(SelectTypeUserActivity.this, MainShelterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnAdopter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectTypeUserActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

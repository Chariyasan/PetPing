package com.example.petping;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterShelterActivity extends AppCompatActivity {
    private EditText mail, pass, conPass, shelterN, shelterO, lic, addr, tel;
    private Button btn;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private TextView logIn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_shelter);
        
        mail = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        conPass = findViewById(R.id.confirmPass);
        shelterN = findViewById(R.id.shelter_name);
        shelterO = findViewById(R.id.shelter_owner);
        lic = findViewById(R.id.license);
        addr = findViewById(R.id.address);
        tel = findViewById(R.id.tel_no);
        btn = findViewById(R.id.button);
        logIn = findViewById(R.id.regTxt);

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterShelterActivity.this, LogInShelterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mail.getText().toString();
                String password = pass.getText().toString();
                String confirmPassword = conPass.getText().toString();
                String shelterName = shelterN.getText().toString();
                String shelterOwner = shelterO.getText().toString();
                String license = lic.getText().toString();
                String address = addr.getText().toString();
                String telNo = tel.getText().toString();

                // Check wrong input from user
//                if(email.isEmpty()){
//                    showMessage("กรุณากรอกอีเมล");
//                }
//                else if(password.isEmpty()){
//                    showMessage("กรุณากรอกพาสเวิร์ด");
//                }
//                else if(!confirmPassword.equals(password)){
//                    showMessage("ยืนยันพาสเวิร์ดไม่ถูกต้อง");
//                }
//                else if(shelterName.isEmpty()){
//                    showMessage("กรุณากรอกชื่อศูนย์พักพิง");
//                }
//                else if(shelterOwner.isEmpty()){
//                    showMessage("กรุณากรอกชื่อเจ้าของศูนย์พักพิง");
//                }
                 if(address.isEmpty()){
                    showMessage("กรุณากรอกที่อยู่ศูนย์พักพิง");
                }
//                else if(telNo.isEmpty()){
//                    showMessage("กรุณากรอกเบอร์ติดต่อศูนย์พักพิง");
//                }
                else{
                    CreateUserAccount(email, password, shelterName, shelterOwner, address, telNo, license);

                }
            }
        });
    }

    private void CreateUserAccount(final String email, String password, final String shelterName, final String shelterOwner, final String address, final String telNo, final String license) {
        GeoLocation geoLocation = new GeoLocation();
        geoLocation.getAddress(address, getApplicationContext(), new GeoHandler());
//        auth.createUserWithEmailAndPassword(email,password)
//                .addOnCompleteListener(RegisterShelterActivity.this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(task.isSuccessful()){
//                            GeoLocation geoLocation = new GeoLocation();
//                            geoLocation.getAddress(address, getApplicationContext(), new GeoHandler());
//                            HashMap<String, Object> data = new HashMap<>();
//                            data.put("Name", shelterName);
//                            data.put("Owner",shelterOwner);
//                            data.put("Address", address);
//                            data.put("TelNo",telNo);
//                            data.put("License", license);
//                            data.put("Facebook", "");
//                            data.put("Instagram", "");
//                            data.put("LineID", "");
//                            data.put("Image", "");
//                            data.put("Email", email);
//                            db.collection("Shelter")
//                                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                                    .set(data)
//                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                        @Override
//                                        public void onSuccess(Void aVoid) {
//                                            Intent Menu = new Intent(RegisterShelterActivity.this, MainShelterActivity.class);
//                                            startActivity(Menu);
//                                            finish();
//                                        }
//                                    });
//                        }
//                    }
//                });
    }

    private void showMessage(String show) {
        Toast.makeText(getApplicationContext(), show, Toast.LENGTH_LONG).show();
    }

    private class GeoHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            String address;
            switch (msg.what){
                case 1:
                    Bundle bundle = msg.getData();
                    address = bundle.getString("address");
                    break;
                    default:
                        address = null;
            }
            Log.d("Address", address);
        }
    }
}

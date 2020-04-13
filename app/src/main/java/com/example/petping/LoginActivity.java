package com.example.petping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    private TextView textRegist;
    private EditText emailL, passL;
    private Button btn;
    private FirebaseAuth Auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        textRegist = findViewById(R.id.log_register);
        emailL = findViewById(R.id.log_email);
        passL = findViewById(R.id.log_pass);
        btn = findViewById(R.id.log_btn);

        textRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //keep string that input in EditText to keep in string
                String email = emailL.getText().toString();
                String password = passL.getText().toString();

                if(email.isEmpty() ){
                    showMessage("กรุณาใส่อีเมลค่ะ");
                }
                else if(password.isEmpty()) {
                    showMessage("กรุณาใส่รหัสผ่านค่ะ");
                }
                else{
                    logIn(email,password);
                }
            }
        });
    }

    private void logIn(String email, String password) {
        Auth = FirebaseAuth.getInstance();
        //log in with email and password that have already register to Firebase Authentication
        Auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent Menu = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(Menu);
                            finish();
//                            showMessage("Can LogIn");
                        }
                        else{
                            showMessage("อีเมลหรือรหัสผ่านไม่ถูกต้อง กรุณาลองอีกครั้ง");
                        }
                    }
                });
    }
    private void showMessage(String show){
        //show message on application
        Toast.makeText(getApplicationContext(),show,Toast.LENGTH_LONG).show();
    }

}
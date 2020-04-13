package com.example.petping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogInShelterActivity extends AppCompatActivity {
    private Button btn;
    private TextView regist;
    private EditText emailL, passL;
    private FirebaseAuth Auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_shelter);
        btn = findViewById(R.id.log_btn);
        emailL = findViewById(R.id.log_email);
        passL = findViewById(R.id.log_pass);
        regist = findViewById(R.id.register);

        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInShelterActivity.this, RegisterShelterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        Auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent Menu = new Intent(LogInShelterActivity.this, MainShelterActivity.class);
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

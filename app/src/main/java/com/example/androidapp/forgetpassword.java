package com.example.androidapp;

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
import com.google.firebase.auth.FirebaseAuth;

public class forgetpassword extends AppCompatActivity {
    private Button backsignin ,btnResetPassword ;
    private EditText emailForgetPassword;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpassword);
        backsignin=findViewById(R.id.backsignin);
        btnResetPassword=findViewById(R.id.btnResetPassword);
        emailForgetPassword=findViewById(R.id.emailForgetPassword);

        firebaseAuth=FirebaseAuth.getInstance();
        btnResetPassword.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=emailForgetPassword.getText().toString();

                if(email.isEmpty()||!email.contains("@")||!email.contains(".")){
                 emailForgetPassword.setError("email is invalid!");
                }else{
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                          if(task.isSuccessful()){
                              Toast.makeText(forgetpassword.this, "password reset email sent!", Toast.LENGTH_SHORT).show();
                              startActivity(new Intent(forgetpassword.this,MainActivity.class));
                              finish();

                        }else{
                              Toast.makeText(forgetpassword.this, "error!", Toast.LENGTH_SHORT).show();
                          }
                        }
                    });
                }
            }
        }));

        backsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(forgetpassword.this,MainActivity.class));
            }
        });
    }
}
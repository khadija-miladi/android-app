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
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class register extends AppCompatActivity {
    private TextView haveaccount;
    private EditText nameUser,emailUser,phoneUser,cinUser,passwordUser;
    private Button register;
    private String name,email,phone,cin,password ;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        haveaccount=findViewById(R.id.haveaccount);
        nameUser=findViewById(R.id.nameUser);
        emailUser=findViewById(R.id.emailUser);
        phoneUser=findViewById(R.id.phoneUser);
        cinUser=findViewById(R.id.cinUser);
        passwordUser=findViewById(R.id.passwordUser);
        register=findViewById(R.id.register);
        firebaseAuth=firebaseAuth.getInstance();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    String email_user =emailUser.getText().toString().trim();
                    String password_user= passwordUser.getText().toString().trim();

                    firebaseAuth.createUserWithEmailAndPassword(email_user,password_user).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                sendEmailVerification();
                            }else {
                                Toast.makeText(register.this , "register failed!" ,Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                    
                }
                else{
                    Toast.makeText(register.this, "error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        haveaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(register.this,MainActivity.class));
            }
        });
    }

    private void sendEmailVerification() {
        FirebaseUser user=firebaseAuth.getCurrentUser();
        if(user!=null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        sendUserData();
                        Toast.makeText(register.this ,"registration done! Please check your email;",Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(register.this ,MainActivity.class));
                    }else{
                        Toast.makeText(register.this, "registration failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            } );

            }
        }


    private void sendUserData() {
        FirebaseDatabase firebaseDatabase =FirebaseDatabase.getInstance();
        DatabaseReference reference=firebaseDatabase.getReference("users");
        userProfil userProfil =new userProfil(name,email,phone,cin);
        reference.child(""+firebaseAuth.getUid()).setValue(userProfil);

    }

    private boolean validate(){
        boolean result= false;

        name=nameUser.getText().toString();
        email=emailUser.getText().toString();
        phone=phoneUser.getText().toString();
        cin=cinUser.getText().toString();
        password=passwordUser.getText().toString();

        if(name.isEmpty() || name.length()<6){
            nameUser.setError("name is invalid !");
        }else if (email.isEmpty()|| !email.contains("@")|| !email.contains(".")){
            emailUser.setError("email is invalid!");
        }else if (phone.isEmpty()|| phone.length()<8) {
            phoneUser.setError("phone is invalid!");
        }else if (cin.isEmpty()|| cin.length()<8){
            cinUser.setError("cin is invalid!");
        }else if (password.isEmpty()|| password.length()<7){
            passwordUser.setError("password is invalid!");
        }
        else{
            result=true;
        }


        return  result;
    }
}
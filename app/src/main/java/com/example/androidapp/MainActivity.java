package com.example.androidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private TextView newuser ,forgetpassword;
    private EditText emailSignIn,passwordSignIn;
    private Button SignIn;
    private CheckBox remember;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newuser=findViewById(R.id.newuser);
        emailSignIn=findViewById(R.id.emailSignIn);
        passwordSignIn=findViewById(R.id.passwordSingIn);
        SignIn=findViewById(R.id.btnSignIn);
        remember=findViewById(R.id.remember);

        forgetpassword=findViewById(R.id.forgetpassword);
        forgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,forgetpassword.class));
            }
        });
        newuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,register.class));
            }
        });
        firebaseAuth= FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);

        SharedPreferences preferences=getSharedPreferences("checkBox", MODE_PRIVATE);
        String checkbox=preferences.getString("remember","");

        if(checkbox.equals("true")){
            startActivity(new Intent(MainActivity.this,principaleActivity.class));

        }else if (checkbox.equals("false")){
            Toast.makeText(this,"please sign in!",Toast.LENGTH_SHORT).show();
        }

        remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    SharedPreferences preferences=getSharedPreferences("checkBox",MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putString("remember","true");
                    editor.apply();
                }else if(!compoundButton.isChecked()){
                    SharedPreferences preferences=getSharedPreferences("checkBox",MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putString("remember","false");
                    editor.apply();
                }
            }
        });


        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(emailSignIn.getText().toString().isEmpty() || !emailSignIn.getText().toString().contains("@")){
                    emailSignIn.setError("email is invalid!");
                }else if (passwordSignIn.getText().toString().isEmpty()|| passwordSignIn.getText().toString().length()<8){
                    passwordSignIn.setError("password is invalid!");
                }else{
                    validate(emailSignIn.getText().toString(),passwordSignIn.getText().toString());
                }
            }
        });
        }

    private void validate(String email, String password) {
        progressDialog.setMessage("please wait ...!");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    checkEmailVerification();
                }else{
                    Toast.makeText(MainActivity.this,"error!",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });
    }

    private void checkEmailVerification() {
        FirebaseUser user=firebaseAuth.getCurrentUser();
        boolean emailFlag=user.isEmailVerified();

        if(emailFlag){
            startActivity(new Intent(MainActivity.this,principaleActivity.class));
        }else if(!emailFlag){
            Toast.makeText(this,"please check your email!",Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }

    ;
    }

package com.example.androidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfilActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private EditText nameProfil, emailProfil, phoneProfil, cinProfil;
    private Button btnEdit, btnLogout;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser user;
    private DatabaseReference reference;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView menuIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        nameProfil = findViewById(R.id.nameProfil);
        emailProfil = findViewById(R.id.emailProfil);
        phoneProfil = findViewById(R.id.phoneProfil);
        cinProfil = findViewById(R.id.cinProfil);
        btnEdit = findViewById(R.id.btnEdit);
        btnLogout = findViewById(R.id.btnLogout);


        drawerLayout = findViewById(R.id.drawer_layout_profil);
        navigationView = findViewById(R.id.navigation_view_profil);
        menuIcon = findViewById(R.id.menu_profil);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        user = firebaseAuth.getCurrentUser();
        reference = firebaseDatabase.getReference().child("users").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String fullName = snapshot.child("name").getValue().toString();
                String email = snapshot.child("email").getValue().toString();
                String phone = snapshot.child("phone").getValue().toString();
                String cin = snapshot.child("cin").getValue().toString();
                nameProfil.setText(fullName);
                emailProfil.setText(email);
                phoneProfil.setText(phone);
                cinProfil.setText(cin);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = getSharedPreferences("checkBox", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("remember", "false");
                editor.apply();
                firebaseAuth.signOut();
                startActivity(new Intent(ProfilActivity.this, MainActivity.class));
                Toast.makeText(ProfilActivity.this, "Log out Succesfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameProfil.setFocusableInTouchMode(true);
                phoneProfil.setFocusableInTouchMode(true);
                cinProfil.setFocusableInTouchMode(true);
                btnEdit.setText("save");
                btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String editName = nameProfil.getText().toString();
                        String editPhone = phoneProfil.getText().toString();
                        String editCin = cinProfil.getText().toString();

                        reference.child("name").setValue(editName);
                        reference.child("phone").setValue(editPhone);
                        reference.child("cin").setValue(editCin);

                        nameProfil.setFocusableInTouchMode(false);
                        phoneProfil.setFocusableInTouchMode(false);
                        cinProfil.setFocusableInTouchMode(false);
                        phoneProfil.clearFocus();
                        nameProfil.clearFocus();
                        cinProfil.clearFocus();
                        btnEdit.setText("edit");

                    }
                });
            }
        });


    }

    private void navigationDrawer() {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.add);

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        drawerLayout.setScrimColor(getResources().getColor(R.color.black));
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }
}
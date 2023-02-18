package com.example.androidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class principaleActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView menuIcon;
    private LinearLayout contenetView;
    private EditText nameDevice,valueDevice;
    private Button btnAddDevice;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principale);

        drawerLayout=findViewById(R.id.drawer_layout_principale);
        navigationView=findViewById(R.id.navigation_view_principale);
        menuIcon=findViewById(R.id.menu_principale);
        contenetView=findViewById(R.id.ll_principale);
        nameDevice=findViewById(R.id.etNameDevice);
        valueDevice=findViewById(R.id.etValueDevice);
        btnAddDevice=findViewById(R.id.btnAddDevice);

        reference= FirebaseDatabase.getInstance().getReference();

        navigationDrawer();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.add:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.profilUserActivity:
                        startActivity(new Intent(principaleActivity.this,ProfilActivity.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }
                return true;
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
                if(drawerLayout.isDrawerVisible(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                }else drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        drawerLayout.setScrimColor(getResources().getColor(R.color.black));
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerVisible(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }
}
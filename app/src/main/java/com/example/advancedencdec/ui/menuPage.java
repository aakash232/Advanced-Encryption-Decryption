package com.example.advancedencdec.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.advancedencdec.R;
import com.google.android.material.navigation.NavigationView;


public class menuPage extends AppCompatActivity {

    private TextView enc,dec;
    NavigationView nav;
    DrawerLayout layout;
    androidx.appcompat.widget.Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_page);

        initViews();
        //navigation menu items click
        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(menuPage.this,layout,toolbar,R.string.start,R.string.end);
        layout.addDrawerListener(toggle);
        toggle.syncState();

        nav.bringToFront();
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.menu_history:
                        Toast.makeText(menuPage.this,"history ",Toast.LENGTH_SHORT).show();
                       // layout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.about:
                        Toast.makeText(menuPage.this,"About us ",Toast.LENGTH_SHORT).show();
                        break;
                    default :
                        return true;
                }
                return true;
            }
        });

        //Click functionality
         enc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(menuPage.this,"Encryption Mode Activated",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(menuPage.this,encPage.class));
            }
        });

        dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(menuPage.this,"Decryption Mode Activated",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(menuPage.this,decPage.class));
            }
        });
    }

    private void initViews() {
        enc = findViewById(R.id.menu_enc);
        dec = findViewById(R.id.menu_dec);

        nav = findViewById(R.id.navmenu);
        layout = findViewById(R.id.drawer);
        toolbar=findViewById(R.id.toolbar);

    }
    @Override  //Back button closes drawer
    public void onBackPressed() {
        if(layout.isDrawerOpen((GravityCompat.START)))
        {
            layout.closeDrawer(GravityCompat.START);
        }
        else{
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }


}
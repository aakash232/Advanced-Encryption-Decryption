package com.example.advancedencdec.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.advancedencdec.R;


public class menuPage extends AppCompatActivity {

    private TextView enc,dec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_page);

        initViews();

        //Click functionality

         enc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(menuPage.this,"Upload file for encryption",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(menuPage.this,encPage.class));
            }
        });

        dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(menuPage.this,"Upload file for decryption",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(menuPage.this,decPage.class));
            }
        });



    }

    private void initViews() {
        enc = findViewById(R.id.menu_enc);
        dec = findViewById(R.id.menu_dec);
    }



}
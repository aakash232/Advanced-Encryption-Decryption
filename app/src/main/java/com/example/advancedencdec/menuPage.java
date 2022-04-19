package com.example.advancedencdec;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class menuPage extends AppCompatActivity {

    private TextView enc,dec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_page);

        initViews();

        //Click functionality check
        enc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(menuPage.this,"Upload file for encryption",Toast.LENGTH_SHORT).show();
            }
        });

        dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(menuPage.this,"Upload file for decryption",Toast.LENGTH_SHORT).show();
            }
        });








    }

    private void initViews() {
        enc = (TextView) findViewById(R.id.menu_enc);
        dec = (TextView) findViewById(R.id.menu_dec);
    }
}
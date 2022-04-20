package com.example.advancedencdec.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.advancedencdec.R;
import com.example.advancedencdec.ui.menuPage;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Testing mode
        startActivity(new Intent(this, menuPage.class));

    }
}
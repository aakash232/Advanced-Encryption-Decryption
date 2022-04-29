package com.example.advancedencdec.ui;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.advancedencdec.R;
import com.example.advancedencdec.func.encDec;
import com.example.advancedencdec.func.rsaFunction;

import java.io.File;
import java.math.BigInteger;

public class decPage extends AppCompatActivity {

    private TextView dec_stat,dec_keyText;
    private EditText dec_enterKey;
    private String key="";

    encDec obj = new encDec(decPage.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dec_page);

        initViews();

        File sdcard = decPage.this.getExternalFilesDir(Environment.DIRECTORY_DCIM); //Environment.getExternalStorageDirectory();
        File dir = new File(sdcard.getAbsolutePath());

        File enc = new File(dir+"/enc.end");

        if(!enc.exists()){
            Log.e("sky", "enc.dec file not found");
            dec_stat.setText("enc.dec file not found at:"+dir);
            Toast.makeText(decPage.this,"Paste the enc.end file at:"+dir,Toast.LENGTH_SHORT).show();
        }
        else{
            Log.d("sky", "enc file found:" + enc.getAbsolutePath());
            dec_stat.setText("enc.dec file found at:"+dir);
            dec_enterKey.setVisibility(View.VISIBLE);
            dec_keyText.setVisibility(View.VISIBLE);

            dec_keyText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(dec_enterKey.getText()!=null || !dec_enterKey.getText().equals("")){
                        key = String.valueOf(dec_enterKey.getText());
                        Log.d("sky", "key:" + key);

                        BigInteger c = new BigInteger(key); // convert to BI
                        BigInteger Deckey = rsaFunction.EncDec(c, rsaFunction.d, rsaFunction.n);
                        Log.d("sky", "Deckey:" + Deckey);

                        key = Deckey.toString();

                        Toast.makeText(decPage.this,"Decrypting file...",Toast.LENGTH_LONG).show();
                        obj.decrypt(dir+"/enc.end", key, decPage.this);
                    }
                }
            });
        }

    }


    private void initViews() {
        dec_stat = findViewById(R.id.dec_stat);
        dec_enterKey = findViewById(R.id.dec_enterKey);
            dec_enterKey.setVisibility(View.INVISIBLE);
        dec_keyText = findViewById(R.id.dec_keyText);
            dec_keyText.setVisibility(View.INVISIBLE);
    }
}
package com.example.advancedencdec.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.advancedencdec.R;
import com.example.advancedencdec.func.encDec;
import com.example.advancedencdec.func.rsaFunction;

import java.io.File;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;

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
            dec_stat.append("\n\nERROR: enc.dec file not found!");
            Toast.makeText(decPage.this,"enc.dec file NOT found!",Toast.LENGTH_SHORT).show();
            dec_stat.append("\n\nPaste the enc.end file at:"+dir);
        }
        else{
            Log.d("sky", "enc file found:" + enc.getAbsolutePath());
            dec_stat.append("\n\nenc.dec file found at:"+dir);
            dec_enterKey.setVisibility(View.VISIBLE);
            dec_keyText.setVisibility(View.VISIBLE);
            dec_keyText.setOnClickListener(view -> {
                if ((dec_enterKey.getText() != null || !dec_enterKey.getText().equals("")) && dec_enterKey.getText().length() >= 500)
                    getUserExtension(dir);
                else
                    dec_enterKey.setError("Enter valid key");
            });
        }
    }

    private void getUserExtension(File dir) {

        // Set up the input
        final EditText input = new EditText(decPage.this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT| InputType.TYPE_TEXT_VARIATION_PASSWORD);

        final AlertDialog dialog = new AlertDialog.Builder(decPage.this)
                .setTitle("Enter EXTENSION for decrypted file (txt,pdf,jpg,mp3,mp4,etc)")
                .setCancelable(false)
                .setView(input)
                .setPositiveButton("Decrypt", null) //Set to null. We override the onclick
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String extn = input.getText().toString();

                        if(!extn.equals("")){
                            dialog.cancel();

                            //Begin decryption
                            key = String.valueOf(dec_enterKey.getText());
                            Log.d("sky", "key:" + key);

                            BigInteger c = new BigInteger(key); // convert to BI
                            BigInteger Deckey = rsaFunction.EncDec(c, rsaFunction.d, rsaFunction.n);
                            Log.d("sky", "Deckey:" + Deckey);

                            key = Deckey.toString();

                            Toast.makeText(decPage.this, "Decrypting file...", Toast.LENGTH_LONG).show();
                            obj.decrypt(dir + "/enc.end", key, decPage.this, extn);
                            dec_enterKey.clearFocus();

                        }
                        else
                            input.setError("Enter valid extension");

                    }
                });
            }
        });
        dialog.show();

    }


    private void initViews() {
        dec_stat = findViewById(R.id.dec_stats);
            dec_stat.setText("***** STATS LOG *****\n\n" + Calendar.getInstance().getTime());
            //set textview scrollable
            dec_stat.setMovementMethod(new ScrollingMovementMethod());
        dec_enterKey = findViewById(R.id.dec_enterKey);
            dec_enterKey.setVisibility(View.INVISIBLE);
        dec_keyText = findViewById(R.id.dec_keyText);
            dec_keyText.setVisibility(View.INVISIBLE);
    }
}
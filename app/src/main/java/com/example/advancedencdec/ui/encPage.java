package com.example.advancedencdec.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.advancedencdec.R;
import com.example.advancedencdec.func.encDec;
import com.example.advancedencdec.func.rsaFunction;
import com.example.advancedencdec.utils.ImageFilePath;

import java.io.File;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;

public class encPage extends AppCompatActivity {

    private TextView enc_upload,enc_stats;
    private ImageView enc_image;
    private Uri mImageUri;
    private BigInteger m,Enkey;

    //Public key (>10 length)
    String key = "1234567891234654"; //default value

    private final encDec obj = new encDec(encPage.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enc_page);
        initViews();
        getUserKey();
        //upload files
        enc_upload.setOnClickListener(view -> showFileChooser());
    }

    private void getUserKey() {

        // Set up the input
        final EditText input = new EditText(encPage.this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);

        final AlertDialog dialog = new AlertDialog.Builder(encPage.this)
                .setTitle("Enter your private key (>10 length)")
                .setCancelable(false)
                .setView(input)
                .setPositiveButton("OK", null) //Set to null. We override the onclick
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(dialogInterface -> {
            Button cancel = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    finish();
                }
            });

            Button ok = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
            ok.setOnClickListener(view -> {

                String enterKey= input.getText().toString();

                if(enterKey.equals("") || enterKey.length()<10)
                    input.setError("Enter valid key");
                else{
                    key = enterKey;
                    dialog.dismiss();
                    writeEncKey();
                }
            });
        });
        dialog.show();
    }

    private void writeEncKey(){
        m = new BigInteger(key);
        // RSA-Encrypt
        Enkey = rsaFunction.EncDec(m, rsaFunction.e, rsaFunction.n);
        Log.d("sky", "Enkey: " + Enkey);

        if(isStoragePermissionGranted()){
            //write encrypted key
            try{
                rsaFunction.WriteEncKey(Enkey, getBaseContext());
                File sdcard = encPage.this.getExternalFilesDir(Environment.DIRECTORY_DCIM); //Environment.getExternalStorageDirectory();
                enc_stats.append("\n\nEncryption key saved at: "+sdcard.getAbsolutePath()+"/key"+"\n");
            }
            catch (NullPointerException ex){
                Log.e("sky", "null ptr -> Enkey: " + ex.getLocalizedMessage());
            }
        }
    }

    private void showFileChooser() {

        String[] mimeTypes =
                {"application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                        "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                        "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                        "text/plain",
                        "application/pdf",
                        "application/zip", "application/vnd.android.package-archive","image/*"};

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a file to upload"),
                    999);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user with a Dialog
            Log.e("sky", "upload error: : " + ex.getLocalizedMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 999:
                if (resultCode == RESULT_OK && data != null && data.getData() != null) {

                    //get the Uri of the selected file
                    mImageUri = data.getData();
  		            //fetch real path
                    String realPath = ImageFilePath.getPath(encPage.this, data.getData());
                    //display image
                    //Glide.with(encPage.this)
                    //        .load(mImageUri)
                    //        .into(enc_image);
                    //update stats log
                    enc_stats.append("\nFile path: "+realPath+"\n");

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(encPage.this,"Encrypting file...",Toast.LENGTH_LONG).show();
                            beginEnc(realPath);
                        }
                    },500);


                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void beginEnc(String path) {
        //begin encryption
        try {
            obj.encrypt(path, key, encPage.this);
        } catch (Exception ex) {
            Log.d("sky", "imageUrl: " + ex.getLocalizedMessage());
        }
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("sky","Permission is granted");
                return true;
            } else {

                Log.v("sky","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.MANAGE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("sky","Permission is granted");
            return true;
        }
        return false;
    }

    private void initViews() {
        enc_upload = findViewById(R.id.enc_upload);
        enc_image = findViewById(R.id.enc_image);
        enc_stats = findViewById(R.id.enc_stats);
            enc_stats.setText("***** STATS LOG *****\n\n" + Calendar.getInstance().getTime());
            //set textview scrollable
            enc_stats.setMovementMethod(new ScrollingMovementMethod());
    }

}

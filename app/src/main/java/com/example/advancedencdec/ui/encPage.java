package com.example.advancedencdec.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.advancedencdec.R;
import com.example.advancedencdec.func.encDec;
import com.example.advancedencdec.func.rsaFunction;
import com.example.advancedencdec.utils.ImageFilePath;

import java.math.BigInteger;

public class encPage extends AppCompatActivity {

    private TextView enc_upload,enc_stats;
    private ImageView enc_image;
    private Uri mImageUri;
    private BigInteger m,Enkey;

    String key = "1234567891234654";

    encDec obj = new encDec();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enc_page);

        initViews();

        isStoragePermissionGranted();

        enc_upload.setOnClickListener(view -> showFileChooser());

        m = new BigInteger(key);
        // RSA-Encrypt
        Enkey = rsaFunction.EncDec(m, rsaFunction.e, rsaFunction.n);
        Log.d("sky", "Enkey: " + Enkey);

        if(isStoragePermissionGranted())
            writeEnckey(Enkey);

    }

    private void showFileChooser() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //encrypting images
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select an image to upload"),
                    999);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user with a Dialog
            Log.d("sky", "upload error: : " + ex.getLocalizedMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 999:
                if (resultCode == RESULT_OK && data != null && data.getData() != null) {

                    // Get the Uri of the selected file
                    mImageUri = data.getData();
                    String realPath = ImageFilePath.getPath(encPage.this, data.getData());
                    //display image
                    Glide.with(encPage.this)
                            .load(mImageUri)
                            .into(enc_image);

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


    private void writeEnckey(BigInteger Enkey) {
        //write encrypted key
        try{
            rsaFunction.WriteEncKey(Enkey, getBaseContext());
        }
        catch (NullPointerException ex){
            Log.e("sky", "null ptr -> Enkey: " + ex.getLocalizedMessage());
        }
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
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("sky","Permission is granted");
                return true;
            } else {

                Log.v("sky","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("sky","Permission is granted");
            return true;
        }
        return false;
    }

    private void initViews() {
        enc_upload = (TextView) findViewById(R.id.enc_upload);
        enc_image = (ImageView) findViewById(R.id.enc_image);
        enc_stats = (TextView) findViewById(R.id.enc_stats);
    }
}
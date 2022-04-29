package com.example.advancedencdec.func;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.advancedencdec.R;
import com.example.advancedencdec.ui.encPage;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class encDec {

    int flag = 0, shiftby = 2;
    String key = "";
    double percent;
    Context context;

    public encDec(Context context){
        this.context=context;
    }

    public void updateEncLog(String data){
        TextView txtView = (TextView) ((Activity)context).findViewById(R.id.enc_stats);
        txtView.append(data);
        final int scrollAmount = txtView.getLayout().getLineTop(txtView.getLineCount()) - txtView.getHeight();
        txtView.scrollTo(0, Math.max(scrollAmount, 0));
    }

    public void updateDecLog(String data){
        TextView txtView = (TextView) ((Activity)context).findViewById(R.id.dec_stats);
        txtView.append(data);
        final int scrollAmount = txtView.getLayout().getLineTop(txtView.getLineCount()) - txtView.getHeight();
        txtView.scrollTo(0, Math.max(scrollAmount, 0));
    }

    public void encrypt(String filename, String key, Context context) // encrypt function
    {

        try {

            Log.d("sky", "ENC: filename rcvd: " + filename);

            //begin
            File sdcard = context.getExternalFilesDir(Environment.DIRECTORY_DCIM); //Environment.getExternalStorageDirectory();
            File dir = new File(sdcard.getAbsolutePath());

            File dirTemp = new File(dir,"TempFiles"); // dir refers destination path.
            if (!dirTemp.exists())
                dirTemp.mkdir(); // make a folder(if do not exist) for temporary files which will b deleted at end
            //update stats log
            updateEncLog("\n\n\t\t\tChecking files...\nENC: stage 1 done");
            Log.d("sky", "ENC: stage 1 done");

            RandomAccessFile fn = new RandomAccessFile(filename, "r");
            RandomAccessFile in = new RandomAccessFile(dir+"/TempFiles/cp-temp.end", "rw"); // heads is the extension given
            //update stats log
            updateEncLog("\n\t\t\tCreating access files...\nENC: stage 2 done");
            Log.d("sky", "ENC: stage 2 done");

            // for file
            RandomAccessFile outTemp = new RandomAccessFile(dir+"/TempFiles/enc-T.end", "rw");
            RandomAccessFile out = new RandomAccessFile(dir + "/enc.end", "rw");
            //update stats log
            updateEncLog("\nENC: stage 3 done");
            Log.d("sky", "ENC: stage 3 done");

            functionSet.copyFile(filename, dir+"/TempFiles/cp-temp.end");// Faster FileCopy using File Channels
            //update stats log
            updateEncLog("\n\t\t\tUpdating File channels...\nENC: stage 4 done\n\t\t\tStarting enc rounds...");
            Log.d("sky", "ENC: stage 4 done");

            //Estimating execution time
            //update stats log
            updateEncLog("\n\nFAST(2 Round Enc/Dec)\t\t--Estimated Time--\n" + EstTime(in, 2)
                            + " seconds (" + (EstTime(in, 2)) / 60 + " minutes)\n");

            String funcData = functionSet.rounds(in, outTemp, key, shiftby, "Encrypting"); //xor
            updateEncLog(funcData);
            //update stats log
            updateEncLog("\n\nENC: stage 5 done");
            Log.d("sky", "ENC: stage 5 done");

            functionSet.shuffle(outTemp, out); // shuffle
            //update stats log
            updateEncLog("\n\t\t\tShuffling bytes..\nENC: stage 6 done\n\t\t\tPermutation complete...");
            Log.d("sky", "ENC: stage 6 done");

            File f1 = new File(dir+"/TempFiles/cp-temp.end");
            File f2 = new File(dir+"/TempFiles/enc-T.end");
            f1.delete();
            f2.delete();

            fn.close();
            in.close();
            out.close(); // Release Resources

            //end
            Log.d("sky", "ENC: ALL done");
            Toast.makeText(context,"Encryption success",Toast.LENGTH_SHORT).show();
            //update stats log
            updateEncLog("\n\nEncryption success!\n\nenc.end file stored at: "+dir);

        } catch (IOException ex) {
            Log.e("sky", "encrypt() error: " + ex.getLocalizedMessage());
        }
    }

    public void decrypt(String filename, String key, Context context) // decrypt function
    {
        try {

            Log.d("sky", "DEC: filename rcvd: " + filename);

            //begin
            File sdcard = context.getExternalFilesDir(Environment.DIRECTORY_DCIM); //Environment.getExternalStorageDirectory();
            File dir = new File(sdcard.getAbsolutePath());

            File dirTemp = new File(dir,"TempFiles");
            if (!dirTemp.exists())
                dirTemp.mkdir(); // make a folder(if do not exist) for temporary files which will b deleted at end
            //update stats log
            updateDecLog("\n\n\t\t\tChecking files...\nDEC: stage 1 done");
            Log.d("sky", "DEC: stage 1 done");

            RandomAccessFile fn = new RandomAccessFile(filename, "r");
            RandomAccessFile in = new RandomAccessFile(dir+"/TempFiles/cp-temp.end", "rw");
            String ext = "png"; //for images
            RandomAccessFile out = new RandomAccessFile(dir + "/dec."+ext, "rw");
            //update stats log
            updateDecLog("\n\t\t\tCreating access files...\nDEC: stage 2 done");
            Log.d("sky", "DEC: stage 2 done");

            functionSet.shuffle(fn, in); // deshuffle
            //update stats log
            updateDecLog("\nDEC: stage 3 done");
            Log.d("sky", "DEC: stage 3 done");

            //Estimating execution time
            //update stats log
            updateDecLog("\n\nFAST(2 Round Enc/Dec)\t\t--Estimated Time--\n" + EstTime(in, 2)
                    + " seconds (" + (EstTime(in, 2)) / 60 + " minutes)\n");


            String funcData = functionSet.rounds(in, out, key, shiftby, "Decrypting"); // xor
            updateDecLog(funcData);
            //update stats log
            updateDecLog("\n\n\t\t\tUpdating File channels...\nDEC: stage 4 done\n\t\t\tStarting dec rounds...");
            Log.d("sky", "DEC: stage 4 done");

            File f = new File("TempFiles/cp-temp.end");
            f.delete();

            // release resources
            in.close();
            out.close();
            fn.close();

            //end
            Log.d("sky", "DEC: ALL done");
            Toast.makeText(context,"Decryption success",Toast.LENGTH_SHORT).show();
            //update stats log
            updateDecLog("\n\nDecryption success!\n\nFile stored at: "+dir);

        } catch (IOException ex) {
            Log.e("sky", "decrypt() error: " + ex.getLocalizedMessage());
        }
    }

    public static double EstTime(RandomAccessFile inputfn, int rounds) {

        // File input=new File(inputfn);
        double bytes = 0;
        try {
            bytes = inputfn.length();
        } catch (IOException e) {
            System.out.println(e);
        }
        double kb = bytes / 1024;

        kb = kb * 100000;
        kb = Math.round(kb);
        kb = kb / 100000; // file size in kb with 5 decimal places only
        double ests = (kb / 58.5) * rounds; // estimated seconds-->ANALYSIS=58.5kb take 1 sec
        ests = ests * 100000;
        ests = Math.round(ests);
        ests = ests / 100000;

        /*
         * double estm=ests/60; //estimated minutes
         * estm=estm*10000;
         * estm=Math.round(estm);
         * estm=estm/10000; //estminutes with only upto 5 decimal places
         */
        return ests;
    }



}

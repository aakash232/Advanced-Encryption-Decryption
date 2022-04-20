package com.example.advancedencdec.func;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class encDec {

    int flag = 0, shiftby = 2;
    String key = "";
    double percent;

    public void encrypt(String filename, String key, Context context) // encrypt function
    {

        try {

            Log.d("sky", "filename rcvd: " + filename);

            //begin
            File sdcard = context.getExternalFilesDir(Environment.DIRECTORY_DCIM); //Environment.getExternalStorageDirectory();
            File dir = new File(sdcard.getAbsolutePath());

            File dirTemp = new File(dir,"TempFiles"); // dir refers destination path.
            if (!dirTemp.exists())
                dirTemp.mkdir(); // make a folder(if do not exist) for temporary files which will b deleted at end
            Log.d("sky", "ENC: stage 1 done");

            RandomAccessFile fn = new RandomAccessFile(filename, "r");
            RandomAccessFile in = new RandomAccessFile(dir+"/TempFiles/cp-temp.end", "rw"); // heads is the extension given
            Log.d("sky", "ENC: stage 2 done");

            // for file
            RandomAccessFile outTemp = new RandomAccessFile(dir+"/TempFiles/enc-T.end", "rw");
            RandomAccessFile out = new RandomAccessFile(dir + "/enc.end", "rw");
            Log.d("sky", "ENC: stage 3 done");

            functionSet.copyFile(filename, dir+"/TempFiles/cp-temp.end");// Faster FileCopy using File Channels
            Log.d("sky", "ENC: stage 4 done");

            functionSet.rounds(in, outTemp, key, shiftby, "Encrypting"); // xor
            Log.d("sky", "ENC: stage 5 done");

            functionSet.shuffle(outTemp, out); // shuffle
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

        } catch (IOException ex) {
            Log.d("sky", "encrypt() error: " + ex.getLocalizedMessage());
        }
    }

    public void decrypt(String filename, String extname, String dirname, String key) // decrypt function
    {
        try {
            File dir = new File("TempFiles");
            if (!dir.exists())
                dir.mkdir(); // make a folder(if donot exist) for temporary files which will b deleted at end
            // of prg

            RandomAccessFile fn = new RandomAccessFile(filename, "rw");
            RandomAccessFile in = new RandomAccessFile("TempFiles/cp-temp.end", "rw");
            RandomAccessFile out = new RandomAccessFile(dirname + "/dec." + extname, "rw");

            functionSet.shuffle(fn, in); // deshuffle
            functionSet.rounds(in, out, key, shiftby, "Decrypting"); // xor

            File f = new File("TempFiles/cp-temp.end");
            f.delete();

            // release resources
            in.close();
            out.close();
            fn.close();

        } catch (IOException e) {
            System.out.println(e);
        }
    }

}

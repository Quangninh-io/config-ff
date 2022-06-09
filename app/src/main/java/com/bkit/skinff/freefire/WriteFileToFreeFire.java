package com.bkit.skinff.freefire;

import android.content.Context;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class WriteFileToFreeFire {


    private static WriteFileToFreeFire instance;
    public static WriteFileToFreeFire getInstance(){
        if(instance==null){
            return new WriteFileToFreeFire();
        }
        return instance;
    }
    //uri : storage location
    public void writeFile(Uri uri, Context context, byte[] bytes) {
        try {
            ParcelFileDescriptor pfd = context.getContentResolver().
                    openFileDescriptor(uri, "w");
            FileOutputStream fileOutputStream = new FileOutputStream(pfd.getFileDescriptor());
            //String value = new String(bytes);
            fileOutputStream.write(bytes);
            // Let the document provider know you're done by closing the stream.
            fileOutputStream.close();
            pfd.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

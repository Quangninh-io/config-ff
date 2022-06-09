package com.bkit.skinff.encode;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class Encode {
    private static Encode instance;
    public static Encode getInstance(){
        if(instance==null){
            return new Encode();
        }
        return instance;
    }

    public String encodeImage(Bitmap bitmap){
        int previewWidth = 150;
        int previewHeight = (previewWidth * bitmap.getHeight())/bitmap.getWidth();

        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap,previewWidth,previewHeight,false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes,Base64.DEFAULT);
    }
}

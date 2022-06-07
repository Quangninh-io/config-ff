package com.bkit.skinff.firebase;

import static com.bkit.skinff.utilities.AssessStorage.NAME_IMAGE;
import static com.bkit.skinff.utilities.AssessStorage.RESCONF;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bkit.skinff.freefire.WriteFileToFreeFire;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DownloadFile {
    private TextView txtGetTime;
    private ProgressBar pbUpload;
    private ImageView imgResult;
    public DownloadFile(TextView txtGetTime, ProgressBar pbUpload, ImageView imgResult) {
        this.txtGetTime = txtGetTime;
        this.pbUpload = pbUpload;
        this.imgResult = imgResult;
    }

    public void downloadImage() {
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();
        String date = txtGetTime.getText().toString().trim();
        StorageReference imgRef = storageReference.child(date+"/"+NAME_IMAGE);
        final long ONE_MEGABYTE = 1024 * 1024;
        imgRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            imgResult.setImageBitmap(bitmap);
        }).addOnFailureListener(e -> {
            Log.d("Fail something", "fail");
        });
    }

    public void downLoadFile(Uri uri, Context context){
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();
        String date = txtGetTime.getText().toString().trim();
        StorageReference imgRef = storageReference.child(date+"/"+RESCONF);
        final long ONE_MEGABYTE = 3000000;
        pbUpload.setVisibility(View.VISIBLE);
        imgRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
            pbUpload.setVisibility(View.INVISIBLE);
            WriteFileToFreeFire.getInstance().writeFile(uri, context, bytes);
            Log.d("byte", String.valueOf(bytes));
        }).addOnFailureListener(e -> {
            pbUpload.setVisibility(View.INVISIBLE);
            Log.d("Fail something", "fail");
        });
    }
}

package com.bkit.skinff.firebase;

import static com.bkit.skinff.utilities.Constants.NAME_IMAGE;
import static com.bkit.skinff.utilities.Constants.RESCONF;

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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.concurrent.atomic.DoubleAccumulator;

public class DownloadFile {

    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference = firebaseStorage.getReference();

    private static DownloadFile instance;

    public static DownloadFile getInstance(){
        if(instance==null){
            return new DownloadFile();
        }
        return instance;
    }

    public void downLoadFile(ProgressBar pb, String time,Uri uri, Context context, String model, String getType,String type){
        String uriLocal = model+"/"+getType+"/"+time;
        StorageReference imgRef = storageReference.child(uriLocal+"/"+type);
        final long ONE_MEGABYTE = 3000000;

        pb.setVisibility(View.VISIBLE);
        imgRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
            pb.setVisibility(View.INVISIBLE);
            WriteFileToFreeFire.getInstance().writeFile(uri, context, bytes);
            Log.d("byte", String.valueOf(bytes));
        }).addOnFailureListener(e -> {
            pb.setVisibility(View.INVISIBLE);
            Log.d("Fail something", "fail");
        });
    }


}

package com.bkit.skinff.firebase;

import static com.bkit.skinff.utilities.Constants.BUTTON_TAG;
import static com.bkit.skinff.utilities.Constants.NAME_IMAGE;
import static com.bkit.skinff.utilities.Constants.RESCONF;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class UploadFile {
    private final TextView txtGetTime;
    private final Button btnUpload;
    private final ProgressBar pbUpload;

    public UploadFile(TextView txtGetTime, Button btnUpload, ProgressBar pbUpload) {
        this.txtGetTime = txtGetTime;
        this.btnUpload = btnUpload;
        this.pbUpload = pbUpload;
    }

    public void putFileFirebaseStorage(Uri uri) {
        StorageReference storageRef;
        String timeUpdate = txtGetTime.getText().toString().trim();
        if(btnUpload.getTag().equals(BUTTON_TAG)){
            storageRef = FirebaseStorage.getInstance().getReference(timeUpdate+ "/"+RESCONF);
        }else{
            storageRef = FirebaseStorage.getInstance().getReference(timeUpdate+ "/"+NAME_IMAGE);
        }
        pbUpload.setVisibility(View.VISIBLE);
        storageRef.putFile(uri)
                .addOnSuccessListener(v -> {
                    Log.d("Upload success", "1");//UploadTask.TaskSnapshot taskSnapshot
                    pbUpload.setVisibility(View.INVISIBLE);
                })
                .addOnFailureListener(v -> {
                    Log.d("Upload fail", "0");//Exception e
                    pbUpload.setVisibility(View.INVISIBLE);
                });
    }


}

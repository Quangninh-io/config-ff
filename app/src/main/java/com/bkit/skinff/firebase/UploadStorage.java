package com.bkit.skinff.firebase;

import static com.bkit.skinff.utilities.Constants.BUTTON_TAG;
import static com.bkit.skinff.utilities.Constants.KEY_IMAGE;
import static com.bkit.skinff.utilities.Constants.NAME_IMAGE;
import static com.bkit.skinff.utilities.Constants.RESCONF;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bkit.skinff.listener.GetLinkImage;
import com.bkit.skinff.model.FileData;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class UploadStorage {

    private static UploadStorage instance;

    public static UploadStorage getInstance() {
        if (instance == null) {
            return new UploadStorage();
        }
        return instance;
    }

    public void putFileFirebaseStorage(Uri uri, String model, String type, String time, ProgressBar pbUpload , String name) {
        StorageReference storageRef;
        pbUpload.setVisibility(View.VISIBLE);
        storageRef = FirebaseStorage.getInstance().getReference(model + "/" + type + "/" + time + "/" + name);
        storageRef.putFile(uri)
                .addOnSuccessListener(taskSnapshot -> {
                    pbUpload.setVisibility(View.INVISIBLE);
                })
                .addOnFailureListener(v -> {
                    Log.d("Upload fail", "0");//Exception e
                    pbUpload.setVisibility(View.INVISIBLE);
                });
    }

    public void getUriImage(String model, String type, String time, TextView txtLinkImage){
        StorageReference storageRef;
        storageRef = FirebaseStorage.getInstance().getReference(model + "/" + type + "/" + time + "/" + KEY_IMAGE);
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                txtLinkImage.setText(String.valueOf(uri));
            }
        });
    }


}

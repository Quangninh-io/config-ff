package com.bkit.skinff.firebase;

import static com.bkit.skinff.utilities.Constants.KEY_IMAGE;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UploadStorage {
    private static UploadStorage instance;
    public static UploadStorage getInstance() {
        if (instance == null) {
            return new UploadStorage();
        }
        return instance;
    }
    // upload file to storage
    public void putFileFirebaseStorage(Context context, Uri uri, String model, String type, String time, ProgressBar pbUpload , String name) {
        StorageReference storageRef;
        pbUpload.setVisibility(View.VISIBLE);
        storageRef = FirebaseStorage.getInstance().getReference(model + "/" + type + "/" + time + "/" + name);
        storageRef.putFile(uri)
                .addOnSuccessListener(taskSnapshot -> {
                    pbUpload.setVisibility(View.INVISIBLE);
                    Toast.makeText(context, "thành công", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(v -> {
                    Log.d("Upload fail", "0");//Exception e
                    pbUpload.setVisibility(View.INVISIBLE);
                    Toast.makeText(context, "thất bại", Toast.LENGTH_SHORT).show();
                });
    }
    // handle get link image in storage to upload link to firestore
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

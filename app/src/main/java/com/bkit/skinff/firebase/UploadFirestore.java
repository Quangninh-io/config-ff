package com.bkit.skinff.firebase;

import static com.bkit.skinff.utilities.Constants.COLLECTION;
import static com.bkit.skinff.utilities.Constants.COLLECTION_NAME;
import static com.bkit.skinff.utilities.Constants.DOCUMENT_PATH;
import static com.bkit.skinff.utilities.Constants.KEY_IMAGE;
import static com.bkit.skinff.utilities.Constants.KEY_MODEL;
import static com.bkit.skinff.utilities.Constants.KEY_NAME;
import static com.bkit.skinff.utilities.Constants.KEY_NAME_FILE;
import static com.bkit.skinff.utilities.Constants.KEY_OUTFIT;
import static com.bkit.skinff.utilities.Constants.KEY_TIME;
import static com.bkit.skinff.utilities.Constants.KEY_TYPE;
import static com.bkit.skinff.utilities.Constants.KEY_WEAPON;
import static com.bkit.skinff.utilities.Constants.LINK_IMAGE_DEFAULT;
import static com.bkit.skinff.utilities.Constants.RESCONF;


import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class UploadFirestore {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static UploadFirestore instance;
    public static UploadFirestore getInstance(){
        if(instance==null){
            return new UploadFirestore();
        }
        return instance;
    }


    public void uploadToFirestore(String model,String name, String time, String type, ProgressBar pbUpload, String linkImage, String nameFile){
        Map<String, Object> fileData = new HashMap<>();
        fileData.put(KEY_IMAGE,linkImage);
        fileData.put(KEY_MODEL,model);
        fileData.put(KEY_NAME,name);
        fileData.put(KEY_TIME, time);
        fileData.put(KEY_TYPE, type);
        fileData.put(KEY_NAME_FILE,nameFile);
        pbUpload.setVisibility(View.VISIBLE);
        db.collection(COLLECTION)
                .add(fileData)
                .addOnSuccessListener(documentReference -> {
                    pbUpload.setVisibility(View.INVISIBLE);
                })
                .addOnFailureListener(e->{
                    pbUpload.setVisibility(View.INVISIBLE);
                    Log.e("error put", String.valueOf(e));
                });
    }

    public void updateName(String type,String weapon, ProgressBar pbUpload){
        Map<String, Object> fileData = new HashMap<>();
        fileData.put(type,weapon);
        pbUpload.setVisibility(View.VISIBLE);
        db.collection(COLLECTION_NAME)
                .document(DOCUMENT_PATH)
                .update(fileData)
                .addOnSuccessListener(documentReference -> {
                    pbUpload.setVisibility(View.INVISIBLE);
                })
                .addOnFailureListener(e->{
                    pbUpload.setVisibility(View.INVISIBLE);
                    Log.e("error put", String.valueOf(e));
                });
    }

}

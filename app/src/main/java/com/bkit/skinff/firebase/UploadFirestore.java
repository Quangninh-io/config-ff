package com.bkit.skinff.firebase;

import static com.bkit.skinff.utilities.Constants.COLLECTION;
import static com.bkit.skinff.utilities.Constants.COLLECTION_NAME;
import static com.bkit.skinff.utilities.Constants.DOCUMENT_PATH;
import static com.bkit.skinff.utilities.Constants.KEY_IMAGE;
import static com.bkit.skinff.utilities.Constants.KEY_MODEL;
import static com.bkit.skinff.utilities.Constants.KEY_NAME;
import static com.bkit.skinff.utilities.Constants.KEY_NAME_FILE;
import static com.bkit.skinff.utilities.Constants.KEY_TIME;
import static com.bkit.skinff.utilities.Constants.KEY_TYPE;
import static com.bkit.skinff.utilities.Constants.NAME_IMAGE;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

// use for admin to uplaod file to firebase
public class UploadFirestore {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static UploadFirestore instance;
    public static UploadFirestore getInstance(){
        if(instance==null){
            return new UploadFirestore();
        }
        return instance;
    }

    // upload to firestore with collection is file
    public void uploadToFirestore(Context context, String model, String name, String time, String type, ProgressBar pbUpload, String linkImage, String nameFile){
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
                    Toast.makeText(context,"thành công",Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e->{
                    pbUpload.setVisibility(View.INVISIBLE);
                    Toast.makeText(context,"thất bại",Toast.LENGTH_SHORT).show();
                    Log.e("error put", String.valueOf(e));
                });
    }
    // upload correct name file current
    // use to compare with name of file, if different , show annotation for admin
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

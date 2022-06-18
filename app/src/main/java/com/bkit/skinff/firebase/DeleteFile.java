package com.bkit.skinff.firebase;

import static com.bkit.skinff.utilities.Constants.COLLECTION;
import static com.bkit.skinff.utilities.Constants.KEY_IMAGE;
import static com.bkit.skinff.utilities.Constants.KEY_TIME;
import static com.bkit.skinff.utilities.Constants.NAME_IMAGE;
import static com.bkit.skinff.utilities.Constants.RESCONF;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DeleteFile {
    private static DeleteFile instance;
    public static DeleteFile getInstance(){
        if(instance==null){
            return new DeleteFile();
        }
        return instance;
    }

    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference = firebaseStorage.getReference();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public void deleteStorage( String model, String type, String time , String name){
        StorageReference docRef = storageReference.child(model + "/" + type + "/" + time + "/" + name);
        docRef.delete().addOnSuccessListener(unused -> {
            Log.d("delete success","fjkal");
        }).addOnFailureListener(e -> {
            Log.d("delete fail","fjkal");
        });

    }
    public void deleteFirestore(String idDocument, ProgressBar pb){
        pb.setVisibility(View.VISIBLE);
        db.collection(COLLECTION)
                .document(idDocument)
                .delete()
                .addOnSuccessListener(unused -> {
                    pb.setVisibility(View.INVISIBLE);
                })
                .addOnFailureListener(e -> {
                    pb.setVisibility(View.VISIBLE);
                });
    }
}

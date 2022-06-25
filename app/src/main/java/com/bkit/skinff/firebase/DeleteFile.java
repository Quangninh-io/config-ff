package com.bkit.skinff.firebase;

import static com.bkit.skinff.utilities.Constants.COLLECTION;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

// use for admin can delete data
public class DeleteFile {
    private static DeleteFile instance;
    private final FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private final StorageReference storageReference = firebaseStorage.getReference();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static DeleteFile getInstance(){
        if(instance==null){
            return new DeleteFile();
        }
        return instance;
    }
    // delete data in storage
    public void deleteStorage( String model, String type, String time , String name){
        StorageReference docRef = storageReference.child(model + "/" + type + "/" + time + "/" + name);
        docRef.delete().addOnSuccessListener(unused -> {
            Log.d("delete success","fjkal");
        }).addOnFailureListener(e -> {
            Log.d("delete fail","fjkal");
        });
    }
    // delete data in firestore
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

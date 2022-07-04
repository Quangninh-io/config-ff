package com.bkit.skinff.firebase;

import static com.bkit.skinff.utilities.Constants.COLLECTION;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

// use for admin can delete data
public class DeleteFile {
    private static DeleteFile instance;
    private final FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private final StorageReference storageReference = firebaseStorage.getReference();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static DeleteFile getInstance() {
        if (instance == null) {
            return new DeleteFile();
        }
        return instance;
    }

    public void deleteFileRoot(String model, String type, String time) {
        StorageReference listRef = firebaseStorage.getReference().child(model + "/" + type + "/" + time);
        listRef.listAll()
                .addOnSuccessListener(listResult -> {
                    for (StorageReference item : listResult.getItems()) {
                        StringBuilder nameFile = new StringBuilder();
                        nameFile.append(String.valueOf(item));
                        nameFile.delete(0, 28);
                        String nameMain = String.valueOf(nameFile);
                        deleteStorage("","","",nameMain);
                    }
                });
    }

    // delete data in storage
    public void deleteStorage(String model, String type, String time, String name) {
        StorageReference docRef = storageReference.child(model + "/" + type + "/" + time + "/" + name);
        if(model.equals("")){
           docRef = storageReference.child(name);
        }
        docRef.delete().addOnSuccessListener(unused -> {
            Log.d("delete success", "fjkal");
        }).addOnFailureListener(e -> {
            Log.d("delete fail", "fjkal");
        });
    }


    // delete data in firestore
    public void deleteFirestore(String idDocument, ProgressBar pb) {
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

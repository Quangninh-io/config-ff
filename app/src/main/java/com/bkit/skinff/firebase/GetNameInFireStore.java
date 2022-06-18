package com.bkit.skinff.firebase;

import static com.bkit.skinff.utilities.Constants.COLLECTION;
import static com.bkit.skinff.utilities.Constants.COLLECTION_NAME;
import static com.bkit.skinff.utilities.Constants.KEY_IMAGE;
import static com.bkit.skinff.utilities.Constants.KEY_MODEL;
import static com.bkit.skinff.utilities.Constants.KEY_NAME;
import static com.bkit.skinff.utilities.Constants.KEY_OUTFIT;
import static com.bkit.skinff.utilities.Constants.KEY_TIME;
import static com.bkit.skinff.utilities.Constants.KEY_TYPE;
import static com.bkit.skinff.utilities.Constants.KEY_WEAPON;

import android.util.Log;

import com.bkit.skinff.model.FileData;
import com.bkit.skinff.model.Name;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class GetNameInFireStore {
    private static GetNameInFireStore instance;
    public static GetNameInFireStore getInstance(){
        if (instance==null){
            return new GetNameInFireStore();
        }
        return instance;
    }
    Name name = new Name();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public void getName(){
        db.collection(COLLECTION_NAME)
                .get()
                .addOnCompleteListener(task -> {
                    ArrayList<FileData> fileData = new ArrayList<>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String weapon = String.valueOf(document.getData().get(KEY_WEAPON));
                            String outfit = String.valueOf(document.getData().get(KEY_OUTFIT));
                            name.setOutfit(outfit);
                            name.setWeapon(weapon);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.d("error read file", "error");
                });
    }
}

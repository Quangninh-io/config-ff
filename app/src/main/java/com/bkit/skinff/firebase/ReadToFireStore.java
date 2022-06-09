package com.bkit.skinff.firebase;

import static com.bkit.skinff.utilities.Constants.COLLECTION;
import static com.bkit.skinff.utilities.Constants.KEY_IMAGE;
import static com.bkit.skinff.utilities.Constants.KEY_TIME;

import android.util.Log;

import com.bkit.skinff.listener.ReceiveDataFromFirebase;
import com.bkit.skinff.model.FileData;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ReadToFireStore {


    ReceiveDataFromFirebase receiveDataFromFirebase;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public ReadToFireStore(ReceiveDataFromFirebase receiveDataFromFirebase) {
        showData();
        this.receiveDataFromFirebase = receiveDataFromFirebase;
    }

    public void showData() {
        db.collection(COLLECTION)
                .get()
                .addOnCompleteListener(task -> {
                    ArrayList<FileData> fileData = new ArrayList<>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String base64Image = String.valueOf(document.getData().get(KEY_IMAGE));
                            String time = String.valueOf(document.getData().get(KEY_TIME));
                            fileData.add(new FileData(time,base64Image));
                        }

                    }
                    receiveDataFromFirebase.data(fileData);


                })
                .addOnFailureListener(e -> {
                    Log.d("error read file","error");
                });

    }

}

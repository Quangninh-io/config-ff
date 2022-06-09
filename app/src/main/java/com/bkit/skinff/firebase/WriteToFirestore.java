package com.bkit.skinff.firebase;

import static com.bkit.skinff.utilities.Constants.COLLECTION;
import static com.bkit.skinff.utilities.Constants.KEY_IMAGE;
import static com.bkit.skinff.utilities.Constants.KEY_TIME;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class WriteToFirestore {
    ProgressBar pbUpload;
    TextView txtGetTime;
    FirebaseFirestore db;

    public WriteToFirestore(ProgressBar pbUpload, TextView txtGetTime, FirebaseFirestore db) {
        this.pbUpload = pbUpload;
        this.txtGetTime = txtGetTime;
        this.db = db;
    }

    public void uploadTimeToFirestore(){
        Map<String, Object> timeUpdate = new HashMap<>();
        String time = txtGetTime.getText().toString().trim();
        timeUpdate.put(KEY_TIME, time);
        timeUpdate.put(KEY_IMAGE,"");
        pbUpload.setVisibility(View.VISIBLE);
        db.collection(COLLECTION)
                .add(timeUpdate)
                .addOnSuccessListener(documentReference -> {
                    pbUpload.setVisibility(View.INVISIBLE);
                })
                .addOnFailureListener(e->{
                    pbUpload.setVisibility(View.INVISIBLE);
                    Log.e("error put", String.valueOf(e));
                });
    }

    public void uploadImageToFirestore(String encodeImage){
        pbUpload.setVisibility(View.VISIBLE);
        String time = txtGetTime.getText().toString().trim();
        db.collection(COLLECTION)
                .whereEqualTo(KEY_TIME,time)
                .get()
                .addOnCompleteListener(task -> {
                    DocumentSnapshot snapshot = task.getResult().getDocuments().get(0);
                    String documentID = snapshot.getId();
                    db.collection(COLLECTION).document(documentID)
                            .update(KEY_IMAGE,encodeImage)
                            .addOnSuccessListener(v->{
                                pbUpload.setVisibility(View.INVISIBLE);
                            })
                            .addOnFailureListener(e -> {
                                pbUpload.setVisibility(View.INVISIBLE);
                                Log.d("error update", String.valueOf(e));
                            });
                })
                .addOnFailureListener(e -> {
                    Log.d("error find collect corresponding", String.valueOf(e));
                });
    }
}

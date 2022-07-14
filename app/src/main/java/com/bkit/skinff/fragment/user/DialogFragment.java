package com.bkit.skinff.fragment.user;

import static com.bkit.skinff.utilities.Constants.COLLECTION;
import static com.bkit.skinff.utilities.Constants.KEY_IMAGE;
import static com.bkit.skinff.utilities.Constants.KEY_MODEL;
import static com.bkit.skinff.utilities.Constants.KEY_NAME;
import static com.bkit.skinff.utilities.Constants.KEY_NAME_FILE;
import static com.bkit.skinff.utilities.Constants.KEY_OUTFIT;
import static com.bkit.skinff.utilities.Constants.KEY_TIME;
import static com.bkit.skinff.utilities.Constants.KEY_TYPE;

import android.app.Dialog;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bkit.skinff.R;
import com.bkit.skinff.model.FileData;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.Locale;

public class DialogFragment extends androidx.fragment.app.DialogFragment {
    private static final String NEXT = "next";
    private static final String PREVIOUS = "previous";
    ImageView ivImage, ivNext, ivPrevious;
    TextView tvName;
    FileData fileData;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    int namePos = -2;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        fileData = (FileData) this.getArguments().getSerializable(KEY_NAME);
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_transform_package);
        int x = ViewGroup.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.shape_dialog));
        ivImage = dialog.findViewById(R.id.iv_trans);
        tvName = dialog.findViewById(R.id.tv_name_trans);
        ivNext = dialog.findViewById(R.id.iv_next);
        ivPrevious = dialog.findViewById(R.id.iv_previous);
        initMain();
        dialog.show();
        return dialog;
    }


    private void initMain() {
        setSizeCollection();
        Picasso.get().load(fileData.getImage()).into(ivImage);
        if (fileData.getType().equals(KEY_OUTFIT)) {
            tvName.setText(getResources().getString(R.string.collection_outfit) + fileData.getName());
        } else {
            tvName.setText(getResources().getString(R.string.collection_gun) + fileData.getName());
        }
        ivImage.setOnClickListener(v -> {

        });
        ivNext.setOnClickListener(v -> {
            handleNext();
        });
        ivPrevious.setOnClickListener(v -> {
            handlePrevious();
        });
    }
    int count =0;
    private void setSizeCollection(){
        db.collection(COLLECTION)
                .whereEqualTo(KEY_TYPE,fileData.getType())
                .whereEqualTo(KEY_MODEL,fileData.getModel())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            count++;
                        }
                    }
                });
    }

    private void handlePrevious() {
        if (namePos < -1) {
            namePos = Integer.parseInt(fileData.getName());
        }
        if(namePos == 1){
            namePos = count;
        }else {
            namePos = namePos - 1;
        }
        getDataFromFirebase(String.valueOf(namePos), PREVIOUS);
    }

    private void handleNext() {
        if (namePos < -1) {
            namePos = Integer.parseInt(fileData.getName());
        }
        if(namePos==count){
            namePos = 1;
        } else{
            namePos = namePos + 1;
        }
        getDataFromFirebase(String.valueOf(namePos), NEXT);
    }

    private void getDataFromFirebase(String name, String type) {
        db.collection(COLLECTION)
                .whereEqualTo(KEY_NAME, name)
                .whereEqualTo(KEY_TYPE,fileData.getType())
                .whereEqualTo(KEY_MODEL,fileData.getModel())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String image = String.valueOf(document.getData().get(KEY_IMAGE));
                                String model = String.valueOf(document.getData().get(KEY_MODEL));
                                String name = String.valueOf(document.getData().get(KEY_NAME));
                                String time = String.valueOf(document.getData().get(KEY_TIME));
                                String type = String.valueOf(document.getData().get(KEY_TYPE));
                                String documentId = document.getId();
                                String nameFile = String.valueOf(document.getData().get(KEY_NAME_FILE));
                                FileData data = new FileData(image, model,name,time,type,documentId,nameFile);
                                handleRefreshNewActivity(data);
                                Log.d("fjdal",name);
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (type.equals(NEXT)) {
                            handleNext();
                        }
                        if (type.equals(PREVIOUS)) {
                            handlePrevious();
                        }
                    }
                });
    }

    private void handleRefreshNewActivity(FileData data) {
        Picasso.get().load(data.getImage()).placeholder(R.drawable.wai).into(ivImage);
        if (data.getType().equals(KEY_OUTFIT)) {
            tvName.setText(getResources().getString(R.string.collection_outfit) + data.getName());
        } else {
            tvName.setText(getResources().getString(R.string.collection_gun) + data.getName());
        }
    }


}

package com.bkit.skinff.activity;

import static com.bkit.skinff.utilities.Constants.CLOTHES;
import static com.bkit.skinff.utilities.Constants.COLLECTION;
import static com.bkit.skinff.utilities.Constants.COLLECTION_NAME;
import static com.bkit.skinff.utilities.Constants.KEY_IMAGE;
import static com.bkit.skinff.utilities.Constants.KEY_MODEL;
import static com.bkit.skinff.utilities.Constants.KEY_NAME;
import static com.bkit.skinff.utilities.Constants.KEY_NAME_FILE;
import static com.bkit.skinff.utilities.Constants.KEY_OUTFIT;
import static com.bkit.skinff.utilities.Constants.KEY_TIME;
import static com.bkit.skinff.utilities.Constants.KEY_TYPE;
import static com.bkit.skinff.utilities.Constants.KEY_WEAPON;
import static com.bkit.skinff.utilities.Constants.RESCONF;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.bkit.skinff.adapter.AdminAdapter;
import com.bkit.skinff.databinding.ActivityAdminPreviewBinding;
import com.bkit.skinff.firebase.DeleteFile;
import com.bkit.skinff.listener.ClickToModify;
import com.bkit.skinff.model.FileData;
import com.bkit.skinff.model.Name;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class AdminPreviewActivity extends AppCompatActivity {

    private ActivityAdminPreviewBinding binding;
    AdminAdapter adapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String nameWeapon ="";
    String nameOutfit = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminPreviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getNameCorrect();
        getDataFromFireStore();

    }

    private void getNameCorrect() {
        db.collection(COLLECTION_NAME)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                           String weapon = String.valueOf(document.getData().get(KEY_WEAPON));
                           String outfit = String.valueOf(document.getData().get(KEY_OUTFIT));
                           nameWeapon = weapon;
                           nameOutfit = outfit;
                        }
                    }

                })
                .addOnFailureListener(e -> {
                    Log.d("error read file", "error");
                });
    }

    private void getDataFromFireStore() {
        db.collection(COLLECTION)
                .get()
                .addOnCompleteListener(task -> {
                    ArrayList<FileData> fileData = new ArrayList<>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String image = String.valueOf(document.getData().get(KEY_IMAGE));
                            String model = String.valueOf(document.getData().get(KEY_MODEL));
                            String name = String.valueOf(document.getData().get(KEY_NAME));
                            String time = String.valueOf(document.getData().get(KEY_TIME));
                            String type = String.valueOf(document.getData().get(KEY_TYPE));
                            String documentId = document.getId();
                            String nameFile = String.valueOf(document.getData().get(KEY_NAME_FILE));
                            boolean checkName = false;
                            if(type.equals("outfit")){
                                if(nameFile.equals(nameOutfit)){
                                    checkName = true;
                                }
                                else{
                                   checkName = false;
                                }
                            }else if(type.equals("gun")){
                                if(nameFile.equals(nameWeapon)){
                                    checkName = true;
                                }else{
                                    checkName = false;
                                }
                            }
                            fileData.add(new FileData(image, model, name, time, type, documentId,nameFile,checkName));
                        }
                    }
                    adapter = new AdminAdapter(fileData, new ClickToModify() {
                        @Override
                        public void modify(FileData fileData) {
                            DeleteFile.getInstance().deleteFirestore(fileData.getDocumentId(), binding.pb);
                            DeleteFile.getInstance().deleteStorage(fileData.getModel(), fileData.getType(), fileData.getTime(), KEY_IMAGE);
                            DeleteFile.getInstance().deleteStorage(fileData.getModel(),fileData.getType(),fileData.getTime(),fileData.getNameFile());
                        }
                    });
                    binding.rcvAdmin.setAdapter(adapter);

                })
                .addOnFailureListener(e -> {
                    Log.d("error read file", "error");
                });
    }

}
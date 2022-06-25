package com.bkit.skinff.activity;

import static com.bkit.skinff.utilities.Constants.COLLECTION;
import static com.bkit.skinff.utilities.Constants.COLLECTION_NAME;
import static com.bkit.skinff.utilities.Constants.KEY_IMAGE;
import static com.bkit.skinff.utilities.Constants.KEY_MODEL;
import static com.bkit.skinff.utilities.Constants.KEY_NAME;
import static com.bkit.skinff.utilities.Constants.KEY_NAME_FILE;
import static com.bkit.skinff.utilities.Constants.KEY_OUTFIT;
import static com.bkit.skinff.utilities.Constants.KEY_OUTFIT_MAX;
import static com.bkit.skinff.utilities.Constants.KEY_TIME;
import static com.bkit.skinff.utilities.Constants.KEY_TYPE;
import static com.bkit.skinff.utilities.Constants.KEY_WEAPON;
import static com.bkit.skinff.utilities.Constants.KEY_WEAPON_MAX;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.bkit.skinff.adapter.AdminAdapter;
import com.bkit.skinff.databinding.ActivityAdminPreviewBinding;
import com.bkit.skinff.firebase.DeleteFile;
import com.bkit.skinff.listener.ClickToModify;
import com.bkit.skinff.model.FileData;
import com.bkit.skinff.utilities.ArrangeTime;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.Collections;

public class AdminPreviewActivity extends AppCompatActivity {
    private ActivityAdminPreviewBinding binding;
    private AdminAdapter adapter;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    String nameWeapon="", nameOutfit = "", nameWeaponMax = "", nameOutfitMax ="";
    DeleteFile delete = DeleteFile.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminPreviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String data =getIntent().getStringExtra("package")+"";
        Log.d("fada",data);
        getNameCorrect();

    }
    // get name from collection "name"
    // target to compare name in collection "name" with name in collection "file"
    private void getNameCorrect() {

        db.collection(COLLECTION_NAME)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            nameWeapon = String.valueOf(document.getData().get(KEY_WEAPON));
                            nameOutfit = String.valueOf(document.getData().get(KEY_OUTFIT));
                            nameWeaponMax = String.valueOf(document.getData().get(KEY_WEAPON_MAX));
                            nameOutfitMax = String.valueOf(document.getData().get(KEY_OUTFIT_MAX));
                        }
                    }
                    getDataFromFireStore();
                })
                .addOnFailureListener(e -> {
                    Log.d("error read file", "error");
                });
    }
    // get all data in collection "file"
    // do check if any field wrong
    // set adapter for recycle view
    // delete data in firestore and storage
    private void getDataFromFireStore() {
        db.collection(COLLECTION)
                .whereEqualTo(KEY_TYPE,getIntent().getStringExtra(KEY_TYPE))
                .whereEqualTo(KEY_MODEL,getIntent().getStringExtra(KEY_MODEL))
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
                            if(model.equals("ff")){
                                if(type.equals("outfit")){
                                    if(nameFile.equals(nameOutfit)){
                                        checkName = true;
                                    }
                                }else if(type.equals("gun")){
                                    if(nameFile.equals(nameWeapon)){
                                        checkName = true;
                                    }
                                }
                            }
                            if(model.equals("ffmax")){
                                if(type.equals("outfit")){
                                    if(nameFile.equals(nameOutfitMax)){
                                        checkName = true;
                                    }
                                }else if(type.equals("gun")){
                                    if(nameFile.equals(nameWeaponMax)){
                                        checkName = true;
                                    }
                                }
                            }
                            ArrangeTime arrangeTime = ArrangeTime.getInstance();
                            Collections.sort(fileData, arrangeTime.comparator);
                            fileData.add(new FileData(image, model, name, time, type, documentId,nameFile,checkName));
                        }
                    }
                    adapter = new AdminAdapter(fileData, new ClickToModify() {
                        @Override
                        public void modify(FileData fileData) {
                            delete.deleteFirestore(fileData.getDocumentId(), binding.pb);
                            delete.deleteStorage(fileData.getModel(), fileData.getType(), fileData.getTime(), KEY_IMAGE);
                            delete.deleteStorage(fileData.getModel(),fileData.getType(),fileData.getTime(),fileData.getNameFile());

                        }
                    });
                    binding.rcvAdmin.setAdapter(adapter);
                })
                .addOnFailureListener(e -> {
                    Log.d("error read file", "error");
                });
    }

}
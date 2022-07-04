package com.bkit.skinff.activity;

import static com.bkit.skinff.utilities.Constants.COLLECTION;
import static com.bkit.skinff.utilities.Constants.INTENT_DETAIL;
import static com.bkit.skinff.utilities.Constants.INTENT_MODEL;
import static com.bkit.skinff.utilities.Constants.INTENT_CHOSE_MODEL;
import static com.bkit.skinff.utilities.Constants.INTENT_NAME;
import static com.bkit.skinff.utilities.Constants.INTENT_OUTFIT;
import static com.bkit.skinff.utilities.Constants.INTENT_WEAPON;
import static com.bkit.skinff.utilities.Constants.KEY_IMAGE;
import static com.bkit.skinff.utilities.Constants.KEY_MODEL;
import static com.bkit.skinff.utilities.Constants.KEY_NAME;
import static com.bkit.skinff.utilities.Constants.KEY_NAME_FILE;
import static com.bkit.skinff.utilities.Constants.KEY_TIME;
import static com.bkit.skinff.utilities.Constants.KEY_TYPE;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import com.bkit.skinff.R;
import com.bkit.skinff.adapter.UserAdapter;
import com.bkit.skinff.databinding.ActivityUserWeaponBinding;
import com.bkit.skinff.firebase.DownloadFile;
import com.bkit.skinff.listener.ClickSpecificItem;
import com.bkit.skinff.model.FileData;
import com.bkit.skinff.model.Name;
import com.bkit.skinff.utilities.ArrangeTime;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

public class UserOutfitActivity extends AppCompatActivity {

    private ActivityUserWeaponBinding binding;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private final DownloadFile downloadFile = DownloadFile.getInstance();
    UserAdapter adapter;
    Uri uriOutfit, uriWeapon;
    Name name;
    String decideChoseModel = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserWeaponBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        SetLanguage.getInstance().configLanguage(this);
        initMain();
    }
    // initial run activity
    // catch event foe button delete, handle delete by the way override file initial of free fire saved in storage
    // receive data from user main activity delivery by the way intent ( uri weapon, outfit, chose model, name file)
    // proceed get data form firestore with collection is "file"
    // with condition is what is model, type = outfit( because we handle in activity outfit)
    // data received is data array, prepare to assign for adapter
    private void initMain() {
        String result = getIntent().getStringExtra(INTENT_MODEL);
        binding.ivBack.setOnClickListener(v->{
            onBackPressed();
        });

        uriOutfit = Uri.parse(getIntent().getStringExtra(INTENT_OUTFIT));
        uriWeapon = Uri.parse(getIntent().getStringExtra(INTENT_WEAPON));
        decideChoseModel = getIntent().getStringExtra(INTENT_CHOSE_MODEL);
        name = (Name) getIntent().getSerializableExtra(INTENT_NAME);
        db.collection(COLLECTION)
                .whereEqualTo("model", result)
                .whereEqualTo("type", "outfit")
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
                            fileData.add(new FileData(image, model, name, time, type, documentId, nameFile));
                        }
                        ArrangeTime arrangeTime = ArrangeTime.getInstance();
                        Collections.sort(fileData, arrangeTime.comparator);
                        uploadRecycleView(fileData);
                    }
                    //receiveDataFromFirebase.data(fileData);
                })
                .addOnFailureListener(e -> {
                    Log.d("error read file", "error");
                });

    }
    // calculated time now with time of packages, if package time less current time -30, set new and vice versa
    // set data for adapter
    // send data contain (uri, model file data, name file) for user detail activity
    private void uploadRecycleView(ArrayList<FileData> list) {
        binding.tvType.setText(getResources().getString(R.string.clothes));
        adapter = new UserAdapter(list, getApplicationContext(), new ClickSpecificItem() {
            @Override
            public void click(FileData fileData) {
                Intent intent = new Intent(getApplication(), UserDetailActivity.class);
                intent.putExtra(INTENT_WEAPON, String.valueOf(uriWeapon));
                intent.putExtra(INTENT_OUTFIT, String.valueOf(uriOutfit));
                intent.putExtra(INTENT_DETAIL, fileData);
                intent.putExtra(INTENT_CHOSE_MODEL, decideChoseModel);
                intent.putExtra(INTENT_NAME, name);
                startActivity(intent);
            }
        });
        binding.rvWeapon.setAdapter(adapter);
    }

}
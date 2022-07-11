package com.bkit.skinff.activity;

import static com.bkit.skinff.utilities.Constants.CHOSE_IMAGE;
import static com.bkit.skinff.utilities.Constants.COLLECTION;
import static com.bkit.skinff.utilities.Constants.COLLECTION_NAME;
import static com.bkit.skinff.utilities.Constants.KEY_IMAGE;
import static com.bkit.skinff.utilities.Constants.KEY_NAME;
import static com.bkit.skinff.utilities.Constants.KEY_NAME_FILE;
import static com.bkit.skinff.utilities.Constants.KEY_OUTFIT;
import static com.bkit.skinff.utilities.Constants.KEY_OUTFIT_MAX;
import static com.bkit.skinff.utilities.Constants.KEY_WEAPON;
import static com.bkit.skinff.utilities.Constants.KEY_WEAPON_MAX;
import static com.bkit.skinff.utilities.Constants.NAME_IMAGE;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.bkit.skinff.R;
import com.bkit.skinff.databinding.ActivityAdminUpdateBinding;
import com.bkit.skinff.firebase.DeleteFile;
import com.bkit.skinff.firebase.UploadStorage;
import com.bkit.skinff.model.FileData;
import com.bkit.skinff.sharepreference.GetUri;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class AdminUpdateActivity extends AppCompatActivity {

    ActivityAdminUpdateBinding binding;
    public static CountDownLatch doneSignal = new CountDownLatch(1);
    Uri uriImage;
    UploadStorage uploadStorage = UploadStorage.getInstance();
    GetUri getUri = GetUri.getInstance();
    FileData fileData;
    Uri uriWeapon, uriOutfit;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DeleteFile deleteFile = DeleteFile.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initMain();
    }

    private void initMain() {
        fileData = (FileData) getIntent().getSerializableExtra(KEY_NAME);
        binding.etName.setText(fileData.getName());
        Picasso.get().load(fileData.getImage()).into(binding.iv);
        binding.btChooseImage.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, CHOSE_IMAGE);
        });
        binding.btUpdateImage.setOnClickListener(v -> {
            if (uriImage != null) {
                uploadStorage.putFileFirebaseStorage(
                        this, uriImage, fileData.getModel(), fileData.getType(),
                        fileData.getTime(), binding.pb, NAME_IMAGE
                );
            }
        });
        binding.btUpdateFile.setOnClickListener(v -> {
            handleUpdateFile();
        });
        binding.btUpdateName.setOnClickListener(v -> {
            handleUpdateName();
        });
    }

    private void handleUpdateName() {
        db.collection(COLLECTION).document(fileData.getDocumentId())
                .update(KEY_NAME,binding.etName.getText().toString().trim())
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Thành công", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e->{
                    Toast.makeText(this, "Thất bại", Toast.LENGTH_SHORT).show();
                });

        fileData.setName(binding.etName.getText().toString().trim());
    }

    String fileName ="";
    private void handleUpdateFile() {
        uriWeapon = Uri.parse(getUri.getData(this, KEY_WEAPON));
        uriOutfit = Uri.parse(getUri.getData(this, KEY_OUTFIT));
        db.collection(COLLECTION_NAME)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String weapon = String.valueOf(document.getData().get(KEY_WEAPON));
                            String outfit = String.valueOf(document.getData().get(KEY_OUTFIT));
                            String weaponMax = String.valueOf(document.getData().get(KEY_WEAPON_MAX));
                            String outfitMax = String.valueOf(document.getData().get(KEY_OUTFIT_MAX));
                            if(fileData.getModel().equals("ff")){
                                if(fileData.getType().equals("gun")){
                                    fileName = weapon;
                                }else{
                                    fileName = outfit;
                                }
                            }
                            else{
                                if(fileData.getType().equals("gun")){
                                    fileName = weaponMax;
                                }else{
                                    fileName = outfitMax;
                                }
                            }
                            updateToFireBase(fileName);

                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.d("error read file", "error");
                });
    }
    private void updateToFireBase(String fileName){

        if (fileData.getType().equals("gun")) {
            uploadStorage.putFileFirebaseStorage(this, uriWeapon, fileData.getModel(), fileData.getType(),
                    fileData.getTime(), binding.pb, fileName);
        }else{
            uploadStorage.putFileFirebaseStorage(this, uriOutfit, fileData.getModel(), fileData.getType(),
                    fileData.getTime(), binding.pb, fileName);
        }
        db.collection(COLLECTION).document(fileData.getDocumentId())
                .update(KEY_NAME_FILE,fileName)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Thành công", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e->{
                    Toast.makeText(this, "Thất bại", Toast.LENGTH_SHORT).show();
                });
        deleteFile.deleteStorage(fileData.getModel(),fileData.getType(),fileData.getTime(),fileData.getNameFile());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == CHOSE_IMAGE && resultCode == Activity.RESULT_OK) {
            assert resultData != null;
            Uri uri = resultData.getData();
            binding.iv.setImageURI(uri);
            binding.iv.setTag("");
            uriImage = uri;
        }
    }

}
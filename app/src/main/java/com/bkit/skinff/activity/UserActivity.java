package com.bkit.skinff.activity;

import static com.bkit.skinff.utilities.Constants.CHOSE_FILE;
import static com.bkit.skinff.utilities.Constants.CLOTHES;
import static com.bkit.skinff.utilities.Constants.COLLECTION;
import static com.bkit.skinff.utilities.Constants.COLLECTION_NAME;
import static com.bkit.skinff.utilities.Constants.FILE_FREE_FIRE;
import static com.bkit.skinff.utilities.Constants.INITIAL_URI;
import static com.bkit.skinff.utilities.Constants.INTENT_NAME;
import static com.bkit.skinff.utilities.Constants.INTENT_OUTFIT;
import static com.bkit.skinff.utilities.Constants.INTENT_WEAPON;
import static com.bkit.skinff.utilities.Constants.KEY_IMAGE;
import static com.bkit.skinff.utilities.Constants.KEY_MODEL;
import static com.bkit.skinff.utilities.Constants.KEY_NAME;
import static com.bkit.skinff.utilities.Constants.KEY_NAME_FILE;
import static com.bkit.skinff.utilities.Constants.KEY_OUTFIT;
import static com.bkit.skinff.utilities.Constants.KEY_TIME;
import static com.bkit.skinff.utilities.Constants.KEY_TYPE;
import static com.bkit.skinff.utilities.Constants.KEY_WEAPON;
import static com.bkit.skinff.utilities.Constants.OPEN_DOCUMENT_TREE;
import static com.bkit.skinff.utilities.Constants.RESCONF;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.View;

import com.bkit.skinff.R;
import com.bkit.skinff.adapter.Viewpager2UserAdapter;
import com.bkit.skinff.databinding.ActivityUserBinding;
import com.bkit.skinff.firebase.GetNameInFireStore;
import com.bkit.skinff.freefire.WriteFileToFreeFire;
import com.bkit.skinff.model.FileData;
import com.bkit.skinff.model.Name;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class UserActivity extends AppCompatActivity {

    private ActivityUserBinding binding;
    private CompositeDisposable disposables = new CompositeDisposable();
    Uri uriWeapon, uriOutfit;
    Name name = new Name();
    String nameWeapon, nameOutfit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getName();
        getNameFile();
        //getUri();
        initMain();
    }
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private void getNameFile() {
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

    private void initMain() {
        Viewpager2UserAdapter adapter = new Viewpager2UserAdapter(this);
        binding.vp2User.setAdapter(adapter);
        binding.vp2User.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        // xu ly luong o day sau
        binding.vp2User.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                handleWelcome(position);
            }
        });

        binding.tvSuccess.setOnClickListener(v->{
            Intent intent = new Intent(getApplicationContext(),UserMainActivity.class);
            intent.putExtra(INTENT_WEAPON,String.valueOf(uriWeapon));
            intent.putExtra(INTENT_OUTFIT,String.valueOf(uriOutfit));
            intent.putExtra(INTENT_NAME, name);
            startActivity(intent);
            finish();
        });


    }

    private void handleWelcome(int position) {
        switch (position){
            case 0:
                binding.iv1.setBackground(getResources().getDrawable(R.drawable.shape_button_transfrom_white));
                binding.iv2.setBackground(getResources().getDrawable(R.drawable.shape_button_transform));
                binding.iv2.setBackground(getResources().getDrawable(R.drawable.shape_button_transform));
                break;
            case 1:
                binding.iv1.setBackground(getResources().getDrawable(R.drawable.shape_button_transform));
                binding.iv2.setBackground(getResources().getDrawable(R.drawable.shape_button_transfrom_white));
                binding.iv3.setBackground(getResources().getDrawable(R.drawable.shape_button_transform));
                break;
            case 2:
                binding.iv1.setBackground(getResources().getDrawable(R.drawable.shape_button_transform));
                binding.iv2.setBackground(getResources().getDrawable(R.drawable.shape_button_transform));
                binding.iv3.setBackground(getResources().getDrawable(R.drawable.shape_button_transfrom_white));
                binding.tvSuccess.setVisibility(View.VISIBLE);
                break;
        }
    }
    private void getName(){
        db.collection(COLLECTION_NAME)
                .get()
                .addOnCompleteListener(task -> {
                    ArrayList<FileData> fileData = new ArrayList<>();
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

//
//    private void getUri(){
//        requestSdcardAccessPermission();
//    }
//    private void requestSdcardAccessPermission() {
//        // to can access android/data
//        Intent intent = new Intent(OPEN_DOCUMENT_TREE);
//        // will be access first when opening storage
//        Uri parse = Uri.parse(FILE_FREE_FIRE);
//        intent.putExtra(INITIAL_URI,
//                DocumentsContract.buildDocumentUriUsingTree(parse, DocumentsContract.getTreeDocumentId(parse)));
//        startActivityForResult(intent, CHOSE_FILE);
//    }
//
//    @SuppressLint("WrongConstant")
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent resultData) {
//        if (resultCode == Activity.RESULT_OK && requestCode == CHOSE_FILE) {
//            assert resultData != null;
//            Uri uri = resultData.getData();
//            getContentResolver().takePersistableUriPermission(uri, resultData.getFlags() & 3);
//            uploadFile(uri);
//        }
//        super.onActivityResult(requestCode, resultCode, resultData);
//    }
//
//    private void uploadFile(Uri treeUri) {
//        getContentResolver().takePersistableUriPermission(treeUri,
//                Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//        DocumentFile pickedDir = DocumentFile.fromTreeUri(getApplicationContext(), treeUri);
//        assert pickedDir != null;
//        DocumentFile[] ddff = pickedDir.listFiles();
//        for (DocumentFile df : ddff) {
//            String name = df.getName();
//            Uri uri = df.getUri();
//            assert name != null;
//            //Log.d("jkfamnv", String.valueOf(uri));
//            if (name.contains(nameWeapon)) {
//                uriWeapon = uri;
//            }
//            if(name.contains(nameOutfit)){
//                uriOutfit = uri;
//            }
//        }
//    }
}
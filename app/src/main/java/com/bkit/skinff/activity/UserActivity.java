package com.bkit.skinff.activity;

import static com.bkit.skinff.utilities.Constants.COLLECTION_NAME;
import static com.bkit.skinff.utilities.Constants.INTENT_NAME;
import static com.bkit.skinff.utilities.Constants.INTENT_OUTFIT;
import static com.bkit.skinff.utilities.Constants.INTENT_WEAPON;
import static com.bkit.skinff.utilities.Constants.KEY_OUTFIT;
import static com.bkit.skinff.utilities.Constants.KEY_OUTFIT_MAX;
import static com.bkit.skinff.utilities.Constants.KEY_WEAPON;
import static com.bkit.skinff.utilities.Constants.KEY_WEAPON_MAX;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.bkit.skinff.R;
import com.bkit.skinff.adapter.DescriptionAdapter;
import com.bkit.skinff.databinding.ActivityUserBinding;
import com.bkit.skinff.model.FileData;
import com.bkit.skinff.model.Name;
import com.bkit.skinff.utilities.SetLanguage;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;

public class UserActivity extends AppCompatActivity {
    private ActivityUserBinding binding;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    Uri uriWeapon, uriOutfit;
    Name name = new Name();
    String nameWeapon, nameOutfit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetLanguage.getInstance().configLanguage(this);
        binding = ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        if (Build.VERSION.SDK_INT < 30) {
            binding.iv3.setVisibility(View.GONE);
        }else{
            binding.iv3.setVisibility(View.VISIBLE);
        }
        binding.tvSuccess.setText(R.string.next_to_continue);
        getName();
        getNameFile();
        initMain();
    }

    // get name in firestore with name collection is "name"
    // set total data to model Name
    private void getNameFile() {
        db.collection(COLLECTION_NAME)
                .get()
                .addOnCompleteListener(task -> {
                    ArrayList<FileData> fileData = new ArrayList<>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String weapon = String.valueOf(document.getData().get(KEY_WEAPON));
                            String outfit = String.valueOf(document.getData().get(KEY_OUTFIT));
                            String weaponMax = String.valueOf(document.getData().get(KEY_WEAPON_MAX));
                            String outfitMax = String.valueOf(document.getData().get(KEY_OUTFIT_MAX));
                            name.setOutfit(outfit);
                            name.setWeapon(weapon);
                            name.setOutfitMax(outfitMax);
                            name.setWeaponMax(weaponMax);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.d("error read file", "error");
                });
    }

    // handle transform descriptions
    // send model Name for another activity
    private void initMain() {

        if (Build.VERSION.SDK_INT >= 23 && Build.VERSION.SDK_INT < 30) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }

        DescriptionAdapter adapter = new DescriptionAdapter(this);
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
        binding.tvSuccess.setOnClickListener(v -> {
            binding.pb.setVisibility(View.VISIBLE);
            Intent intent = new Intent(getApplicationContext(), UserMainActivity.class);
            intent.putExtra(INTENT_WEAPON, String.valueOf(uriWeapon));
            intent.putExtra(INTENT_OUTFIT, String.valueOf(uriOutfit));
            intent.putExtra(INTENT_NAME, name);
            startActivity(intent);
            finish();
        });


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void handleWelcome(int position) {
        setBackground();
        if (Build.VERSION.SDK_INT < 30) {
            switch (position) {
                case 0:
                    binding.iv1.setBackground(getResources().getDrawable(R.drawable.shape_button_transfrom_white));
                    break;
                case 1:
                    binding.iv2.setBackground(getResources().getDrawable(R.drawable.shape_button_transfrom_white));
                    binding.tvSuccess.setVisibility(View.VISIBLE);
                    break;

            }
        }else{
            switch (position) {
                case 0:
                    binding.iv1.setBackground(getResources().getDrawable(R.drawable.shape_button_transfrom_white));
                    break;
                case 1:
                    binding.iv2.setBackground(getResources().getDrawable(R.drawable.shape_button_transfrom_white));
                    break;
                case 2:
                    binding.iv3.setBackground(getResources().getDrawable(R.drawable.shape_button_transfrom_white));
                    binding.tvSuccess.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }


    private void setBackground() {
        binding.iv1.setBackground(getResources().getDrawable(R.drawable.shape_button_transform));
        binding.iv2.setBackground(getResources().getDrawable(R.drawable.shape_button_transform));
        binding.iv3.setBackground(getResources().getDrawable(R.drawable.shape_button_transform));
    }

    private void getName() {
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

}
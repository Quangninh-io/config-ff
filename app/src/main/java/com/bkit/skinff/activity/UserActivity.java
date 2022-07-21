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

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.bkit.skinff.R;
import com.bkit.skinff.ads.MyApplication;
import com.bkit.skinff.databinding.ActivityUserBinding;
import com.bkit.skinff.model.Name;
import com.bkit.skinff.utilities.SetLanguage;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class UserActivity extends AppCompatActivity {
    private ActivityUserBinding binding;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    Uri uriWeapon, uriOutfit;
    Name name = new Name();
    private ObjectAnimator progressAnimator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetLanguage.getInstance().configLanguage(this);
        binding = ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initMain();
        getNameFile();
    }

    // get name in firestore with name collection is "name"
    // set total data to model Name
    private void getNameFile() {
        setProgressbar();
        db.collection(COLLECTION_NAME)
                .get()
                .addOnCompleteListener(task -> {
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

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                initAdsAndStartActivity();
                            }
                        }, 5000);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.d("error read file", "error");
                });
    }

    private long secondsRemaining;
    private void setProgressbar() {

        CountDownTimer countDownTimer = new CountDownTimer(5*1000,50) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d("fja", String.valueOf(secondsRemaining));
                secondsRemaining = 100- ((millisUntilFinished/100)+1);
                binding.pb.setProgress((int) secondsRemaining);
            }

            @Override
            public void onFinish() {
                secondsRemaining = 0;
                binding.tvLoading.setText(getResources().getString(R.string.done));
            }
        };
        countDownTimer.start();
    }

    private void initAdsAndStartActivity() {
        Application application = getApplication();
        if (!(application instanceof MyApplication)) {
            startNewActivity();
            return;
        };

        ((MyApplication) application)
                .showAdIfAvailable(UserActivity.this, new MyApplication.OnShowAdCompleteListener() {
                    @Override
                    public void onShowAdComplete() {
                        startNewActivity();
                    }
                });
    }

    private void startNewActivity() {
        Intent intent = new Intent(this, UserMainActivity.class);
        intent.putExtra(INTENT_WEAPON, String.valueOf(uriWeapon));
        intent.putExtra(INTENT_OUTFIT, String.valueOf(uriOutfit));
        intent.putExtra(INTENT_NAME, name);
        startActivity(intent);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finishAffinity();
            }
        },2000);
    }

    // handle transform descriptions
    // send model Name for another activity
    private void initMain() {

        if (Build.VERSION.SDK_INT >= 23 && Build.VERSION.SDK_INT < 30) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }


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

}
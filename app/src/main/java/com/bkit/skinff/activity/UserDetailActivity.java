package com.bkit.skinff.activity;

import static com.bkit.skinff.utilities.Constants.INTENT_CHOSE_MODEL;
import static com.bkit.skinff.utilities.Constants.INTENT_DETAIL;
import static com.bkit.skinff.utilities.Constants.INTENT_NAME;
import static com.bkit.skinff.utilities.Constants.INTENT_OUTFIT;
import static com.bkit.skinff.utilities.Constants.INTENT_WEAPON;
import static com.bkit.skinff.utilities.Constants.LIMITED_DATE_SET_NEW;
import static com.bkit.skinff.utilities.Constants.STATUS_ACTIVE;
import static com.bkit.skinff.utilities.Constants.STATUS_INACTIVE;
import androidx.appcompat.app.AppCompatActivity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.bkit.skinff.databinding.ActivityUserDetailBinding;
import com.bkit.skinff.firebase.DownloadFile;
import com.bkit.skinff.model.FileData;
import com.bkit.skinff.model.Name;
import com.squareup.picasso.Picasso;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

public class UserDetailActivity extends AppCompatActivity {
    private ActivityUserDetailBinding binding;
    private FileData fileData;
    Uri uriWeapon, uriOutfit;
    Name name;
    String decideChoseModel = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        requestSdcardAccessPermission();
        binding.pbDowload.setVisibility(View.INVISIBLE);
        initMain();

    }

    // init when open activity
    private void initMain() {
        fileData = (FileData) getIntent().getSerializableExtra(INTENT_DETAIL);
        binding.tvName.setText(fileData.getName());
        Picasso.get().load(fileData.getImage()).into(binding.iv);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Instant now = Instant.now(); //current date
        Instant before = now.minus(Duration.ofDays(LIMITED_DATE_SET_NEW));
        Date dateBefore = Date.from(before);
        String thirtyDayAgo = simpleDateFormat.format(dateBefore);
        Log.d("time", thirtyDayAgo);
        try {
            Date dateInFirebase = simpleDateFormat.parse(fileData.getTime());
            Date dateInReality = simpleDateFormat.parse(thirtyDayAgo);
            if (dateInFirebase.compareTo(dateInReality) > 0) {
                binding.tvNew.setVisibility(View.VISIBLE);
            } else {
                binding.tvNew.setVisibility(View.INVISIBLE);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        binding.btActive.setOnClickListener(v -> {
            handleClick();
        });

        binding.btActive.setTag(STATUS_INACTIVE);
    }
    // handle click button "active"
    private void handleClick() {
        if (binding.btActive.getTag() == STATUS_INACTIVE) {
            binding.btActive.setTag(STATUS_ACTIVE);
            binding.btActive.setText("Đã kích hoạt");
            handleActive();
        } else {
            binding.btActive.setTag(STATUS_INACTIVE);
            binding.btActive.setText("Kích hoạt");
        }
    }

    // download file corresponding from storage
    private void handleActive() {
        if (fileData.getModel().equals("ffmax")) {
            if (fileData.getType().equals("gun")) {
                downLoadFile(uriWeapon, name.getWeaponMax(), fileData.getTime());
            } else {
                downLoadFile(uriOutfit, name.getOutfitMax(), fileData.getTime());
            }
        } else {
            if (fileData.getType().equals("gun")) {
                downLoadFile(uriWeapon, name.getWeapon(), fileData.getTime());
            } else {
                downLoadFile(uriOutfit, name.getOutfit(), fileData.getTime());
            }
        }

    }
    // place receive uri, what is model and name
    private void requestSdcardAccessPermission() {
        decideChoseModel = getIntent().getStringExtra(INTENT_CHOSE_MODEL);
        uriWeapon = Uri.parse(getIntent().getStringExtra(INTENT_WEAPON));
        uriOutfit = Uri.parse(getIntent().getStringExtra(INTENT_OUTFIT));
        name = (Name) getIntent().getSerializableExtra(INTENT_NAME);
    }
    // handle download
    private void downLoadFile(Uri uri, String nameFile, String time) {
        DownloadFile.getInstance().downLoadFile(binding.pbDowload, time, uri, this, fileData.getModel(), fileData.getType(), nameFile);
    }
}
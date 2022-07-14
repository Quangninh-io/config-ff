package com.bkit.skinff.activity;

import static com.bkit.skinff.utilities.Constants.CHECK_FF_EXIST;
import static com.bkit.skinff.utilities.Constants.CHECK_FF_MAX_EXIST;
import static com.bkit.skinff.utilities.Constants.INTENT_CHOSE_MODEL;
import static com.bkit.skinff.utilities.Constants.INTENT_DETAIL;
import static com.bkit.skinff.utilities.Constants.INTENT_NAME;
import static com.bkit.skinff.utilities.Constants.INTENT_OUTFIT;
import static com.bkit.skinff.utilities.Constants.INTENT_WEAPON;
import static com.bkit.skinff.utilities.Constants.KEY_MODEL;
import static com.bkit.skinff.utilities.Constants.KEY_NAME;
import static com.bkit.skinff.utilities.Constants.KEY_OUTFIT;
import static com.bkit.skinff.utilities.Constants.KEY_TIME;
import static com.bkit.skinff.utilities.Constants.KEY_TYPE;
import static com.bkit.skinff.utilities.Constants.LIMITED_DATE_SET_NEW;
import static com.bkit.skinff.utilities.Constants.STATUS_ACTIVE;
import static com.bkit.skinff.utilities.Constants.STATUS_INACTIVE;
import static com.bkit.skinff.utilities.Constants.STORAGE_WEAPON;
import static com.bkit.skinff.utilities.Constants.TIME_DELETE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bkit.skinff.R;
import com.bkit.skinff.adapter.UserAdapter;
import com.bkit.skinff.ads.GoogleAds;
import com.bkit.skinff.databinding.ActivityUserDetailBinding;
import com.bkit.skinff.firebase.DownloadFile;
import com.bkit.skinff.fragment.user.DialogFragment;
import com.bkit.skinff.model.FileData;
import com.bkit.skinff.model.Name;
import com.bkit.skinff.sharepreference.GetUri;
import com.bkit.skinff.sharepreference.SaveUri;
import com.bkit.skinff.utilities.SetLanguage;
import com.squareup.picasso.Picasso;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

public class UserDetailActivity extends AppCompatActivity {
    private ActivityUserDetailBinding binding;
    private FileData fileData;
    Uri uriWeapon, uriOutfit;
    Name name;
    GetUri getUri = GetUri.getInstance();
    SaveUri saveUri = SaveUri.getInstance();
    String decideChoseModel = "", time = "", model = "", type = "";
    GoogleAds googleAds = GoogleAds.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetLanguage.getInstance().configLanguage(this);
        binding = ActivityUserDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        requestSdcardAccessPermission();
        binding.pbDowload.setVisibility(View.INVISIBLE);
        initMain();

    }

    // init when open activity
    @SuppressLint("SetTextI18n")

    private void initMain() {
        if(CHECK_FF_EXIST.equals("") && CHECK_FF_MAX_EXIST.equals("")){
            binding.btActive.setVisibility(View.GONE);
        }
        fileData = (FileData) getIntent().getSerializableExtra(INTENT_DETAIL);
        if(fileData.getType().equals(KEY_OUTFIT)){
            binding.tvName.setText(getResources().getString(R.string.collection_outfit)+ fileData.getName());
        }else{
            binding.tvName.setText(getResources().getString(R.string.collection_gun)+ fileData.getName());
        }
        binding.tvTime.setText(fileData.getTime());

        Picasso.get().load(fileData.getImage()).into(binding.iv);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Instant now = Instant.now(); //current date
        Instant before = now.minus(Duration.ofDays(LIMITED_DATE_SET_NEW));
        Date dateBefore = Date.from(before);
        String thirtyDayAgo = simpleDateFormat.format(dateBefore);
        try {
            Date dateInFirebase = simpleDateFormat.parse(fileData.getTime());
            Date dateInReality = simpleDateFormat.parse(thirtyDayAgo);
            if (dateInFirebase.compareTo(dateInReality) > 0) {
                binding.tvNew.setVisibility(View.VISIBLE);
            } else {
                binding.tvNew.setVisibility(View.GONE);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        binding.btActive.setOnClickListener(v -> {
            handleClick();
        });

        binding.btActive.setTag(STATUS_INACTIVE);
        if(fileData.getType().equals("gun")){
            time = getUri.getStatusGun(getApplication(),KEY_TIME);
            model = getUri.getStatusGun(getApplication(),KEY_MODEL);
            type = getUri.getStatusGun(getApplication(),KEY_TYPE);
        }else{
            time = getUri.getStatusOutfit(getApplication(),KEY_TIME);
            model = getUri.getStatusOutfit(getApplication(),KEY_MODEL);
            type = getUri.getStatusOutfit(getApplication(),KEY_TYPE);
        }

        if(time.equals(fileData.getTime()) && model.equals(fileData.getModel())  && type.equals(fileData.getType())){
            binding.btActive.setTag(STATUS_ACTIVE);
            binding.btActive.setText(getResources().getString(R.string.activated_button));
        }else{
            binding.btActive.setTag(STATUS_INACTIVE);
            binding.btActive.setText(getResources().getString(R.string.not_active_button));
        }

        binding.ivBack.setOnClickListener(v->{
            onBackPressed();///
        });
//        binding.iv.setOnClickListener(v->{
//            Bundle bundle = new Bundle();
//            bundle.putSerializable(KEY_NAME,fileData);
//            DialogFragment dialog = new DialogFragment();
//            dialog.setArguments(bundle);
//            dialog.show(getSupportFragmentManager(),"dialog");
//        });

    }
    // handle click button "active"
    private void handleClick() {
        if (binding.btActive.getTag() == STATUS_INACTIVE) {
            googleAds.initInterstitialAds(this);
            binding.btActive.setTag(STATUS_ACTIVE);
            handleActive(fileData.getTime());
            binding.btActive.setText(getResources().getString(R.string.activated_button));
            if(fileData.getType().equals("gun")){
                saveUri.saveStatusClearDataGun(getApplication());
                saveUri.saveStatusGun(getApplication(),fileData.getModel(),fileData.getType(),fileData.getTime());
            }else{
                saveUri.saveStatusClearDataOutFit(getApplication());
                saveUri.saveStatusOutfit(getApplication(),fileData.getModel(),fileData.getType(),fileData.getTime());
            }
        }
        else {
            googleAds.initInterstitialAds(this);
            if(fileData.getType().equals("gun")){
                saveUri.saveStatusClearDataGun(getApplication());
            }else{
                saveUri.saveStatusClearDataOutFit(getApplication());
            }
            binding.btActive.setTag(STATUS_INACTIVE);
            binding.btActive.setText(getResources().getString(R.string.not_active_button));
            handleActive(TIME_DELETE);
        }
    }


    // download file corresponding from storage
    private void handleActive(String time) {
        if (fileData.getModel().equals("ffmax")) {
            if (fileData.getType().equals("gun")) {
                downLoadFile(uriWeapon, name.getWeaponMax(), time);
            } else {
                downLoadFile(uriOutfit, name.getOutfitMax(), time);
            }
        } else {
            if (fileData.getType().equals("gun")) {
                downLoadFile(uriWeapon, name.getWeapon(),time);
            } else {
                downLoadFile(uriOutfit, name.getOutfit(), time);
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
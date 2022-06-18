package com.bkit.skinff.activity;

import static com.bkit.skinff.utilities.Constants.CLOTHES;
import static com.bkit.skinff.utilities.Constants.INTENT_DETAIL;
import static com.bkit.skinff.utilities.Constants.INTENT_NAME;
import static com.bkit.skinff.utilities.Constants.INTENT_OUTFIT;
import static com.bkit.skinff.utilities.Constants.INTENT_WEAPON;
import static com.bkit.skinff.utilities.Constants.LIMITED_DATE_SET_NEW;
import static com.bkit.skinff.utilities.Constants.RESCONF;
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
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

public class UserDetailActivity extends AppCompatActivity {

    private ActivityUserDetailBinding binding;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference = firebaseStorage.getReference();
    FileData fileData;
    Uri uriWeapon, uriOutfit;
    Name name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        requestSdcardAccessPermission();
        binding.pbDowload.setVisibility(View.INVISIBLE);
        initMain();

    }



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
            if(dateInFirebase.compareTo(dateInReality)>0){
                binding.tvNew.setVisibility(View.VISIBLE);
            }else{
                binding.tvNew.setVisibility(View.INVISIBLE);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }


        binding.btActive.setOnClickListener(v->{
            handleClick();
        });
        binding.btActive.setTag(STATUS_INACTIVE);
    }

    private void handleClick() {
        if(binding.btActive.getTag()==STATUS_INACTIVE){
            binding.btActive.setTag(STATUS_ACTIVE);
            binding.btActive.setText("Chuyen toi free fire");
            handleActive();

        }else{
            UserDetailActivity.this.startActivity(UserDetailActivity.this.getPackageManager().getLaunchIntentForPackage("com.dts.freefireth"));
            //binding.btActive.setTag(STATUS_INACTIVE);
            //binding.btActive.setText("Kích hoạt");
            //handleDelete();

        }
    }

    private void handleDelete() {
        String time = "3-12-2000";
        if(fileData.getModel().equals("ffmax")){
            handleDelete("ffmax");
        }else{
            handleDelete("ff");
        }
    }


    private void handleDelete(String model){
        String time = "3-12-2000";
        DownloadFile.getInstance().downLoadFile(binding.pbDowload,time,uriWeapon,this,model,"gun",name.getWeapon());
        DownloadFile.getInstance().downLoadFile(binding.pbDowload,time,uriOutfit,this,model,"outfit",name.getOutfit());
    }

    private void handleActive() {

        if(fileData.getModel().equals("ffmax")){
            if(fileData.getType().equals("gun")){
                downLoadFile(uriWeapon,name.getWeapon(),fileData.getTime());
            }else{
                downLoadFile(uriOutfit,name.getOutfit(),fileData.getTime());
            }
        }else{
            if(fileData.getType().equals("gun")){
                downLoadFile(uriWeapon,name.getWeapon(),fileData.getTime());
            }else{
                downLoadFile(uriOutfit,name.getOutfit(),fileData.getTime());
            }
        }

    }
    private void requestSdcardAccessPermission() {
        uriWeapon = Uri.parse(getIntent().getStringExtra(INTENT_WEAPON));
        uriOutfit = Uri.parse(getIntent().getStringExtra(INTENT_OUTFIT));
        name = (Name) getIntent().getSerializableExtra(INTENT_NAME);
    }

    private void downLoadFile(Uri uri,String type,String time){
        DownloadFile.getInstance().downLoadFile(binding.pbDowload,time,uri,this,fileData.getModel(),fileData.getType(),type);
    }
}
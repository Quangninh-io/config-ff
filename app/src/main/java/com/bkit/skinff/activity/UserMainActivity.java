package com.bkit.skinff.activity;

import static com.bkit.skinff.utilities.Constants.BUNDLE_NAME;
import static com.bkit.skinff.utilities.Constants.CHECK_FF_EXIST;
import static com.bkit.skinff.utilities.Constants.CHECK_FF_MAX_EXIST;
import static com.bkit.skinff.utilities.Constants.INTENT_NAME;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentManager;

import android.app.Application;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bkit.skinff.R;

import com.bkit.skinff.ads.MyApplication;
import com.bkit.skinff.databinding.ActivityUserMainBinding;
import com.bkit.skinff.fragment.user.UserMainFragment;
import com.bkit.skinff.listener.KnowWhichItemClicked;
import com.bkit.skinff.model.FileData;
import com.bkit.skinff.model.Name;
import com.bkit.skinff.sharepreference.GetUri;
import com.bkit.skinff.sharepreference.SaveUri;;
import com.bkit.skinff.utilities.LanguageManager;
import com.bkit.skinff.utilities.SetLanguage;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.material.navigation.NavigationView;

public class UserMainActivity extends AppCompatActivity implements OnUserEarnedRewardListener, NavigationView.OnNavigationItemSelectedListener, KnowWhichItemClicked {

    private ActivityUserMainBinding binding;
    UserMainFragment mainFragment = new UserMainFragment();
    FragmentManager fm = getSupportFragmentManager();
    LanguageManager language = LanguageManager.getInstance();
    SaveUri saveUri = SaveUri.getInstance();

    GetUri getUri = GetUri.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        SetLanguage.getInstance().configLanguage(this);
        binding = ActivityUserMainBinding.inflate(getLayoutInflater());
        initBanner();
        setContentView(binding.getRoot());
        initMain();
    }


    // initial run activity
    // get uri in share preference, if data exist, user don't have to choose uri again
    private void initMain() {

        Name fileName = (Name) getIntent().getSerializableExtra(INTENT_NAME);
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_NAME, fileName);
        mainFragment.setArguments(bundle);
        UserMainFragment userMainFragment = (UserMainFragment) fm.findFragmentByTag("main");

        if (userMainFragment == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_main, mainFragment, "main").commit();
        }
        binding.ivMenu.setOnClickListener(v -> {
            binding.dlMain.openDrawer(GravityCompat.START);
        });
        binding.vChangeModel.setOnClickListener(v -> {
            openDialog();
        });
        binding.ivLogo.setOnLongClickListener(v -> {
            startActivity(new Intent(getApplication(), LoginActivity.class));

            return false;
        });

        TextView tv = findViewById(R.id.tv_close_guide_first);
        tv.setOnClickListener(v->{
            binding.clMain.setVisibility(View.VISIBLE);
            binding.llGuide.setVisibility(View.GONE);
        });

        binding.vHelp.setOnClickListener(v->{
            binding.clMain.setVisibility(View.GONE);
            binding.llGuide.setVisibility(View.VISIBLE);
        });

        openGuide();

        binding.nvMain.setNavigationItemSelectedListener(this);
    }
    
    private void openGuide(){

        String guide = getUri.getGuide(this);
        if (guide != "") {
            binding.clMain.setVisibility(View.VISIBLE);
            binding.llGuide.setVisibility(View.GONE);
        } else {
            saveUri.saveGuide(this);
        }
    }

    private void openDialog() {

        UserMainFragment userMainFragment = (UserMainFragment) fm.findFragmentByTag("main");
        if (CHECK_FF_EXIST.equals("") && CHECK_FF_MAX_EXIST.equals("")) {
            userMainFragment.openDialog(getResources().getString(R.string.not_installed));
        } else {
            userMainFragment.openDialog(getResources().getString(R.string.installed));
        }

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.item_delete_skin) {
            adsDelete();

            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.alert_delete);
            int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
            dialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_dialog));
            Button btYes = dialog.findViewById(R.id.bt_decide_delete);
            btYes.setOnClickListener(v -> {

                if (CHECK_FF_EXIST.equals("") && CHECK_FF_MAX_EXIST.equals("")) {
                    Toast.makeText(this, getResources().getString(R.string.not_installed), Toast.LENGTH_SHORT).show();
                } else {
                    UserMainFragment userMainFragment = (UserMainFragment) fm.findFragmentByTag("main");
                    userMainFragment.delete();
                }
                dialog.dismiss();
            });
            Button btNo = dialog.findViewById(R.id.bt_decide_cancel);
            btNo.setOnClickListener(v -> {
                dialog.dismiss();
            });


            dialog.show();

        } else if (id == R.id.item_rate) {
            handleRating();
        } else if (id == R.id.item_share) {
            handleShare();
        } else if (id == R.id.item_language) {
            handleLanguage();
        } else if (id == R.id.item_infor) {
            handleIntroduceAbutApp();
        } else if (id == R.id.item_guide) {
            handleGuide();
        }else if (id==R.id.item_response){
            startActivity(new Intent(this,ResponseActivity.class));

        }
        binding.dlMain.closeDrawer(GravityCompat.START);
        return true;
    }



    private void handleGuide() {
        startActivity(new Intent(this, GuideMainActivity.class));

    }

    private void handleIntroduceAbutApp() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.alert_infor_about);
        ImageView imageView = dialog.findViewById(R.id.iv_cancel_about);
        imageView.setOnClickListener(v->{
            dialog.dismiss();
        });
        dialog.show();
    }

    private void handleLanguage() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.alret_choose_language);
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        dialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_dialog));
        Button btSaveLanguage = dialog.findViewById(R.id.bt_save_language);
        Button btExit = dialog.findViewById(R.id.bt_cancel_language);
        ImageView ivEnglish = dialog.findViewById(R.id.iv_english);
        ImageView ivVietnamese = dialog.findViewById(R.id.iv_vietnamese);
        TextView tvVietnamese = dialog.findViewById(R.id.tv_vietnamese);
        TextView tvEnglish = dialog.findViewById(R.id.tv_english);
        String code = GetUri.getInstance().getCode(this);
        if (code.equals("en")) {
            ivVietnamese.setVisibility(View.INVISIBLE);
            ivEnglish.setVisibility(View.VISIBLE);
            tvVietnamese.setTag("en");
        } else {
            ivEnglish.setVisibility(View.VISIBLE);
            ivEnglish.setVisibility(View.INVISIBLE);
            tvVietnamese.setTag("vi");
        }
        tvVietnamese.setOnClickListener(v -> {
            ivVietnamese.setVisibility(View.VISIBLE);
            ivEnglish.setVisibility(View.INVISIBLE);
            tvVietnamese.setTag("vi");
        });
        tvEnglish.setOnClickListener(v -> {
            ivVietnamese.setVisibility(View.INVISIBLE);
            ivEnglish.setVisibility(View.VISIBLE);
            tvVietnamese.setTag("en");
        });
        btExit.setOnClickListener(v->{dialog.dismiss();});
        btSaveLanguage.setOnClickListener(v -> {
            if (tvVietnamese.getTag().equals("vi")) {
                String cod = GetUri.getInstance().getCode(this);
                if (!cod.equals("vi")) {
                    language.updateLanguage(this, "vi");
                    recreate();
                }
            } else {
                String cod = GetUri.getInstance().getCode(this);
                if (!cod.equals("en")) {
                    language.updateLanguage(this, "en");
                    recreate();
                }
            }
            dialog.dismiss();
        });
        dialog.show();
    }

    private void handleShare() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.facebook.katana");

        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);

    }

    private void handleRating() {
        try {
            // when play store available
            // initialize uri
            Uri uri = Uri.parse("market://details?id=" +
                    //getPackageName()
                    "com.facebook.katana"
            );
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            //when play store unavailable
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" +
//                    getPackageName()
                            "com.facebook.katana"
            );
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
    public InterstitialAd interstitialAd;

    private void adsDelete() {
        intiInterstitial();
    }

    // add
    @Override
    public void click(FileData fileData) {
        intiInterstitial();
    }


    public void intiInterstitial(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Application application = getApplication();
                if(application instanceof MyApplication){
                    interstitialAd =  ((MyApplication) application).mInterstitialAd;
                    if (interstitialAd != null) {
                        interstitialAd.show(UserMainActivity.this);
                    } else {
                        ((MyApplication) application).loadInterstitial();
                        if(((MyApplication) application).mInterstitialAd != null){
                            ((MyApplication) application).mInterstitialAd.show(UserMainActivity.this);
                        }
                    }
                }
            }
        },1000);

    }

    private void initBanner() {
        Application application = getApplication();
        if(application instanceof MyApplication){
            ((MyApplication) application).showBanner(binding.av);
        }
    }

    @Override
    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {

    }
}
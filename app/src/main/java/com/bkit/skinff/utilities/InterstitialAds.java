package com.bkit.skinff.utilities;

import android.app.Activity;
import android.app.Application;
import android.os.Handler;
import android.util.Log;

import com.bkit.skinff.ads.MyApplication;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.rewarded.RewardedAd;

public class InterstitialAds {

    private static InterstitialAds instance;

    public static InterstitialAds getInstance(){
        if(instance==null){
            return new InterstitialAds();
        }
        return instance;
    }

    private RewardedAd rewardedAd;
    private InterstitialAd interstitialAd;

    public void intiInterstitial(Activity activity){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("fjla","click");
                Application application = activity.getApplication();
                if(application instanceof MyApplication){
                    interstitialAd =  ((MyApplication) application).mInterstitialAd;
                    if (interstitialAd != null) {
                        interstitialAd.show(activity);
                    } else {
                        ((MyApplication) application).loadInterstitial();
                        if(((MyApplication) application).mInterstitialAd != null){
                            ((MyApplication) application).mInterstitialAd.show(activity);
                        }
                    }
                }
            }
        },1000);
    }




}

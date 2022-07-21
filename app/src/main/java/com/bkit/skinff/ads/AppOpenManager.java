package com.bkit.skinff.ads;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;

import java.util.Date;

public class AppOpenManager {
    private static final String LOG_TAG = "AppOpenAdManager";
    public static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/3419835294";

    private AppOpenAd appOpenAd = null;
    public boolean isLoadingAd = false;
    public boolean isShowingAd = false;

    private long loadTime = 0;


    public void AppOpenAdManager() {

    }

    private void loadAd(Context context) {
        // Do not load ad if there is an unused ad or one is already loading.
        if (isLoadingAd || isAdAvailable()) {
            return;
        }

        isLoadingAd = true;
        AdRequest request = new AdRequest.Builder().build();
        AppOpenAd.load(
                context,
                AD_UNIT_ID,
                request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                new AppOpenAd.AppOpenAdLoadCallback() {
                    /**
                     * Called when an app open ad has loaded.
                     *
                     * @param ad the loaded app open ad.
                     */
                    @Override
                    public void onAdLoaded(AppOpenAd ad) {
                        appOpenAd = ad;
                        isLoadingAd = false;
                        loadTime = (new Date()).getTime();

                    }

                    /**
                     * Called when an app open ad has failed to load.
                     *
                     * @param loadAdError the error.
                     */
                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        isLoadingAd = false;
                        Log.d(LOG_TAG, "onAdFailedToLoad: " + loadAdError.getMessage());
                    }
                });
    }

    /**
     * Check if ad was loaded more than n hours ago.
     */
    private boolean wasLoadTimeLessThanNHoursAgo(long numHours) {
        long dateDifference = (new Date()).getTime() - loadTime;
        long numMilliSecondsPerHour = 3600000;
        return (dateDifference < (numMilliSecondsPerHour * numHours));
    }

    /**
     * Check if ad exists and can be shown.
     */
    private boolean isAdAvailable() {
        // Ad references in the app open beta will time out after four hours, but this time limit
        // may change in future beta versions. For details, see:
        // https://support.google.com/admob/answer/9341964?hl=en
        return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4);
    }

    /**
     * Show the ad if one isn't already showing.
     *
     * @param activity the activity that shows the app open ad
     */
    public void showAdIfAvailable(@NonNull final Activity activity) {
        showAdIfAvailable(
                activity,
                new MyApplication.OnShowAdCompleteListener() {
                    @Override
                    public void onShowAdComplete() {
                        // Empty because the user will go back to the activity that shows the ad.
                    }
                });
    }

    /**
     * Show the ad if one isn't already showing.
     *
     * @param activity                 the activity that shows the app open ad
     * @param onShowAdCompleteListener the listener to be notified when an app open ad is complete
     */
    public void showAdIfAvailable(
            @NonNull final Activity activity,
            @NonNull MyApplication.OnShowAdCompleteListener onShowAdCompleteListener) {
        // If the app open ad is already showing, do not show the ad again.
        if (isShowingAd) {
//            Log.d(LOG_TAG, "The app open ad is already showing.");
            return;
        }

        // If the app open ad is not available yet, invoke the callback then load the ad.
        if (!isAdAvailable()) {
            Log.d(LOG_TAG, "The app open ad is not ready yet.");
            onShowAdCompleteListener.onShowAdComplete();
            loadAd(activity);
            return;
        }

        Log.d(LOG_TAG, "Will show ad.");

        appOpenAd.setFullScreenContentCallback(
                new FullScreenContentCallback() {
                    /** Called when full screen content is dismissed. */
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Set the reference to null so isAdAvailable() returns false.
                        appOpenAd = null;
                        isShowingAd = false;

                        Log.d(LOG_TAG, "onAdDismissedFullScreenContent.");
//                        Toast.makeText(activity, "onAdDismissedFullScreenContent", Toast.LENGTH_SHORT).show();

                        onShowAdCompleteListener.onShowAdComplete();
                        loadAd(activity);
                    }

                    /** Called when fullscreen content failed to show. */
                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        appOpenAd = null;
                        isShowingAd = false;

                        Log.d(LOG_TAG, "onAdFailedToShowFullScreenContent: " + adError.getMessage());
//                        Toast.makeText(activity, "onAdFailedToShowFullScreenContent", Toast.LENGTH_SHORT)
//                                .show();

                        onShowAdCompleteListener.onShowAdComplete();
                        loadAd(activity);
                    }

                    /** Called when fullscreen content is shown. */
                    @Override
                    public void onAdShowedFullScreenContent() {
                        Log.d(LOG_TAG, "onAdShowedFullScreenContent.");
//                        Toast.makeText(activity, "onAdShowedFullScreenContent", Toast.LENGTH_SHORT).show();
                    }
                });

        isShowingAd = true;
        appOpenAd.show(activity);
    }

}

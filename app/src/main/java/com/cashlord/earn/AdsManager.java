package com.cashlord.earn;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;

public class AdsManager {

    private Context context;
    private InterstitialAds interstitialAds;
    private RewardedAds rewardedAds;

    public AdsManager(Context context) {
        this.context = context;
        initializeAds((Activity) context);
    }

    private void initializeAds(Activity activity) {
        // Lấy các giá trị từ PrefManager
        //String interstitialAdId = PrefManager.APPLOVIN_MAX_INTERSTITIAL_ID;
        //String rewardedAdId = PrefManager.APPLOVIN_MAX_REWARDED_ID;
        //String openAppAdId = PrefManager.APPLOVIN_MAX_OPEN_APP_ID;

        AppLovinSdk.initializeSdk(context, new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(AppLovinSdkConfiguration configuration) {
                Log.d("AdsManager", "AppLovin SDK initialized.");

                // Khởi tạo và tải quảng cáo dựa trên ID từ PrefManager
                loadInterstitialAd(activity);
                loadRewardedAd(activity);
            }
        });
    }

    public boolean isInterstitialAdLoaded() {
        return interstitialAds != null && interstitialAds.isAdReady();
    }

    public static boolean isRewardedVideoAdLoaded(Activity activity) {
        return RewardedAds.getInstance(activity).isAdReady();
    }

    public void loadInterstitialAd(Activity activity) {
        interstitialAds = new InterstitialAds(activity);
        interstitialAds.loadAd();
        // Thêm listener nếu cần
    }

    public void loadRewardedAd(Activity activity) {
        rewardedAds = RewardedAds.getInstance(activity);
        rewardedAds.loadAd();
        // Thêm listener nếu cần
    }

    public void showInterstitialAd() {
        if (interstitialAds != null) {
            interstitialAds.showAd();
        } else {
            Log.d("AdsManager", "InterstitialAds instance is null.");
        }
    }

    public void showRewardedAd(OnVideoAdEnded onVideoAdEnded, Activity activity) {
        if (rewardedAds != null && rewardedAds.isAdReady()) {
            rewardedAds.showAd(() -> {
                onVideoAdEnded.videoWatched();
            }, activity);
        } else {
            Log.d("AdsManager", "RewardedAds instance is null.");
        }
    }
}
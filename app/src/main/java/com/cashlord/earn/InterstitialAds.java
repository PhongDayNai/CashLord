package com.cashlord.earn;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.mediation.ads.MaxRewardedAd;
import com.cashlord.earn.helper.PrefManager;

import java.util.concurrent.TimeUnit;

public class InterstitialAds extends Activity
        implements MaxAdListener
{
    private MaxInterstitialAd interstitialAd;
    private int retryAttempt;
    private static InterstitialAds instance;

    public InterstitialAds(Activity activity)
    {
        interstitialAd = new MaxInterstitialAd(PrefManager.APPLOVIN_MAX_INTERSTITIAL_ID, this );
        interstitialAd.setListener( this );
    }

    public void loadAd() {
        // Load the first ad
        interstitialAd.loadAd();
    }

    // MAX Ad Listener
    @Override
    public void onAdLoaded(final MaxAd maxAd)
    {
        // Interstitial ad is ready to be shown. interstitialAd.isReady() will now return 'true'

        // Reset retry attempt
        retryAttempt = 0;
    }

    public void showAd() {
        if (isAdReady()) {
            interstitialAd.showAd();
        } else {
            // Xử lý khi quảng cáo không sẵn sàng
        }
    }

    @Override
    public void onAdLoadFailed(final String adUnitId, final MaxError error)
    {
        // Interstitial ad failed to load
        // AppLovin recommends that you retry with exponentially higher delays up to a maximum delay (in this case 64 seconds)

        retryAttempt++;
        long delayMillis = TimeUnit.SECONDS.toMillis( (long) Math.pow( 2, Math.min( 6, retryAttempt ) ) );

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                interstitialAd.loadAd();
            }
        }, delayMillis );
    }

    @Override
    public void onAdDisplayFailed(final MaxAd maxAd, final MaxError error)
    {
        // Interstitial ad failed to display. AppLovin recommends that you load the next ad.
        interstitialAd.loadAd();
    }

    @Override
    public void onAdDisplayed(final MaxAd maxAd) {}

    @Override
    public void onAdClicked(final MaxAd maxAd) {}

    @Override
    public void onAdHidden(final MaxAd maxAd)
    {
        // Interstitial ad is hidden. Pre-load the next ad
        interstitialAd.loadAd();
    }

    public boolean isAdReady() {
        return interstitialAd.isReady();
    }

    public static InterstitialAds getInstance(Activity activity) {
        if (instance == null) {
            instance = new InterstitialAds(activity);
        }
        return instance;
    }
}

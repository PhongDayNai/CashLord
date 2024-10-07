package com.cashlord.earn;

import android.app.Activity;
import android.os.Handler;
import android.widget.Toast;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.MaxReward;
import com.applovin.mediation.MaxRewardedAdListener;
import com.applovin.mediation.ads.MaxRewardedAd;
import com.cashlord.earn.helper.PrefManager;

import java.util.concurrent.TimeUnit;

public class RewardedAds extends Activity
        implements MaxRewardedAdListener
{
    private MaxRewardedAd rewardedAd;
    private int retryAttempt;
    private static RewardedAds instance;
    private Runnable onUserRewardedCallback;

    public RewardedAds(Activity activity)
    {
        rewardedAd = MaxRewardedAd.getInstance(PrefManager.APPLOVIN_MAX_REWARDED_ID, this );
        rewardedAd.setListener( this );
        loadAd();
    }

    public void loadAd() {
        // Load the first ad
        rewardedAd.loadAd();
    }

    public void showAd(Runnable onUserRewarded, Activity activity) {
        if (rewardedAd.isReady()) {
            rewardedAd.showAd();
            // Gán callback khi người dùng nhận thưởng
            this.onUserRewardedCallback = onUserRewarded;
        } else {
            Toast.makeText(activity, "Rewarded video ad is not ready", Toast.LENGTH_SHORT).show();
        }
    }

    // MAX Ad Listener
    @Override
    public void onAdLoaded(final MaxAd maxAd)
    {
        // Rewarded ad is ready to be shown. rewardedAd.isReady() will now return 'true'

        // Reset retry attempt
        retryAttempt = 0;
    }

    @Override
    public void onAdLoadFailed(final String adUnitId, final MaxError error)
    {
        // Rewarded ad failed to load
        // AppLovin recommends that you retry with exponentially higher delays up to a maximum delay (in this case 64 seconds)

        retryAttempt++;
        long delayMillis = TimeUnit.SECONDS.toMillis( (long) Math.pow( 2, Math.min( 6, retryAttempt ) ) );

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                rewardedAd.loadAd();
            }
        }, delayMillis );
    }

    @Override
    public void onAdDisplayFailed(final MaxAd maxAd, final MaxError error)
    {
        // Rewarded ad failed to display. AppLovin recommends that you load the next ad.
        rewardedAd.loadAd();
    }

    @Override
    public void onAdDisplayed(final MaxAd maxAd) {}

    @Override
    public void onAdClicked(final MaxAd maxAd) {}

    @Override
    public void onAdHidden(final MaxAd maxAd)
    {
        // rewarded ad is hidden. Pre-load the next ad
        rewardedAd.loadAd();
    }

    @Override
    public void onUserRewarded(final MaxAd maxAd, final MaxReward maxReward)
    {
        // Rewarded ad was displayed and user should receive the reward
    }

    public boolean isAdReady() {
        return rewardedAd.isReady();
    }

    public static RewardedAds getInstance(Activity activity) {
        if (instance == null) {
            instance = new RewardedAds(activity);
            instance.initializeRewardedAd(activity);
        }
        return instance;
    }

    private void initializeRewardedAd(Activity activity) {
        rewardedAd = MaxRewardedAd.getInstance(PrefManager.APPLOVIN_MAX_REWARDED_ID, activity);
        rewardedAd.setListener(this);
    }
}
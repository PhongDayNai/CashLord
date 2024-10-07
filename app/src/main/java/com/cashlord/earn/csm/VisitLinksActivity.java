package com.cashlord.earn.csm;

import static com.cashlord.earn.helper.ContextExtensions.isAndroid13;
import static com.cashlord.earn.helper.Helper.ONE_THOUSAND_MILISECOND;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.cashlord.earn.R;
import com.cashlord.earn.csm.model.WebsiteModel;

public class VisitLinksActivity extends AppCompatActivity {
    private TextView timerTextView;
    private WebView webView;
    private VisitLinksActivity activity;
    private String visitType;
    private boolean isNull = false;
    private boolean isCountdownFinish = false;
    private boolean isCountDownStart = false;
    private CountDownTimer countDownTimer;
    private long time = 0;
    private WebsiteModel websiteModel;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_links);

        activity = this;
        timerTextView = findViewById(R.id.time_text);
        webView = findViewById(R.id.webView);

        Intent intent = getIntent();
        if (intent != null) {
            visitType = intent.getStringExtra("type");
            Log.e("TYPE", "onCreate: " + visitType);

            if (visitType.equalsIgnoreCase("website") || visitType.equalsIgnoreCase("video")) {
                if (isAndroid13(this)) {
                    websiteModel = (WebsiteModel) intent.getSerializableExtra("websiteModal", WebsiteModel.class);
                } else {
                    websiteModel = (WebsiteModel) intent.getSerializableExtra("websiteModal");
                }
                setDataToActivity(websiteModel.getVisitLink(), websiteModel.getVisitTimer());
            } else {
                unableToGetIntent();
            }

            setUpWebViewAndLoadUrl();
        } else {
            unableToGetIntent();
            return;
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setUpWebViewAndLoadUrl() {
        if (url != null) {
            webView.setWebViewClient(new MyBrowser());
            webView.getSettings().setLoadsImagesAutomatically(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            webView.loadUrl(url);
            webView.setVisibility(View.VISIBLE);
        } else {
            unableToGetIntent();
        }
    }

    private void setDataToActivity(String link, String time_visit) {
        if (link != null) {
            url = link;
            if (!link.contains("http://") && !link.contains("https://")) {
                url = "http://" + url;
            }

            if (time_visit != null) {
                time = Long.parseLong(time_visit) * 60;
                timerTextView.setText(time + " s");
            }
        } else {
            unableToGetIntent();
        }
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (countDownTimer == null && !isCountdownFinish) {
                isCountDownStart = true;
                startCountdownTimer();
            }
            super.onPageFinished(view, url);
        }
    }

    @Override
    protected void onResume() {
        if (isCountDownStart) {
            startCountdownTimer();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        stopTimer();
        super.onPause();
    }

    private void unableToGetIntent() {
        Toast.makeText(activity, "Something went wrong try again", Toast.LENGTH_SHORT).show();
        isNull = true;
        onBackPressed();
    }

    private void startCountdownTimer() {
        if (countDownTimer != null) {
            return;
        }

        countDownTimer = new CountDownTimer(
                time * ONE_THOUSAND_MILISECOND,
                ONE_THOUSAND_MILISECOND
        ) {
            @Override
            public void onTick(long millisUntilFinished) {
                long t = millisUntilFinished / 1000;
                time = t;
                timerTextView.setText(t + " s");
            }

            @Override
            public void onFinish() {
                isCountdownFinish = true;
                stopTimer();
                setResultToActivity(RESULT_OK);
            }
        };
        countDownTimer.start();
    }

    private void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
            super.onBackPressed();
            return;
        }

        if (isNull) {
            stopTimer();
            setResultToActivity(RESULT_CANCELED);
            super.onBackPressed();
            return;
        }

        if (countDownTimer != null) {
            new AlertDialog.Builder(activity)
                    .setTitle("Are You Sure?")
                    .setMessage("If You back then no points will credited to your wallet")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        dialog.dismiss();
                        stopTimer();
                        setResultToActivity(RESULT_CANCELED);
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .show();
            super.onBackPressed();
        } else {
            stopTimer();
            setResultToActivity(RESULT_CANCELED);
            super.onBackPressed();
        }
    }

    private void setResultToActivity(int result) {
        setResult(result, new Intent());
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.destroy();
            webView = null;
        }
    }
}

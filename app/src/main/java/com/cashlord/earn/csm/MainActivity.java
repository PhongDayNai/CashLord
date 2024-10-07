package com.cashlord.earn.csm;

import static com.cashlord.earn.Just_base.addPoint;
import static com.cashlord.earn.helper.Constatnt.ACCESS_KEY;
import static com.cashlord.earn.helper.Constatnt.ACCESS_Value;
import static com.cashlord.earn.helper.Constatnt.API;
import static com.cashlord.earn.helper.Constatnt.Base_Url;
import static com.cashlord.earn.helper.Constatnt.USERNAME;
import static com.cashlord.earn.helper.PrefManager.setWindowFlag;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.cashlord.earn.AdsManager;
import com.cashlord.earn.InterstitialAds;
import com.cashlord.earn.R;
import com.cashlord.earn.csm.fragment.FragmentProfile;
import com.cashlord.earn.csm.fragment.FragmentRefer;
import com.cashlord.earn.csm.fragment.LeaderBoardFragment;
import com.cashlord.earn.csm.fragment.Main_Fragment;
import com.cashlord.earn.csm.fragment.RewardFragment;
import com.cashlord.earn.helper.AppController;
import com.cashlord.earn.helper.JsonRequest;
import com.cashlord.earn.helper.PrefManager;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    //This is our viewPager
    FragmentRefer tournament_fragment;
    ChipNavigationBar chipNav;
    Boolean isBack = false;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // Set Portrait
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        loadFragment(new Main_Fragment());
        chipNav = findViewById(R.id.chipNav);
        chipNav.setItemSelected(R.id.play, true);

        chipNav.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                if (i == R.id.play) {
                    loadFragment(new Main_Fragment());
                } else if (i == R.id.battle) {
                    loadFragment(new LeaderBoardFragment());
                } else if (i == R.id.tournament) {
                    loadFragment(new FragmentRefer());
                    loadFragment(tournament_fragment);
                } else if (i == R.id.profile) {
                    loadFragment(new FragmentProfile());
                } else if (i == R.id.Rewards) {
                    loadFragment(new RewardFragment());
                }
            }
        });

        checkPermissions();
        time_update();
        //AdsManager.loadInterstitalAd(this);
        InterstitialAds interstitialAds = new InterstitialAds(this);
        interstitialAds.loadAd();
    }

    public static void change(int position, ViewPager viewPager) {
        viewPager.setCurrentItem(position);
    }

    public void time_update() {
        int delay = 0; // delay for 0 sec.
        int period = 10000000; // repeat every 10 sec.
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                update_point();
            }
        }, delay, period);
    }

    private void update_point() {
        JsonRequest jsonReq = new JsonRequest(Request.Method.POST, Base_Url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (AppController.getInstance().authorize(response)) {
                            // Xử lý phản hồi nếu cần
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Xử lý lỗi nếu cần
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(ACCESS_KEY, ACCESS_Value);
                params.put("user_point_update", API);
                params.put("fcm_id", "FirebaseInstanceId.getInstance().getToken()");
                params.put(USERNAME, AppController.getInstance().getUsername());
                return params;
            }
        };
        AppController.getInstance().getRequestQueue().getCache().clear();
        AppController.getInstance().addToRequestQueue(jsonReq);
    }

    public static void toast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    private void checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (!isBack) {
            isBack = true;
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.fragment_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            Button no = dialog.findViewById(R.id.no);
            Button yes = dialog.findViewById(R.id.yes);

            yes.setOnClickListener(view -> {
                finish();
                System.exit(0);
            });

            no.setOnClickListener(view -> {
                dialog.dismiss();
                isBack = false;
            });
            dialog.setCancelable(false);
            dialog.show();
        } else {
            super.onBackPressed();
        }
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}

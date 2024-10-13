package com.cashlord.earn.helper;

import static com.cashlord.earn.Just_base.addPoint;
import static com.cashlord.earn.helper.Constatnt.ACCESS_KEY;
import static com.cashlord.earn.helper.Constatnt.ACCESS_Value;
import static com.cashlord.earn.helper.Constatnt.ACCOUNT_STATE_ENABLED;
import static com.cashlord.earn.helper.Constatnt.API;
import static com.cashlord.earn.helper.Constatnt.Base_Url;
import static com.cashlord.earn.helper.Constatnt.DAILY_CHECKIN_API;
import static com.cashlord.earn.helper.Constatnt.DAILY_TYPE;
import static com.cashlord.earn.helper.Constatnt.GET_USER;
import static com.cashlord.earn.helper.Constatnt.ID;
import static com.cashlord.earn.helper.Constatnt.Main_Url;
import static com.cashlord.earn.helper.Constatnt.SPIN_TYPE;
import static com.cashlord.earn.helper.Constatnt.USERNAME;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.cashlord.earn.MainActivity;
import com.cashlord.earn.OnScratchComplete;
import com.cashlord.earn.R;
import com.cashlord.earn.WelcomeActivity;
import com.cashlord.earn.csm.ActivitySplash;
import com.cashlord.earn.csm.topsheet.Coins_Dialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PrefManager {

    // Preferences name and context
    //private static final String PREF_NAME = "ad_pref";
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    private Context context;
    public static AppCompatActivity activity;
    private static final String SETTING_PREF = "settings_pref";

    private static final String PREF_NAME = "cashlord_pref";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    // Ad types (only AppLovin Max)
    public static final String APPLOVIN_AD_TYPE = "applovin";

    // Các biến COUNT (có thể giữ lại nếu cần)
    public static final String SPIN_COUNT = "spin_count";
    public static final String SPIN_COUNT_PER_DAY = "spin_count_per_day";
    public static final String EXTRA_SPIN_COUNT = "extra_spin_count";
    public static final String SCRATCH_COUNT = "scratch_count";

    // Các biến khác mà bạn có thể muốn giữ lại
    public static final String USER_MULTIPLE_ACCOUNT = "use_multiple_account";
    public static final String SCRATCH_COUNT_BEETWEN = "scratch_count_beetween";
    public static final String DAILY_VIDEO_LIMIT = "daily_video_limit";
    public static final String REWARDED_AD_TYPE = "rewarded_ad_type";

    // AppLovin Max Ad IDs
    //public static final String APPLOVIN_MAX_BANNER_ID = "applovin_max_banner_id";
    public static final String APPLOVIN_MAX_INTERSTITIAL_ID = "54e75d9a0beb297f";
    public static final String APPLOVIN_MAX_REWARDED_ID = "ff3d8a4b9c489ff4";
    public static final String APPLOVIN_MAX_OPEN_APP_ID = "12741ca01f369f84";

    // Mediation Ad Unit IDs (for other networks)
    public static final String ADMOB_APP_ID = "admob_ad_unit_id";
    public static final String META_APP_ID = "meta_ad_unit_id";
    public static final String IRONSOURCE_APP_ID = "ironsource_ad_unit_id";
    public static final String UNITY_GAME_ID = "unity_gmae_id";
    public static final String VUNGLE_APP_ID = "vungle_ad_unit_id";
    public static final String PANGLE_APP_ID = "pangle_ad_unit_id";

    public PrefManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public boolean isFirstTimeLaunch() {
        return sharedPreferences.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.apply(); // Nên dùng apply() để ghi không đồng bộ
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Log.d("PrefManager", "Calling setWindowFlag function");
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public static void setString(Context context, String key, String prefString) {
        Log.d("PrefManager", "Calling setString function");
        SharedPreferences sharedPreferences = context.getSharedPreferences(SETTING_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, prefString);
        editor.apply();
    }

    public static String getSavedString(Context context, String pref) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SETTING_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getString(pref, "");
    }

    public static void setInt(Context context, String key, int prefString) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SETTING_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, prefString);
        editor.apply();
    }

    public static int getSavedInt(Context context, String pref) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SETTING_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(pref, 0);
    }

    public static void Add_Coins_(Context context, String coins, String from, String showAd, OnScratchComplete onScratchComplete) {
        Log.d("PrefManager", "Calling Add_Coins_ function");
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        Coins_Dialog newFragment = new Coins_Dialog();
        Bundle args = new Bundle();
        args.putString("coins", coins);
        args.putString("from", from);
        args.putString("show_ad", showAd);
        newFragment.setArguments(args);
        newFragment.setOnScratchCompleteListener(onScratchComplete);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
        addPoint(context, coins, from);
    }

    public static void user_points(TextView t) {
        Log.d("PrefManager", "Calling user_points function");
        final String[] s = {"0"};
        //JsonRequest<JSONObject> stringRequest = new JsonRequest<JSONObject>(Request.Method.POST, Base_Url, null,
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Base_Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            if (jsonResponse.getString("error").equalsIgnoreCase("false")) {
                                t.setText(jsonResponse.getString("points"));
                            } //else {}
                        } catch (Exception e) {
                            ContextExtensions.showLongToast(t.getContext(), e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {}
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ACCESS_KEY, ACCESS_Value);
                params.put("get_points", AppController.getInstance().getId());
                return params;
            }
            /*@Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(new JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response));
                } catch (JSONException e) {
                    Log.e("ERROR", "JSONException: ", e);
                    // Trả về phản hồi lỗi hoặc xử lý theo cách khác
                    return Response.error(new ParseError(e));
                } catch (UnsupportedEncodingException e) {
                    Log.e("ERROR", "UnsupportedEncodingException: ", e);
                    return Response.error(new ParseError(e));
                } catch (Exception e) {
                    Log.e("ERROR", "Unexpected Error: ", e);
                    return Response.error(new ParseError(e));
                }
            }*/
        };
        if (AppController.getInstance() != null) {
            AppController.getInstance().addToRequestQueue(stringRequest);
        } else {
            Log.e("ERROR", "AppController instance is null");
        }
    }

    private static void updateAppLovinConfig(Context context, JSONObject data) {
        try {
            // Lưu AppLovin Max Banner ID, Interstitial ID và Rewarded ID từ server
            //setString(context, APPLOVIN_MAX_BANNER_ID, data.getString("applovin_max_banner_id"));
            setString(context, APPLOVIN_MAX_INTERSTITIAL_ID, data.getString("applovin_max_interstitial_id"));
            setString(context, APPLOVIN_MAX_REWARDED_ID, data.getString("applovin_max_rewarded_id"));

            // Có thể thêm các phần khác nếu cần như số lần quảng cáo được xem hoặc cấu hình quảng cáo
            setString(context, REWARDED_AD_TYPE, data.getString("rewarded_ad_type"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void A(Context context) {
        Log.d("PrefManager", "Calling A function");

        if (!AppController.isConnected((AppCompatActivity) context)) {
            Log.e("ERROR", "No internet connection");
            return;  // Dừng lại nếu không có kết nối
        } else {
            Log.d("NETWORK", "Internet is available");
        }

        // Tạo một Map để chứa tham số
        /*Map<String, String> params = new HashMap<>();
        params.put(ACCESS_KEY, ACCESS_Value);
        params.put("c", DAILY_TYPE);
        String stringParams = params.toString();*/

        //JSONObject jsonParams = new JSONObject(params);

        //JsonRequest<JSONObject> stringRequest = new JsonRequest<JSONObject>(Request.Method.POST, Base_Url, params,
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Base_Url,
        //JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, Base_Url, null,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("ON_RESPONSE", "Calling onResponse function");
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            if (jsonResponse.getString("error").equalsIgnoreCase("1")) {
                                Log.d("CHECK_ERROR", "Error is 1, processing further...");
                                if (jsonResponse.getString("vpn").equals("1")) {
                                    if (vpn()) {
                                        ContextExtensions.showShortToast(context, "Please disconnect the vpn and reopen the app!");
                                        ((Activity) context).finish();
                                    } else {
                                        Log.d("ALGORATHAM", "Calling algoratham with data: " + jsonResponse.getJSONObject("data").toString());
                                        algoratham(context, jsonResponse.getJSONObject("data"));
                                    }
                                } else {
                                    Log.d("ALGORATHAM", "Calling algoratham with data: " + jsonResponse.getJSONObject("data").toString());
                                    algoratham(context, jsonResponse.getJSONObject("data"));
                                }
                            }
                        } catch (Exception e) {}
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ON_ERROR_help_PrefManager_A", "onErrorResponse: " + error.getMessage());
                        if (error.networkResponse != null) {
                            Log.e("ON_ERROR_help_PrefManager_A", "Status Code: " + error.networkResponse.statusCode);
                            Log.e("ON_ERROR_help_PrefManager_A", "Response Data: " + new String(error.networkResponse.data));
                        }
                        Log.e("ON_ERROR_help_PrefManager_A", "onErrorResponse: ", error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ACCESS_KEY, ACCESS_Value);
                params.put("c", DAILY_TYPE);
                Log.d("REQUEST_PARAMS", "Params: " + params.toString());
                return params;
            }
            /*@Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    Log.d("RESPONSE", "Response: " + jsonString);  // Log toàn bộ phản hồi từ server

                    int statusCode = response.statusCode;
                    Log.d("RESPONSE", "Status Code: " + statusCode);  // Log status code

                    // Kiểm tra mã trạng thái HTTP
                    if (statusCode != 200) {
                        return Response.error(new ParseError(new Exception("Unexpected status code: " + statusCode)));
                    }

                    // Kiểm tra phản hồi có thật sự rỗng
                    if (jsonString.isEmpty()) {
                        Log.e("ERROR", "Phản hồi rỗng từ server");
                        return Response.error(new ParseError(new Exception("Phản hồi rỗng từ server")));
                    }

                    // Kiểm tra định dạng JSON
                    if (jsonString.startsWith("{") && jsonString.endsWith("}")) {
                        return Response.success(new JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response));
                    } else {
                        Log.e("ERROR", "Định dạng JSON không hợp lệ: " + jsonString);
                        return Response.error(new ParseError(new JSONException("Định dạng JSON không hợp lệ")));
                    }
                } catch (JSONException e) {
                    Log.e("ERROR", "JSONException: ", e);
                    // Trả về phản hồi lỗi hoặc xử lý theo cách khác
                    return Response.error(new ParseError(e));
                } catch (UnsupportedEncodingException e) {
                    Log.e("ERROR", "UnsupportedEncodingException: ", e);
                    return Response.error(new ParseError(e));
                } catch (Exception e) {
                    Log.e("ERROR", "Unexpected Error: ", e);
                    return Response.error(new ParseError(e));
                }
            }*/
        };
        if (AppController.getInstance() != null) {
            AppController.getInstance().addToRequestQueue(stringRequest);
            Log.d("REQUEST", "Request added to queue");
        } else {
            Log.e("ERROR", "AppController instance is null");
        }

        //AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public static void algoratham(Context context, JSONObject data) {
        Log.d("PrefManager", "Calling algoratham function");
        try {
            // Lưu các ID quảng cáo vào SharedPreferences
            setString(context, APPLOVIN_MAX_INTERSTITIAL_ID, data.getString("applovin_max_interstitial_id"));
            setString(context, APPLOVIN_MAX_REWARDED_ID, data.getString("applovin_max_rewarded_id"));

            // Các biến đếm (nếu cần)
            setString(context, SPIN_COUNT, data.getString("spin_count"));
            setString(context, SCRATCH_COUNT, data.getString("scratch_count"));

            // Các giá trị khác
            setString(context, USER_MULTIPLE_ACCOUNT, data.getString("use_multiple_account"));
            setString(context, SCRATCH_COUNT_BEETWEN, data.getString("scratch_count_beetween"));
            setString(context, DAILY_VIDEO_LIMIT, data.getString("daily_video_limit"));
            setString(context, REWARDED_AD_TYPE, data.getString("rewarded_ad_type"));

            // Khởi tạo các mạng quảng cáo
            AppController.initializeAdNetworks();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Kiểm tra kết nối và ID người dùng
        if (AppController.isConnected((AppCompatActivity) context) && !(AppController.getInstance().getId().equals("0"))) {
            Log.d("ALGORATHAM", "Connection is available. Preparing to send request.");
            Log.d("ALGORATHAM", "User ID: " + AppController.getInstance().getId());

            //JsonRequest<JSONObject> jsonReq = new JsonRequest<JSONObject>(Request.Method.POST, Base_Url, null,
            StringRequest stringReq = new StringRequest(Request.Method.POST, Base_Url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("ALGORATHAM", "Response received: " + response.toString());

                            // Xử lý phản hồi từ server
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                if (AppController.getInstance().authorize(jsonResponse)) {
                                    Intent i = new Intent(context, MainActivity.class);
                                    i.putExtra("new_user", "old");
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    context.startActivity(i);
                                    ((Activity) context).finish();
                                } else {
                                    Intent i = new Intent(context, WelcomeActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    context.startActivity(i);
                                    AppController.getInstance().logout((AppCompatActivity) context);
                                    ((Activity) context).finish();
                                }
                            } catch (JSONException e) {
                                Log.e("ALGORATHAM", "JSON parsing error: " + e.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("ALGORATHAM", "Error occurred: " + error.getMessage());

                    Intent i = new Intent(context, WelcomeActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(i);
                    ((Activity) context).finish();
                    ContextExtensions.showLongToast(context, error.getMessage());
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Log.d("ALGORATHAM", "Preparing params to send: ");
                    Map<String, String> params = new HashMap<>();
                    params.put(ACCESS_KEY, ACCESS_Value);
                    params.put(GET_USER, API);
                    params.put(ID, "" + AppController.getInstance().getId());
                    Log.d("ALGORATHAM", "Params: " + params.toString());
                    return params;
                }
                /*@Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                    try {
                        String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                        Log.d("ALGORATHAM", "Raw response data: " + jsonString);
                        return Response.success(new JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response));
                    } catch (UnsupportedEncodingException | JSONException e) {
                        Log.e("ALGORATHAM", "Parse error: " + e.getMessage());
                        return Response.error(new ParseError(e));
                    }
                }*/
            };
            Log.d("ALGORATHAM", "Adding request to queue.");
            AppController.getInstance().addToRequestQueue(stringReq);
        } else {
            Log.w("ALGORATHAM", "Invalid user ID.");
            Intent i = new Intent(context, WelcomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(i);
            ((Activity) context).finish();
        }
    }

    public static boolean vpn() {
        Log.d("PrefManager", "Calling vpn function");
        String iface = "";
        try {
            for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (networkInterface.isUp()) iface = networkInterface.getName();
                Log.d("DEBUG", "IFACE NAME: " + iface);
                if (iface.contains("tun") || iface.contains("ppp") || iface.contains("pptp")) {
                    return true;
                }
            }
        } catch (SocketException e1) {
            e1.printStackTrace();
        }
        return false;
    }


    public static void check_n(Context context, Activity activity) {
        Log.d("PrefManager", "Calling check_n function");
        if (isConnected(context)) {
        } else {
            // Create the object of
            // AlertDialog Builder class
            AlertDialog.Builder builder
                    = new AlertDialog
                    .Builder(context);

            // Set the message show for the Alert time
            builder.setMessage("Please check your internet.");

            // Set Alert Title
            builder.setTitle("No connection!");
            builder.setCancelable(false);

            // Set Cancelable false
            // for when the user clicks on the outside
            // the Dialog Box then it will remain show
            builder.setCancelable(false);

            // Set the positive button with yes name
            // OnClickListener method is use of
            // DialogInterface interface.

            builder.setPositiveButton("Retry",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // When the user click yes button
                            // then app will close
                            dialog.dismiss();
                            Intent i = new Intent(context, ActivitySplash.class);
                            context.startActivity(i);
                            activity.finish();
                        }
                    });

            // Set the Negative button with No name
            // OnClickListener method is use
            // of DialogInterface interface.
            builder.setNegativeButton("Exit",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            activity.finishAffinity();
                            System.exit(0);
                        }
                    });

            // Create the Alert dialog
            AlertDialog alertDialog = builder.create();

            // Show the Alert Dialog box
            alertDialog.show();
        }
    }

    public static boolean isConnected(Context context) {
        Log.d("PrefManager", "Calling isConnected function");
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }

    public static void redeem_package(final Context contextt, String package_id, String p_details, String amount_id) {
        Log.d("PrefManager", "Calling redeem_package function");
        Dialog dialogg = new Dialog(contextt);
        dialogg.setContentView(R.layout.loading);
        dialogg.getWindow().setBackgroundDrawable(new ColorDrawable
                (Color.TRANSPARENT));
        dialogg.setCancelable(false);
        dialogg.show();
        //JsonRequest<JSONObject> stringRequest = new JsonRequest<JSONObject>(Request.Method.POST, Base_Url, null,
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Base_Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            dialogg.dismiss();
                            ContextExtensions.showShortToast(contextt, jsonResponse.getString("message"));
                        } catch (Exception e) {
                            dialogg.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ContextExtensions.showShortToast(contextt, error.getMessage());}
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ACCESS_KEY, ACCESS_Value);
                params.put("redeem", package_id);
                params.put("id", AppController.getInstance().getId());
                params.put("p_details", p_details);
                params.put("amount_id", amount_id);
                return params;
            }
            /*@Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(new JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response));
                } catch (JSONException e) {
                    Log.e("ERROR", "JSONException: ", e);
                    // Trả về phản hồi lỗi hoặc xử lý theo cách khác
                    return Response.error(new ParseError(e));
                } catch (UnsupportedEncodingException e) {
                    Log.e("ERROR", "UnsupportedEncodingException: ", e);
                    return Response.error(new ParseError(e));
                } catch (Exception e) {
                    Log.e("ERROR", "Unexpected Error: ", e);
                    return Response.error(new ParseError(e));
                }
            }*/
        };
        if (AppController.getInstance() != null) {
            AppController.getInstance().addToRequestQueue(stringRequest);
        } else {
            Log.e("ERROR", "AppController instance is null");
        }
    }

    public static void claim_points(Context context) {
        Log.d("PrefManager", "Calling claim_points function");
        //JsonRequest<JSONObject> stringRequest = new JsonRequest<JSONObject>(Request.Method.POST, Base_Url, null,
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Base_Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            if (jsonResponse.getString("error").equalsIgnoreCase("false")) {
                                String p = jsonResponse.getString("points");
                                Add_Coins_(context, p, "Daily checkin bonus", "true", null);
                            } else {
                                ContextExtensions.showShortToast(context, "You've already claim your daily bonus!");
                            }
                        } catch (Exception e) {}
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {}
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ACCESS_KEY, ACCESS_Value);
                params.put(DAILY_CHECKIN_API, API);
                params.put(USERNAME, AppController.getInstance().getUsername());
                params.put(SPIN_TYPE, DAILY_TYPE);
                return params;
            }
            /*@Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(new JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response));
                } catch (JSONException e) {
                    Log.e("ERROR", "JSONException: ", e);
                    // Trả về phản hồi lỗi hoặc xử lý theo cách khác
                    return Response.error(new ParseError(e));
                } catch (UnsupportedEncodingException e) {
                    Log.e("ERROR", "UnsupportedEncodingException: ", e);
                    return Response.error(new ParseError(e));
                } catch (Exception e) {
                    Log.e("ERROR", "Unexpected Error: ", e);
                    return Response.error(new ParseError(e));
                }
            }*/
        };

        if (AppController.getInstance() != null) {
            AppController.getInstance().addToRequestQueue(stringRequest);
        } else {
            Log.e("ERROR", "AppController instance is null");
        }
    }
}
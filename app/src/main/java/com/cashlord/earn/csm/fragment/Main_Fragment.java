package com.cashlord.earn.csm.fragment;

import static android.Manifest.permission.POST_NOTIFICATIONS;
import static android.content.ContentValues.TAG;
import static com.cashlord.earn.helper.Constatnt.ACCESS_KEY;
import static com.cashlord.earn.helper.Constatnt.ACCESS_Value;
import static com.cashlord.earn.helper.Constatnt.API;
import static com.cashlord.earn.helper.Constatnt.Base_Url;
import static com.cashlord.earn.helper.Constatnt.DAILY_CHECKIN_API;
import static com.cashlord.earn.helper.Constatnt.DAILY_TYPE;
import static com.cashlord.earn.helper.Constatnt.Main_Url;
import static com.cashlord.earn.helper.Constatnt.SPIN_TYPE;
import static com.cashlord.earn.helper.Constatnt.USERNAME;
import static com.cashlord.earn.helper.Constatnt.WHEEL_URL;
import static com.cashlord.earn.helper.Helper.FRAGMENT_SCRATCH;
import static com.cashlord.earn.helper.Helper.FRAGMENT_TYPE;
import static com.cashlord.earn.helper.PrefManager.check_n;
import static com.cashlord.earn.helper.PrefManager.user_points;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cashlord.earn.FragmentLoadingActivity;
import com.cashlord.earn.R;
import com.cashlord.earn.csm.FragViewerActivity;
import com.cashlord.earn.csm.GameActivity;
import com.cashlord.earn.csm.VideoActivity;
import com.cashlord.earn.csm.adapter.GameAdapter;
import com.cashlord.earn.csm.adapter.SliderAdapter;
import com.cashlord.earn.csm.model.GameModel;
import com.cashlord.earn.csm.model.SliderItems;
import com.cashlord.earn.csm.model.WebsiteModel;
import com.cashlord.earn.helper.AppController;
import com.cashlord.earn.helper.Constatnt;
import com.cashlord.earn.helper.ContextExtensions;
import com.cashlord.earn.helper.CustomVolleyJsonRequest;
import com.cashlord.earn.helper.Helper;
import com.cashlord.earn.helper.JsonRequest;
import com.cashlord.earn.helper.PrefManager;
import com.cashlord.earn.luck_draw.Activity_Notification;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Main_Fragment extends Fragment {
    private View root_view;
    private TextView points;
    private String p, res_game;
    private ViewPager2 viewPager2;
    private Boolean is_game = false;

    private ShimmerFrameLayout game_shim;
    private RecyclerView game_list;
    private final List<GameModel> gameModel = new ArrayList<>();
    private final Handler sliderHandler = new Handler();
    private List<SliderItems> sliderItems = new ArrayList<>();

    private Boolean isWebsiteLoaded = false, isVideoVisitLoaded = false;

    // ActivityResultLauncher for permission request
    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (!isGranted) {
                    checkNotificationPermissionForAndroid13AndAbove();
                }
            }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.fragment_main, container, false);
        points = root_view.findViewById(R.id.points);
        TextView name = root_view.findViewById(R.id.name);
        points.setText("0");
        check_n(getContext(), getActivity());

        name.setText(AppController.getInstance().getFullname());
        TextView rank = root_view.findViewById(R.id.rank);
        rank.setText(AppController.getInstance().getRank());

        viewPager2 = root_view.findViewById(R.id.viewPagerImageSlider);
        game_shim = root_view.findViewById(R.id.game_shimmer);
        LinearLayout scratch_btn = root_view.findViewById(R.id.scratch_btn);
        CircleImageView pro_img = root_view.findViewById(R.id.pro_img);
        LinearLayout pro_lin = root_view.findViewById(R.id.pro_lin);
        LinearLayout spin = root_view.findViewById(R.id.spin);
        TextView game_t = root_view.findViewById(R.id.game_t);
        LinearLayout game_btn = root_view.findViewById(R.id.game_btn);
        LinearLayout task = root_view.findViewById(R.id.task);
        game_list = root_view.findViewById(R.id.game);
        LinearLayout game_more = root_view.findViewById(R.id.game_more);
        LinearLayout video_visit_btn = root_view.findViewById(R.id.video_visit_btn);
        ImageView wheel = root_view.findViewById(R.id.wheel);

        Glide.with(requireContext()).load(WHEEL_URL)
                .apply(new RequestOptions().placeholder(R.mipmap.ic_launcher_round))
                .into(wheel);

        spin.setOnClickListener(view -> {
            Intent i = new Intent(getContext(), VideoActivity.class);
            startActivity(i);
        });

        scratch_btn.setOnClickListener(view -> {
            Intent i = new Intent(getContext(), FragmentLoadingActivity.class);
            i.putExtra(FRAGMENT_TYPE, FRAGMENT_SCRATCH);
            startActivity(i);
        });

        game_more.setOnClickListener(view -> {
            if (is_game) {
                Intent i = new Intent(getContext(), GameActivity.class);
                i.putExtra("res", res_game);
                startActivity(i);
            } else {
                Toast.makeText(getContext(), "Game is loading...", Toast.LENGTH_SHORT).show();
            }
        });

        game_btn.setOnClickListener(view -> {
            if (is_game) {
                Intent i = new Intent(getContext(), GameActivity.class);
                i.putExtra("res", res_game);
                startActivity(i);
            } else {
                Toast.makeText(getContext(), "Game is loading...", Toast.LENGTH_SHORT).show();
            }
        });

        game_t.setOnClickListener(view -> {
            if (is_game) {
                Intent i = new Intent(getContext(), GameActivity.class);
                i.putExtra("res", res_game);
                startActivity(i);
            } else {
                Toast.makeText(getContext(), "Game is loading...", Toast.LENGTH_SHORT).show();
            }
        });

        pro_lin.setOnClickListener(view -> {
            Intent i = new Intent(getContext(), FragViewerActivity.class);
            startActivity(i);
        });

        parseJsonFeedd();

        Glide.with(this).load(AppController.getInstance().getProfile())
                .apply(new RequestOptions().placeholder(R.mipmap.ic_launcher_round))
                .into(pro_img);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(10));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });

        viewPager2.setPageTransformer(compositePageTransformer);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 5000); // slide duration 5 seconds
            }
        });

        load_game();

        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        daily_Point();

        RelativeLayout bell = root_view.findViewById(R.id.bell);

        bell.setOnClickListener(view -> {
            Intent i = new Intent(getContext(), Activity_Notification.class);
            startActivity(i);
        });

        TextView badge = root_view.findViewById(R.id.badge);

        try {
            int notification_count = Integer.parseInt(AppController.getInstance().getBadge());
            if (notification_count != 0) {
                badge.setText("" + notification_count);
                badge.setVisibility(View.VISIBLE);
            } else {
                badge.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        getVisitSettingsFromAdminPannel();
        getVideoSettingsFromAdminPannel();
        return root_view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new Handler().postDelayed((Runnable) this::checkNotificationPermissionForAndroid13AndAbove, 2000);
    }

    private void getVisitSettingsFromAdminPannel() {
        if (AppController.isConnected((AppCompatActivity) requireActivity())) {
            try {
                String tag_json_obj = "json_login_req";
                Map<String, String> map = new HashMap<>();
                map.put("get_visit_settings", "any");
                CustomVolleyJsonRequest customVolleyJsonRequest = new CustomVolleyJsonRequest(Request.Method.POST,
                        Constatnt.WEBSITE_SETTINGS, map, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean status = response.getBoolean("status");
                            if (status) {
                                ArrayList<WebsiteModel> websiteModelArrayList = new ArrayList<>();
                                JSONArray jb = response.getJSONArray("data");
                                PrefManager.setString(requireActivity(), Helper.TODAY_DATE, response.getString("date"));
                                for (int i = 0; i < jb.length(); i++) {
                                    JSONObject visitObject = jb.getJSONObject(i);
                                    if (visitObject.getString("is_visit_enable").equalsIgnoreCase("true")) {
                                        WebsiteModel websiteModel = new WebsiteModel(
                                                visitObject.getString("id"),
                                                visitObject.getString("is_visit_enable"),
                                                visitObject.getString("visit_title"),
                                                visitObject.getString("visit_link"),
                                                visitObject.getString("visit_coin"),
                                                visitObject.getString("visit_timer"),
                                                visitObject.getString("browser"),
                                                null,
                                                null,
                                                null
                                        );
                                        websiteModelArrayList.add(websiteModel);
                                    }
                                }
                                if (!websiteModelArrayList.isEmpty()) {
                                    Gson gson = new Gson();
                                    String json = gson.toJson(websiteModelArrayList);
                                    PrefManager.setString(getActivity(), Helper.WEBSITE_LIST, json);
                                } else {
                                    PrefManager.setString(getActivity(), Helper.WEBSITE_LIST, "");
                                }
                            }
                            isWebsiteLoaded = true;

                        } catch (Exception e) {
                            e.printStackTrace();
                            isWebsiteLoaded = true;
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(getActivity(), "Slow Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                        isWebsiteLoaded = true;
                    }
                });
                customVolleyJsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                        1000 * 30,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                // Adding request to request queue
                AppController.getInstance().addToRequestQueue(customVolleyJsonRequest, tag_json_obj);

            } catch (Exception e) {
                Log.e("TAG", "Withdraw Settings: excption " + e.getMessage());
            }
        } else {
            Toast.makeText(requireActivity(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void getVideoSettingsFromAdminPannel() {
        if (AppController.isConnected((AppCompatActivity) requireActivity())) {
            try {
                String tag_json_obj = "json_login_req";
                Map<String, String> map = new HashMap<>();
                map.put("get_video_settings", "any");
                CustomVolleyJsonRequest customVolleyJsonRequest = new CustomVolleyJsonRequest(Request.Method.POST,
                        Constatnt.VIDEO_SETTINGS, map, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean status = response.getBoolean("status");
                            if (status) {
                                ArrayList<WebsiteModel> websiteModelArrayList = new ArrayList<>();
                                JSONArray jb = response.getJSONArray("data");
                                for (int i = 0; i < jb.length(); i++) {
                                    JSONObject visitObject = jb.getJSONObject(i);
                                    if (visitObject.getString("is_enable").equalsIgnoreCase("true")) {
                                        WebsiteModel websiteModel = new WebsiteModel(
                                                visitObject.getString("id"),
                                                visitObject.getString("is_enable"),
                                                visitObject.getString("title"),
                                                visitObject.getString("link"),
                                                visitObject.getString("coin"),
                                                visitObject.getString("timer"),
                                                visitObject.getString("browser"),
                                                null,
                                                null,
                                                null
                                        );
                                        websiteModelArrayList.add(websiteModel);
                                    }
                                }
                                if (!websiteModelArrayList.isEmpty()) {
                                    Gson gson = new Gson();
                                    String json = gson.toJson(websiteModelArrayList);
                                    PrefManager.setString(getActivity(), Helper.VIDEO_LIST, json);
                                } else {
                                    PrefManager.setString(getActivity(), Helper.VIDEO_LIST, "");
                                }
                            }
                            isVideoVisitLoaded = true;

                        } catch (Exception e) {
                            e.printStackTrace();
                            isVideoVisitLoaded = true;
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(getActivity(), "Slow Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                        isVideoVisitLoaded = true;
                    }
                });
                customVolleyJsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                        1000 * 30,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                // Adding request to request queue
                AppController.getInstance().addToRequestQueue(customVolleyJsonRequest, tag_json_obj);

            } catch (Exception e) {
                Log.e("TAG", "Withdraw Settings: excption " + e.getMessage());
            }
        } else {
            Toast.makeText(requireActivity(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void LoadRedeemList() {
        JsonArrayRequest request = new JsonArrayRequest(Main_Url + "offerswj.php", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray array) {
                //offers.clear();
                for (int i = 0; i < array.length(); i++) {
                    try {
                        //offerwalls = array.toString();
                        //is_offer_loaded = true;

                        JSONObject object = array.getJSONObject(i);

                        String id = object.getString("id").trim();
                        String image = object.getString("image").trim();
                        String title = object.getString("title").trim();
                        String sub = object.getString("sub").trim();
                        String offer_name = object.getString("offer_name").trim();
                        String status = object.getString("status").trim();
                        String type = object.getString("type").trim();
                        String points = object.getString("points").trim();

                        if (type.equals("1")) {
                            SliderItems itemm = new SliderItems("1", title, sub, sub, offer_name, image);
                            sliderItems.add(itemm);
                        }

                        //offers_model item = new offers_model(id, image, title, sub, offer_name, status);
                        //offers.add(item);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                viewPager2.setAdapter(new SliderAdapter(sliderItems, viewPager2, getContext()));
            }
        }, error -> Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show());

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }

    private void daily_Point() {
        JsonRequest stringRequest = new JsonRequest(Request.Method.POST, Base_Url, null, response -> {
            try {
                if (response.getString("error").equalsIgnoreCase("false")) {
                    p = response.getString("points");
                    SliderItems item = new SliderItems("0", "Daily Bonus", "Claim your daily bonus", p, "true", ".");
                    sliderItems.add(item);
                    LoadRedeemList();
                } else {
                    p = response.getString("points");
                    SliderItems item = new SliderItems("0", "Daily Bonus", "Claim your daily bonus", p, "false", ".");
                    sliderItems.add(item);
                    LoadRedeemList();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        },
                error -> {
                    // Xử lý lỗi ở đây nếu cần
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(ACCESS_KEY, ACCESS_Value);
                params.put(DAILY_CHECKIN_API, API);
                params.put(USERNAME, AppController.getInstance().getUsername());
                params.put(SPIN_TYPE, DAILY_TYPE);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void parseJsonFeedd() {}

    public void load_game() {
        JsonRequest stringRequest = new JsonRequest(Request.Method.POST, Base_Url, null, response -> {
            VolleyLog.d(TAG, "Response: " + response.toString());
            if (response != null) {
                set_game(response);
            }
        }, error -> {
            if (getActivity() != null) {
                Toast.makeText(getActivity(), "" + error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(ACCESS_KEY, ACCESS_Value);
                params.put("game", "game");
                params.put("id", AppController.getInstance().getId());
                params.put("usser", AppController.getInstance().getUsername());
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void set_game(JSONObject response) {
        try {
            JSONArray feedArray = response.getJSONArray("data");
            res_game = feedArray.toString();
            is_game = true;
            gameModel.clear();
            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = feedArray.getJSONObject(i);
                Integer id = feedObj.getInt("id");
                String title = feedObj.getString("title");
                String image = feedObj.getString("image");
                String game_link = feedObj.getString("game");
                String gamePoints = feedObj.getString("points");
                String gameTime = feedObj.getString("game_time");
                GameModel item = new GameModel(id, title, image, game_link, gamePoints, gameTime);
                gameModel.add(item);
            }
            GameAdapter game_adapter = new GameAdapter(gameModel, getActivity(), 0);
            game_list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

            RelativeLayout lin_game_c = root_view.findViewById(R.id.lin_game_c);
            game_list.setAdapter(game_adapter);
            game_shim.setVisibility(View.GONE);
            lin_game_c.setVisibility(View.VISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 2000);
        user_points(points);
    }

    Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };

    // Phương thức load_offer có thể loại bỏ nếu không cần
    public void load_offer() {
        // Nếu không cần phương thức này, có thể loại bỏ
    }

    /*private void pass_offer(JSONObject response) {
        // Loại bỏ tất cả các tham chiếu đến Offertoro
        // Nếu phương thức này không cần thiết, bạn có thể xóa nó hoàn toàn
        // ShimmerFrameLayout offer_toro_shimmer = root_view.findViewById(R.id.offer_toro_shimmer);
        try {
            // Xử lý phản hồi từ server nếu cần thiết
            // JSONObject offers_json = response.getJSONObject("response");
            // JSONArray feedArray = offers_json.getJSONArray("offers");
            // res_offer = feedArray.toString();
            // is_offer = true;
            // offerToro_model.clear();

            // Phần này đã được loại bỏ vì không sử dụng Offertoro
            // for (int i = 0; i < feedArray.length(); i++) {
            //     JSONObject feedObj = (JSONObject) feedArray.get(i);
            //     String offer_id = feedObj.getString("offer_id");
            //     String offer_name = feedObj.getString("offer_name");
            //     String offer_desc = feedObj.getString("offer_desc");
            //     String call_to_action = feedObj.getString("call_to_action");
            //     String disclaimer = feedObj.getString("disclaimer");
            //     String offer_url = feedObj.getString("offer_url");
            //     String offer_url_easy = feedObj.getString("offer_url_easy");
            //     String payout = feedObj.getString("payout");
            //     String payout_type = feedObj.getString("payout_type");
            //     String amount = feedObj.getString("amount");
            //     String image_url = feedObj.getString("image_url");
            //     String image_url_220x124 = feedObj.getString("image_url_220x124");
            //     OfferToro_Model item = new OfferToro_Model(offer_id, offer_name, offer_desc, call_to_action, disclaimer,
            //             offer_url, offer_url_easy, payout_type, amount, image_url, image_url_220x124);
            //     offerToro_model.add(item);
            // }

            // Cập nhật lại giao diện hoặc các phần cần thiết
            // OfferToro_Adapter offerToro_adapter = new OfferToro_Adapter(offerToro_model, getContext(), 0);
            // offer_t.setLayoutManager(new LinearLayoutManager(getContext()));
            // offer_t.setAdapter(offerToro_adapter);
            // offer_toro_shimmer.stopShimmer();
            // offer_toro_shimmer.setVisibility(View.GONE);
            // offer_t.setVisibility(View.VISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
            // Chỉnh sửa để phù hợp với giao diện bạn cần
            // root_view.findViewById(R.id.more_offer).setVisibility(View.GONE);
            // offer_toro_shimmer.stopShimmer();
            // offer_toro_shimmer.setVisibility(View.GONE);
        }
    }*/

    private void pass_offer(JSONObject response) {
        try {
            // Kiểm tra xem response có phải là JSONObject không
            if (response != null) {
                // Ví dụ, kiểm tra một thuộc tính
                String someValue = response.optString("some_key");
                if (someValue.isEmpty()) {
                    throw new JSONException("Value is empty");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // Xử lý lỗi nếu cần
        }
    }

    private void checkNotificationPermissionForAndroid13AndAbove() {
        if (getContext() != null && ContextExtensions.isAndroid13(requireContext())) {
            if (ActivityCompat.checkSelfPermission(requireContext(), POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(POST_NOTIFICATIONS)) {
                    showNotificationPermissionDialog();
                } else {
                    requestPermissionLauncher.launch(POST_NOTIFICATIONS);
                }
            }
        }
    }

    private void showNotificationPermissionDialog() {
        new AlertDialog.Builder(requireActivity())
                .setTitle(null)
                .setMessage(R.string.notification_permission_dialog)
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                    startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            .setData(Uri.fromParts("package", getActivity().getPackageName(), null)));
                })
                .create()
                .show();
    }

}

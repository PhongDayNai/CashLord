package com.cashlord.earn.csm;

import static com.cashlord.earn.helper.PrefManager.getSavedString;
import static com.cashlord.earn.helper.PrefManager.setWindowFlag;
import static com.cashlord.earn.helper.PrefManager.user_points;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cashlord.earn.AdsManager;
import com.cashlord.earn.InterstitialAds;
import com.cashlord.earn.R;
import com.cashlord.earn.csm.adapter.GameAdapter;
import com.cashlord.earn.csm.model.GameModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity {
    private List<GameModel> gameModel = new ArrayList<>();
    RecyclerView rv_game;
    GameAdapter game_adapter;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Cấu hình trạng thái màn hình và thanh trạng thái
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

        setContentView(R.layout.activity_game);

        // Hiển thị quảng cáo xen kẽ (Interstitial Ad) nếu đã sẵn sàng
        InterstitialAds interstitialAds = InterstitialAds.getInstance(this);
        if (interstitialAds.isAdReady()) {
            interstitialAds.showAd();  // Hiển thị quảng cáo
        } else {
            // Quảng cáo chưa sẵn sàng, tải lại hoặc thông báo cho người dùng
            interstitialAds.loadAd();
            Toast.makeText(this, "Loading ad, please wait...", Toast.LENGTH_SHORT).show();
        }
        /*if (AdsManager.isInterstitialLoaded()) {
            AdsManager.showInterstitalAd(this);
        }*/

        rv_game = findViewById(R.id.rv_game);

        // Hiển thị điểm người dùng
        TextView points = findViewById(R.id.points);
        back = findViewById(R.id.back);
        user_points(points);

        // Nút trở lại
        back.setOnClickListener(view -> finish());

        // Nhận dữ liệu game từ Intent
        Intent i = getIntent();
        try {
            JSONArray array = new JSONArray(i.getStringExtra("res"));
            for (int index = 0; index < array.length(); index++) {
                JSONObject feedObj = array.getJSONObject(index);
                Integer id = feedObj.getInt("id");
                String title = feedObj.getString("title");
                String image = feedObj.getString("image");
                String game_link = feedObj.getString("game");
                String gamePoints = feedObj.getString("points");
                String gameTime = feedObj.getString("game_time");

                // Tạo đối tượng GameModel và thêm vào danh sách
                GameModel item = new GameModel(id, title, image, game_link, gamePoints, gameTime);
                gameModel.add(item);
            }

            // Thiết lập adapter cho RecyclerView
            game_adapter = new GameAdapter(gameModel, GameActivity.this, 1);
            rv_game.setLayoutManager(new LinearLayoutManager(GameActivity.this));
            rv_game.setAdapter(game_adapter);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Hiển thị quảng cáo banner
        //AdsManager.loadBannerAd(this, findViewById(R.id.banner_ad_container));
    }
}
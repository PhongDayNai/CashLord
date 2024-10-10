package com.cashlord.earn;

import static android.content.ContentValues.TAG;
import static com.cashlord.earn.helper.Constatnt.ACCESS_KEY;
import static com.cashlord.earn.helper.Constatnt.ACCESS_Value;
import static com.cashlord.earn.helper.Constatnt.Base_Url;
import static com.cashlord.earn.helper.Constatnt.TASK;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cashlord.earn.csm.adapter.Task_adapter;
import com.cashlord.earn.csm.model.Task_model;
import com.cashlord.earn.helper.AppController;
import com.cashlord.earn.helper.ContextExtensions;
import com.cashlord.earn.helper.ContextExtensions;
import com.cashlord.earn.helper.JsonRequest;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Refer_Activity extends AppCompatActivity {

    private Task_adapter task_adapter;
    RecyclerView task_list;
    private List<Task_model> task_item = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        task_list = findViewById(R.id.task_list);
        getquizlist();
    }


    public void getquizlist() {
        JsonRequest stringRequest = new JsonRequest(Request.Method.POST,
                Base_Url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                VolleyLog.d(TAG, "Response: " + response.toString());
                if (response != null) {
                    parseJsonFeed(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ContextExtensions.showLongToast(Refer_Activity.this, error.toString());
                //  hidepDialog();
                Log.e("ON_ERROR_Refer_Activity", "onErrorResponse: ", error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ACCESS_KEY, ACCESS_Value);
                params.put(TASK, TASK);
                params.put("id", AppController.getInstance().getId());
                return params;
            }
        };
        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(stringRequest);
    }


    private void parseJsonFeed(JSONObject response) {
        try {

            JSONArray feedArray = response.getJSONArray("data");
            task_item.clear();
            int count = 0;
            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);
                count++;

                String id = (feedObj.getString("id"));
                Integer invites = (feedObj.getInt("invites"));
                String points = (feedObj.getString("points"));
                String check = (feedObj.getString("check"));
                String title = "Task " + count;
                Integer reff = response.getInt("ref");


                Task_model item = new Task_model(id, invites, points, check, title, reff);
                task_item.add(item);

            }

            task_adapter = new Task_adapter(task_item, Refer_Activity.this, "1");

            task_list.setHasFixedSize(true);
            task_list.setLayoutManager(new LinearLayoutManager(Refer_Activity.this));
            task_list.setAdapter(task_adapter);

        } catch (JSONException e) {
            e.printStackTrace();
            ContextExtensions.showLongToast(Refer_Activity.this, "error");

        }
    }

}
package com.cashlord.earn.csm.fragment;

import static com.cashlord.earn.helper.AppController.hidepDialog;
import static com.cashlord.earn.helper.AppController.showpDialog;
import static com.cashlord.earn.helper.Constatnt.ACCESS_KEY;
import static com.cashlord.earn.helper.Constatnt.ACCESS_Value;
import static com.cashlord.earn.helper.Constatnt.API;
import static com.cashlord.earn.helper.Constatnt.Base_Url;
import static com.cashlord.earn.helper.Constatnt.POINTS;
import static com.cashlord.earn.helper.Constatnt.USERNAME;
import static com.cashlord.earn.helper.Constatnt.USER_TRACKER;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cashlord.earn.Adapter.Earning_Adapter;
import com.cashlord.earn.R;
import com.cashlord.earn.helper.AppController;
import com.cashlord.earn.helper.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryFragment extends Fragment {
    View root_view;
    Earning_Adapter adapter;
    RecyclerView list;
    private List<Model> historyList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root_view = inflater.inflate(R.layout.fragment_history, container, false);
        list = root_view.findViewById(R.id.list);
        prepareData();
        return root_view;
    }

    private void prepareData() {
        showpDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Base_Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response1) {
                        try {
                            JSONObject response = new JSONObject(response1);
                            if (response.getString("error").equalsIgnoreCase("false")) {
                                JSONArray jsonArray = response.getJSONArray("data");
                                historyList.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String desc = jsonObject.getString("type");
                                    String date = jsonObject.getString("date");
                                    String time = jsonObject.getString("time");
                                    String coin = jsonObject.getString(POINTS);

                                    Model model = new Model(desc, date, coin,time);
                                    historyList.add(model);

                                }
                                adapter = new Earning_Adapter(historyList);
                                // recyclerView.setVisibility(View.VISIBLE);
                                list.setLayoutManager(new LinearLayoutManager(getContext()));
                                list.setAdapter(adapter);
                                hidepDialog();

                            } else {
                                if (response.getString("message").equalsIgnoreCase("No tracking history found!")) {
                                    Toast.makeText(getContext(),response.getString("message"),Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(),e.toString(),Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),error.toString(),Toast.LENGTH_LONG).show();
                        Log.e("ON_ERROR_HistoryFragment", "onErrorResponse: ", error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ACCESS_KEY, ACCESS_Value);
                params.put(USER_TRACKER, API);
                params.put(USERNAME, AppController.getInstance().getUsername());
                return params;
            }
        };
        AppController.getInstance().getRequestQueue().getCache().clear();
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
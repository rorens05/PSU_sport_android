package com.example.psusports;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.psusports.engine.MySingleton;
import com.example.psusports.global.GlobalVariables;
import com.example.psusports.models.Sport;
import com.example.psusports.models.SportEvent;
import com.example.psusports.templates.SportAdapter;
import com.example.psusports.templates.SportEventAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SportsActivity extends AppCompatActivity {
    private static final String TAG = "SportsActivity";
    RecyclerView recyclerView;
    SportEventAdapter sportEventAdapter;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_sports);
        GlobalVariables.loadAllTeams(this);
        init();
        loadSports();
    }

    public void init(){
        recyclerView = findViewById(R.id.recyclerView2);
    }

    public void loadSports(){

        pd = new ProgressDialog(SportsActivity.this);
        pd.setMessage("Loading Sports");
        pd.show();

        StringRequest events = new StringRequest(Request.Method.GET, GlobalVariables.SPORT_URL + "?event_id=" + GlobalVariables.selectedEvent.id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response Recieved");
                try {
                    GlobalVariables.sportList = new ArrayList<>();
                    JSONObject jResponse = new JSONObject(response);
                    Log.d(TAG, "message: " + jResponse.getString("message"));
                    Log.d(TAG, "event: " + GlobalVariables.selectedEvent.name);
                    JSONArray data = jResponse.getJSONArray("data");
                    Log.d(TAG, "Looping through data, size: " + data.length());
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject temp = data.getJSONObject(i);
                        Sport sport = new Sport();
                        sport.id = temp.getString("id");
                        sport.name = temp.getString("name");
                        sport.game_type = temp.getString("game_type");
                        sport.logo = temp.getString("logo");
                        GlobalVariables.sportList.add(sport);
                        Log.d(TAG, sport.name);
                    }
                    Log.d(TAG, "Sports Loaded Successfully");
                    displayRecyclerView();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(TAG, "Response error, Loading failed");
                }

                pd.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Response Recieved");
                Toast.makeText(SportsActivity.this, "Failed to connect to the server", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        });

        MySingleton.getInstance(this).addToRequestQueue(events);

        Log.d(TAG, "sports loaded");
    }

    public void displayRecyclerView() {
        Log.d(TAG, "Displaying data");
        Log.d(TAG, "Creating Adapter");
        SportAdapter sportAdapter = new SportAdapter(GlobalVariables.sportList, this);
        Log.d(TAG, "Adapter Created");

        Log.d(TAG, "Creating layout");
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        Log.d(TAG, "setting adapter");
        recyclerView.setAdapter(sportAdapter);
        Log.d(TAG, "Data Displayed Successfully");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

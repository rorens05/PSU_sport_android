package com.example.psusports;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.psusports.engine.MySingleton;
import com.example.psusports.global.GlobalVariables;
import com.example.psusports.models.SportEvent;
import com.example.psusports.models.Team;
import com.example.psusports.templates.SportEventAdapter;
import com.example.psusports.templates.TeamAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TeamsActivity extends AppCompatActivity {
    private static final String TAG = "TeamsActivity";
    RecyclerView recyclerView;
    TeamAdapter teamAdapter;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_teams);
        setTitle(GlobalVariables.selectedSport.name + " Teams");
        loadTeams();
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

    public void loadRecyclerView(){

        recyclerView = findViewById(R.id.recycler_view_team);
        Log.d(TAG, "initializing recyclerview adapter");
        teamAdapter = new TeamAdapter(GlobalVariables.teamList, this);
        recyclerView.setAdapter(teamAdapter);
        Log.d(TAG, "adapter initialized ");

        Log.d(TAG, "Displaying Data");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        dividerItemDecoration.setDrawable(getApplicationContext().getResources().getDrawable(R.drawable.mdivider));
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    public void loadTeams(){
        Log.d(TAG, "loading teams");
        pd = new ProgressDialog(this);
        pd.setMessage("Loading Teams");
        String url = GlobalVariables.TEAM_URL + "?sport_id=" + GlobalVariables.selectedSport.id +
                "&event_id=" + GlobalVariables.selectedEvent.id;
        Log.d(TAG, url);
        StringRequest teams = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response Recieved");
                try {
                    JSONObject jResponse = new JSONObject(response);
                    JSONArray data = jResponse.getJSONArray("data");
                    Log.d(TAG, "Looping through data, size: " + data.length());
                    GlobalVariables.teamList = new ArrayList<>();
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject temp = data.getJSONObject(i);
                        Team team = new Team();
                        team.name = temp.getString("name");
                        team.sport_id = temp.getString("sport_id");
                        team.event_id = temp.getString("event_id");
                        team.logo = temp.getString("logo");
                        GlobalVariables.teamList.add(team);
                        Log.d(TAG, team.name);
                    }
                    loadRecyclerView();
                    Log.d(TAG, "Team Loaded Successfully");
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(TAG, "Response error, Loading failed");
                }

                loadRecyclerView();
                pd.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TeamsActivity.this, "Cannot connect to the server", Toast.LENGTH_SHORT).show();
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(teams);
    }
}

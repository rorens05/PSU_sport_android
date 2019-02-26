package com.example.psusports;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.psusports.R;
import com.example.psusports.engine.MySingleton;
import com.example.psusports.global.GlobalVariables;
import com.example.psusports.models.Game;
import com.example.psusports.models.Team;
import com.example.psusports.templates.GamesAdapter;
import com.example.psusports.templates.TeamAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class GamesActivity extends AppCompatActivity {
    private static final String TAG = "GamesActivity";
    TextView eventName;
    TextView date;
    TextView venue;
    TextView organizer;
    TextView sport;
    Button addGame;
    Button viewTeams;

    ProgressDialog pd;

    RecyclerView recyclerView;
    GamesAdapter gameAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_games);
        init();
        loadGames();
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

    public void init(){
        eventName = findViewById(R.id.etxt_event_name);
        date = findViewById(R.id.etxt_event_date);
        venue = findViewById(R.id.etxt_event_venue);
        organizer = findViewById(R.id.etxt_event_organizer);
        addGame = findViewById(R.id.ebtn_add_game);
        viewTeams = findViewById(R.id.ebtn_view_teams);
        sport = findViewById(R.id.etxt_event_sport);

        eventName.setText(GlobalVariables.selectedEvent.name);
        date.setText("Date: " + GlobalVariables.selectedEvent.event_date);
        venue.setText("Venue: " + GlobalVariables.selectedEvent.venue);
        organizer.setText("Organizer: " + GlobalVariables.selectedEvent.organizer);
        sport.setText(GlobalVariables.selectedSport.name);

        viewTeams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GamesActivity.this, TeamsActivity.class));
            }
        });

        addGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(GamesActivity.this, AddGameActivity.class), 2);
            }
        });
    }

    public void loadGames(){
        Log.d(TAG, "LOADING GAMES");
        pd = new ProgressDialog(this);
        pd.show();
        String url = GlobalVariables.GAME_URL + "?sport_id=" + GlobalVariables.selectedSport.id  +
                "&event_id=" + GlobalVariables.selectedEvent.id;
        Log.d(TAG, url);
        StringRequest games = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "response received");
                try {
                    JSONObject jResponse = new JSONObject(response);
                    JSONArray data = jResponse.getJSONArray("data");
                    Log.d(TAG, "Looping through data, size: " + data.length());
                    GlobalVariables.gameList = new ArrayList<>();
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject temp = data.getJSONObject(i);
                        Game game = new Game();
                        game.id = temp.getString("id");
                        game.name = temp.getString("name");
                        game.game_type = temp.getString("game_type");
                        game.sport_id = temp.getString("sport_id");
                        game.schedule = temp.getString("schedule");
                        game.event_id = temp.getString("event_id");
                        game.status = temp.getString("status");

                        JSONArray contestant = temp.getJSONArray("contestant");

                        JSONObject team1 = contestant.getJSONObject(0);
                        game.c1_id = team1.getString("id");
                        game.team1 = team1.getString("team_id");
                        game.score1 = team1.getString("score");

                        JSONObject team2 = contestant.getJSONObject(1);
                        game.c2_id = team2.getString("id");
                        game.team2 = team2.getString("team_id");
                        game.score2 = team2.getString("score");

                        GlobalVariables.gameList.add(game);
                        Log.d(TAG, game.toString());
                    }
                    loadRecyclerView();
                    Log.d(TAG, "game Loaded Successfully");
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(TAG, "Response error, Loading failed");
                    Toast.makeText(GamesActivity.this, "Error Occured, Contact developers", Toast.LENGTH_SHORT).show();
                }
                pd.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(GamesActivity.this, "Unable to connect to the server", Toast.LENGTH_SHORT).show();
            }
        });

        MySingleton.getInstance(this).addToRequestQueue(games);

    }


    public void loadRecyclerView(){

        recyclerView = findViewById(R.id.recycler_view_games);
        Log.d(TAG, "initializing recyclerview adapter");
        gameAdapter = new GamesAdapter(GlobalVariables.gameList, this);
        recyclerView.setAdapter(gameAdapter);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==2)
        {
            loadGames();
        }
    }
}

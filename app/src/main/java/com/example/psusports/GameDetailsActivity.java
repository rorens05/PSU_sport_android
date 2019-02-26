package com.example.psusports;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.psusports.engine.MySingleton;
import com.example.psusports.global.GlobalVariables;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class GameDetailsActivity extends AppCompatActivity {

    private static final String TAG = "GameDetailsActivity";

    String status_new;

    TextView event;
    TextView status;
    TextView schedule;
    TextView game;
    TextView team1;
    TextView team2;
    ImageView iTeam1;
    ImageView iTeam2;

    EditText score1;
    EditText score2;

    Button finish;
    Button save;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_details);
        init();
    }

    public void init(){
        Log.d(TAG, "selected game: " + GlobalVariables.selectedGame.toString());
        Log.d(TAG, "selected game index value: " + GlobalVariables.gameList.get(GlobalVariables.selectedGameIndex).toString());

        event = findViewById(R.id.gd_txt_event);
        status = findViewById(R.id.gd_txt_stat);
        schedule = findViewById(R.id.gd_txt_sched);
        game = findViewById(R.id.gd_txt_game);

        team1 = findViewById(R.id.gd_txt_team1);
        team2 = findViewById(R.id.gd_txt_team2);
        iTeam1 = findViewById(R.id.gd_img_1);
        iTeam2 = findViewById(R.id.gd_img_2);

        score1 = findViewById(R.id.gd_edit_score1);
        score2 = findViewById(R.id.gd_edit_score2);

        finish = findViewById(R.id.gd_finish);
        save = findViewById(R.id.gd_save);

        event.setText(GlobalVariables.selectedEvent.name);
        status.setText(GlobalVariables.selectedGame.status);
        schedule.setText(GlobalVariables.selectedGame.schedule);
        game.setText(GlobalVariables.selectedGame.name);

        score1.setText(GlobalVariables.selectedGame.score1);
        score2.setText(GlobalVariables.selectedGame.score2);

        team1.setText(getIntent().getExtras().getString("name1"));
        team2.setText(getIntent().getExtras().getString("name2"));

        Picasso.get()
                .load(getIntent().getExtras().getString("image1"))
                .resize(100, 100)
                .centerCrop()
                .into(iTeam1);

        Picasso.get()
                .load(getIntent().getExtras().getString("image2"))
                .resize(100, 100)
                .centerCrop()
                .into(iTeam2);

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status_new = "Finished";
                updateGame();
                Intent intent=new Intent();
                setResult(2,intent);
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status_new = "Ongoing";
                status.setText(status_new);
                updateGame();
            }
        });

    }

    public void updateGame(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating Score");
        progressDialog.show();

        Log.d(TAG, "Updating Game");
        StringRequest updateGame = new StringRequest(Request.Method.POST, GlobalVariables.UPDATE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Toast.makeText(GameDetailsActivity.this, "Updated", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(GameDetailsActivity.this, "Can't connect to the server", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("game_id", GlobalVariables.selectedGame.id);
                params.put("status", status_new);
                params.put("last_updated_by_id", GlobalVariables.currentUser.id);
                params.put("contestant_team_id1", GlobalVariables.selectedGame.c1_id);
                params.put("score1", score1.getText().toString());
                params.put("contestant_team_id2", GlobalVariables.selectedGame.c2_id);
                params.put("score2", score2.getText().toString());
                return params;
            }
        };

        MySingleton.getInstance(this).addToRequestQueue(updateGame);

    }


}

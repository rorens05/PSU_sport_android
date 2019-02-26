package com.example.psusports;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.psusports.global.GlobalVariables;
import com.squareup.picasso.Picasso;

public class GameDetailsActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_details);
        init();
    }

    public void init(){
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

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}

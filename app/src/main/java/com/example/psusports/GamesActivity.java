package com.example.psusports;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.psusports.R;
import com.example.psusports.global.GlobalVariables;

public class GamesActivity extends AppCompatActivity {

    TextView eventName;
    TextView date;
    TextView venue;
    TextView organizer;
    TextView sport;
    Button addGame;
    Button viewTeams;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_games);
        init();

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
    }
}

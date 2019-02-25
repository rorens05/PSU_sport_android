package com.example.psusports;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.psusports.models.SportEvent;
import com.example.psusports.templates.SportEventAdapter;

import java.util.ArrayList;
import java.util.List;

public class EventsActivity extends AppCompatActivity {
    private static final String TAG = "EventsActivity";
    RecyclerView recyclerView;
    SportEventAdapter sportEventAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        init();
        loadEvents();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setMessage("Are you sure you want to exit?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                System.exit(1);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void init(){
        recyclerView = findViewById(R.id.recycler_view);
    }

    public void loadEvents(){
        List<SportEvent> eventList = new ArrayList<>();

        Log.d(TAG, "Loading sample data");
        //<editor-fold desc="sample data">
        SportEvent sb = new SportEvent();
        sb.name = "event1";
        sb.event_date = "date1";
        sb.organizer = "organizer1";
        sb.venue = "venue1";
        eventList.add(sb);

        sb = new SportEvent();
        sb.name = "event2";
        sb.event_date = "date2";
        sb.organizer = "organizer2";
        sb.venue = "venue2";
        eventList.add(sb);

        sb = new SportEvent();
        sb.name = "event1";
        sb.event_date = "date1";
        sb.organizer = "organizer1";
        sb.venue = "venue1";
        eventList.add(sb);
        //</editor-fold>

        Log.d(TAG, "data loaded");

        Log.d(TAG, "initializing adapter");
        sportEventAdapter = new SportEventAdapter(eventList, this);
        recyclerView.setAdapter(sportEventAdapter);
        Log.d(TAG, "adapter initialized ");


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(linearLayoutManager);

    }
}

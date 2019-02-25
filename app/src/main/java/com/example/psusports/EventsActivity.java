package com.example.psusports;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.psusports.engine.MySingleton;
import com.example.psusports.global.GlobalVariables;
import com.example.psusports.models.SportEvent;
import com.example.psusports.templates.SportEventAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EventsActivity extends AppCompatActivity {
    private static final String TAG = "EventsActivity";
    RecyclerView recyclerView;
    SportEventAdapter sportEventAdapter;
    ProgressDialog pd;
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

        //<editor-fold desc="sample data">
//        List<SportEvent> eventList = new ArrayList<>();
//        Log.d(TAG, "Loading sample data");
//        SportEvent sb = new SportEvent();
//        sb.name = "event1";
//        sb.event_date = "date1";
//        sb.organizer = "organizer1";
//        sb.venue = "venue1";
//        eventList.add(sb);
//
//        sb = new SportEvent();
//        sb.name = "event2";
//        sb.event_date = "date2";
//        sb.organizer = "organizer2";
//        sb.venue = "venue2";
//        eventList.add(sb);
//
//        sb = new SportEvent();
//        sb.name = "event1";
//        sb.event_date = "date1";
//        sb.organizer = "organizer1";
//        sb.venue = "venue1";
//        eventList.add(sb);
        //</editor-fold>

        pd = new ProgressDialog(EventsActivity.this);
        pd.setMessage("Loading Events");
        pd.show();

        StringRequest events = new StringRequest(Request.Method.GET, GlobalVariables.EVENT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response Recieved");
                try {
                    JSONObject jResponse = new JSONObject(response);
                    JSONArray data = jResponse.getJSONArray("data");
                    Log.d(TAG, "Looping through data, size: " + data.length());
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject temp = data.getJSONObject(i);
                        SportEvent event = new SportEvent();
                        event.id = temp.getString("id");
                        event.name = temp.getString("name");
                        event.event_date = temp.getString("event_date");
                        event.venue = temp.getString("venue");
                        event.organizer = temp.getString("organizer");
                        Log.d(TAG, event.toString());
                        GlobalVariables.eventList.add(event);
                    }
                    Log.d(TAG, "Events Loaded Successfully");
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
                Log.d(TAG, "Response Recieved");
                Toast.makeText(EventsActivity.this, "Failed to connect to the server", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }
        };

        MySingleton.getInstance(this).addToRequestQueue(events);

        Log.d(TAG, "eventsloaded");

    }

    public void loadRecyclerView(){
        Log.d(TAG, "initializing recyclerview adapter");
        sportEventAdapter = new SportEventAdapter(GlobalVariables.eventList, this);
        recyclerView.setAdapter(sportEventAdapter);
        Log.d(TAG, "adapter initialized ");

        Log.d(TAG, "Displaying Data");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(linearLayoutManager);

    }

}

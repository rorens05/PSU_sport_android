package com.example.psusports;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.psusports.engine.MySingleton;
import com.example.psusports.global.GlobalVariables;
import com.example.psusports.models.Team;
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddGameActivity extends AppCompatActivity {


    private static final String TAG = "AddGameActivity";
    private static final String TAG_DATETIME_FRAGMENT = "TAG_DATETIME_FRAGMENT";
    private static final String STATE_TEXTVIEW = "STATE_TEXTVIEW";
    private TextView textView;
    private SwitchDateTimeDialogFragment dateTimeFragment;

    Spinner team1;
    Spinner team2;
    ProgressDialog pd;
    EditText gameName;
    EditText description;
    Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);
        init();
        setupDateTimePicker(savedInstanceState);
        loadTeams();
    }

    public void init(){
        gameName = findViewById(R.id.add_game_name);
        description = findViewById(R.id.add_description);
        addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addGame();
            }
        });
    }

    public void setupDateTimePicker(Bundle savedInstanceState){
        textView = findViewById(R.id.add_schedule);
        if (savedInstanceState != null) {
            // Restore value from saved state
            textView.setText(savedInstanceState.getCharSequence(STATE_TEXTVIEW));
        }

        dateTimeFragment = (SwitchDateTimeDialogFragment) getSupportFragmentManager().findFragmentByTag(TAG_DATETIME_FRAGMENT);
        if(dateTimeFragment == null) {
            dateTimeFragment = SwitchDateTimeDialogFragment.newInstance(
                    getString(R.string.label_datetime_dialog),
                    getString(android.R.string.ok),
                    getString(android.R.string.cancel)
            );
        }

        final SimpleDateFormat myDateFormat = new SimpleDateFormat("yyyy-M-d HH:mm:ss", java.util.Locale.getDefault());
        textView.setText(myDateFormat.format(Calendar.getInstance().getTime()));

        // Assign unmodifiable values
        dateTimeFragment.set24HoursMode(false);
        dateTimeFragment.setHighlightAMPMSelection(false);
        dateTimeFragment.setMinimumDateTime(new GregorianCalendar(2015, Calendar.JANUARY, 1).getTime());
        dateTimeFragment.setMaximumDateTime(new GregorianCalendar(2025, Calendar.DECEMBER, 31).getTime());

        try {
            dateTimeFragment.setSimpleDateMonthAndDayFormat(new SimpleDateFormat("MMMM dd", Locale.getDefault()));
        } catch (SwitchDateTimeDialogFragment.SimpleDateMonthAndDayFormatException e) {
            Log.e(TAG, e.getMessage());
        }

        dateTimeFragment.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonWithNeutralClickListener() {
            @Override
            public void onPositiveButtonClick(Date date) {
                textView.setText(myDateFormat.format(date));
            }

            @Override
            public void onNegativeButtonClick(Date date) {
                // Do nothing
            }

            @Override
            public void onNeutralButtonClick(Date date) {
                // Optional if neutral button does'nt exists
                textView.setText("");
            }
        });

        Button buttonView = findViewById(R.id.add_sched);
        buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Re-init each time
                dateTimeFragment.startAtCalendarView();
                dateTimeFragment.setDefaultDateTime(Calendar.getInstance().getTime());
                dateTimeFragment.show(getSupportFragmentManager(), TAG_DATETIME_FRAGMENT);
            }
        });
    }

    public void setupSpinners(){
//        String[] arraySpinner = new String[] {
//                "1", "2", "3", "4", "5", "6", "7"
//        };

        String[] teamSpinner = new String[GlobalVariables.teamList.size()];

        for (int i = 0; i < teamSpinner.length; i++) {
            teamSpinner[i] = GlobalVariables.teamList.get(i).name;
        }

        team1 = findViewById(R.id.spin_team1);
        team2 = findViewById(R.id.spin_team2);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, teamSpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        team1.setAdapter(adapter);
        team2.setAdapter(adapter);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the current textView
        savedInstanceState.putCharSequence(STATE_TEXTVIEW, textView.getText());
        super.onSaveInstanceState(savedInstanceState);
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
                        team.id = temp.getString("id");
                        team.name = temp.getString("name");
                        team.sport_id = temp.getString("sport_id");
                        team.event_id = temp.getString("event_id");
                        team.logo = temp.getString("logo");
                        GlobalVariables.teamList.add(team);
                        Log.d(TAG, team.name);
                    }
                    setupSpinners();
                    Log.d(TAG, "Team Loaded Successfully");
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(TAG, "Response error, Loading failed");
                }

                pd.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddGameActivity.this, "Cannot connect to the server", Toast.LENGTH_SHORT).show();
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(teams);
    }

    public void addGame(){
        Log.d(TAG, "validating inputs");
        if (validate()) {
            Log.d(TAG, "Valid input");
            Log.d(TAG, "Collecting all params");
            final String name = gameName.getText().toString();
            final String event_id = GlobalVariables.selectedEvent.id;
            final String game_type = description.getText().toString();
            final String sport_id = GlobalVariables.selectedSport.id;

            final String schedule = textView.getText().toString();
            final String status = "Scheduled";
            final String created_by_id = GlobalVariables.currentUser.id;
            final String updated_by_id = GlobalVariables.currentUser.id;

            final String team1_id = GlobalVariables.teamList.get(team1.getSelectedItemPosition()).id;
            final String team2_id =  GlobalVariables.teamList.get(team2.getSelectedItemPosition()).id;

            //<editor-fold desc="logging params">
            Log.d(TAG,"name: " + name);
            Log.d(TAG,"event_id: " + event_id);
            Log.d(TAG,"game_type: " + game_type);
            Log.d(TAG,"sport_id: " + sport_id);

            Log.d(TAG,"schedule: " + schedule);
            Log.d(TAG,"status: " + status);
            Log.d(TAG,"created_by_id: " + created_by_id);
            Log.d(TAG,"updated_by_id: " + updated_by_id);

            Log.d(TAG,"team1_id: " + team1_id);
            Log.d(TAG,"team2_id: " + team2_id);
            //</editor-fold>

            Log.d(TAG, "All params collected");

            StringRequest addGame = new StringRequest(Request.Method.POST, GlobalVariables.NEW_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jResponse = new JSONObject(response);
                            Toast.makeText(AddGameActivity.this, jResponse.getString("message"), Toast.LENGTH_SHORT).show();
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "ERROR Received");
                        Toast.makeText(AddGameActivity.this, "Cannot Connect to the server", Toast.LENGTH_SHORT).show();
                    }
                }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("name", name);
                    params.put("event_id", event_id);
                    params.put("game_type", game_type);
                    params.put("sport_id", sport_id);

                    params.put("schedule", schedule);
                    params.put("status", status);
                    params.put("created_by_id", created_by_id);
                    params.put("updated_by_id", updated_by_id);

                    params.put("team1_id", team1_id);
                    params.put("team2_id", team2_id);
                    return params;
                }
            };
            MySingleton.getInstance(this).addToRequestQueue(addGame);

        }else{
            Log.d(TAG, "invalid input");
        }
    }

    public boolean validate(){
        if(team1.getSelectedItemPosition() == team2.getSelectedItemPosition()){
            Toast.makeText(this, "Team1 and team2 should not be the same", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (gameName.getText().toString().isEmpty()) {
            Toast.makeText(this, "Game name should not be blank", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }
}

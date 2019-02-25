package com.example.psusports.global;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.psusports.TeamsActivity;
import com.example.psusports.engine.MySingleton;
import com.example.psusports.models.Game;
import com.example.psusports.models.Sport;
import com.example.psusports.models.SportEvent;
import com.example.psusports.models.Team;
import com.example.psusports.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GlobalVariables {
    private static final String TAG = "GlobalVariables";
    public static final String SERVER_URL = "http://192.168.43.194:3000/api/v1/";
    public static final String LOGIN_URL = SERVER_URL + "access/attempt_login";
    public static final String EVENT_URL = SERVER_URL + "editor/events";
    public static final String SPORT_URL = SERVER_URL + "editor/sports";
    public static final String TEAM_URL = SERVER_URL + "editor/teams";
    public static final String GAME_URL = SERVER_URL + "editor/games";

    public static User currentUser = new User();

    public static List<SportEvent> eventList = new ArrayList<>();
    public static SportEvent selectedEvent = new SportEvent();

    public static List<Sport> sportList = new ArrayList<>();
    public static Sport selectedSport = new Sport();

    public static List<Team> teamList = new ArrayList<>();
    public static Team selectedTeam = new Team();

    public static List<Game> gameList = new ArrayList<>();
    public static Game selectedGame = new Game();

    public static ProgressDialog pd;
    public static void loadAllTeams(final Context context){
        Log.d(TAG, "loading teams");
        pd = new ProgressDialog(context);
        pd.setMessage("Loading Teams");
        String url = GlobalVariables.TEAM_URL;
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
                        try{
                            team.logo = temp.getString("logo");
                        }catch(Exception ex){
                            team.logo = "NA";
                        }
                        GlobalVariables.teamList.add(team);
                        Log.d(TAG, team.name);
                    }
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
                Toast.makeText(context.getApplicationContext(), "Cannot connect to the server", Toast.LENGTH_SHORT).show();
            }
        });
        MySingleton.getInstance(context).addToRequestQueue(teams);
    }
}

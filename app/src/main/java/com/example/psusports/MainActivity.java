package com.example.psusports;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.psusports.engine.MySingleton;
import com.example.psusports.global.GlobalVariables;
import com.example.psusports.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    EditText username;
    EditText password;
    Button login;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_main);
        GlobalVariables.setServer();
        init();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    public void init(){
        username = findViewById(R.id.editText);
        password = findViewById(R.id.editText4);
        login = findViewById(R.id.button);
    }

    public void login(){
        pd = new ProgressDialog(MainActivity.this);
        pd.setMessage("Logging in");
        pd.show();
        Log.d(TAG, "inside login");
        Log.d(TAG, GlobalVariables.SERVER_URL);
        Log.d(TAG, GlobalVariables.LOGIN_URL);
        StringRequest login = new StringRequest(Request.Method.POST, GlobalVariables.LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "response received");
                try {
                    JSONObject jresponse = new JSONObject(response);
                    String message = jresponse.getString("message");
                    String status = jresponse.getString("status");
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "message: " + message);
                    Log.d(TAG, "response no error");

                    if(status.equalsIgnoreCase("success")){
                        Log.d(TAG, "login success");
                        Log.d(TAG, "serializing user");

                        JSONObject user_data = jresponse.getJSONObject("user");
                        User user = new User();
                        user.id = user_data.getString("id");
                        user.username = user_data.getString("username");
                        GlobalVariables.currentUser = user;
                        Log.d(TAG, GlobalVariables.currentUser.toString());
                        Log.d(TAG, "serialize success");
                        pd.dismiss();
                        startActivity(new Intent(MainActivity.this, EventsActivity.class));
                        finish();
                    }

                } catch (JSONException e) {
                    Log.d(TAG, "response received error: " + e.getMessage());
                    e.printStackTrace();
                }
                pd.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "received error");
                Log.d(TAG, error.toString());
                Toast.makeText(MainActivity.this, "Unable to connect to the Server", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("username", username.getText().toString());
                params.put("password", password.getText().toString());
                return params;
            }
        };

        MySingleton.getInstance(this).addToRequestQueue(login);
    }
}

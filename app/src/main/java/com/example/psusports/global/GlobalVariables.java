package com.example.psusports.global;

import com.example.psusports.models.SportEvent;
import com.example.psusports.models.User;

import java.util.ArrayList;
import java.util.List;

public class GlobalVariables {
    public static final String SERVER_URL = "http://192.168.43.194:3000/api/v1/";
    public static final String LOGIN_URL = SERVER_URL + "access/attempt_login";
    public static final String EVENT_URL = SERVER_URL + "editor/events";

    public static User currentUser = new User();
    public static List<SportEvent> eventList = new ArrayList<>();
}

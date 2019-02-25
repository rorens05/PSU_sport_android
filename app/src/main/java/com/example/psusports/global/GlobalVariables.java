package com.example.psusports.global;

import com.example.psusports.models.User;

public class GlobalVariables {
    public static final String SERVER_URL = "http://192.168.43.194:3000/api/v1/";
    public static final String LOGIN_URL = SERVER_URL + "access/attempt_login";



    public static User currentUser;
}

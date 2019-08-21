package com.latiif.duoapi;

import com.google.gson.JsonObject;

import java.util.Map;

public interface IDuoRequest {


    void setUserCredentials(String username, String password);

    JsonObject makeRequest(String url, Map<String, String> data);

    /**
     * @return A raw json representation of the user data
     */
    JsonObject getUserData();
    Map<String, String> getCookies();
}

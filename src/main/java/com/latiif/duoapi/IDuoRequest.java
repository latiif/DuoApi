package com.latiif.duoapi;

import com.google.gson.JsonObject;

import java.util.Map;

public interface IDuoRequest {
    public JsonObject makeRequest(String url, Map<String, String> data);
    Map<String, String> getCookies();
}

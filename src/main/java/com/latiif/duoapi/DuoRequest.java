package com.latiif.duoapi;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.util.HashMap;
import java.util.Map;

public class DuoRequest implements IDuoRequest {

    private Map<String, String> cookies = new HashMap<String, String>();

    @Override
    public JsonObject makeRequest(String url, Map<String, String> data) {
        JsonParser parser = new JsonParser();
        JsonObject object;
        try {
            if (data != null) {
                Connection connection = Jsoup.connect(url)
                        .header("Accept", "*/*")
                        .method(Connection.Method.POST)
                        .requestBody(jsonifyMap(data))
                        .ignoreHttpErrors(true)
                        .ignoreContentType(true)
                        .maxBodySize(Integer.MAX_VALUE)
                        .cookies(cookies)
                        .header("Content-Type", "application/json");

                String toParse = connection.post().body().text();
                object = parser.parse(toParse).getAsJsonObject();

                Connection.Response response = connection.execute();
                cookies = response.cookies();

            } else {
                String raw = Jsoup.connect(url).ignoreContentType(true).cookies(cookies).execute().body();
                object = parser.parse(raw).getAsJsonObject();
            }
            return object;
        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Map<String, String> getCookies() {
        return cookies;
    }

    private String jsonifyMap(Map<String, String> data) {
        Gson gson = new Gson();
        return gson.toJson(data);
    }
}

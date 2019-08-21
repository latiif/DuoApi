package com.latiif.duoapi;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DuoRequest implements IDuoRequest {

    private String userUrl = "https://duolingo.com/users/%s";



    private Map<String, String> cookies = new HashMap<String, String>();

    @Override
    public void setUserCredentials(String username, String password) {
        userUrl = String.format(userUrl, username);
    }

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
    public JsonObject getUserData() {


        String raw = "{}";

        try {
            raw =
                    Jsoup
                            .connect(userUrl)
                            .ignoreContentType(true)
                            .cookies(getCookies())
                            .maxBodySize(Integer.MAX_VALUE)
                            .method(Connection.Method.GET)
                            .execute().body();

        } catch (Exception ex) {
            Logger.getLogger(DuoApi.class.getName()).log(Level.SEVERE, null, ex);
        }

        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new StringReader(raw));
        reader.setLenient(true);

        return gson.fromJson(reader, JsonObject.class);
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

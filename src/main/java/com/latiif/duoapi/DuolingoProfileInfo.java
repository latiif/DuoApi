/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.latiif.duoapi;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

/**
 *
 * @author latiif
 */
public class DuolingoProfileInfo {

    @Getter
    private String username;
    @Getter
    private String fullname;
    @Getter
    private String bio;
    @Getter
    private String location;
    @Getter
    private Integer rupees;
    @Getter
    private String created;

    @Getter
    private String imgurl;

    @Getter
    private Integer streak;

    @Getter
    private List<String> languages;

    public DuolingoProfileInfo(JsonObject userData) {
        if (userData == null) {
            return;
        }

        this.username = safeRetrieve(userData, "username", "duolearner");
        this.fullname = safeRetrieve(userData, "fullname", "Language Learner");
        this.bio = safeRetrieve(userData, "bio", " ");
        this.location = safeRetrieve(userData, "location", "Earth");

        this.rupees = Integer.valueOf(safeRetrieve(userData, "rupees", "0"));

        this.streak = Integer.valueOf(safeRetrieve(userData, "site_streak", "0"));

        this.created = safeRetrieve(userData, "created", "Sometime in the past");
        this.created = this.created.replace("\n", "");

        String url = safeRetrieve(userData, "avatar", "//s3.amazonaws.com/duoli…images/avatar/default_2");
        this.imgurl = "https:" + url + "/xlarge";

        this.languages = getLanguages(true, userData);

    }

    private List<String> getLanguages(boolean inAbbr, JsonObject userData) {
        List<String> res = new ArrayList<String>();
        if (userData != null) {
            for (JsonElement element : userData.getAsJsonArray("languages")) {
                if (element.getAsJsonObject().get("learning").getAsBoolean()) {
                    if (inAbbr) {
                        res.add(element.getAsJsonObject().get("language").getAsString());
                    } else {
                        res.add(element.getAsJsonObject().get("language_string").getAsString());
                    }
                }
            }
        }
        return res;
    }

    private String safeRetrieve(JsonObject userData, String field, String defaulValue) {
        JsonElement element = userData.get(field);

        if (element == null || element.isJsonNull()) {
            return defaulValue;
        }

        return element.getAsString();
    }

}

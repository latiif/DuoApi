package com.latiif.duoapi;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author latiif
 */
public class DuoApi {

    private String username;
    private String password;
    private String word_url_template = "%stts/%s/token/%s";

    private Boolean isLoggedIn;


    private JsonObject userData;

    private IDuoRequest duoRequest;


    /**
     * Initializes the class for further functionality sanctioned by auth
     * cookies
     *
     * @param username username used to sign in to Duolingo
     * @param password password in string format
     */
    public DuoApi(IDuoRequest duoRequest, String username, String password) {
        this.duoRequest = duoRequest;

        this.username = username;
        this.password = password;

        duoRequest.setUserCredentials(username, password);

        if (password == null || !getData()) {
            throw new IllegalArgumentException("Incorrect username or password");
        }

        isLoggedIn = true;
    }

    public DuoApi(String username, String password){
        this(new DuoRequest(),username,password);
    }

    public boolean getIsLoggedIn() {
        return isLoggedIn;
    }

    /**
     * Retrieves a list of languages being learned by the user
     *
     * @param inAbbr request abbreviation of the languages e.g. EN for English
     * @return a list of languages
     */
    public List<String> getLanguages(boolean inAbbr) {
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

    /**
     * Logs in and saves the auth info for the user
     *
     * @return status of login attempt
     */
    private boolean login() {
        String loginUrl = "https://www.duolingo.com/2017-06-30/login";
        Map<String, String> data = new HashMap<>();
        data.put("identifier", this.username);
        data.put("password", this.password);
        JsonObject res = makeRequest(loginUrl, data);
        try {
            if (res != null) {
                if (!res.entrySet().isEmpty()) {
                    return true;
                }
            }
        } catch (NullPointerException e) {
            return false;
        }

        return false;
    }

    /**
     * Switches the current course of the user. Useful for other functionality
     *
     * @param languageAbbr the abbreviation of the language to switch to
     * @throws IllegalStateException if user is not logged in
     */
    public void switchLanguage(String languageAbbr) throws IllegalStateException {

        if (!isLoggedIn) {
            throw new IllegalStateException("Action requires logging in.");
        }

        Map<String, String> data = new HashMap<>();
        data.put("learning_language", languageAbbr);
        String url = "https://www.duolingo.com/switch_language";

        makeRequest(url, data);
        getData();
    }


    /**
     * Helper function to retrieve certain key-value pairs from a json object
     *
     * @param keys   the keys to extract
     * @param object the json object
     * @return a map of key-value pairs successfully extracted from object
     */
    private Map<String, String> getDict(List<String> keys, JsonObject object) {

        Map<String, String> res = new HashMap<>();

        for (String key : keys) {
            if (object.get(key) != null) {
                try {
                    res.put(key, object.get(key).getAsString());
                } catch (Exception ignored) {
                }

            }
        }

        return res;
    }

    /**
     * Retrieves user account related settings
     *
     * @return a map of key-value pairs
     */
    public Map<String, String> getUserSettings() {
        return getDict(Arrays.asList("notify_comment", "deactivated", "is_following"), userData);
    }


    /**
     * Helper function to perform POST and GET requests to Duolingo's API
     *
     * @param url  The url to connect to
     * @param data map of key-value pairs to passed to a POST request
     * @return Raw JSON object from the requested URL
     */
    private JsonObject makeRequest(String url, Map<String, String> data) {
        return duoRequest.makeRequest(url, data);
    }

    /**
     * After a successful login, it retrieves the raw data from Duolingo about
     * the current user
     */
    private boolean getData() {
        boolean successful = login();
        if (!successful) {
            return false;
        }
        this.userData = duoRequest.getUserData();
        return true;
    }

    /**
     * Retrieves the full language name from its abbreviation
     *
     * @param abbr the abbreviation to look up e.g. "EN"
     * @return Full language name, or an empty string
     */
    public String getLanguageFromAbbreviation(String abbr) {
        for (JsonElement element : userData.getAsJsonArray("languages")) {
            if (element.getAsJsonObject().get("language").getAsString().equals(abbr)) {
                return element.getAsJsonObject().get("language_string").getAsString();
            }

        }
        return "";
    }

    /**
     * Retrieves the abbreviation from a full language name
     *
     * @param language full language name, e.g. "English"
     * @return Language abbreviation or an empty string
     */
    public String getAbbrOfLanguage(String language) {
        language = language.toLowerCase();
        for (JsonElement element : userData.getAsJsonArray("languages")) {
            if (element.getAsJsonObject().get("language_string").getAsString().toLowerCase().equals(language)) {
                return element.getAsJsonObject().get("language").getAsString();
            }

        }
        return "";
    }

    /**
     * @param language to retrieve information about
     * @return JsonObject containing relevant information about the language
     */
    public JsonObject getLanguageDetails(String language) {
        for (JsonElement element : userData.getAsJsonArray("languages")) {
            if (element.getAsJsonObject().get("language_string").getAsString().toLowerCase().equals(language.toLowerCase())) {
                return element.getAsJsonObject();
            }
        }
        return new JsonObject();
    }

    /**
     * @return A dictionary of most relevant information about the user
     */
    public Map<String, String> getUserInfo() {
        return getDict(Arrays.asList("username", "bio", "id", "num_following", "cohort",
                "language_data", "num_followers", "learning_language_string",
                "created", "contribution_points", "gplus_id", "twitter_id",
                "admin", "invites_left", "location", "fullname", "avatar",
                "ui_language"), userData);
    }

    public Map<String, String> getStreakInfo() {
        return getDict(Arrays.asList("daily_goal", "site_streak", "streak_extended_today"), userData);
    }

    /**
     *
     * @param abbr Abbreviation of a language
     * @return boolean representing whether @abbr is the current language
     */
    public boolean isCurrentLanguage(String abbr) {
        return getCurrentLanguage().toLowerCase().equals(abbr.toLowerCase());
    }

    /**
     *
     * @return Abbreviation of the current language
     */
    public String getCurrentLanguage() {
        return String.valueOf(userData.get("language_data").getAsJsonObject().keySet().toArray()[0]);
    }

    /**
     *
     * @param abbr Abbreviation of a language
     * @return A dictionary of relevant information about language @abbr
     */
    public Map<String, String> getLanguageProgress(String abbr) {
        if (!isCurrentLanguage(abbr)) {
            switchLanguage(abbr);
        }
        return getDict(Arrays.asList("streak", "language_string", "level_progress",
                "num_skills_learned", "level_percent", "level_points",
                "points_rank", "next_level", "level_left", "language",
                "points", "fluency_score", "level"), userData);
    }

    public List<Map<String, String>> getFriends() {

        JsonArray points_ranking_data = userData.get("language_data").getAsJsonObject().get(getCurrentLanguage()).getAsJsonObject().get("points_ranking_data").getAsJsonArray();

        List<Map<String, String>> res = new LinkedList<Map<String, String>>();

        for (JsonElement element : points_ranking_data) {
            res.add(getDict(Arrays.asList("username", "fullname", "avatar", "id", "rank"), element.getAsJsonObject()));
            res.get(res.size() - 1).putAll(getDict(Arrays.asList("total"), element.getAsJsonObject().get("points_data").getAsJsonObject()));
        }

        return res;
    }

    /**
     *
     * @return A list of known words in the current language
     */
    public List<String> getKnownWords() {
        List<String> res = new ArrayList<String>();

        String currLang = getCurrentLanguage();
        JsonArray skills = new JsonArray();
        try {
            skills = userData.get("language_data").getAsJsonObject().get(currLang).getAsJsonObject().get("skills").getAsJsonArray();
        } catch (Exception e) {
            System.out.println(currLang);
        }

        for (JsonElement element : skills) {
            if (!element.getAsJsonObject().get("learned").getAsBoolean()) {
                continue;
            }
            JsonArray words = element.getAsJsonObject().get("words").getAsJsonArray();

            for (JsonElement word : words) {
                res.add(word.getAsString());
            }
        }

        return res;
    }

    /**
     * Switches language to @abbr and returns list of known words in that language
     * @param abbr Abbreviation of language
     * @return List of known words in language @abbr
     */
    public List<String> getKnownWords(String abbr) {
        if (isCurrentLanguage(abbr)) {
            return getKnownWords();
        }
        switchLanguage(abbr);
        return getKnownWords();
    }

    /**
     * @return the learned skill objects sorted by the order they were learned
     */
    public List<Map<String, String>> getLearnedSkills() {
        List<Map<String, String>> res = new LinkedList<Map<String, String>>();
        JsonArray skills = userData.get("language_data").getAsJsonObject().get(getCurrentLanguage()).getAsJsonObject().get("skills").getAsJsonArray();

        for (JsonElement skill : skills) {
            if (!skill.getAsJsonObject().get("learned").getAsBoolean()) {
                continue;
            }
            res.add(getDict(Arrays.asList("language_string", "practice_recommended", "disabled", "test_count", "more_lessons", "missing_lessons", "progress_percent", "id", "description", "num_lessons", "language", "strength", "beginner", "title", "url-title", "lesson_number", "learned", "bonus", "explanation", "num_lexemes", "num_missing", "left_lessons", "short", "locked", "name", "has_explanation", "mastered"), skill.getAsJsonObject()));
        }
        return res;
    }

    public List<Map<String, String>> getLearnedSkills(String abbr) {
        if (!isCurrentLanguage(abbr)) {
            switchLanguage(abbr);
        }
        return getLearnedSkills();
    }

    public List<String> getKnownTopics(String abbr) {
        if (!isCurrentLanguage(abbr)) {
            switchLanguage(abbr);
        }
        return getKnownTopics();
    }

    public List<String> getKnownTopics() {
        List<String> res = new ArrayList<String>();
        JsonArray skills = userData.get("language_data").getAsJsonObject().get(getCurrentLanguage()).getAsJsonObject().get("skills").getAsJsonArray();
        for (JsonElement skill : skills) {
            if (!skill.getAsJsonObject().get("learned").getAsBoolean()) {
                continue;
            }
            res.add(skill.getAsJsonObject().get("title").getAsString());
        }
        return res;
    }

    public List<String> getUnknownTopics() {
        List<String> res = new ArrayList<String>();
        JsonArray skills = userData.get("language_data").getAsJsonObject().get(getCurrentLanguage()).getAsJsonObject().get("skills").getAsJsonArray();
        for (JsonElement skill : skills) {
            if (skill.getAsJsonObject().get("learned").getAsBoolean()) {
                continue;
            }
            res.add(skill.getAsJsonObject().get("title").getAsString());
        }
        return res;
    }

    public List<String> getUnknownTopics(String abbr) {
        if (!isCurrentLanguage(abbr)) {
            switchLanguage(abbr);
        }
        return getUnknownTopics();
    }

    public List<String> getGoldenTopics() {
        List<String> res = new ArrayList<String>();
        JsonArray skills = userData.get("language_data").getAsJsonObject().get(getCurrentLanguage()).getAsJsonObject().get("skills").getAsJsonArray();
        for (JsonElement skill : skills) {
            if (!skill.getAsJsonObject().get("learned").getAsBoolean()) {
                continue;
            }
            if (skill.getAsJsonObject().get("strength").getAsDouble() != 1.0) {
                continue;
            }
            res.add(skill.getAsJsonObject().get("title").getAsString());
        }
        return res;
    }

    public List<String> getGoldenTopics(String abbr) {
        if (!isCurrentLanguage(abbr)) {
            switchLanguage(abbr);
        }
        return getGoldenTopics();
    }

    public List<String> getReviewableTopics() {
        List<String> res = new ArrayList<String>();
        JsonArray skills = userData.get("language_data").getAsJsonObject().get(getCurrentLanguage()).getAsJsonObject().get("skills").getAsJsonArray();
        for (JsonElement skill : skills) {
            if (!skill.getAsJsonObject().get("learned").getAsBoolean()) {
                continue;
            }
            if (skill.getAsJsonObject().get("strength").getAsDouble() == 1.0) {
                continue;
            }
            res.add(skill.getAsJsonObject().get("title").getAsString());
        }
        return res;
    }

    public List<String> getReviewableTopics(String abbr) {
        if (!isCurrentLanguage(abbr)) {
            switchLanguage(abbr);
        }
        return getReviewableTopics();
    }

    /**
     * Get translations from
     * https://d2.duolingo.com/api/1/dictionary/hints/<source>/<target>?tokens=``<words>``
     *
     * @param words A single word or a list
     * @param from  Source language as abbreviation
     * @param to    Destination language as abbreviation
     * @return Dict with words as keys and translations as values
     */
    public Map<String, List<String>> getTranslations(List<String> words, String from, String to) {
        Map<String, List<String>> res = new HashMap<String, List<String>>();

        if (from == null) {
            from = getCurrentLanguage();
        }

        String url = "https://d2.duolingo.com/api/1/dictionary/hints/%1$s/%2$s?tokens=%3$s";
        url = String.format(url, from, to, getListFormatted(words));

        JsonObject resp = makeRequest(url, null);

        for (String word : words) {
            List<String> translations = new ArrayList<String>();
            JsonArray jsonTranslations = resp.get(word).getAsJsonArray();
            for (JsonElement element : jsonTranslations) {
                translations.add(element.getAsString());
            }

            res.put(word, translations);
        }
        return res;
    }

    /**
     * Formats a list of strings adding quotations
     *
     * @param list
     * @return A string representing quoted strings in array
     */
    private String getListFormatted(List<String> list) {
        //tmp = "https://d2.duolingo.com/api/1/dictionary/hints/de/en?tokens=[%22trinke%22,%22trinkt%22]";
        List<String> res = new ArrayList<String>();
        for (String word : list) {
            try {
                res.add("%20%22" + URLEncoder.encode(word, "UTF-8") + "%22");
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(DuoApi.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        String tmp = Arrays.toString(res.toArray());
        tmp = tmp.replace("[%20", "[");
        tmp = tmp.replace(" ", "");
        return tmp;
    }

    /**
     * Fetches url of word pronunciation
     *
     * @param word the word to look up
     * @param abbr the language abbreviation
     * @return url of audio clip for the requested word in the requested
     * language
     */
    public String getWordAudio(String word, String abbr) {
        if (!isCurrentLanguage(abbr)) {
            switchLanguage(abbr);
        }

        return getWordAudio(word);
    }

    /**
     * Fetches url of word pronunciation
     *
     * @param word the word to lookup in the current language of the user
     * @return url of audio clip for the requested word in the current language
     * @deprecated Use AudioMapper instead
     */
    public String getWordAudio(String word) {
        String res;

        String tts_root = getDict(Arrays.asList("tts_base_url"), userData).get("tts_base_url");

        res = String.format(word_url_template, tts_root, getCurrentLanguage(), word);

        return res;
    }

    /**
     * @param abbrFrom abbreviation of target language
     * @param abbrTo   abbreviation of the language of the course
     * @return dictionary of a word and a list of its translations
     */
    public Map<String, List<String>> getDictionaryOfKnownWords(String abbrFrom, String abbrTo) {

        Map<String, List<String>> res
                = getTranslations(getKnownWords(abbrTo), abbrTo, abbrFrom);

        return res;
    }

    /**
     * Extracts useful profile information from user
     *
     * @return an instance of DuolingoProfileInfo
     * @see DuolingoProfileInfo
     */
    public DuolingoProfileInfo getProfileInfo() {
        return new DuolingoProfileInfo(userData);
    }


}

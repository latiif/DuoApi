
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.istack.internal.Nullable;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.*;

/**
 * Created by latiif on 7/20/17.
 */
public class DuoApi {

	private String username;
	private String password;
	private Map<String,String> cookies = new HashMap<String, String>();

	private JsonObject userData;

	private String userUrl="http://duolingo.com/users/%s";

	public DuoApi(String username,String password){
		this.username=username;
		this.password=password;

		userUrl=String.format(userUrl,username);
		userData=getUserData();

		if (password!=null){
			login();
		}
	}


	public List<String> getLanguages(boolean inAbbr){
		List<String> res= new ArrayList<String>();

		for (JsonElement element:userData.getAsJsonArray("languages")){
			if (element.getAsJsonObject().get("learning").getAsBoolean()){
				if (inAbbr){
					res.add(element.getAsJsonObject().get("language").getAsString());
				}else {
					res.add(element.getAsJsonObject().get("language_string").getAsString());
				}
			}
		}

		return res;
	}

	public boolean login(){
		String loginUrl="https://www.duolingo.com/login";
		Map<String,String> data = new HashMap<String, String>();
		data.put("login",this.username);
		data.put("password",this.password);
		JsonObject res = makeRequest(loginUrl,data);
		try {
			if (res != null) {
				if (res.get("response").getAsString().equals("OK")) {
					return true;
				}
			}
		}
		catch (NullPointerException e){
			return false;
		}

		return false;
	}


	private void switchLanguage(String languageAbbr){
		Map<String,String> data = new HashMap<String, String>();
		data.put("learning_language",languageAbbr);
		String url="https://www.duolingo.com/switch_language";

		JsonObject res = makeRequest(url,data);
		try {
			JsonObject trackingProperties = res.getAsJsonObject("tracking_properties");
			if (trackingProperties.get("learning_language").getAsString().equals(languageAbbr)) {
				getData();
			}
		}
		catch (Exception e){
			throw new RuntimeException("Failed to switch language");
		}
	}

	public JsonObject getUserData(){
		return makeRequest(userUrl,null);
	}

	private Map<String,String> getDict(List<String> keys, JsonObject object){


		Map<String,String> res= new HashMap<String, String>();

		for (String key : keys){
			if (object.get(key)!=null){
				try {
					res.put(key,object.get(key).getAsString());
				}
				catch (Exception e){
					continue;
				}

			}
		}

		return res;
	}

	public Map<String, String> getUserSettings(){
		return getDict(Arrays.asList("notify_comment","deactivated","is_following"),userData);
	}

	/*
	No longer supported by Duolingo
	 */
	public JsonObject getActivityStream(String before){
		String url;
		if (before!=null){
			url = "https://www.duolingo.com/stream/%s?before=%s";
			url=String.format(url,this.username,before);
		}else {
			url = "https://www.duolingo.com/activity/%s";
			url=String.format(url,this.username);
		}
		JsonObject res = makeRequest(url,null);

		if (res!=null){
			return res;
		}

		return null;
	}



	private JsonObject makeRequest(String url,Map<String,String> data) {

		JsonParser parser =  new JsonParser();
		JsonObject object;
		try {
			if (data!=null) {
				Connection connection= Jsoup.connect(url).data(data).cookies(cookies).userAgent("mozilla").ignoreContentType(true);
				Connection.Response response = connection.execute();
				cookies=response.cookies();

				object=parser.parse(connection.post().body().text()).getAsJsonObject();
			}
			else {
			object = parser.parse(Jsoup.connect(url).ignoreContentType(true).cookies(cookies).execute().body()).getAsJsonObject();
			}
			return object;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}


	private void getData(){
		this.userData=makeRequest(this.userUrl,null);
	}

	public String getLanguageFromAbbreviataion(String abbr){
		for (JsonElement element:userData.getAsJsonArray("languages")){
			if (element.getAsJsonObject().get("language").getAsString().equals(abbr)){
				return element.getAsJsonObject().get("language_string").getAsString();
			}

		}
		return "";
	}

	public String getAbbrOfLanguage(String language){
		language=language.toLowerCase();
		for (JsonElement element:userData.getAsJsonArray("languages")){
			if (element.getAsJsonObject().get("language_string").getAsString().toLowerCase().equals(language)){
				return element.getAsJsonObject().get("language").getAsString();
			}

		}
		return "";
	}

	public JsonObject getLanguageDetails(String language){
		for (JsonElement element:userData.getAsJsonArray("languages")){
			if (element.getAsJsonObject().get("language_string").getAsString().toLowerCase().equals(language.toLowerCase())){
				return element.getAsJsonObject();
			}
		}
		return new JsonObject();
	}

	public Map<String, String> getUserInfo(){
		return getDict(Arrays.asList("username", "bio", "id", "num_following", "cohort",
				"language_data", "num_followers", "learning_language_string",
				"created", "contribution_points", "gplus_id", "twitter_id",
				"admin", "invites_left", "location", "fullname", "avatar",
				"ui_language"),userData);
	}

	public Map<String,String> getStreakInfo(){
		return getDict(Arrays.asList("daily_goal", "site_streak", "streak_extended_today"),userData);
	}

	public boolean isCurrentLanguage(String abbr){
		return getCurrentLanguage().toLowerCase().equals(abbr.toLowerCase());
	}

	public String getCurrentLanguage(){

		return
			String.valueOf(userData.get("language_data").getAsJsonObject().keySet().toArray()[0]);

	}

	public Map<String,String> getLanguageProgress(String abbr){
		if (!isCurrentLanguage(abbr)){
			switchLanguage(abbr);
		}
		return getDict(Arrays.asList("streak", "language_string", "level_progress",
				"num_skills_learned", "level_percent", "level_points",
				"points_rank", "next_level", "level_left", "language",
				"points", "fluency_score", "level"),userData);
	}

	public List<Map<String,String>> getFriends(){

		JsonArray points_ranking_data = userData.get("language_data").getAsJsonObject().get(getCurrentLanguage()).getAsJsonObject().get("points_ranking_data").getAsJsonArray();

		List<Map<String,String>> res = new LinkedList<Map<String, String>>();

		for (JsonElement element:points_ranking_data){
			res.add(getDict(Arrays.asList("username","fullname","avatar","id","rank"),element.getAsJsonObject()));
			res.get(res.size()-1).putAll(getDict(Arrays.asList("total"),element.getAsJsonObject().get("points_data").getAsJsonObject()));
		}

		return res;
	}


	public List<String> getKnownWords(){
		List<String> res= new ArrayList<String>();

		String currLang=getCurrentLanguage();
		JsonArray skills= new JsonArray();
		try {
			 skills = userData.get("language_data").getAsJsonObject().get(currLang).getAsJsonObject().get("skills").getAsJsonArray();
		}
		catch (Exception e){
			System.out.println(currLang);
		}

		for (JsonElement element:skills){
			if (!element.getAsJsonObject().get("learned").getAsBoolean()){
				continue;
			}
			JsonArray words = element.getAsJsonObject().get("words").getAsJsonArray();

			for (JsonElement word:words) {
				res.add(word.getAsString());
			}
		}

		return res;
	}

	public List<String> getKnownWords(String abbr){
		if (isCurrentLanguage(abbr)){
			return getKnownWords();
		}
		switchLanguage(abbr);
		return getKnownWords();
	}


	/**
	 * @return the learned skill objects sorted by the order they were learned
	 */
	public List<Map<String,String>> getLearnedSkills(){
		List<Map<String,String>> res= new LinkedList<Map<String, String>>();
		JsonArray skills = userData.get("language_data").getAsJsonObject().get(getCurrentLanguage()).getAsJsonObject().get("skills").getAsJsonArray();

		for (JsonElement skill :skills){
			if (!skill.getAsJsonObject().get("learned").getAsBoolean()){
				continue;
			}
			res.add(getDict(Arrays.asList("language_string","practice_recommended","disabled","test_count","more_lessons","missing_lessons","progress_percent","id","description","num_lessons","language","strength","beginner","title","url-title","lesson_number","learned","bonus","explanation","num_lexemes","num_missing","left_lessons","short","locked","name","has_explanation","mastered"),skill.getAsJsonObject()));
		}
		return res;
	}

	public List<Map<String,String>> getLearnedSkills(String abbr){
		if (!isCurrentLanguage(abbr)){
			switchLanguage(abbr);
		}
		return getLearnedSkills();
	}


	public List<String> getKnownTopics(String abbr){
		if (!isCurrentLanguage(abbr)){
			switchLanguage(abbr);
		}
		return getKnownTopics();
	}

	public List<String> getKnownTopics(){
		List<String> res = new ArrayList<String>();
		JsonArray skills = userData.get("language_data").getAsJsonObject().get(getCurrentLanguage()).getAsJsonObject().get("skills").getAsJsonArray();
		for(JsonElement skill:skills){
			if (!skill.getAsJsonObject().get("learned").getAsBoolean()){
				continue;
			}
			res.add(skill.getAsJsonObject().get("title").getAsString());
		}
		return res;
	}

	public List<String> getUnknownTopics(){
		List<String> res = new ArrayList<String>();
		JsonArray skills = userData.get("language_data").getAsJsonObject().get(getCurrentLanguage()).getAsJsonObject().get("skills").getAsJsonArray();
		for(JsonElement skill:skills){
			if (skill.getAsJsonObject().get("learned").getAsBoolean()){
				continue;
			}
			res.add(skill.getAsJsonObject().get("title").getAsString());
		}
		return res;
	}

	public List<String> getUnknownTopics(String abbr){
		if (!isCurrentLanguage(abbr)){
			switchLanguage(abbr);
		}
		return getUnknownTopics();
	}

	public List<String> getGoldenTopics(){
		List<String> res = new ArrayList<String>();
		JsonArray skills = userData.get("language_data").getAsJsonObject().get(getCurrentLanguage()).getAsJsonObject().get("skills").getAsJsonArray();
		for(JsonElement skill:skills){
			if (!skill.getAsJsonObject().get("learned").getAsBoolean()){
				continue;
			}
			if (skill.getAsJsonObject().get("strength").getAsDouble()!=1.0){
				continue;
			}
			res.add(skill.getAsJsonObject().get("title").getAsString());
		}
		return res;
	}

	public List<String> getGoldenTopics(String abbr){
		if (!isCurrentLanguage(abbr)){
			switchLanguage(abbr);
		}
		return getGoldenTopics();
	}

	public List<String> getReviewableTopics(){
		List<String> res = new ArrayList<String>();
		JsonArray skills = userData.get("language_data").getAsJsonObject().get(getCurrentLanguage()).getAsJsonObject().get("skills").getAsJsonArray();
		for(JsonElement skill:skills){
			if (!skill.getAsJsonObject().get("learned").getAsBoolean()){
				continue;
			}
			if (skill.getAsJsonObject().get("strength").getAsDouble()==1.0){
				continue;
			}
			res.add(skill.getAsJsonObject().get("title").getAsString());
		}
		return res;
	}

	public List<String> getReviewableTopics(String abbr){
		if (!isCurrentLanguage(abbr)){
			switchLanguage(abbr);
		}
		return getReviewableTopics();
	}
}

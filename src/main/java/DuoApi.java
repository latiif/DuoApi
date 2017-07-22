
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.istack.internal.Nullable;
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
				this.userData = res;
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
		return getDict(Arrays.asList(new String[]{"notify_comment","deactivated","is_following"}),userData);
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
				object=parser.parse(Jsoup.connect(url).data(data).userAgent("mozilla").ignoreContentType(true).post().body().text()).getAsJsonObject();
			}
			else {
			object = parser.parse(Jsoup.connect(url).ignoreContentType(true).execute().body()).getAsJsonObject();
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
		return getDict(Arrays.asList(new String[]{"username", "bio", "id", "num_following", "cohort",
				"language_data", "num_followers", "learning_language_string",
				"created", "contribution_points", "gplus_id", "twitter_id",
				"admin", "invites_left", "location", "fullname", "avatar",
				"ui_language"}),userData);
	}

	public Map<String,String> getStreakInfo(){
		return getDict(Arrays.asList(new String[]{"daily_goal", "site_streak", "streak_extended_today"}),userData);
	}
}

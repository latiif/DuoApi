
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.istack.internal.Nullable;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by latiif on 7/20/17.
 */
public class DuoApi {

	private String username;
	private String password;

	private String userUrl="http://duolingo.com/users/%s";

	public DuoApi(String username,String password){
		this.username=username;
		this.password=password;

		userUrl=String.format(userUrl,username);

		if (password!=null){
			login();
		}
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


}

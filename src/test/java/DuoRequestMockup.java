import com.google.gson.JsonObject;
import com.latiif.duoapi.IDuoRequest;

import java.util.Map;

public class DuoRequestMockup implements IDuoRequest {
    @Override
    public void setUserCredentials(String username, String password) {

    }

    @Override
    public JsonObject makeRequest(String url, Map<String, String> data) {
        if (data.containsKey("password") && data.get("password").equals("correctpassword")){
            JsonObject res = new JsonObject();
            res.addProperty("KEY","VALUE");
            return res;
        }
        return null;
    }

    @Override
    public JsonObject getUserData() {
        return null;
    }

    @Override
    public Map<String, String> getCookies() {
        return null;
    }
}

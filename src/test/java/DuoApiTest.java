import com.latiif.duoapi.DuoApi;
import com.latiif.duoapi.DuoRequest;
import org.junit.Test;

import static junit.framework.TestCase.*;

public class DuoApiTest {
    @Test
    public void authTestFail() {
        // Testing with wrong password
        try {
            DuoApi duoApi = new DuoApi(new DuoRequest(), "llusx", "wrongpassword");
        }
        catch (IllegalArgumentException e){
            // User shouldn't be logged in
            assertTrue(true);
            return;
        }
        assertTrue(false);
    }

    @Test
    public void authTestSuccess(){
        // Testing with correct password
        DuoApi duoApi = new DuoApi(new DuoRequestMockup(),"llusx", "correctpassword");
        // User should be logged in
        assertTrue(duoApi.getIsLoggedIn());
    }



}

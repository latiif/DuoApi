import com.latiif.duoapi.DuoApi;
import org.junit.Test;

import static junit.framework.TestCase.*;

public class DuoApiTest {
    @Test
    public void authTestFail() {
        // Testing with wrong password
        DuoApi duoApi = new DuoApi("llusx", "wrongpassword");
        // User shouldn't be logged in
        assertFalse(duoApi.getIsLoggedIn());
    }

    @Test
    public void authTestSuccess(){
        // Testing with correct password
        DuoApi duoApi = new DuoApi("llusx", "London12");
        // User should be logged in
        assertTrue(duoApi.getIsLoggedIn());
    }



}

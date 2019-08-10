import com.latiif.duoapi.AudioMapper;
import com.latiif.duoapi.DuoApi;

public class Main {
    public static void main(String[] args) {

        System.out.println(AudioMapper.getInstance().getAudioUrl("läget","sv"));
    }
}

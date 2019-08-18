import com.latiif.duoapi.AudioMapper;
import com.latiif.duoapi.DuoApi;


public class Main {
    public static void main(String[] args) {

        DuoApi latiif = new DuoApi("llusx","wrongpassword");

        System.out.println(AudioMapper.getInstance().getAudioUrl("organisation","sv"));
        System.out.println(AudioMapper.getInstance().getAudioUrl("organisation","en"));
        System.out.println(AudioMapper.getInstance().getAudioUrl("organisation","fr"));
        System.out.println(AudioMapper.getInstance().getAudioUrl("organisation","dn"));

    }
}

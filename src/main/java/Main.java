import com.latiif.duoapi.AudioMapper;
import com.latiif.duoapi.DuoApi;
import com.latiif.duoapi.DuoRequest;


public class Main {
    public static void main(String[] args) {

        DuoApi latiif = new DuoApi(new DuoRequest(),"llusx","wrongpassword");

        System.out.println(latiif.getCurrentLanguage());

        System.out.println(AudioMapper.getInstance().getAudioUrl("organisation","sv"));
        System.out.println(AudioMapper.getInstance().getAudioUrl("organisation","en"));
        System.out.println(AudioMapper.getInstance().getAudioUrl("organisation","fr"));
        System.out.println(AudioMapper.getInstance().getAudioUrl("organisation","dn"));

    }
}

import com.latiif.duoapi.AudioMapper;
import com.latiif.duoapi.DuoApi;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        System.out.println(AudioMapper.getInstance().getAudioUrl("organisation","sv"));
        System.out.println(AudioMapper.getInstance().getAudioUrl("organisation","en"));
        System.out.println(AudioMapper.getInstance().getAudioUrl("organisation","fr"));
        System.out.println(AudioMapper.getInstance().getAudioUrl("organisation","dn"));

    }
}

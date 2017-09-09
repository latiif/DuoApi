import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by latiif on 7/20/17.
 */
public class Main {
	public static void main(String[] args) {
		DuoApi api= new DuoApi("llusx","miamaszeijnan");


/*
		List<Map<String,String>> res=api.getLearnedSkills();
		for(Map<String,String> data:res) {
			for (String key : data.keySet()) {
				System.out.println(key + ": " + data.get(key));
			}
		}
*/

/*
		List<String> words = api.getGoldenTopics("dn");

		System.out.println("Size: "+words.size());
		for(String word:words){
			System.out.println(word);
		}
*/

		System.out.println(api.getWordAudio("france","fr"));
	}


	static private  void printTrans(Map<String,List<String>> map){
		for (String key:map.keySet()){
			System.out.println(key+": ");
			for (String entry:map.get(key)){
				System.out.println("\t"+entry);
			}
		}
	}
}

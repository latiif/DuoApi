
package com.latiif.duoapi;


import java.util.HashMap;
import java.util.Map;

public class AudioMapper {

    private static AudioMapper INSTANCE = null;

    private final Map<String, String> mapping;

    public boolean isValidLanguage(String langAbbr) {
        return mapping.containsKey(langAbbr.toLowerCase());
    }

    public String getAudioUrl(String word, String langAbbr) throws IllegalArgumentException{
        if (!isValidLanguage(langAbbr)){
            throw new IllegalArgumentException("No audio support for language "+ langAbbr);
        }
        return String.format(mapping.get(langAbbr), word);
    }

    private AudioMapper() {
        mapping = new HashMap<>();

        mapping.put("en", "http://d7mj4aqfscim2.cloudfront.net/tts/en/token/%s");
        mapping.put("sv", "http://d7mj4aqfscim2.cloudfront.net/tts/sv/token/%s");
        mapping.put("es", "https://d7mj4aqfscim2.cloudfront.net/tts/es/penelope/token/%s");
        mapping.put("dn", "https://d7mj4aqfscim2.cloudfront.net/tts/dn/lotte/token/%s");
        mapping.put("ru", "https://d7mj4aqfscim2.cloudfront.net/tts/ru/julia/token/%s");
        mapping.put("ko", "https://d7mj4aqfscim2.cloudfront.net/tts/ko/seoyeon/token/%s");
        mapping.put("de", "https://d7mj4aqfscim2.cloudfront.net/tts/de/marlene/token/%s");
        mapping.put("ja", "https://d7mj4aqfscim2.cloudfront.net/tts/ja/takumi/token/%s");
        mapping.put("fr", "https://d7mj4aqfscim2.cloudfront.net/tts/fr/lea/token/%s");
        mapping.put("zs", "https://d7mj4aqfscim2.cloudfront.net/tts/zs/zhiyu/token/%s");
        mapping.put("it", "https://d7mj4aqfscim2.cloudfront.net/tts/it/giorgio/token/%s");
        mapping.put("ro", "https://d7mj4aqfscim2.cloudfront.net/tts/ro/carmen/token/%s");
        mapping.put("pl", "https://d7mj4aqfscim2.cloudfront.net/tts/pl/jan/token/%s");
        mapping.put("da", "https://d7mj4aqfscim2.cloudfront.net/tts/da/mads/token/%s");
        mapping.put("pt", "https://d7mj4aqfscim2.cloudfront.net/tts/pt/token/%s");
    }

    public static AudioMapper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AudioMapper();
        }

        return INSTANCE;
    }
}

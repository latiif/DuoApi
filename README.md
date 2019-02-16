# com.latiif.duoapi.DuoApi
Unofficial Java API for Duolingo


#### Documentation

##### `public class com.latiif.duoapi.DuoApi`

Created by Latiif on 7/20/17.

##### `public com.latiif.duoapi.DuoApi(String username, String password)`

Initializes the class for further functionality sanctioned by auth cookies

 * **Parameters:**
   * `username` — username used to sign in to Duolingo
   * `password` — password in string format

##### `public List<String> getLanguages(boolean inAbbr)`

Retrieves a list of languages being learned by the user

 * **Parameters:** `inAbbr` — request abbreviation of the languages e.g. EN for English
 * **Returns:** a list of languages

##### `private boolean login()`

Logs in and saves the auth info for the user

 * **Returns:** status of login attempt

##### `public void switchLanguage(String languageAbbr)`

Switches the current course of the user. Useful for other functionality

 * **Parameters:** `languageAbbr` — the abbreviation of the language to switch to

##### `public JsonObject getUserData()`

 * **Returns:** A raw json representation of the user data

##### `private Map<String, String> getDict(List<String> keys, JsonObject object)`

Helper function to retrieve certain key-value pairs from a json object

 * **Parameters:**
   * `keys` — the keys to extract
   * `object` — the json object
 * **Returns:** a map of key-value pairs successfully extracted from object

##### `public Map<String, String> getUserSettings()`

Retrieves user account related settings

 * **Returns:** a map of key-value pairs

##### `private JsonObject makeRequest(String url, Map<String, String> data)`

Helper function to perform POST and GET requests to Duolingo's API

 * **Parameters:**
   * `url` — The url to connect to
   * `data` — map of key-value pairs to passed to a POST request
 * **Returns:** Raw JSON object from the requested URL

##### `private void getData()`

After a successful login, it retrieves the raw data from Duolingo about the current user

##### `public String getLanguageFromAbbreviataion(String abbr)`

Retrieves the full language name from its abbreviation

 * **Parameters:** `abbr` — the abbreviation to look up e.g. "EN"
 * **Returns:** Full language name, or an empty string

##### `public String getAbbrOfLanguage(String language)`

Retrieves the abbreviation from a full language name

 * **Parameters:** `language` — full language name, e.g. "English"
 * **Returns:** Language abbreviation or an empty string

##### `public JsonObject getLanguageDetails(String language)`

 * **Parameters:** `language` — 
 * **Returns:** 

##### `public List<Map<String, String>> getLearnedSkills()`

 * **Returns:** the learned skill objects sorted by the order they were learned

##### `public Map<String, List<String>> getTranslations(List<String> words, String from, String to)`

Get translations from https://d2.duolingo.com/api/1/dictionary/hints/<source>/<target>?tokens=``<words>``

 * **Parameters:**
   * `words` — A single word or a list
   * `from` — Source language as abbreviation
   * `to` — Destination language as abbreviation
 * **Returns:** Dict with words as keys and translations as values

##### `private String getListFormatted(List<String> list)`

Formats a list of strings adding quotations

 * **Parameters:** `list` — 
 * **Returns:** A string representing quoted strings in array

##### `public String getWordAudio(String word, String abbr)`

Fetches url of word pronunciation

 * **Parameters:**
   * `word` — the word to look up
   * `abbr` — the language abbreviation
 * **Returns:** url of audio clip for the requested word in the requested language

##### `public String getWordAudio(String word)`

Fetches url of word pronunciation

 * **Parameters:** `word` — the word to lookup in the current language of the user
 * **Returns:** url of audio clip for the requested word in the current language

package com.example.herald.preferences;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.content.SharedPreferences;

public class AppPreferences {

    private static final String PREFS_NAME = "app_preferences";
    private static final String KEY_URLS = "urls";
    private static final String KEY_PARAMETERS = "parameters";

    private final SharedPreferences prefs;
    private final Gson gson = new Gson();

    public AppPreferences(Context context) {
        this.prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        if (prefs.getAll().isEmpty()) {
            initDefaultValues();
        }
    }

    /**
     * Initialize the default values for the app preferences if none are already set
     */
    private void initDefaultValues() {
        ArrayList<String> defaultUrls = new ArrayList<>();
        defaultUrls.add("https://www.cloudflarestatus.com/api/v2/status.json");
        defaultUrls.add("https://www.githubstatus.com/api/v2/status.json");
        defaultUrls.add("https://www.vercel-status.com/api/v2/status.json");
        defaultUrls.add("https://status.openai.com/api/v2/status.json");
        defaultUrls.add("https://status.anthropic.com/api/v2/status.json");
        defaultUrls.add("https://discordstatus.com/api/v2/status.json");
        defaultUrls.add("https://status.digitalocean.com/api/v2/status.json");
        defaultUrls.add("https://status.atlassian.com/api/v2/status.json");
        defaultUrls.add("https://status.sentry.io/api/v2/status.json");
        defaultUrls.add("https://status.twilio.com/api/v2/status.json");
        defaultUrls.add("https://status.dropbox.com/api/v2/status.json");
        defaultUrls.add("https://status.newrelic.com/api/v2/status.json");
        defaultUrls.add("https://status.datadoghq.com/api/v2/status.json");
        defaultUrls.add("https://www.redditstatus.com/api/v2/status.json");
        defaultUrls.add("https://status.zoom.us/api/v2/status.json");
        defaultUrls.add("https://status.klarna.com/api/v2/status.json");
        saveUrls(defaultUrls);

        Map<String, String> defaultParams = new HashMap<>();
        saveParameters(defaultParams);
    }

    /**
     * Get the list of URLs from SharedPreference
     * @return ArrayList<String>
     */

    public ArrayList<String> getUrls() {
        String json = prefs.getString(KEY_URLS, "[]");
        Type type = new TypeToken<ArrayList<String>>(){}.getType();
        return gson.fromJson(json, type);
    }

    /**
     * Save the list of URLs to SharedPreference
     * @param urls ArrayList<String>
     */
    public void saveUrls(ArrayList<String> urls) {
        String json = gson.toJson(urls);
        prefs.edit().putString(KEY_URLS, json).apply();
    }

    /**
     * !! THIS METHOD WILL BE USED IN LATER VERSIONS !!
     * Add a URL to the list of URLs
     * @param url String
     */
    public void addUrl(String url) {
        ArrayList<String> urls = getUrls();
        urls.add(url);
        saveUrls(urls);
    }

    /**
     * !! THIS METHOD WILL BE USED IN LATER VERSIONS !!
     * Remove a URL to the list of URLs
     * @param name String
     */
    public void removeUrl(String name) {
        ArrayList<String> urls = getUrls();
        urls.remove(name);
        saveUrls(urls);
    }

    /**
     * Get the parameters from SharedPreference
     * @return Map<String, String>
     */
    public Map<String, String> getParameters() {
        String json = prefs.getString(KEY_PARAMETERS, "{}");
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        return gson.fromJson(json, type);
    }

    /**
     * Save the parameters in SharedPreference
     * @param parameters Map<String, String>
     */
    public void saveParameters(Map<String, String> parameters) {
        String json = gson.toJson(parameters);
        prefs.edit().putString(KEY_PARAMETERS, json).apply();
    }
}

package com.example.herald.preferences;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
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

        // Si vide, initialiser avec les valeurs par défaut
        if (prefs.getAll().isEmpty()) {
            initDefaultValues();
        }
    }

    private void initDefaultValues() {
        Map<String, String> defaultUrls = new HashMap<>();
        defaultUrls.put("Cloudflare", "https://www.cloudflarestatus.com/api/v2/status.json");
        defaultUrls.put("GitHub", "https://www.githubstatus.com/api/v2/status.json");
        defaultUrls.put("Vercel", "https://www.vercel-status.com/api/v2/status.json");
        defaultUrls.put("OpenAI", "https://status.openai.com/api/v2/status.json");
        defaultUrls.put("Anthropic", "https://status.anthropic.com/api/v2/status.json");
        defaultUrls.put("Stripe", "https://status.stripe.com/api/v2/status.json");
        defaultUrls.put("Slack", "https://status.slack.com/api/v2/status.json");
        defaultUrls.put("Discord", "https://discordstatus.com/api/v2/status.json");
        defaultUrls.put("DigitalOcean", "https://status.digitalocean.com/api/v2/status.json");
        defaultUrls.put("GitLab", "https://status.gitlab.com/api/v2/status.json");
        defaultUrls.put("Atlassian", "https://status.atlassian.com/api/v2/status.json");
        defaultUrls.put("Sentry", "https://status.sentry.io/api/v2/status.json");
        defaultUrls.put("Twilio", "https://status.twilio.com/api/v2/status.json");
        defaultUrls.put("Fastly", "https://www.fastlystatus.com/api/v2/status.json");
        defaultUrls.put("Dropbox", "https://status.dropbox.com/api/v2/status.json");
        defaultUrls.put("New Relic", "https://status.newrelic.com/api/v2/status.json");
        defaultUrls.put("Datadog", "https://status.datadoghq.com/api/v2/status.json");
        defaultUrls.put("Google Cloud", "https://status.cloud.google.com/index.json");
        defaultUrls.put("AWS", "https://health.aws.amazon.com/health/status/status.json");
        saveUrls(defaultUrls);

        Map<String, String> defaultParams = new HashMap<>();
        saveParameters(defaultParams);
    }

    // --- URLs ---

    public Map<String, String> getUrls() {
        String json = prefs.getString(KEY_URLS, "{}");
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        return gson.fromJson(json, type);
    }

    public void saveUrls(Map<String, String> urls) {
        String json = gson.toJson(urls);
        prefs.edit().putString(KEY_URLS, json).apply();
    }

    public void addUrl(String name, String url) {
        Map<String, String> urls = getUrls();
        urls.put(name, url);
        saveUrls(urls);
    }

    public void removeUrl(String name) {
        Map<String, String> urls = getUrls();
        urls.remove(name);
        saveUrls(urls);
    }

    // --- Parameters ---

    public Map<String, String> getParameters() {
        String json = prefs.getString(KEY_PARAMETERS, "{}");
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        return gson.fromJson(json, type);
    }

    public void saveParameters(Map<String, String> parameters) {
        String json = gson.toJson(parameters);
        prefs.edit().putString(KEY_PARAMETERS, json).apply();
    }
}

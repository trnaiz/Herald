package com.example.herald.service;

import android.content.Context;

import com.example.herald.dto.APIStatusResponse;
import com.example.herald.model.API;
import com.example.herald.preferences.AppPreferences;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class APIService extends HttpService {

    private List<API> apis = new ArrayList<>();
    private static APIService instance;

    public static APIService getInstance() {
        if(instance == null) {
            instance = new APIService();
        }
        return instance;
    }

    private APIService() {}

    public void init(Context context) {
        AppPreferences preferences = new AppPreferences(context);
        ArrayList<String> urls = preferences.getUrls();

        apis.clear();
        for (String url : urls) {
            apis.add(new API(url));
        }
    }


    /**
     * Updates the API with the new status
     *
     * @param api The API to update
     * @return A CompletableFuture for chaining methods
     */
    public CompletableFuture<Void> updateAPI(API api) {
       return this.getStatus(api.getUrl()).thenAccept(response -> {
            api.setName(response.getPage().getName());
            api.setUpdatedAt(response.getPage().getUpdatedAt());
            api.setIndicator(response.getStatus().getIndicatorEnum());
            api.setDescription(response.getStatus().getDescription());
        });
    }

    /**
     * Gets the status of the website from the given status url
     *
     * @param statusUrl The url to get the status from
     * @return A CompletableFuture containing the status
     */
    public CompletableFuture<APIStatusResponse> getStatus(String statusUrl) {
        return this.doGetJsonAsync(statusUrl, APIStatusResponse.class);
    }

    public List<API> getAPIs() {
        return apis;
    }

}


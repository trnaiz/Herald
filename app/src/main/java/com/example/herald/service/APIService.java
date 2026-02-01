package com.example.herald.service;

import com.bumptech.glide.Glide;
import com.example.herald.dto.APIStatusResponse;
import com.example.herald.model.API;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class APIService extends HttpService {

    private final List<API> apis = List.of(
            new API("https://www.githubstatus.com/api/v2/status.json"),
            new API("https://www.cloudflarestatus.com/api/v2/status.json"),
            new API("https://status.openai.com/api/v2/status.json")
    );

    private static APIService instance;

    public static APIService getInstance() {
        if(instance == null) {
            instance = new APIService();
        }
        return instance;
    }

    private APIService() {}


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
            api.setIndicator(response.getStatus().getIndicator());
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

    public void getIcon() {

    }


}


package com.example.herald;

import com.example.herald.dto.APIStatusResponse;

import java.util.concurrent.CompletableFuture;

public class APIService extends HttpService {

    private static APIService instance;

    public static APIService getInstance() {
        if(instance == null) {
            instance = new APIService();
        }
        return instance;
    }

    private APIService() {}

    /**
     * Gets the status of the website from the given status url
     *
     * @param statusUrl The url to get the status from
     * @return A CompletableFuture containing the status
     */
    public CompletableFuture<APIStatusResponse> getStatus(String statusUrl) {
        return this.doGetJsonAsync(statusUrl, APIStatusResponse.class);
    }
}


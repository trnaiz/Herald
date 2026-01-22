package com.example.herald;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpService {
    protected final ObjectMapper jsonMapper = new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

    private final ExecutorService executor;

    protected HttpService() {
        this.executor = Executors.newFixedThreadPool(4);

    }

    /**
     * Performs a GET request on the given URL.
     *
     * @param urlString The URL to request
     * @return An InputStream containing the response
     */
    protected InputStream doGet(String urlString) {
        try {
            URL url = new URL(urlString);
            URLConnection apiConnection = url.openConnection();
            return apiConnection.getInputStream();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Performs a GET request on the given URL read it as JSON and maps the response to the given class.
     *
     * @param urlString The URL to request
     * @param clazz The class to map the response to
     * @return The mapped response
     * @param <T> The type of the response
     */
    protected <T> T doGetJson(String urlString, Class<T> clazz) {
        try {
            return this.jsonMapper.readValue(this.doGet(urlString), clazz);
        } catch (StreamReadException e) {
            throw new RuntimeException(e);
        } catch (DatabindException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Performs an async GET request on the given URL read it as JSON and maps the response to the given class.
     *
     * @param urlString The URL to request
     * @param clazz The class to map the response to
     * @return A {@link CompletableFuture} containing the mapped response
     * @param <T> The type of the response
     */
    protected <T> CompletableFuture<T> doGetJsonAsync(String urlString, Class<T> clazz) {
        return CompletableFuture.supplyAsync(() -> this.doGetJson(urlString, clazz), this.executor);
    }
}

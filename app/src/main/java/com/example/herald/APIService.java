package com.example.herald;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.widget.TextView;
import android.widget.Toast;

import com.example.herald.dto.APIStatus;
import com.example.herald.dto.APIStatusResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.*;
import java.io.*;
import java.lang.Thread;
import java.time.LocalDate;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class APIService {
    private final ExecutorService executor;

    public APIService() {
        this.executor = Executors.newFixedThreadPool(4);
    }
//    public static void refreshAllAPI(MainActivity mainActivity) {
//        mainActivity.testInputButton.setText("Helloo");
//        Toast.makeText(mainActivity, "Statut des APIs actualisé", Toast.LENGTH_SHORT).show();
//    }
//
//    public static void autoRefreshAPI(MainActivity mainActivity) {
//        ObjectAnimator animation = ObjectAnimator.ofInt(mainActivity.progressIndicator, "progress", 0, 10000);
//        animation.setDuration(30000);
//
//        animation.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                APIService.refreshAllAPI(mainActivity);
//                mainActivity.progressIndicator.setProgress(0);
//                autoRefreshAPI(mainActivity);
//            }
//        });
//
//        animation.start();
//
//    }

    private APIStatusResponse getStatusAwait(String urlString) {
        try {
            URL url = new URL(urlString);
            URLConnection apiConnection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(apiConnection.getInputStream()));

            ObjectMapper jsonMapper = new ObjectMapper();
            APIStatusResponse response = jsonMapper.readValue(in.readLine(), APIStatusResponse.class);

            return response;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Future<APIStatusResponse> getStatus(String urlString) {
        return executor.submit(() -> this.getStatusAwait(urlString));
    }
}


package com.example.herald;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.net.*;
import java.io.*;
import java.lang.Thread;

public class API {
    public static void refreshAllAPI(MainActivity mainActivity) {
        if (!NetworkCheck.checkConnection(mainActivity)) {
            return;
        }
        mainActivity.testInputButton.setText("Helloo");
        Toast.makeText(mainActivity, "Statut des APIs actualisé", Toast.LENGTH_SHORT).show();
    }

    public static void autoRefreshAPI(MainActivity mainActivity) {
        ObjectAnimator animation = ObjectAnimator.ofInt(mainActivity.progressIndicator, "progress", 0, 10000);
        animation.setDuration(30000);

        animation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                API.refreshAllAPI(mainActivity);
                mainActivity.progressIndicator.setProgress(0);
                autoRefreshAPI(mainActivity);
            }
        });

        animation.start();
    }
    public void callAPI(MainActivity mainActivity, String urlString) {
        new Thread(() -> {
            try {
                URL url = new URL(urlString);
                URLConnection apiConnection = url.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(apiConnection.getInputStream()));
                String apiResponse = in.readLine();
                TextView apiResponseText = mainActivity.findViewById(R.id.testAPI);
                apiResponseText.setText(apiResponse);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}



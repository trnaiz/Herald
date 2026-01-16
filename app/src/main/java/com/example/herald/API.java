package com.example.herald;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class API {
    public static void refreshAllAPI(MainActivity mainActivity) {
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
}

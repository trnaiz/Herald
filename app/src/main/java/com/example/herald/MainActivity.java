package com.example.herald;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.herald.dto.APIStatusResponse;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    protected ImageButton refreshButton;
    protected TextView testInputButton;
    protected CircularProgressIndicator progressIndicator;
    protected TextView testAPI;
    protected ObjectAnimator animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        refreshButton = findViewById(R.id.refreshButton);
        testInputButton = findViewById(R.id.testInputButton);
        progressIndicator = findViewById(R.id.progressWheel);
        testAPI = findViewById(R.id.testAPI);


//        #### WE NEED TO MAKE IT ASYNC, RIGHT NOW IT CRASHES INSTANTLY WHEN TRYING TO CALL API

//        APIService apiService = new APIService();
//        try {
//            APIStatusResponse github = apiService.getStatus("https://www.githubstatus.com/api/v2/status.json").get();
//            testAPI.setText(github.getPage().getName());
//
//        } catch (ExecutionException e) {
//            throw new RuntimeException(e);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }

        autoRefreshAPI();

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoRefreshAPI();
            }
        });
    }
    public void refreshAllAPI() {
        if (!NetworkCheck.checkConnection(this)) {
            if (animation != null) {
                animation.removeAllListeners();
                animation.cancel();
            }
            return;
        }
        this.testInputButton.setText("Helloo");
        Toast.makeText(this, "Statut des APIs actualisé", Toast.LENGTH_SHORT).show();
    }

   public void autoRefreshAPI() {
        if (animation != null) {
            animation.removeAllListeners();
            animation.cancel();
        }

       ObjectAnimator animation = ObjectAnimator.ofInt(this.progressIndicator, "progress", 0, 10000);
       animation.setDuration(30000);

       animation.addListener(new AnimatorListenerAdapter() {
           @Override
           public void onAnimationEnd(Animator animation) {
               refreshAllAPI();
               autoRefreshAPI();
           }
       });

       animation.start();

   }

    @Override
    protected void onResume() {
        super.onResume();
        NetworkCheck.checkConnection(this);
    }
}
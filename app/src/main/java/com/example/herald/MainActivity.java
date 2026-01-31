package com.example.herald;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.herald.dto.APIStatusResponse;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.concurrent.CompletableFuture;

public class MainActivity extends AppCompatActivity {

    protected ImageButton refreshButton;
    protected TextView testInputButton;
    protected CircularProgressIndicator progressIndicator;
    protected TextView testAPI;
    protected ObjectAnimator animation;
    private AlertDialog noConnectionDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        refreshButton = findViewById(R.id.refreshButton);
        testInputButton = findViewById(R.id.testInputButton);
        progressIndicator = findViewById(R.id.progressWheel);
        testAPI = findViewById(R.id.testAPI);

        APIComponent github = new APIComponent(this, findViewById(R.id.globalLinearLayout), "https://www.githubstatus.com/api/v2/status.json");
        github.createInterface();

        APIComponent cloudflare = new APIComponent(this, findViewById(R.id.globalLinearLayout), "https://www.cloudflarestatus.com/api/v2/status.json");
        cloudflare.createInterface();

        APIComponent openai = new APIComponent(this, findViewById(R.id.globalLinearLayout), "https://status.openai.com/api/v2/status.json");
        openai.createInterface();

        createRefreshAnimation();
        startRefreshAnimation();

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APIComponent.refreshAllInterface();
            }
        });
    }
    /**
     * Refreshes all API statuses.
     * @return true if refresh was successful, false if no connection
     */
    public boolean refreshAllAPI() {
        if (!NetworkCheck.isNetworkAvailable(this)) {
            stopRefreshAnimation();
            showNoConnectionDialog();
            return false;
        }
        this.testInputButton.setText("Helloo");
        APIComponent.refreshAllInterface();
        Toast.makeText(this, "Statut des APIs actualisé", Toast.LENGTH_SHORT).show();
        startRefreshAnimation();
        return true;
    }

    private void showNoConnectionDialog() {
        if (noConnectionDialog != null && noConnectionDialog.isShowing()) {
            return;
        }

        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_no_connection, null);

        noConnectionDialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        dialogView.findViewById(R.id.btnRetry).setOnClickListener(v -> {
            if (NetworkCheck.isNetworkAvailable(this)) {
                noConnectionDialog.dismiss();
            } else {
                Toast.makeText(this, "Toujours pas de réseau...", Toast.LENGTH_SHORT).show();
            }
        });

        noConnectionDialog.show();
    }

    public void stopRefreshAnimation() {
        if (animation != null) {
            animation.removeAllListeners();
            animation.cancel();
            animation = null;
        }
    }

    public void createRefreshAnimation() {
        this.animation = ObjectAnimator.ofInt(this.progressIndicator, "progress", 0, 10000);
        this.animation.setDuration(30000);

        this.animation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                stopRefreshAnimation();
                refreshAllAPI();
            }
        });
    }

    public void startRefreshAnimation() {
        if (animation != null) {
            animation.start();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!NetworkCheck.isNetworkAvailable(this)) {
            showNoConnectionDialog();
        }
    }
}
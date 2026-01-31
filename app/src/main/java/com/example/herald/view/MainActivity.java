package com.example.herald.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.example.herald.model.API;
import com.example.herald.utils.NetworkUtils;
import com.example.herald.R;
import com.example.herald.service.APIService;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    protected ImageButton refreshButton;
    protected TextView testInputButton;
    protected CircularProgressIndicator progressIndicator;
    protected TextView testAPI;
    protected ObjectAnimator animation;
    private AlertDialog noConnectionDialog;
    private ConstraintLayout rootLayout;

    private final Map<API, APIComponent> apiComponents = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        refreshButton = findViewById(R.id.refreshButton);
        testInputButton = findViewById(R.id.testInputButton);
        progressIndicator = findViewById(R.id.progressWheel);
        testAPI = findViewById(R.id.testAPI);
        LinearLayout globalLinearLayout = findViewById(R.id.globalLinearLayout);
        rootLayout = findViewById(R.id.rootLayout);

        for(API api : APIService.getInstance().getAPIs()) {
            APIComponent apiComponent = new APIComponent(this, globalLinearLayout, api);
            APIService.getInstance().updateAPI(api).thenRun(apiComponent::createInterface);
            apiComponents.put(api, apiComponent);
        }

        createRefreshAnimation();
        startRefreshAnimation();

        refreshButton.setOnClickListener(view -> refreshAllApi());
    }

    public void refreshApi(API api) {
        APIService.getInstance().updateAPI(api).thenRun(() -> this.apiComponents.get(api).refreshInterface());
    }

    /**
     * Refreshes all API statuses.
     * @return true if refresh was successful, false if no connection
     */
    private void refreshAllApi() {
        apiComponents.keySet().forEach(this::refreshApi);
        if (!NetworkUtils.isNetworkAvailable(this)) {
            showNoConnectionDialog();
        }
        this.testInputButton.setText("Helloo");
        Snackbar.make(rootLayout, "Statut des APIs actualisé", Snackbar.LENGTH_SHORT).show();
        startRefreshAnimation();
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
            if (NetworkUtils.isNetworkAvailable(this)) {
                noConnectionDialog.dismiss();
            } else {
                Snackbar.make(rootLayout, "Toujours pas de réseau...", Snackbar.LENGTH_SHORT).show();
            }
        });

        noConnectionDialog.show();
    }

    public void createRefreshAnimation() {
        this.animation = ObjectAnimator.ofInt(this.progressIndicator, "progress", 0, 10000);
        this.animation.setDuration(10000);

        this.animation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                refreshAllApi();
            }
        });
    }

    public void startRefreshAnimation() {
        if (animation != null) {
            animation.setCurrentFraction(0f);
            animation.start();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!NetworkUtils.isNetworkAvailable(this)) {
            showNoConnectionDialog();
        }
    }
}
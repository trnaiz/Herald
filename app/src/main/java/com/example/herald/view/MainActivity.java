package com.example.herald.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;

import com.example.herald.preferences.AppPreferences;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.herald.model.API;
import com.example.herald.utils.NetworkUtils;
import com.example.herald.R;
import com.example.herald.service.APIService;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    protected ImageButton refreshButton, searchButton, settingsButton;
    protected CircularProgressIndicator progressIndicator;
    protected ObjectAnimator animation;
    private AlertDialog noConnectionDialog;
    private ConstraintLayout rootLayout;
    private LinearLayout globalLinearLayout;
    private String lastQuerySearch;
    private Comparator<API> currentComparator;
    private final Map<API, APIComponent> apiComponents = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        change_language();

        refreshButton = findViewById(R.id.refreshButton);
        searchButton = findViewById(R.id.searchButton);
        settingsButton = findViewById(R.id.settingsButton);
        progressIndicator = findViewById(R.id.progressWheel);
        globalLinearLayout = findViewById(R.id.globalLinearLayout);
        rootLayout = findViewById(R.id.rootLayout);
        update_theme();
        lastQuerySearch = "";
        currentComparator = Comparator.comparing(API::getName);

        APIService.getInstance().init(this);

        for(API api : APIService.getInstance().getAPIs()) {
            APIComponent apiComponent = new APIComponent(this, globalLinearLayout, api);
            APIService.getInstance().updateAPI(api).thenRun(apiComponent::createInterface);
            apiComponents.put(api, apiComponent);
        }

        createRefreshAnimation();
        startRefreshAnimation();

        refreshButton.setOnClickListener(view -> refreshAllApi());
        searchButton.setOnClickListener(view -> showMenuSearch());
        settingsButton.setOnClickListener(view -> showOptionActivity());
    }

    /**
     * Update the language according to the saved parameters
     * Use /AppPreference to get the saved parameters
     */
    protected void change_language(){
        String lang;
        AppPreferences appPreferences = new AppPreferences(this);

        Map<String, String> params = appPreferences.getParameters();
        lang = params.getOrDefault("lang", "");

        LocaleListCompat appLocale = LocaleListCompat.forLanguageTags(lang);
        AppCompatDelegate.setApplicationLocales(appLocale);
    }

    /**
     * Update the theme of the application according to the saved parameters
     * Use /AppPreference to get the saved parameters
     */
    protected boolean update_theme() {
        int themeMode;
        AppPreferences appPreferences = new AppPreferences(this);
        Map<String, String> params = appPreferences.getParameters();
        String savedTheme = params.getOrDefault("theme_mode", String.valueOf(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM));
        if (savedTheme == null) themeMode = -1;
        else themeMode = Integer.parseInt(savedTheme);

        if (AppCompatDelegate.getDefaultNightMode() != themeMode) {
            AppCompatDelegate.setDefaultNightMode(themeMode);
            return true;
        }
        return false;
    }

    /**
     * Opens the options activity
     */
    private void showOptionActivity() {
        Intent intent = new Intent(this, OptionsActivity.class);
        startActivity(intent);
    }
    private void showMenuSearch() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.search_menu, null);
        TextInputEditText searchInput = view.findViewById(R.id.search_input);
        searchInput.setText(lastQuerySearch);
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchAPI(s.toString());
                lastQuerySearch = s.toString();
            }
            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        });

        view.findViewById(R.id.sort_by_name).setOnClickListener(v -> updateSort(Comparator.comparing(API::getName)));
        view.findViewById(R.id.sort_by_status).setOnClickListener(v -> updateSort(Comparator.comparing(API::getIndicator)));
        dialog.setContentView(view);
        dialog.show();


    }

    private void applyFilterAndSort() {
        this.runOnUiThread(() -> {
            this.globalLinearLayout.removeAllViews();

            List<API> apis = new ArrayList<>(APIService.getInstance().getAPIs());

            if (currentComparator != null) {
                apis.sort(currentComparator);
            }

            for (API api : apis) {
                if (api.getName().toLowerCase().contains(lastQuerySearch.toLowerCase())) {
                    APIComponent component = this.apiComponents.get(api);
                    if (component != null) {
                        component.addTo(this.globalLinearLayout);
                    }
                }
            }
        });
    }
    public void updateSort(Comparator<API> comparator) {
        this.currentComparator = comparator;
        applyFilterAndSort();
    }

    private void searchAPI(String text) {
        this.lastQuerySearch = text;
        applyFilterAndSort();
    }

    public void refreshApi(API api) {
        if (!NetworkUtils.isNetworkAvailable(this)) {
            showNoConnectionDialog();
        }
        APIService.getInstance().updateAPI(api).thenRun(() -> this.apiComponents.get(api).refreshInterface());
    }

    /**
     * Refreshes all API statuses.
     */
    private void refreshAllApi() {
        if (apiComponents.isEmpty()) return;
        apiComponents.keySet().forEach(this::refreshApi);
        if (!NetworkUtils.isNetworkAvailable(this)) {
            showNoConnectionDialog();
        }
        Snackbar.make(rootLayout, getString(R.string.api_update), Snackbar.LENGTH_SHORT).show();
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
                Snackbar.make(rootLayout, getString(R.string.still_no_network), Snackbar.LENGTH_SHORT).show();
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
        if (update_theme()) return;
        change_language();
        if (!NetworkUtils.isNetworkAvailable(this)) {
            showNoConnectionDialog();
        }
    }

    @Override
    protected void onDestroy() {
        if (noConnectionDialog != null && noConnectionDialog.isShowing()) {
            noConnectionDialog.dismiss();
        }
        if (animation != null) {
            animation.cancel();
        }
        super.onDestroy();
    }
}
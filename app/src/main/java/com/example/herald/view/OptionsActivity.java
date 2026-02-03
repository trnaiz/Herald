package com.example.herald.view;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.herald.R;
import com.example.herald.preferences.AppPreferences;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.Map;

public class OptionsActivity extends AppCompatActivity {
    private ImageView leaveButton;
    private AppPreferences appPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options);

        appPreferences = new AppPreferences(this);

        leaveButton = findViewById(R.id.leave_button);
        leaveButton.setOnClickListener(view -> finish());

        setupThemeToggle();
    }

    private void setupThemeToggle() {
        MaterialButtonToggleGroup toggleGroup = findViewById(R.id.toggleGroupTheme);
        Map<String, String> params = appPreferences.getParameters();

        String savedTheme = params.getOrDefault("theme_mode",
                String.valueOf(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM));
        int themeMode = Integer.parseInt(savedTheme);

        toggleGroup.clearOnButtonCheckedListeners();

        switch (themeMode) {
            case AppCompatDelegate.MODE_NIGHT_NO:
                toggleGroup.check(R.id.btnThemeLight);
                break;
            case AppCompatDelegate.MODE_NIGHT_YES:
                toggleGroup.check(R.id.btnThemeDark);
                break;
            default:
                toggleGroup.check(R.id.btnThemeAuto);
                break;
        }


        toggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (!isChecked) return;

            int mode;
            if (checkedId == R.id.btnThemeLight) {
                mode = AppCompatDelegate.MODE_NIGHT_NO;
            } else if (checkedId == R.id.btnThemeDark) {
                mode = AppCompatDelegate.MODE_NIGHT_YES;
            } else {
                mode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
            }

            if (AppCompatDelegate.getDefaultNightMode() == mode) return;

            Map<String, String> currentParams = appPreferences.getParameters();
            currentParams.put("theme_mode", String.valueOf(mode));
            appPreferences.saveParameters(currentParams);
            AppCompatDelegate.setDefaultNightMode(mode);
        });
    }
}


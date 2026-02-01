package com.example.herald.view;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.herald.R;
import com.example.herald.model.API;
import com.example.herald.model.Indicator;

public class APIComponent {

    private final Activity activity;
    private final LinearLayout parentLayout;
    private final API api;

    // Vues du layout button_main_activity.xml
    private View rootView;
    private View colorTag;
    private ImageView siteLogo;
    private TextView siteName;
    private TextView siteDate;
    private TextView siteTime;

    public APIComponent(Activity activity, LinearLayout parentLayout, API api) {
        this.activity = activity;
        this.parentLayout = parentLayout;
        this.api = api;
    }

    /**
     * Creates the GUI by inflating button_main_activity.xml and loading icon with Glide
     */
    public void createInterface() {
        this.activity.runOnUiThread(() -> {
            // Inflate le layout button_main_activity.xml
            LayoutInflater inflater = LayoutInflater.from(activity);
            rootView = inflater.inflate(R.layout.button_main_activity, parentLayout, false);

            // Récupère les vues du layout
            colorTag = rootView.findViewById(R.id.colorTag);
            siteLogo = rootView.findViewById(R.id.siteLogo);
            siteName = rootView.findViewById(R.id.siteName);
            siteDate = rootView.findViewById(R.id.siteDate);
            siteTime = rootView.findViewById(R.id.siteTime);

            // Met à jour les données
            siteName.setText(this.api.getName());
            siteDate.setText(this.api.getUpdateDate());
            siteTime.setText(this.api.getUpdateTime());
            colorTag.setBackground(getColorIndicator(Indicator.valueOf(this.api.getIndicator().toUpperCase()), activity));

            // Charge l'icône avec Glide
            Glide.with(this.activity).load(this.api.getUrlIcon()).into(siteLogo);

            // Ajoute le layout au parent
            parentLayout.addView(rootView);
        });
    }

    /**
     * Refreshes the GUI with the new data
     */
    public void refreshInterface() {
        this.activity.runOnUiThread(() -> {
            siteName.setText(this.api.getName());
            siteDate.setText(this.api.getUpdateDate());
            siteTime.setText(this.api.getUpdateTime());
            colorTag.setBackground(getColorIndicator(Indicator.valueOf(this.api.getIndicator().toUpperCase()), activity));
        });
    }

    public Drawable getColorIndicator(Indicator indicator, Activity activity) {
        switch (indicator) {
            case NONE:
                return ContextCompat.getDrawable(activity, R.drawable.circle_green);
            case MINOR:
                return ContextCompat.getDrawable(activity, R.drawable.circle_yellow);
            case MAJOR:
                return ContextCompat.getDrawable(activity, R.drawable.circle_orange);
            case CRITICAL:
                return ContextCompat.getDrawable(activity, R.drawable.circle_red);
            case MAINTENANCE:
                return ContextCompat.getDrawable(activity, R.drawable.circle_blue);
            default:
                return ContextCompat.getDrawable(activity, R.drawable.circle_purple);
        }
    }
}


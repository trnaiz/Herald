package com.example.herald.view;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.herald.R;
import com.example.herald.model.API;

public class APIComponent {

    private final AppCompatActivity activity;
    private LinearLayout parentLayout;
    private LinearLayout apiLinearContainer;
    private TextView textName, textUpdatedAt;
    private ImageView statusCircle;
    private ImageView icon;
    private final API api;

    // Vues du layout button_main_activity.xml
    private View rootView;
    private View colorIndicator;
    private ImageView siteLogo;
    private TextView siteName;
    private TextView siteDate;
    private TextView siteTime;

    public APIComponent(AppCompatActivity activity, LinearLayout parentLayout, API api) {
        this.activity = activity;
        this.parentLayout = parentLayout;
        this.api = api;
    }

    /**
     * Creates the GUI for the current APIComponent object
     */
    public void createInterface() {
        this.activity.runOnUiThread(() -> {
            // Inflate le layout button_main_activity.xml
            LayoutInflater inflater = LayoutInflater.from(activity);
            rootView = inflater.inflate(R.layout.button_main_activity, parentLayout, false);

            // Récupère les vues du layout
            colorIndicator = rootView.findViewById(R.id.color_indicator);
            siteLogo = rootView.findViewById(R.id.siteLogo);
            siteName = rootView.findViewById(R.id.siteName);
            siteDate = rootView.findViewById(R.id.siteDate);
            siteTime = rootView.findViewById(R.id.siteTime);

            // Met à jour les données
            siteName.setText(this.api.getName());
            siteDate.setText(this.api.getUpdateDate());
            siteTime.setText(this.api.getUpdateTime());
            colorIndicator.setBackground(this.api.getIndicator().asDrawable(activity));

            // Charge l'icône avec Glide
            Glide.with(this.activity).load(this.api.getUrlIcon()).into(siteLogo);

            rootView.setOnClickListener(listener -> {
                Intent intent = new Intent(this.activity, DetailActivity.class);
                intent.putExtra("api", this.api);
                this.activity.startActivity(intent);
            });

            // Ajoute le layout au parent
            this.addTo(this.parentLayout);
        });
    }

    public void addTo(LinearLayout parentLayout) {
        parentLayout.addView(this.rootView);
    }

    /**
     * Refreshes the GUI with the new data for the current APIComponent object
     */
    public void refreshInterface() {
        this.activity.runOnUiThread(() -> {
            siteName.setText(this.api.getName());
            siteDate.setText(this.api.getUpdateDate());
            siteTime.setText(this.api.getUpdateTime());
            colorIndicator.setBackground(this.api.getIndicator().asDrawable(activity));
        });
    }
}


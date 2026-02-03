package com.example.herald.view;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.herald.R;
import com.example.herald.model.API;

/**
 * Represents a component for an API in the main activity (the line of GUI of a given API)
 */
public class APIComponent {

    private final AppCompatActivity activity;
    private LinearLayout parentLayout;
    private LinearLayout apiLinearContainer;
    private TextView textName, textUpdatedAt;
    private ImageView statusCircle;
    private ImageView icon;
    private final API api;

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
            LayoutInflater inflater = LayoutInflater.from(activity);
            rootView = inflater.inflate(R.layout.button_main_activity, parentLayout, false);
            colorIndicator = rootView.findViewById(R.id.color_indicator);
            siteLogo = rootView.findViewById(R.id.siteLogo);
            siteName = rootView.findViewById(R.id.siteName);
            siteDate = rootView.findViewById(R.id.siteDate);
            siteTime = rootView.findViewById(R.id.siteTime);

            siteName.setText(this.api.getName());
            siteDate.setText(this.api.getUpdateDate());
            siteTime.setText(this.api.getUpdateTime());
            colorIndicator.setBackground(this.api.getIndicator().asDrawable(activity));

            if (!this.activity.isDestroyed()) {
                Glide.with(this.activity).load(this.api.getUrlIcon()).into(siteLogo);
            }

            rootView.setOnClickListener(listener -> {
                Intent intent = new Intent(this.activity, DetailActivity.class);
                intent.putExtra("api", this.api);
                this.activity.startActivity(intent);
            });

            this.addTo(this.parentLayout);
        });
    }

    /**
     * Adds the current APIComponent object to the given LinearLayout
     *
     * @param parentLayout The LinearLayout to add the current APIComponent object to
     */
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


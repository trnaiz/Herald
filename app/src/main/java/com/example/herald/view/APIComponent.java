package com.example.herald.view;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.herald.R;
import com.example.herald.model.API;
import com.example.herald.model.Indicator;
import com.example.herald.service.APIService;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class APIComponent {

    private AppCompatActivity activity;
    private LinearLayout parentLayout;
    private LinearLayout apiLinearContainer;
    private TextView textName, textUpdatedAt;
    private ImageView statusCircle;
    private ImageView icon;
    private final API api;

    public APIComponent(AppCompatActivity activity, LinearLayout parentLayout, API api) {
        this.activity = activity;
        this.parentLayout = parentLayout;
        this.api = api;
    }

    /**
     * Creates a TextView with the given text and center it
     *
     * @param text The text to display
     * @return The created TextView
     */
    public TextView createCenteredTextView(String text) {
        LinearLayout.LayoutParams sameSpreadParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        );

        TextView textView = new TextView(activity);
        textView.setLayoutParams(sameSpreadParams);
        textView.setGravity(Gravity.CENTER);
        textView.setText(text);
        return textView;
    }

    /**
     * Creates the GUI for the current APIComponent object
     */
    public void createInterface() {
        this.activity.runOnUiThread(() -> {
            apiLinearContainer = new LinearLayout(activity);
            LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            apiLinearContainer.setLayoutParams(rowParams);
            apiLinearContainer.setOrientation(LinearLayout.HORIZONTAL);
            apiLinearContainer.setGravity(Gravity.CENTER);

            icon = new ImageView(activity);
            icon.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            ));
            Glide.with(this.activity).load(this.api.getUrlIcon()).into(icon);
            textName = createCenteredTextView(this.api.getName());
//                   TextView textDescription = createCenteredTextView(response.getStatus().getDescription());
            textUpdatedAt = createCenteredTextView(this.api.getUpdatedAt());
            statusCircle = new ImageView(activity);
            statusCircle.setImageDrawable(getColorIndicator(Indicator.valueOf(this.api.getIndicator().toUpperCase()), activity));
            parentLayout.addView(apiLinearContainer);
            apiLinearContainer.addView(icon);
            apiLinearContainer.addView(textName);
//                   apiLinearContainer.addView(textDescription);
            apiLinearContainer.addView(statusCircle);
            apiLinearContainer.addView(textUpdatedAt);
        });
    }

    /**
     * Refreshes the GUI with the new data for the current APIComponent object
     */
    public void refreshInterface() {
        this.activity.runOnUiThread(() -> {
            textName.setText(this.api.getName());
            textUpdatedAt.setText(this.api.getUpdatedAt().toString());
            statusCircle.setImageDrawable(getColorIndicator(Indicator.valueOf(this.api.getIndicator().toUpperCase()), activity));
            apiLinearContainer.setBackgroundColor(Color.GREEN);
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


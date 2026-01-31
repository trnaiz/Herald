package com.example.herald;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.herald.model.Indicator;
import com.example.herald.service.APIService;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class APIComponent {

    private Activity activity;
    private LinearLayout parentLayout;
    private LinearLayout apiLinearContainer;
    private TextView textName, textUpdatedAt;
    private ImageView statusCircle;
    private ImageView icon;
    private final String url;
    private final String urlIcon;
    public static final Set<APIComponent> REGISTRY = ConcurrentHashMap.newKeySet();

    public APIComponent(Activity activity, LinearLayout parentLayout, String url) {
        this.activity = activity;
        this.parentLayout = parentLayout;
        this.url = url;
        this.urlIcon = "https://icons.duckduckgo.com/ip3/" + this.url + ".ico";
        REGISTRY.add(this);
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
        try {
            APIService.getInstance().getStatus(this.url).thenAccept(response -> {
                Log.d("API", "Réponse reçue");
                this.activity.runOnUiThread(() -> {
                    apiLinearContainer = new LinearLayout(activity);
                    LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    apiLinearContainer.setLayoutParams(rowParams);
                    apiLinearContainer.setOrientation(LinearLayout.HORIZONTAL);
                    apiLinearContainer.setGravity(Gravity.CENTER);
                    textName = createCenteredTextView(response.getPage().getName());
//                   TextView textDescription = createCenteredTextView(response.getStatus().getDescription());
                    textUpdatedAt = createCenteredTextView(response.getPage().getUpdatedAt());
                    statusCircle = new ImageView(activity);
                    statusCircle.setImageDrawable(getColorIndicator(Indicator.valueOf(response.getStatus().getIndicator().toUpperCase()), activity));
                    parentLayout.addView(apiLinearContainer);
                    apiLinearContainer.addView(textName);
//                   apiLinearContainer.addView(textDescription);
                    apiLinearContainer.addView(statusCircle);
                    apiLinearContainer.addView(textUpdatedAt);
                });
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Refreshes the GUI with the new data for the current APIComponent object
     */

    public void refreshInterface() {
        try {
            APIService.getInstance().getStatus(this.url).thenAccept(response -> {
                this.activity.runOnUiThread(() -> {
                    textName.setText(response.getPage().getName());
                    textUpdatedAt.setText(response.getPage().getUpdatedAt());
                    apiLinearContainer.setBackgroundColor(Color.GREEN);
                });
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void refreshAllInterface() {
        for (APIComponent api : REGISTRY) {
            api.refreshInterface();
        }
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


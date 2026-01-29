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

public class APIComponent {

    private Activity activity;
    private LinearLayout parentLayout;
    private LinearLayout apiLinearContainer;
    private TextView textName, textUpdatedAt;
    private ImageView statusCircle;
    private final String url;

    public APIComponent(Activity activity, LinearLayout parentLayout, String url) {
        this.activity = activity;
        this.parentLayout = parentLayout;
        this.url = url;
    }

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
                    statusCircle.setImageDrawable(setColorIndicator(response.getStatus().getIndicator(), activity));
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

    public void refreshInterface(){
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

    public Drawable setColorIndicator(String indicator, Activity activity) {
        if (indicator.equals("none")) {
            return ContextCompat.getDrawable(activity, R.drawable.circle_green);
        }
        if (indicator.equals("minor")) {
            return ContextCompat.getDrawable(activity, R.drawable.circle_yellow);
        }
        if (indicator.equals("major")) {
            return ContextCompat.getDrawable(activity, R.drawable.circle_orange);
        }
        if (indicator.equals("critical")) {
            return ContextCompat.getDrawable(activity, R.drawable.circle_red);
        }
        if (indicator.equals("maintenance")) {
            return ContextCompat.getDrawable(activity, R.drawable.circle_blue);
        }
        return ContextCompat.getDrawable(activity, R.drawable.circle_purple);
    }
}


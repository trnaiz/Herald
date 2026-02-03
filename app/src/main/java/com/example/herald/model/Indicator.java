package com.example.herald.model;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.example.herald.R;

/**
 * Defines the available indicator levels and their associated resources.
 */
public enum Indicator {
    NONE(R.drawable.circle_green),
    MINOR(R.drawable.circle_yellow),
    MAJOR(R.drawable.circle_orange),
    CRITICAL(R.drawable.circle_red),
    MAINTENANCE(R.drawable.circle_blue),
    UNKNOWN(R.drawable.circle_purple),
    ;


    private final int circle;

    Indicator(int circle) {
        this.circle = circle;
    }

    public int getCircle() {
        return circle;
    }

    /**
     * Converts the indicator to a drawable
     */
    public Drawable asDrawable(Context context) {
        return ContextCompat.getDrawable(context, this.circle);
    }

}

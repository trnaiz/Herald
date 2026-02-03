package com.example.herald.dto;

import com.example.herald.model.Indicator;

/**
 * Java object used to extract the sublevel of the APIStatusResponse object named Status.
 */
public class APIStatus {

    private String indicator;
    private String description;

    public String getIndicator() {
        return indicator;
    }

    public void setIndicator(String indicator) {
        this.indicator = indicator;
    }

    /**
     * Converts the indicator string to an Indicator enum
     *
     * @return The Indicator enum
     */
    public Indicator getIndicatorEnum() {
        Indicator indicator;
        try {
            indicator = Indicator.valueOf(this.indicator.toUpperCase());
        } catch (IllegalArgumentException e) {
            indicator = Indicator.UNKNOWN;
        }
        return indicator;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

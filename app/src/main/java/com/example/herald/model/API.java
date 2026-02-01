package com.example.herald.model;

import android.net.Uri;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class API {
    private String urlIcon;
    private String name;
    private String updatedAt;
    private String url;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIndicator() {
        return indicator;
    }

    public void setIndicator(String indicator) {
        this.indicator = indicator;
    }

    private String indicator;


    public API (String url){
        this.url = url;
        this.urlIcon = String.format("https://icons.duckduckgo.com/ip3/%s.ico", Uri.parse(this.url).getHost());
    }

    public String getUrlIcon() {
        return urlIcon;
    }

    public void setUrlIcon(String urlIcon) {
        this.urlIcon = urlIcon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUpdatedAt() {
        OffsetDateTime date = OffsetDateTime.parse(this.updatedAt);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy H:mm:ss");
        this.updatedAt = date.format(formatter);
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

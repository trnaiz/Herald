package com.example.herald.model;

import android.net.Uri;

import java.io.Serializable;
import java.net.URL;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class API implements Serializable {
    private String shortUrl;
    private String urlIcon;
    private String name;
    private String updatedAt;
    private String url;
    private String description;
    private Indicator indicator;

    public API (String url){
        this.url = url;
        this.urlIcon = String.format("https://www.google.com/s2/favicons?domain=%s&sz=256", Uri.parse(this.url).getHost());
        this.shortUrl = "https://" + Uri.parse(this.url).getHost();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Indicator getIndicator() {
        return indicator;
    }

    public void setIndicator(Indicator indicator) {
        this.indicator = indicator;
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

    public String getUpdateDate() {
        OffsetDateTime date = OffsetDateTime.parse(this.updatedAt);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(formatter);
    }

    public String getUpdateTime() {
        OffsetDateTime time = OffsetDateTime.parse(this.updatedAt);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return time.format(formatter);
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

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }
}

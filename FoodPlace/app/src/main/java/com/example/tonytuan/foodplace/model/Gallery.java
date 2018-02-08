package com.example.tonytuan.foodplace.model;

/**
 * Created by Tony Tuan on 01/10/2018.
 */

public class Gallery {
    private String url;
    private String description;

    public Gallery() {
    }

    public Gallery(String url, String description) {
        this.url = url;
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

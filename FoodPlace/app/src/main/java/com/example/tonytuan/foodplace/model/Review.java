package com.example.tonytuan.foodplace.model;

import java.util.List;

/**
 * Created by Tony Tuan on 10/19/2017.
 */

public class Review {
    private User user;
    private String header;
    private String content;
    private String time;
    private double rating;
    private List<String> imageUrl;

    public Review() {
    }

    public Review(User user, String header, String content, String time, double rating, List<String> imageUrl) {
        this.user = user;
        this.header = header;
        this.content = content;
        this.time = time;
        this.rating = rating;
        this.imageUrl = imageUrl;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public List<String> getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(List<String> imageUrl) {
        this.imageUrl = imageUrl;
    }
}

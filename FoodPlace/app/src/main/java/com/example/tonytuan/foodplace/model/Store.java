package com.example.tonytuan.foodplace.model;

/**
 * Created by Tony Tuan on 10/18/2017.
 */

public class Store {
    private String id;
    private String url;
    private String name;
    private String address;
    private Integer review;
    private float rating;
    private String foodkind;

    public Store() {
    }

    public Store(String url, String name, String address, Integer review, float rating, String foodkind) {
        this.url = url;
        this.name = name;
        this.address = address;
        this.review = review;
        this.rating = rating;
        this.foodkind = foodkind;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getReview() {
        return review;
    }

    public void setReview(Integer review) {
        this.review = review;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getFood() {
        return foodkind;
    }

    public void setFood(String food) {
        this.foodkind = food;
    }
}

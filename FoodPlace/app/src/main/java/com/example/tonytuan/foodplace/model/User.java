package com.example.tonytuan.foodplace.model;

/**
 * Created by Tony Tuan on 10/18/2017.
 */

public class User {
    private String id;
    private String username;
    private String name;
    private String gender;
    private Integer friendTotal;
    private Integer reviewTotal;
    private Integer commentTotal;
    private String avatar;

    public User(String id, String username, String gender, Integer friendTotal, Integer reviewTotal, Integer commentTotal, String avatar) {
        this.id = id;
        this.username = username;
        this.gender = gender;
        this.friendTotal = friendTotal;
        this.reviewTotal = reviewTotal;
        this.commentTotal = commentTotal;
        this.avatar = avatar;
    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getFriendTotal() {
        return friendTotal;
    }

    public void setFriendTotal(Integer friendTotal) {
        this.friendTotal = friendTotal;
    }

    public Integer getReviewTotal() {
        return reviewTotal;
    }

    public void setReviewTotal(Integer reviewTotal) {
        this.reviewTotal = reviewTotal;
    }

    public Integer getCommentTotal() {
        return commentTotal;
    }

    public void setCommentTotal(Integer commentTotal) {
        this.commentTotal = commentTotal;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}

package com.example.tonytuan.foodplace.model;

/**
 * Created by Tony Tuan on 10/19/2017.
 */

public class Tip {
    private User user;
    private String content;
    private String time;

    public Tip() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
}

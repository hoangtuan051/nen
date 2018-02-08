package com.example.tonytuan.foodplace.model;

/**
 * Created by Tony Tuan on 10/20/2017.
 */

public class Item {
    private String ID;
    private String MyImageLink;

    public Item(String id, String myImageLink) {
        this.ID = id;
        this.MyImageLink = myImageLink;
    }

    public Item() {
    }

    public String getId() {
        return ID;
    }

    public void setId(String id) {
        this.ID = id;
    }

    public String getMyImageLink() {
        return MyImageLink;
    }

    public void setMyImageLink(String myImageLink) {
        this.MyImageLink = myImageLink;
    }
}

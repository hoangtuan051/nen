package com.example.tonytuan.foodplace.model;

/**
 * Created by Tony Tuan on 10/19/2017.
 */

public class ListOpenHour {
    private String name;
    private Time timeClose;
    private Time timeOpen;
    private String placeID;

    public ListOpenHour() {
    }

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Time getTimeClose() {
        return timeClose;
    }

    public void setTimeClose(Time timeClose) {
        this.timeClose = timeClose;
    }

    public Time getTimeOpen() {
        return timeOpen;
    }

    public void setTimeOpen(Time timeOpen) {
        this.timeOpen = timeOpen;
    }
}

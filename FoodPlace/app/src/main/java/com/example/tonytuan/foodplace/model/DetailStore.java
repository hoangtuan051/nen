package com.example.tonytuan.foodplace.model;

import java.util.List;
/**
 * Created by Tony Tuan on 10/19/2017.
 */

public class DetailStore {
    private String ID;
    private float AvgRating;
    private int MinPrice;
    private int MaxPrice;
    private String Name;
    private String FullAddress;
    private String Description;
    private String PlacePhoto;
    private int ReviewTotal;
    private int CheckinTotal;
    private int TipTotal;
    private int BookmarkTotal;
    private int PhotoTotal;
    private double Lat;
    private double Lng;
    private double Distance;
    private String FoodKind;
    private int Star1Total;
    private int Star2Total;
    private int Star3Total;
    private int Star4Total;
    private int Star5Total;
    private List<Item> Items;

    public DetailStore() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public float getAvgRating() {
        return AvgRating;
    }

    public void setAvgRating(float avgRating) {
        AvgRating = avgRating;
    }

    public int getMinPrice() {
        return MinPrice;
    }

    public void setMinPrice(int minPrice) {
        MinPrice = minPrice;
    }

    public int getMaxPrice() {
        return MaxPrice;
    }

    public void setMaxPrice(int maxPrice) {
        MaxPrice = maxPrice;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getFullAddress() {
        return FullAddress;
    }

    public void setFullAddress(String fullAddress) {
        FullAddress = fullAddress;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPlacePhoto() {
        return PlacePhoto;
    }

    public void setPlacePhoto(String placePhoto) {
        PlacePhoto = placePhoto;
    }

    public int getReviewTotal() {
        return ReviewTotal;
    }

    public void setReviewTotal(int reviewTotal) {
        ReviewTotal = reviewTotal;
    }

    public int getCheckinTotal() {
        return CheckinTotal;
    }

    public void setCheckinTotal(int checkinTotal) {
        CheckinTotal = checkinTotal;
    }

    public int getTipTotal() {
        return TipTotal;
    }

    public void setTipTotal(int tipTotal) {
        TipTotal = tipTotal;
    }

    public int getBookmarkTotal() {
        return BookmarkTotal;
    }

    public void setBookmarkTotal(int bookmarkTotal) {
        BookmarkTotal = bookmarkTotal;
    }

    public int getPhotoTotal() {
        return PhotoTotal;
    }

    public void setPhotoTotal(int photoTotal) {
        PhotoTotal = photoTotal;
    }

    public double getLat() {
        return Lat;
    }

    public void setLat(double lat) {
        Lat = lat;
    }

    public double getLng() {
        return Lng;
    }

    public void setLng(double lng) {
        Lng = lng;
    }

    public double getDistance() {
        return Distance;
    }

    public void setDistance(double distance) {
        Distance = distance;
    }

    public String getFoodKind() {
        return FoodKind;
    }

    public void setFoodKind(String foodKind) {
        FoodKind = foodKind;
    }

    public int getStar1Total() {
        return Star1Total;
    }

    public void setStar1Total(int star1Total) {
        Star1Total = star1Total;
    }

    public int getStar2Total() {
        return Star2Total;
    }

    public void setStar2Total(int star2Total) {
        Star2Total = star2Total;
    }

    public int getStar3Total() {
        return Star3Total;
    }

    public void setStar3Total(int star3Total) {
        Star3Total = star3Total;
    }

    public int getStar4Total() {
        return Star4Total;
    }

    public void setStar4Total(int star4Total) {
        Star4Total = star4Total;
    }

    public int getStar5Total() {
        return Star5Total;
    }

    public void setStar5Total(int star5Total) {
        Star5Total = star5Total;
    }

    public List<Item> getItems() {
        return Items;
    }

    public void setItems(List<Item> items) {
        Items = items;
    }
}

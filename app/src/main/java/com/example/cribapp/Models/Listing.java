package com.example.cribapp.Models;

import com.google.firebase.firestore.Exclude;

public class Listing {
    private String listingId;
    private String address1;
    private String address2;
    private String state;
    private int zip;
    private int price;
    private double latitude;
    private double longitude;

    public Listing(){

    }

    public Listing(String address1, String address2, String state, int zip, int price, double latitude, double longitude) {
        this.address1 = address1;
        this.address2 = address2;
        this.state = state;
        this.zip = zip;
        this.price = price;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Exclude
    public String getListingId() {
        return listingId;
    }

    public void setListingId(String listingId) {
        this.listingId = listingId;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}

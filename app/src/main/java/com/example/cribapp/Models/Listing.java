package com.example.cribapp.Models;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;

public class Listing {

    private String listingId;
    private String address1;
    private String address2;
    private String state;
    private String city;
    private int zip;
    private double squareFeet;
    private int price;
    private int beds;
    private double baths;
    private String property;
    private String description;
    private ArrayList<String> amenities;
    private double latitude;
    private double longitude;
    private String imageUrl;

    public Listing(){

    }

    public Listing(String address1, String address2, String state, String city, int zip,
                   double squareFeet, int price, int beds, double baths, String property, String description,
                   ArrayList<String> amenities, double latitude, double longitude)
    {
        this.address1 = address1;
        this.address2 = address2;
        this.state = state;
        this.city = city;
        this.zip = zip;
        this.squareFeet = squareFeet;
        this.price = price;
        this.beds = beds;
        this.baths = baths;
        this.property = property;
        this.description = description;
        this.amenities = amenities;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public double getSquareFeet() {
        return squareFeet;
    }

    public void setSquareFeet(double squareFeet) {
        this.squareFeet = squareFeet;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getBeds() {
        return beds;
    }

    public void setBeds(int beds) {
        this.beds = beds;
    }

    public double getBaths() {
        return baths;
    }

    public void setBaths(double baths) {
        this.baths = baths;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getAmenities() {
        return amenities;
    }

    public void setAmenities(ArrayList<String> amenities) {
        this.amenities = amenities;
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

    public String getImageUrl(){
        return imageUrl;
    }
}

package com.example.cribapp;

public class Listing {
    private String address1;
    private String address2;
    private String state;
    private int zip;
    private int price;

    public Listing(){

    }

    public Listing(String address1, String address2, String state, int zip, int price) {
        this.address1 = address1;
        this.address2 = address2;
        this.state = state;
        this.zip = zip;
        this.price = price;
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
}

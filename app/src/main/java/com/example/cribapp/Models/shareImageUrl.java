package com.example.cribapp.Models;

public class shareImageUrl {
    private String mImageUrl;

    public shareImageUrl() {
        //empty constructor needed
    }

    public shareImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }
}

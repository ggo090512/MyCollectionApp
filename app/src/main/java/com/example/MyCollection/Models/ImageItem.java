package com.example.MyCollection.Models;

import androidx.annotation.NonNull;

public class ImageItem {
    private String mCate;
    private String mName;
    private String mAddress;
    private String mEmail;
    private String mImage;

    public ImageItem(){}


    public ImageItem(String mCate, String mName, String mAddress, String mEmail, String mImage) {
        this.mCate = mCate;
        this.mName = mName;
        this.mAddress = mAddress;
        this.mEmail = mEmail;
        this.mImage = mImage;
    }

    public String getmCate() {
        return mCate;
    }

    public void setmCate(String mCate) {
        this.mCate = mCate;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmImage() {
        return mImage;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage;
    }
}

package com.example.MyCollection.Models;

public class Category {
    private String name;
    private String image;
    private String gmail;

    public Category(String name, String image, String gmail) {
        this.name = name;
        this.image = image;
        this.gmail = gmail;
    }

    public Category() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }
}

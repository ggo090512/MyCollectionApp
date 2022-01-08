package com.example.MyCollection.Track;

public class Track {
    private String latLng;
    private String lonLng;
    private String placeAddress;

    public String getPlaceAddress() {
        return placeAddress;
    }

    public void setPlaceAddress(String placeAddress) {
        this.placeAddress = placeAddress;
    }

    public Track(String latLng, String lonLng, String placeAddress) {
        this.latLng = latLng;
        this.lonLng = lonLng;
        this.placeAddress = placeAddress;
    }

    public Track() {
    }

    public String getLatLng() {
        return latLng;
    }

    public void setLatLng(String latLng) {
        this.latLng = latLng;
    }

    public String getLonLng() {
        return lonLng;
    }

    public void setLonLng(String lonLng) {
        this.lonLng = lonLng;
    }

}

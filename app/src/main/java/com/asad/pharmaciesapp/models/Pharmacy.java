package com.asad.pharmaciesapp.models;

public class Pharmacy {

    String id = "";
    double lat = 0.0;
    double lng = 0.0;
    String icon = "";
    String name = "";

    public Pharmacy() {
    }

    public Pharmacy(String id, double lat, double lng, String icon, String name) {
        this.id = id;
        this.lat = lat;
        this.lng = lng;
        this.icon = icon;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setName(String name) {
        this.name = name;
    }
}

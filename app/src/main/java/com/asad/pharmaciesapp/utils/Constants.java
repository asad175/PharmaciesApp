package com.asad.pharmaciesapp.utils;

public class Constants {

    public static String GoogleApiKey = "AIzaSyDHg9vKhli-IMONvEu6pwrRjMMmUK2dN-g";
    public static String SearchPlacesBaseUrl = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=pharmacies&radius=5000&key=" + GoogleApiKey;
    public static String GetDirectionsUrl = "https://maps.googleapis.com/maps/api/directions/json?sensor=false&key=" + GoogleApiKey;
}

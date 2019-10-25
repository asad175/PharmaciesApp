package com.asad.pharmaciesapp.service;

import com.asad.pharmaciesapp.models.Pharmacy;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Parser {

    public static ArrayList<Pharmacy> parsePharmaciesJsonToArrayList(String json) {
        ArrayList<Pharmacy> pharmacies = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray results = jsonObject.getJSONArray("results");
            for (int i=0;i<results.length();i++) {
                Pharmacy pharmacy = new Pharmacy();
                JSONObject pharmacyJSONObj = results.getJSONObject(i);
                pharmacy.setId(pharmacyJSONObj.optString("id"));
                pharmacy.setIcon(pharmacyJSONObj.optString("icon"));
                pharmacy.setName(pharmacyJSONObj.optString("name"));
                JSONObject locationJSONObj = pharmacyJSONObj.getJSONObject("geometry").getJSONObject("location");
                pharmacy.setLat(locationJSONObj.optDouble("lat",0));
                pharmacy.setLng(locationJSONObj.optDouble("lng",0));
                pharmacies.add(pharmacy);
            }
        }catch (Exception e){}
        return pharmacies;
    }
}

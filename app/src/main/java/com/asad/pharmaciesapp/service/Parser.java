package com.asad.pharmaciesapp.service;

import android.graphics.Color;
import android.util.Log;

import com.asad.pharmaciesapp.models.Pharmacy;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    public static PolylineOptions parseDirectionsJsonPolyline(String json) {
        JSONObject jsonObject;

        List<List<HashMap<String, String>>> routes = null;

        try {
            jsonObject = new JSONObject(json);
            // Starts parsing data
            routes = parseRoutesJSON(jsonObject);

            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < routes.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = routes.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.BLUE);

                Log.e("", "PolylineOptions Decoded");
            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                return lineOptions;
            } else {
                return null;
            }

        } catch (Exception e) {
            Log.e("", "Exception in Executing Routes : " + e.toString());
            return null;
        }

    }


    public static List<List<HashMap<String, String>>> parseRoutesJSON(JSONObject jObject) {

        List<List<HashMap<String, String>>> routes = new ArrayList<>();
        JSONArray jRoutes;
        JSONArray jLegs;
        JSONArray jSteps;

        try {

            jRoutes = jObject.getJSONArray("routes");

            /** Traversing all routes */
            for (int i = 0; i < jRoutes.length(); i++) {
                jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
                List path = new ArrayList<>();

                /** Traversing all legs */
                for (int j = 0; j < jLegs.length(); j++) {
                    jSteps = ((JSONObject) jLegs.get(j)).getJSONArray("steps");

                    /** Traversing all steps */
                    for (int k = 0; k < jSteps.length(); k++) {
                        String polyline = "";
                        polyline = (String) ((JSONObject) ((JSONObject) jSteps.get(k)).get("polyline")).get("points");
                        List<LatLng> list = decodePoly(polyline);

                        /** Traversing all points */
                        for (int l = 0; l < list.size(); l++) {
                            HashMap<String, String> hm = new HashMap<>();
                            hm.put("lat", Double.toString((list.get(l)).latitude));
                            hm.put("lng", Double.toString((list.get(l)).longitude));
                            path.add(hm);
                        }
                    }
                    routes.add(path);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
        }


        return routes;
    }

    //Method to decode polyline points
    public static List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

}

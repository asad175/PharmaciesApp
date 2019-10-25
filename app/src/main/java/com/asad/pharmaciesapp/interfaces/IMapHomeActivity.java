package com.asad.pharmaciesapp.interfaces;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public interface IMapHomeActivity {
    void setMapMyLocationEnabled();
    void moveMapToCurrentLocation(LatLng latLng, float zoom);
    void addMarkerOnMap(MarkerOptions marker);
    void removeAllMarkers();
    void drawRouteOnMap(PolylineOptions polyline);
}

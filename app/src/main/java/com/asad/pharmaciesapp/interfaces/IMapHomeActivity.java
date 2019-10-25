package com.asad.pharmaciesapp.interfaces;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public interface IMapHomeActivity {
    void setMapMyLocationEnabled();
    void moveMapToCurrentLocation(LatLng latLng);
    void addMarkerOnMap(MarkerOptions marker);
    void removeAllMarkers();
}

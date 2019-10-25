package com.asad.pharmaciesapp.views;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.asad.pharmaciesapp.R;
import com.asad.pharmaciesapp.interfaces.IMapHomeActivity;
import com.asad.pharmaciesapp.presenters.MapHomePresenter;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapHomeActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, IMapHomeActivity {

    private GoogleMap mMap;
    private MapHomePresenter presenter;

    RelativeLayout calloutLayout;
    TextView calloutTitle;
    Marker selectedMarker;
    Polyline drawnPolyline;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewInitializations();
        presenter = new MapHomePresenter(this, this);

// Initialization of Google Map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    void viewInitializations() {
        calloutLayout = findViewById(R.id.callout_layout);
        calloutTitle = findViewById(R.id.place_title);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) { // called by Google MAP SDK
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        if (presenter.canMapEnableCurrentLocation()) {
            mMap.setMyLocationEnabled(true);
        }

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        selectedMarker = marker;
        if (drawnPolyline != null) drawnPolyline.remove();
        showCallOutView();
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
        presenter.onRequestPermissionsResult(requestCode,permissions,grantResults); // pass it to presenter to take an action on it
    }

    @Override
    public void setMapMyLocationEnabled() {
        if (mMap != null)
            mMap.setMyLocationEnabled(true);
    }

    @Override
    public void addMarkerOnMap(MarkerOptions marker) {
        if (mMap == null) return;

        mMap.addMarker(marker);
    }

    @Override
    public void moveMapToCurrentLocation(LatLng latLng, float zoom) {
        if (mMap == null) return;

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,13.0f));
    }

    @Override
    public void removeAllMarkers() {
        if (mMap == null) return;
        mMap.clear();
    }

    @Override
    public void drawRouteOnMap(PolylineOptions polyline) {
        if (mMap == null) return;
        drawnPolyline = mMap.addPolyline(polyline);

    }

    public void GoClick(View v) {
        calloutLayout.setVisibility(View.GONE);
        presenter.fetchDirectionPolygon(selectedMarker);
    }

    void showCallOutView() {
        calloutTitle.setText(selectedMarker.getTitle());
        calloutLayout.setVisibility(View.VISIBLE);
    }
}

package com.asad.pharmaciesapp.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.asad.pharmaciesapp.R;
import com.asad.pharmaciesapp.interfaces.IMapHomeActivity;
import com.asad.pharmaciesapp.presenters.MapHomePresenter;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.Arrays;
import java.util.List;

public class MapHomeActivity extends AppCompatActivity implements OnMapReadyCallback, IMapHomeActivity {

    private GoogleMap mMap;
    private MapHomePresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new MapHomePresenter(this, this);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (presenter.canMapEnableCurrentLocation()) {
            mMap.setMyLocationEnabled(true);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
        presenter.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }

    @Override
    public void setMapMyLocationEnabled() {
        if (mMap != null)
            mMap.setMyLocationEnabled(true);
    }
}

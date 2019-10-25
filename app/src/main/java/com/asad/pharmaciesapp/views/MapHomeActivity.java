package com.asad.pharmaciesapp.views;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.asad.pharmaciesapp.R;
import com.asad.pharmaciesapp.interfaces.IMapHomeActivity;
import com.asad.pharmaciesapp.presenters.MapHomePresenter;
import com.asad.pharmaciesapp.utils.PermissionHelper;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

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

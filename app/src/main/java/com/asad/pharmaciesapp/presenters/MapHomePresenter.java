package com.asad.pharmaciesapp.presenters;

import android.content.Context;
import android.location.Location;

import com.asad.pharmaciesapp.interfaces.IMapHomeActivity;
import com.asad.pharmaciesapp.location.LocationHelper;
import com.asad.pharmaciesapp.utils.PermissionHelper;

public class MapHomePresenter implements LocationHelper.MyLocationListener, PermissionHelper.PermissionListener {

    Context context;
    IMapHomeActivity uiListener;
    LocationHelper locationHelper;
    PermissionHelper permissionHelper;

    public MapHomePresenter(Context context, IMapHomeActivity uiListener) {
        this.uiListener = uiListener;
        this.context = context;
        initializations();
    }

    void initializations() {
        permissionHelper = new PermissionHelper(context, this);
        locationHelper = new LocationHelper(context);

        if (permissionHelper.askForPermissions()) {
            locationHelper.startListeningUserLocation(this);
        }
    }

    public boolean canMapEnableCurrentLocation() {
        return permissionHelper.isPermissionsAllowed();
    }

    @Override
    public void onLocationChanged(Location location) {
        locationHelper.startListeningUserLocation(this);
    }

    @Override
    public void permissionsGranted() {
        uiListener.setMapMyLocationEnabled();
        locationHelper.startListeningUserLocation(this);
    }

    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
        permissionHelper.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }




}

package com.asad.pharmaciesapp.presenters;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.asad.pharmaciesapp.interfaces.IMapHomeActivity;
import com.asad.pharmaciesapp.location.LocationHelper;
import com.asad.pharmaciesapp.models.Pharmacy;
import com.asad.pharmaciesapp.service.ApiManager;
import com.asad.pharmaciesapp.helpers.PermissionHelper;
import com.asad.pharmaciesapp.service.Parser;
import com.asad.pharmaciesapp.utils.Constants;

import java.util.ArrayList;

public class MapHomePresenter implements LocationHelper.MyLocationListener, PermissionHelper.PermissionListener, ApiManager.ApiResponseListener {

    Context context;
    IMapHomeActivity uiListener;
    LocationHelper locationHelper;
    PermissionHelper permissionHelper;
    ApiManager apiManager;
    final String TAG = "MapHomePresenter";
    Location userCurrentLocation = null;

    ArrayList<Pharmacy> pharmaciesList = new ArrayList<>();

    public MapHomePresenter(Context context, IMapHomeActivity uiListener) {
        this.uiListener = uiListener;
        this.context = context;
        initializations();
    }

    void initializations() {
        apiManager = new ApiManager(context,this);
        permissionHelper = new PermissionHelper(context, this);
        locationHelper = new LocationHelper(context);

        if (permissionHelper.askForPermissions()) {
            initializationCallsAfterPermissions();
        }
    }

    void initializationCallsAfterPermissions() {
        locationHelper.startListeningUserLocation(this);
        fetchNearestPharmacies();
    }

    void fetchNearestPharmacies() {
        if (userCurrentLocation != null)
            apiManager.ExecuteVolleyRequest(TAG, Constants.SearchPlacesBaseUrl + "&location=" + userCurrentLocation.getLatitude() + "," + userCurrentLocation.getLongitude());
    }

    public boolean canMapEnableCurrentLocation() {
        return permissionHelper.isPermissionsAllowed();
    }

    @Override
    public void onLocationChanged(Location location) {
        userCurrentLocation = location;
        fetchNearestPharmacies();
    }

    @Override
    public void permissionsGranted() {
        uiListener.setMapMyLocationEnabled();
        initializationCallsAfterPermissions();
    }

    @Override
    public void onApiSuccessResponse(String tag, String response) {

        pharmaciesList = Parser.parsePharmaciesJsonToArrayList(response);
        Log.e("pharmaciescount",":" + pharmaciesList.size());

    }
    @Override
    public void onApiErrorResponse(String tag) {

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        permissionHelper.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }




}

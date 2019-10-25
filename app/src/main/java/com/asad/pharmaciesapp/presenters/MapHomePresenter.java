package com.asad.pharmaciesapp.presenters;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.asad.pharmaciesapp.R;
import com.asad.pharmaciesapp.interfaces.IMapHomeActivity;
import com.asad.pharmaciesapp.location.LocationHelper;
import com.asad.pharmaciesapp.models.Pharmacy;
import com.asad.pharmaciesapp.service.ApiManager;
import com.asad.pharmaciesapp.helpers.PermissionHelper;
import com.asad.pharmaciesapp.service.Parser;
import com.asad.pharmaciesapp.utils.Constants;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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

        if (permissionHelper.askForPermissions()) { // check for permissions first
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
    public void onLocationChanged(Location location) {  // listener called by LocationHelper
        userCurrentLocation = location;
        fetchNearestPharmacies();
    }

    @Override
    public void permissionsGranted() {  // Listener called by PermissionsHelper
        uiListener.setMapMyLocationEnabled();
        initializationCallsAfterPermissions();
    }

    @Override
    public void onApiSuccessResponse(String tag, String response) {  // Listener called by APIManager

        pharmaciesList = Parser.parsePharmaciesJsonToArrayList(response);
        // add markers on map through activity interface
        uiListener.removeAllMarkers();
        uiListener.moveMapToCurrentLocation(new LatLng(userCurrentLocation.getLatitude(),userCurrentLocation.getLongitude()));
        for (Pharmacy pharmacy: pharmaciesList) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(pharmacy.getLat(),pharmacy.getLng()));
            markerOptions.title(pharmacy.getName());
            uiListener.addMarkerOnMap(markerOptions);
        }
    }
    @Override
    public void onApiErrorResponse(String tag) { // Listener called by APIManager in case of API error
        Toast.makeText(context,context.getResources().getString(R.string.problem_occurred),Toast.LENGTH_SHORT).show();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        permissionHelper.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }




}

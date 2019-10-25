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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MapHomePresenter implements LocationHelper.MyLocationListener, PermissionHelper.PermissionListener, ApiManager.ApiResponseListener {

    Context context;
    IMapHomeActivity uiListener;
    LocationHelper locationHelper;
    PermissionHelper permissionHelper;
    ApiManager apiManager;
    final String TAG_FETCH_PHARMACIES = "Pharmacies";
    final String TAG_FETCH_DIRECTIONS = "Directions";
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
            apiManager.ExecuteVolleyRequest(TAG_FETCH_PHARMACIES, Constants.SearchPlacesBaseUrl + "&location=" + userCurrentLocation.getLatitude() + "," + userCurrentLocation.getLongitude());
    }

    public void fetchDirectionPolygon(Marker marker) {
        if (userCurrentLocation != null)
            apiManager.ExecuteVolleyRequest(TAG_FETCH_DIRECTIONS, Constants.GetDirectionsUrl + "&origin=" + userCurrentLocation.getLatitude() + "," + userCurrentLocation.getLongitude() + "&destination=" + marker.getPosition().latitude + "," + marker.getPosition().longitude);
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
        if (tag.equals(TAG_FETCH_PHARMACIES)){
            addMarkersToMap();
        } else if (tag.equals(TAG_FETCH_DIRECTIONS)) {
            PolylineOptions polyline =  Parser.parseDirectionsJsonPolyline(response);
            uiListener.drawRouteOnMap(polyline);
//            uiListener.moveMapToCurrentLocation(new LatLng(userCurrentLocation.getLatitude(),userCurrentLocation.getLongitude()),13.0f);
        }

    }
    @Override
    public void onApiErrorResponse(String tag) { // Listener called by APIManager in case of API error
        Toast.makeText(context,context.getResources().getString(R.string.problem_occurred),Toast.LENGTH_SHORT).show();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        permissionHelper.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }


    void addMarkersToMap() {
        // add markers on map through activity interface
        uiListener.removeAllMarkers();
        uiListener.moveMapToCurrentLocation(new LatLng(userCurrentLocation.getLatitude(),userCurrentLocation.getLongitude()),13.0f);
        for (Pharmacy pharmacy: pharmaciesList) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(pharmacy.getLat(),pharmacy.getLng()));
            markerOptions.title(pharmacy.getName());
            uiListener.addMarkerOnMap(markerOptions);
        }
    }



}

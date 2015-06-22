package com.example.daniel.app0;


import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.util.List;

/**
 * Created by Daniel on 21/05/2015.
 */
public class MyLocationListener implements LocationListener {

    public static final long TIEMPO_MIN = 10 * 1000 ; // 10 segundos
    public static final long DISTANCIA_MIN = 5 ; // 5 metros
    
    public static LocationManager mLocationManager;
    public static String mProvider;

    public Location mLoc;

    boolean isGPSEnabled;


    public MyLocationListener(){


        mLocationManager = (LocationManager)MainActivity.getAppContext().getSystemService(Context.LOCATION_SERVICE);
        setProvider();

        isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);


        if(mLocationManager == null){
            Intent intent = new Intent( android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            MainActivity.getAppContext().startActivity(intent);
        }
        if(isGPSEnabled) {
            // mLocationManager.requestLocationUpdates(mProvider, TIEMPO_MIN, DISTANCIA_MIN, this);
            mLoc = getLastKnownLocation();
        }
        else{
            mLoc = new Location(mProvider);
            mLoc.setLongitude(12.497170);
            mLoc.setLatitude(41.914216);
        }
    }

    public void update(){
        mLocationManager.requestLocationUpdates(mProvider, TIEMPO_MIN, DISTANCIA_MIN, this);
        if(isGPSEnabled)
            mLoc = getLastKnownLocation();

    }

    private Location getLastKnownLocation() {
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    private void setProvider(){
        Criteria mCriteria = new Criteria();
        mCriteria.setCostAllowed(false);
        mCriteria.setAltitudeRequired(false);
        mCriteria.setAccuracy(Criteria.ACCURACY_FINE);
        mProvider = mLocationManager.getBestProvider(mCriteria, true);
    }
    
    // Metodi di interfaccia LocationListener
    public void onLocationChanged(Location location) {
        mLoc = getLastKnownLocation();
    }

    public void onProviderDisabled(String mProvider) {
        Intent intent = new Intent( android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        MainActivity.getAppContext().startActivity(intent);
    }

    public void onProviderEnabled(String mProvider) {}

    public void onStatusChanged(String mProvider, int state,Bundle extras) {}



}

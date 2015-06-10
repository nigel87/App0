package com.example.daniel.app0;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Daniel on 08/06/2015.
 */
public class GestioneMappa {

    private static GoogleMap mMap;
    private Context context;

    public GestioneMappa(){
        context = MainActivity.getAppContext();
        setUpMapIfNeeded();

    }

    public void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) MainActivity.fragmentManager.findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                //setUpMap();
            }
        }
    }


   public void setUpMap() {
       MainActivity.location=MainActivity.mLocationListener.mLoc;

        //Para mostrar latitud y longitud por pantalla
        String name = "My Position";
        String coord = MainActivity.mLocationListener.mLoc.getLatitude() + ", " + MainActivity.mLocationListener.mLoc.getLongitude();

       MarkerOptions markerOptions = new MarkerOptions()
               .position(new LatLng(MainActivity.mLocationListener.mLoc.getLatitude(), MainActivity.mLocationListener.mLoc.getLongitude()))
               .title(name)
               .snippet(coord)
               .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
       addMarker(markerOptions);

        moveCameraTo(new LatLng(MainActivity.mLocationListener.mLoc.getLatitude(), MainActivity.mLocationListener.mLoc.getLongitude()), 16);
   }

    public void moveCameraTo(LatLng nPos, int nZoom){
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(nPos)      // Sets the center of the map to mi position
                .zoom(nZoom)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }


    public void addMarker(MarkerOptions newMarker){
        mMap.addMarker(newMarker);
    }

    public static GoogleMap getMap(){
        return mMap;
    }

    public static void addMakerFurtoMap(Furto newFurto){
        //Decide between the different makers
        int idDrawable;
        switch (newFurto.mTipo){
            case "Bicicletta":
                idDrawable = R.drawable.ic_bike;
                break;
            case "Cellulare":
                idDrawable = R.drawable.ic_phone;
                break;
            case "Macchina":
                idDrawable = R.drawable.ic_car;
                break;
            case "Portafoglio":
                idDrawable = R.drawable.ic_wallet;
                break;
            case "Moto":
                idDrawable = R.drawable.ic_bike;
                break;
            case "Casa":
                idDrawable = R.drawable.ic_house;
                break;
            default:
                idDrawable = R.drawable.ic_other;
                break;
        }
        Marker newMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(newFurto.mLatitude, newFurto.mLongitude))
                .title(newFurto.mTipo)
                .icon(BitmapDescriptorFactory.fromResource(idDrawable)));
        newFurto.mIdMarker = newMarker.getId();
        newMarker.isInfoWindowShown();
    }
}

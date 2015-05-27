package com.example.daniel.app0;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Daniel on 07/05/2015.
 */
public class MapCercarePosto extends ActionBarActivity implements GoogleMap.OnMapClickListener{

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private double[] mLoc;
    private ImageButton mButtonAccettare;
    private MarkerOptions mMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.map_cercare_posto);
        Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setUpMapIfNeeded();
        mButtonAccettare = (ImageButton) findViewById(R.id.accettare_loc);
        Toast.makeText(getApplicationContext(), "Tap in nel posto del furto da segnalare", Toast.LENGTH_SHORT).show();
        mMap.setOnMapClickListener(this);
        mButtonAccettare.setClickable(false);
        mLoc = new double[2];
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Recarga el mapa si se necesita
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_cercare))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }
    @Override
    public void onMapClick(LatLng point) {
        mLoc[0] = point.latitude;
        mLoc[1] = point.longitude;

        mButtonAccettare.setClickable(true);
        mMap.clear();
        mMap.addMarker(new MarkerOptions()  .position(point)
                .title("Furto"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 16));
    }

    public void locAccettata(View view){
        Intent intent = new Intent(this,AggiungereFurto.class);
        intent.putExtra("location", mLoc);
        setResult(Activity.RESULT_OK, intent);
        MapCercarePosto.this.finish();
    }
    /**
     * Localiza las coordenadas de tu dispositivo. Situa la camara y un marcador sobre la posición indicada
     * mediante la longitud y la latitud de tu dispositivo.
     */
    private void setUpMap() {
        Location mLoc = MainActivity.mLocationListener.mLoc;//MainActivity.mLocationManager.getLastKnownLocation(MainActivity.mProvider);
        //Para mostrar latitud y longitud por pantalla
        if (mLoc != null){
           mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLoc.getLatitude(), mLoc.getLongitude()), 16));
        }
    }


}

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
        /* Setting a custom info window adapter for the google map
                * */
        /*mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            // Use default InfoWindow frame
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            // Defines the contents of the InfoWindow
            @Override

            public View getInfoContents(Marker marker) {

                LayoutInflater inflater = (LayoutInflater) MainActivity.getAppContext().getSystemService(MainActivity.getAppContext().LAYOUT_INFLATER_SERVICE);

                switch (marker.getTitle()){
                    case "My Position":
                        // Getting view from the layout file info_window_layout
                        View p = inflater.inflate(R.layout.info_marker_furto, null);

                        // Getting reference to the TextView to set Name
                        TextView tvPosName = (TextView) p.findViewById(R.id.tv_name);

                        // Getting reference to the TextView to set Date
                        TextView tvPosLat = (TextView) p.findViewById(R.id.tv_date);

                        // Getting reference to the TextView to set Ora
                        TextView tvPosLon = (TextView) p.findViewById(R.id.tv_ora);

                        tvPosName.setText("My Location");
                        tvPosLat.setText("Lat:" + MainActivity.mLocationListener.mLoc.getLatitude());
                        tvPosLon.setText("Lon: " + MainActivity.mLocationListener.mLoc.getLongitude());
                        return p;

                    case "Favoriti":
                        // Getting view from the layout file info_window_layout
                        View f = inflater.inflate(R.layout.info_marker_fav, null);

                        // Getting reference to the TextView to set Name
                        TextView tvFavName = (TextView) f.findViewById(R.id.tv_name);

                        // Getting reference to the TextView to set Date
                        TextView tvFavIndirizzo = (TextView) f.findViewById(R.id.tv_indirizzo);

                        Favoriti newFav = MainActivity.getArrayFavoriti().get(0);     //TODO: Provisional

                        // Setting the Name, Date & Ora
                        tvFavName.setText(newFav.getNome());
                        tvFavIndirizzo.setText(newFav.getIndirizzo());
                        return f;

                    case "Carabinieri":
                        // Getting view from the layout file info_window_layout
                        View c = inflater.inflate(R.layout.info_marker_carabinieri, null);

                        // Getting reference to the TextView to set Name
                        TextView tvCName = (TextView) c.findViewById(R.id.tv_name);

                        // Getting reference to the TextView to set Date
                        TextView tvCIndirizzo = (TextView) c.findViewById(R.id.tv_indirizzo);

                        // Getting reference to the TextView to set Ora
                        TextView tvCPhone = (TextView) c.findViewById(R.id.tv_phone);


                        // Setting the Name, Date & Ora
                        tvCName.setText("Carabinieri");
                        tvCIndirizzo.setText(MainActivity.getPolizia().getIndirizzo());
                        tvCPhone.setText("Phone: " + MainActivity.getPolizia().mPhone);
                        return c;

                    default:
                        // Getting view from the layout file info_window_layout
                        View v = inflater.inflate(R.layout.info_marker_furto, null);

                        // Getting reference to the TextView to set Name
                        TextView tvName = (TextView) v.findViewById(R.id.tv_name);

                        // Getting reference to the TextView to set Date
                        TextView tvDate = (TextView) v.findViewById(R.id.tv_date);

                        // Getting reference to the TextView to set Ora
                        TextView tvOra = (TextView) v.findViewById(R.id.tv_ora);

                        Furto furto = MainActivity.getFurto(marker.getId());

                        // Setting the Name, Date & Ora
                        tvName.setText(furto.mTitolo);
                        tvDate.setText("Date:" + furto.mDate);
                        tvOra.setText("Fascia Oraria: " + furto.mOra);
                        return v;

                }
            }
        });
        /*
        *   Listener Click in Info Windows di Markers
        */
        /*mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker marker) {

                switch(marker.getTitle()){
                    case "My Position":
                        Toast.makeText(MainActivity.getAppContext(), "Posizione attuale", Toast.LENGTH_SHORT).show();
                        break;
                    case "Favoriti":
                        break;
                    case "Carabinieri":
                        break;
                    default:
                        for (int i = 0; i < MainActivity.arrayFurti.size(); i++) {
                            if (marker.getId().matches(MainActivity.arrayFurti.get(i).mIdMarker)) {
                                Furto furto = MainActivity.getFurto(marker.getId());
                                Intent intent = new Intent(MainActivity.getAppContext(), InfoFurtoActivity.class);
                                intent.putExtra("idFurto", furto.mId);
                                ((ActionBarActivity) MainActivity.getAppContext()).startActivityForResult(intent, MainActivity.INFO_FURTO_STATE);
                            }
                        }
                        break;
                }
            }
        });*/
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
                .bearing(0)                // Sets the orientation of the camera to east
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

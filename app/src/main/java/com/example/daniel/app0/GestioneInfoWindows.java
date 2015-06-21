package com.example.daniel.app0;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Daniel on 08/06/2015.
 */
public class GestioneInfoWindows implements GoogleMap.InfoWindowAdapter {

    LayoutInflater lInflater;

    public GestioneInfoWindows(LayoutInflater lf) {
        lInflater = lf;
    }

    // Use default InfoWindow frame
    @Override
    public View getInfoWindow(Marker arg0) {
        return null;
    }

    // Defines the contents of the InfoWindow
    @Override

    public View getInfoContents(Marker marker) {

        switch (marker.getTitle()) {
            case "My Position":

                // Getting view from the layout file info_window_layout
                View p = lInflater.inflate(R.layout.info_marker_furto, null);

                // Getting reference to the TextView to set Name
                TextView tvPosName = (TextView) p.findViewById(R.id.tv_name);

                // Getting reference to the TextView to set Date
                TextView tvPosLat = (TextView) p.findViewById(R.id.tv_date);

                // Getting reference to the TextView to set Ora
                TextView tvPosLon = (TextView) p.findViewById(R.id.tv_ora);

                tvPosName.setText("My Location");
                return p;

            case "Favoriti":
                // Getting view from the layout file info_window_layout
                View f = lInflater.inflate(R.layout.info_marker_fav, null);

                // Getting reference to the TextView to set Name
                TextView tvFavName = (TextView) f.findViewById(R.id.tv_name);

                // Getting reference to the TextView to set Date
                TextView tvFavIndirizzo = (TextView) f.findViewById(R.id.tv_indirizzo);

                Preferiti newFav = MainActivity.getArrayPreferiti().get(0);     //TODO: Provisional

                // Setting the Name, Date & Ora
                //tvFavName.setText(newFav.getNome());
                //tvFavIndirizzo.setText(newFav.getIndirizzo());
                return f;

            case "Carabinieri":
                // Getting view from the layout file info_window_layout
                View c = lInflater.inflate(R.layout.info_marker_carabinieri, null);

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
                View v = lInflater.inflate(R.layout.info_marker_furto, null);

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

};


class GestioneInfoWindowsClick implements GoogleMap.OnInfoWindowClickListener {

    Activity actualActivity;

    public GestioneInfoWindowsClick(Activity aActivity) {
        actualActivity = aActivity;
    }

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
                        actualActivity.startActivityForResult(intent, MainActivity.INFO_FURTO_STATE);
                    }
                }
                break;
        }
    }
}

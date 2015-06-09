package com.example.daniel.app0;

import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by nigel on 09/06/15.
 * Gestisce il click sui vari opzioni nella bara sinsitra  (DrawerLayout)
 */
public class GestisciBarraSinistra implements AdapterView.OnItemClickListener {
    DrawerLayout drawerLayout;
    public GestisciBarraSinistra(DrawerLayout dl)
    {
        drawerLayout=dl;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0://home
                MainActivity.getMap().clear();
                MainActivity.restartMap2();
                drawerLayout.closeDrawer(R.id.drawer_layout);
                break;
            case 1://Tipo di furto
                // aggiungereFurto( view); //come test apre la pagina di un nuovo furto
                break;
            case 2: //Carabinieri
                MainActivity.getMap().clear();
                Polizia polizia=MainActivity.getPolizia();
                if (polizia!= null) {
                    //Para mostrar latitud y longitud por pantalla
                    String name = MainActivity.getPolizia().mIndirizzo;
                    MainActivity.getMap().addMarker(new MarkerOptions().position(new LatLng(polizia.getmLatitude(), polizia.getmLongitude()))
                            .title(name)
                            .snippet(polizia.mPhone)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_carabbinieri)));
                    MainActivity.getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(polizia.getmLatitude(), polizia.getmLongitude()), 16));

                    drawerLayout.closeDrawer(R.id.drawer_layout);
                }
                break;
            case 3: //Notifiche
                break;
            case 4: //Mie segnalazioni
                break;
            case 5: //Informazioni
                break;
        }
    }
}


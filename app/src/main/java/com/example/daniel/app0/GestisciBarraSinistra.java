package com.example.daniel.app0;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
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
                MainActivity.mMapManager.getMap().clear();
                MainActivity.restartMap2();
              drawerLayout.closeDrawers();
                break;
            case 1://Tipo di furto
                // aggiungereFurto( view); //come test apre la pagina di un nuovo furto
                break;
            case 2: //Carabinieri
                MainActivity.mMapManager.getMap().clear();
                Polizia polizia=MainActivity.getPolizia();
                if (polizia!= null) {
                    //Para mostrar latitud y longitud por pantalla
                    String name = "Carabinieri";//MainActivity.getPolizia().mIndirizzo;
                    MainActivity.mMapManager.getMap().addMarker(new MarkerOptions().position(new LatLng(polizia.getmLatitude(), polizia.getmLongitude()))
                            .title(name)
                            .snippet(polizia.mPhone)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_carabbinieri)));
                    MainActivity.mMapManager.getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(polizia.getmLatitude(), polizia.getmLongitude()), 16));

                  drawerLayout.closeDrawers();
                }
                break;
            case 3: //Notifiche
                break;
            case 4: //Mie segnalazioni
                MainActivity.mMapManager.getMap().clear();
                LatLngBounds.Builder builder = new LatLngBounds.Builder();

                for (int i=0;i<MainActivity.getMiesegnalazioni().size();i++)
                {
                    Furto miofurto= MainActivity.getMiesegnalazioni().get(i);
                    builder.include(new LatLng(miofurto.mLatitude, miofurto.mLongitude));

                    MainActivity.mMapManager.addMakerFurtoMap(miofurto);
                }
                LatLngBounds bounds = builder.build();

                int padding = 100; // offset from edges of the map in pixels
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                MainActivity.mMapManager.getMap().moveCamera(cu);

                drawerLayout.closeDrawers();

                break;
            case 5: //Informazioni
                Intent intent = new Intent(MainActivity.getAppContext(), Informazioni.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MainActivity.getAppContext().startActivity(intent);
                break;
        }
    }
}


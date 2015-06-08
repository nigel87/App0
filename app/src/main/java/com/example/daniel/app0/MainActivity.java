package com.example.daniel.app0; //nigel

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import java.util.ArrayList;
import java.util.List;

import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements TouchableWrapper.UpdateMapAfterUserInterection {

    private static final int REQUEST_STATE = 0;
    private static final int PREFERITI_STATE = 5;
    private static final int INFO_FURTO_STATE = 2;


    private static GoogleMap mMap; // Might be null if Google Play services APK is not available.
    public static MyLocationListener mLocationListener;
    public static List<Furto> arrayFurti;
    private static List<Preferiti> arrayPreferiti;
    private static Context context;


    private static Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView leftDrawerList;
    private ArrayAdapter<String> navigationDrawerAdapter;
    private String[] barasinistra;

    private static Polizia polizia;

    Location location;

    ServerAPI api;
   public static ServerAPI staticapi;


        public static Toolbar getToolbar () {
        return toolbar;
    }

    public static void setPolizia(Polizia polizia) {
        MainActivity.polizia = polizia;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        api = new ServerAPI(Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID));
        staticapi=api;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity.context = getApplicationContext();

        mLocationListener = new MyLocationListener();
        setUpMapIfNeeded();

        nitView();
        if (toolbar != null) {
            //      toolbar.setTitle("Navigation Drawer");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }
        initDrawer();

        /*
        *
        * Gestisce il click ai bottoni della barra sinistra
        * */
        leftDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                switch (position) {
                    case 0://home
                        mMap.clear();
                        restartMap2();
                        drawerLayout.closeDrawer(R.id.drawer_layout);
                        break;
                    case 1://Tipo di furto
                        // aggiungereFurto( view); //come test apre la pagina di un nuovo furto
                        break;
                    case 2: //Carabinieri
                        mMap.clear();
                        if (polizia != null) {
                            //Para mostrar latitud y longitud por pantalla
                            String name = polizia.mIndirizzo;
                            mMap.addMarker(new MarkerOptions().position(new LatLng(polizia.getmLatitude(), polizia.getmLongitude()))
                                    .title(name)
                                    .snippet(polizia.mPhone)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_carabbinieri)));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(polizia.getmLatitude(), polizia.getmLongitude()), 16));

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
        });

      api.furti(mLocationListener.mLoc.getLatitude(), mLocationListener.mLoc.getLongitude());
        api.fav();
   api.police(mLocationListener.mLoc.getLatitude(), mLocationListener.mLoc.getLongitude());


       /* Setting a custom info window adapter for the google map
                * */
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            // Use default InfoWindow frame
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            // Defines the contents of the InfoWindow
            @Override

            public View getInfoContents(Marker marker) {



                switch (marker.getTitle()){
                    case "My Position":
                        // Getting view from the layout file info_window_layout
                        View p = getLayoutInflater().inflate(R.layout.info_marker_furto, null);

                        // Getting reference to the TextView to set Name
                        TextView tvPosName = (TextView) p.findViewById(R.id.tv_name);

                        // Getting reference to the TextView to set Date
                        TextView tvPosLat = (TextView) p.findViewById(R.id.tv_date);

                        // Getting reference to the TextView to set Ora
                        TextView tvPosLon = (TextView) p.findViewById(R.id.tv_ora);

                        tvPosName.setText("My Location");
                        tvPosLat.setText("Lat:" + mLocationListener.mLoc.getLatitude());
                        tvPosLon.setText("Lon: " + mLocationListener.mLoc.getLongitude());
                        return p;

                    case "Preferiti":
                        // Getting view from the layout file info_window_layout
                        View f = getLayoutInflater().inflate(R.layout.info_marker_fav, null);

                        // Getting reference to the TextView to set Name
                        TextView tvFavName = (TextView) f.findViewById(R.id.tv_name);

                        // Getting reference to the TextView to set Date
                        TextView tvFavIndirizzo = (TextView) f.findViewById(R.id.tv_indirizzo);

                        Preferiti newFav = arrayPreferiti.get(0);     //TODO: Provisional

                        // Setting the Name, Date & Ora
                        tvFavName.setText(newFav.getNome());
                        tvFavIndirizzo.setText(newFav.getIndirizzo());
                        return f;

                    case "Carabinieri":
                        // Getting view from the layout file info_window_layout
                        View c = getLayoutInflater().inflate(R.layout.info_marker_carabinieri, null);

                        // Getting reference to the TextView to set Name
                        TextView tvCName = (TextView) c.findViewById(R.id.tv_name);

                        // Getting reference to the TextView to set Date
                        TextView tvCIndirizzo = (TextView) c.findViewById(R.id.tv_indirizzo);

                        // Getting reference to the TextView to set Ora
                        TextView tvCPhone = (TextView) c.findViewById(R.id.tv_phone);


                        // Setting the Name, Date & Ora
                        tvCName.setText("Carabinieri");
                        tvCIndirizzo.setText(polizia.getIndirizzo());
                        tvCPhone.setText("Phone: " + polizia.mPhone);
                        return c;

                    default:
                        // Getting view from the layout file info_window_layout
                        View v = getLayoutInflater().inflate(R.layout.info_marker_furto, null);

                        // Getting reference to the TextView to set Name
                        TextView tvName = (TextView) v.findViewById(R.id.tv_name);

                        // Getting reference to the TextView to set Date
                        TextView tvDate = (TextView) v.findViewById(R.id.tv_date);

                        // Getting reference to the TextView to set Ora
                        TextView tvOra = (TextView) v.findViewById(R.id.tv_ora);

                        Furto furto = getFurto(marker.getId());

                        // Setting the Name, Date & Ora
                        tvName.setText(furto.mTitolo);
                        tvDate.setText("Date:" + furto.mDate);
                        tvOra.setText("Fascia Oraria: " + furto.mOra);
                        return v;

                }
                // Returning the view containing InfoWindow contents


            }
        });
/*
       *Listener Click in Info Windows di Markers
        */
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker marker) {

                switch(marker.getTitle()){
                    case "My Position":
                        Toast.makeText(MainActivity.getAppContext(), "Posizione attuale", Toast.LENGTH_SHORT).show();
                        break;
                    case "Preferiti":
                        break;
                    case "Carabinieri":
                        break;
                    default:
                        for (int i = 0; i < arrayFurti.size(); i++) {
                            if (marker.getId().matches(arrayFurti.get(i).mIdMarker)) {
                                Furto furto = getFurto(marker.getId());
                                Intent intent = new Intent(MainActivity.this, InfoFurtoActivity.class);
                                intent.putExtra("idFurto", furto.mId);
                                MainActivity.this.startActivityForResult(intent, INFO_FURTO_STATE);
                            }
                        }
                        break;
                }
            }
        });

    }

    private void nitView()
    {
        leftDrawerList = (ListView) findViewById(R.id.left_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        barasinistra = getResources().getStringArray(R.array.bara_sinistra);
        navigationDrawerAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, barasinistra);
        leftDrawerList.setAdapter(navigationDrawerAdapter);
    }

    private void initDrawer()
    {
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close)
        {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawerLayout.post(new Runnable() {
            @Override
            public void run() {
                drawerToggle.syncState();
            }
        });
        drawerLayout.setDrawerListener(drawerToggle);
    }



    @Override
    protected void onResume()
    {
        super.onResume();
        mLocationListener.update();
        setUpMapIfNeeded();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_STATE)
            if (resultCode == Activity.RESULT_OK)
               newFurto();

  //      if (resultCode==PREFERITI_STATE)
//            newPreferito();

    //    setUpMapIfNeeded();

    }

    /*
    * Aggoingi un nuovo preferito sulla mappa
    *
    * */
    public static void newPreferito(int position) {


     Preferiti newPreferito  = arrayPreferiti.get(position);

        //Decide between the different makers
        mMap.addMarker(new MarkerOptions().position(new LatLng(newPreferito.mLatitude, newPreferito.mLongitude))
                .title("Preferiti")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_star_small)));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(newPreferito.mLatitude, newPreferito.mLongitude), 16));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }


    /**
     * Recarga el mapa si se necesita
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * Localiza las coordenadas de tu dispositivo. Situa la camara y un marcador sobre la posición indicada
     * mediante la longitud y la latitud de tu dispositivo.
     */
    private void setUpMap() {

        location=mLocationListener.mLoc;

        //Para mostrar latitud y longitud por pantalla
        String name = "My Position";
        String coord = mLocationListener.mLoc.getLatitude() + ", " +mLocationListener.mLoc.getLongitude();

        mMap.addMarker(new MarkerOptions()  .position(new LatLng(mLocationListener.mLoc.getLatitude(), mLocationListener.mLoc.getLongitude()))
                .title(name)
                .snippet(coord)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLocationListener.mLoc.getLatitude(), mLocationListener.mLoc.getLongitude()), 16));

    }

    /**
     * Aggiungere un nuovo furto --> Nuova View di Nuovo Furto
     */
    public void aggiungereFurto(View view){
        Intent intent = new Intent(MainActivity.this, AggiungereFurto.class);
        MainActivity.this.startActivityForResult(intent, REQUEST_STATE);
    }

    public void newFurto()
    {
        Furto newFurto = arrayFurti.get(arrayFurti.size() - 1);
        addMakerFurtoMap(newFurto);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(newFurto.mLatitude, newFurto.mLongitude), 16));
        api.addfurto(newFurto);
    }

    public static void addFurto(Furto mFurto) {
        if (arrayFurti == null)
            arrayFurti = new ArrayList<>();

        boolean is_new_furto=true;

        /*Contorlla gli elementi gia presenti in database in modo da non inserire lo stesso furto due volte nel array*/
        for (int i=0;i<arrayFurti.size();i++)
            if(mFurto.mId==arrayFurti.get(i).mId)
                is_new_furto=false;



        if(is_new_furto) {
            staticapi.getCommenti(mFurto.mId);
            arrayFurti.add(mFurto);
        }
    }

    public static  List<Preferiti> getArrayPreferiti()
    {
        return arrayPreferiti;

    }

    public static void addPreferiti(Preferiti mFavoriti) {
        if (arrayPreferiti == null)
            arrayPreferiti = new ArrayList<>();

        arrayPreferiti.add(mFavoriti);

     //   if (addToServer==true)

      // staticapi.fav(arrayPreferiti);
    }


    public static Context getAppContext(){
        return  MainActivity.context;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_favorites) {//seleziono preferiti

            Intent intent = new Intent(MainActivity.this, GestionePreferiti.class);
            MainActivity.this.startActivityForResult(intent,REQUEST_STATE);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /*Riaggiunge sulla mappa i marker per tutti i furti presenti nel array arrayFurti,
    * e vissualizza la posizione corente
    * */
    public   void restartMap ()
    {
        for(int i = 0; i < arrayFurti.size(); i++)
            if (arrayFurti.get(i).mIdMarker==null)
            MainActivity.addMakerFurtoMap(arrayFurti.get(i));

     /*   mMap.addMarker(new MarkerOptions().position(new LatLng(initLat,initLon))
                .title("Posizione " )
                .snippet("" + initLat+ " " +initLon)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(initLat,initLon), 16));*/
    }




    /*Riaggiunge sulla mappa i marker per tutti i furti presenti nel array arrayFurti,
    * e vissualizza la posizione corente
    * */
    public   void restartMap2 ()
    {
        for(int i = 0; i < arrayFurti.size(); i++)
              MainActivity.addMakerFurtoMap(arrayFurti.get(i));


        mLocationListener.update();

        mMap.addMarker(new MarkerOptions().position(new LatLng(mLocationListener.mLoc.getLatitude(),mLocationListener.mLoc.getLongitude()))
                .title("Posizione " )
                .snippet("" + mLocationListener.mLoc.getLatitude()+ " " +mLocationListener.mLoc.getLongitude())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLocationListener.mLoc.getLatitude(),mLocationListener.mLoc.getLongitude()), 16));
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


    public Furto getFurto(String idMarker){
        Furto f = new Furto();
        for(int i = 0; i < arrayFurti.size(); i++){
            if(idMarker.matches(arrayFurti.get(i).mIdMarker))
                f = arrayFurti.get(i);
        }
        return f;
    }

    public static void aggiornaLIstaPreferiti() {
        arrayPreferiti =null;
    }

    public static void remplaceFurto(Furto nFurto){
        for(int i = 0; i < arrayFurti.size(); i++){
            if(arrayFurti.get(i).mId == nFurto.mId){
                arrayFurti.remove(i);
                arrayFurti.add(nFurto);
            }
        }
    }


    @Override
    public void onUpdateMapAfterUserInterection() {
      //  Toast.makeText(MainActivity.getAppContext(), "Map Updated ", Toast.LENGTH_SHORT).show();
       LatLng latLng= mMap.getCameraPosition().target;

        Location new_location = new Location("new location");
        new_location.setLatitude(latLng.latitude);
        new_location.setLongitude(latLng.longitude);


        if (location.distanceTo(new_location)>5000 ) {
            location=new_location;
            api.furti(latLng.latitude, latLng.longitude);
            restartMap();
        }
    }
}

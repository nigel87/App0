package com.example.daniel.app0; //nigel

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import java.util.ArrayList;
import java.util.List;

import android.support.v7.widget.Toolbar;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements TouchableWrapper.UpdateMapAfterUserInterection {

    private static final int REQUEST_STATE = 0;
    private static final int FAVORITI_STATE = 5;
    public static final int INFO_FURTO_STATE = 2;
    public static final int DIST_MIN_RECALL_API = 5000;
    public static int start = 0;


    public static MyLocationListener mLocationListener;
    public static List<Furto> arrayFurti;
    private static List<Preferiti> arrayPreferiti;
    private static Context context;


    public static Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView leftDrawerList;
    private ArrayAdapter<String> navigationDrawerAdapter;
    private String[] barasinistra;

    public static GestioneNotifiche mNotifiche;

    public static List <Furto> miesegnalazioni;


    private static Polizia polizia;

    public static Location location;

    ServerAPI api;
    static String deviceID;


    public static FragmentManager fragmentManager;
    public static GestioneMappa mMapManager;


   public static ServerAPI staticapi;


        public static Toolbar getToolbar () {
        return toolbar;
    }

    public static void setPolizia(Polizia polizia) {
        MainActivity.polizia = polizia;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        deviceID=Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        api = new ServerAPI(deviceID);
        staticapi=api;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity.context = getApplicationContext();
        fragmentManager = getSupportFragmentManager();


        mMapManager = new GestioneMappa();
        miesegnalazioni = new ArrayList<>();

        mLocationListener = new MyLocationListener();
        //setUpMapIfNeeded();

        nitView();
        if (toolbar != null) {
            //      toolbar.setTitle("Navigation Drawer");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }
        initDrawer();
        mNotifiche = new GestioneNotifiche();
        //api.furti(mLocationListener.mLoc.getLatitude(), mLocationListener.mLoc.getLongitude());
        for(int i = 0; i<2; i++){
            int START = 10*i;
            api.furti(mLocationListener.mLoc.getLatitude(), mLocationListener.mLoc.getLongitude(),START);
            start = START;
        }

        /*
        *
        * Gestisce il click ai bottoni della barra sinistra
        * */
        leftDrawerList.setOnItemClickListener(new GestisciBarraSinistra(drawerLayout));

        /*
        *   Gestiscel?infoWindows de le markers
         */
        mMapManager.getMap().setInfoWindowAdapter(new GestioneInfoWindows(getLayoutInflater()));
        mMapManager.getMap().setOnInfoWindowClickListener(new GestioneInfoWindowsClick(this));

        //restartMap();
        mMapManager.setUpMap();

        api.fav();
        api.police(mLocationListener.mLoc.getLatitude(), mLocationListener.mLoc.getLongitude());


        Toast.makeText(this, R.string.caricando_dati, Toast.LENGTH_LONG).show();
        restartMap2();

    }

    public static List <Furto> getMiesegnalazioni()
    {return miesegnalazioni;}

    private void nitView()
    {
        leftDrawerList = (ListView) findViewById(R.id.left_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_left);
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

    public static Polizia getPolizia(){    return polizia; }

    @Override
    protected void onStart() {
        super.onStart();
        start = start+10;
        api.furti(mLocationListener.mLoc.getLatitude(), mLocationListener.mLoc.getLongitude(),start);
        restartMap();

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        //start = start+10;
        //api.furti(mLocationListener.mLoc.getLatitude(), mLocationListener.mLoc.getLongitude(),start);
        mLocationListener.update();
        mMapManager.setUpMapIfNeeded();
        restartMap();
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
            if(resultCode != FAVORITI_STATE)
                restartMap2();
    }

    /*
    * Aggoingi un nuovo favorito sulla mappa
    *
    * */
    public static void newPreferito(int position) {


     Preferiti newPreferito = arrayPreferiti.get(position);

        //Decide between the different makers
        MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(newPreferito.mLatitude, newPreferito.mLongitude))
                .title("Favoriti")
                        //     .snippet(newFnewFavoritourto.mMostrare) //drawable/btn_star
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_star_small));
        mMapManager.addMarker(markerOptions);
        mMapManager.moveCameraTo(new LatLng(newPreferito.mLatitude, newPreferito.mLongitude), 16);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);


        return true;
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
        mMapManager.addMakerFurtoMap(newFurto);
        mMapManager.moveCameraTo(new LatLng(newFurto.mLatitude, newFurto.mLongitude), 16);
        api.addfurto(newFurto);
    }

    public static void addFurto(Furto mFurto) {
        if (arrayFurti == null)
            arrayFurti = new ArrayList<>();

        boolean exist_this_furto = false;

        /*Contorlla gli elementi gia presenti in database in modo da non inserire lo stesso furto due volte nel array*/
        for (int i=0;i<arrayFurti.size();i++)
            if(mFurto.mId==arrayFurti.get(i).mId && mFurto.mTitolo.matches(arrayFurti.get(i).mTitolo))
                exist_this_furto=true;

        if(!exist_this_furto) {
            if(mFurto.mId!=-1)
                staticapi.getCommenti(mFurto.mId);

            arrayFurti.add(mFurto);

            if (mFurto.mDeviceId.matches(deviceID))
                miesegnalazioni.add(mFurto);
        }
    }

    public static  List<Preferiti> getArrayPreferiti ()
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
        if(arrayFurti != null)
            for(int i = 0; i < arrayFurti.size(); i++)
                if (arrayFurti.get(i).mIdMarker==null)
                    mMapManager.addMakerFurtoMap(arrayFurti.get(i));
    }




    /*Riaggiunge sulla mappa i marker per tutti i furti presenti nel array arrayFurti,
    * e vissualizza la posizione corente
    * */
    public static  void restartMap2 ()
    {
        mMapManager.getMap().clear();
        if(arrayFurti != null)
            for(int i = 0; i < arrayFurti.size(); i++)
                  //MainActivity.addMakerFurtoMap(arrayFurti.get(i));
                    mMapManager.addMakerFurtoMap(arrayFurti.get(i));

        mLocationListener.update();
        mMapManager.setUpMap();
    }


    public static Furto getFurto(String idMarker){
        Furto f = new Furto();
        for(int i = 0; i < arrayFurti.size(); i++){
            if(idMarker.matches(arrayFurti.get(i).mIdMarker))
                f = arrayFurti.get(i);
        }
        return f;
    }

    public static void aggiornaListaPreferiti() {
        arrayPreferiti=null;
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
       LatLng latLng= mMapManager.getMap().getCameraPosition().target;

        Location new_location = new Location("new location");
        new_location.setLatitude(latLng.latitude);
        new_location.setLongitude(latLng.longitude);


        if (location.distanceTo(new_location)>DIST_MIN_RECALL_API ) {
            location=new_location;
            api.furti(latLng.latitude, latLng.longitude);
            restartMap();
        }
        else{
            start =start + 10;
            api.furti(latLng.latitude, latLng.longitude, start);
            restartMap();
        }
    }
}

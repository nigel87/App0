package com.example.daniel.app0;

import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by nigel on 12/05/15.
 */
public class Preferiti {
    String mNome;
    String mIndirizzo;
    public double mLatitude;
    public double mLongitude;


    public Preferiti(String nome, String indirizzo)
    {
        mNome=nome;
        mIndirizzo=indirizzo;
        coordinate();
    }
    public Preferiti(String nome, double lat, double lon)
    {
        mNome=nome;
        mLatitude=lat;
        mLongitude=lon;
        mIndirizzo="indirizzo";
    }


    public String getNome()
    {
        return mNome;
    }
    public String getIndirizzo()
    {
        return mIndirizzo;
    }


    public void coordinate (){
        Geocoder geocoder = new Geocoder(MainActivity.getAppContext(), Locale.getDefault());
        List<Address> fromLocationName = null;
        try {
            fromLocationName = geocoder.getFromLocationName(mIndirizzo,   1);
            if (fromLocationName != null && fromLocationName.size() > 0) {
                Address a = fromLocationName.get(0);
                Point point = new Point(
                        (int) a.getLatitude() ,
                        (int) a.getLongitude() );

                mLatitude = a.getLatitude();
                mLongitude = a.getLongitude();

                System.out.print("\n \n \n le coordinate sono "+ a.getLatitude() + "  " + a.getLongitude() +"\n \n \n");
            }


        } catch (IOException e) {
            e.printStackTrace();

        }


    }

}

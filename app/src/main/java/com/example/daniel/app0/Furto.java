package com.example.daniel.app0;

import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Daniel on 03/05/2015.
 */

public class Furto {
    public int mId;
    public String mTitolo;
    public String mTipo;
    public double mLatitude;
    public double mLongitude;
    public String mIndirizzo;
    public String mDate;
    public String mOra;
    public String mDescrizione;
    public String mIdMarker;
    public String mDeviceId;
    public List<String> mCommenti;

    public Furto(){}

    public Furto(int newId, String newTitolo, String newTipo, String newIndirizzo, String newDate, String newOra, String newDescizione, String newDeviceId){
        mId = newId;
        mTitolo = newTitolo;
        mTipo = newTipo;
        mIndirizzo = newIndirizzo;
        indirizzoACoordinate();
        mDate = newDate;
        mOra = newOra;
        mDescrizione = newDescizione;
        mDeviceId = newDeviceId;

        mCommenti = new ArrayList<String>();
    }

    public String getDeviceId()
        {return mDeviceId;}


    public void indirizzoACoordinate (){
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

    public static String coordinateAIndirizzo (double lat, double lon) {
        Geocoder geocoder = new Geocoder(MainActivity.getAppContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
            if (addresses.isEmpty()) {
            } else {
                if (addresses.size() > 0) {
                    String newIndirizzo = addresses.get(0).getAddressLine(0) + ", " + addresses.get(0).getLocality() + ", "  + addresses.get(0).getCountryName();
                    //mIndirizzo = newIndirizzo;
                    return newIndirizzo;
                }
            }


        } catch (IOException e) {
            e.printStackTrace();

        }
        return "";
    }

    public void addCommento(String newCommento){
        mCommenti.add(newCommento);
    }

    public String showCommenti(){
        if(mCommenti.isEmpty())
            return "";
        else{
            String showComm = "1. " + mCommenti.get(0);
            for(int i = 1; i < mCommenti.size(); i++){
                showComm = showComm + "\n" + i+1 + ". " + mCommenti.get(i);
            }
            return showComm;
        }
    }
}

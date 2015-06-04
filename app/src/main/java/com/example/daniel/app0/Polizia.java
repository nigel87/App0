package com.example.daniel.app0;

import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by nigel on 22/05/15.
 */

public class Polizia {
    public String mTipo;
    public String mLatitude;
    public String mLongitude;
    public String mIndirizzo;
    public String mPhone;
    public String mUpdated;

        public Polizia (String tipo, String latitude, String longitude, String indrizzo, String phone, String updated )
        {
            mTipo=tipo;
            mLatitude=latitude;
            mLongitude=longitude;
            mIndirizzo=indrizzo;
            mPhone=phone;
            mUpdated=updated;
        }

        public double getmLatitude (){
            return Double.parseDouble(mLatitude);
        }
    public double getmLongitude(){
        return Double.parseDouble(mLongitude);
    }

    public String getIndirizzo(){
        Geocoder geocoder = new Geocoder(MainActivity.getAppContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(mLatitude), Double.parseDouble(mLongitude), 1);
            if (addresses.isEmpty()) {
            } else {
                if (addresses.size() > 0) {
                    String newIndirizzo = addresses.get(0).getAddressLine(0) + ", " + addresses.get(0).getLocality() + ", " + addresses.get(0).getCountryName();
//mIndirizzo = newIndirizzo;
                    return newIndirizzo;
                }
            }


        } catch (IOException e) {
            e.printStackTrace();

        }
        return "";
    }


}

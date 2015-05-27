package com.example.daniel.app0;

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




}

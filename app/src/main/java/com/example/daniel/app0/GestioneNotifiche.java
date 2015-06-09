package com.example.daniel.app0;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

/**
 * Created by Daniel on 08/06/2015.
 */
public class GestioneNotifiche {


    private NotificationCompat.Builder mBuilder;
    private PendingIntent resultPendingIntent;
    private NotificationManager mNotifyMgr;
    private int idNotif;

    public GestioneNotifiche(){

        idNotif = 0;

        // Gets an instance of the NotificationManager service
        mNotifyMgr =
                (NotificationManager) MainActivity.getAppContext().getSystemService(MainActivity.getAppContext().NOTIFICATION_SERVICE);

    }

    public void generateNotifiche(Furto f, String nomeFav){
        // Sets an ID for the notification
        idNotif++;

        mBuilder =  new NotificationCompat.Builder(MainActivity.getAppContext())
                .setSmallIcon(R.drawable.ic_wallet)
                .setContentTitle("Nueovo furto segnalato")
                .setContentText("Furto vicino a " + nomeFav);      //TODO: Modificare
        Intent resultIntent = new Intent(MainActivity.getAppContext(), InfoFurtoActivity.class);
        resultIntent.putExtra("idFurto", f.mId);
        resultPendingIntent =
                PendingIntent.getActivity(
                        MainActivity.getAppContext(),
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setAutoCancel(true);

// Builds the notification and issues it.
        mNotifyMgr.notify(idNotif, mBuilder.build());
    }


}

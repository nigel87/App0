package com.example.daniel.app0;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by nigel on 12/05/15.
 */
public class NuovoPreferito extends ActionBarActivity {
    private static final int REQUEST_STATE = 0;
    private static final int REQUEST_POSTO = 1;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nuovofavorito);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void salvareFavorito(View view)
    {
        EditText nome = (EditText) findViewById(R.id.nome_favorito);
        EditText indirizzo = (EditText) findViewById(R.id.indirizzo_favorito);
/*
* TODO:Aggiungere controlli su nome e indirizzo vuoiri e size > 3
*       if (arrayFavoriti.size()<3)
        arrayFavoriti.add(mFavoirti);
        else   Toast.makeText(GestionePreferiti.getAppContext(), "Si possono insererire solo fino a 3 favoriti ", Toast.LENGTH_SHORT).show();
*
* */

        MainActivity.addPreferiti(new Preferiti(nome.getText().toString(), indirizzo.getText().toString()));

        MainActivity.staticapi.fav(MainActivity.getArrayPreferiti());
        Intent intent = new Intent(this,NuovoPreferito.class);
        setResult(Activity.RESULT_OK, intent);

        NuovoPreferito.this.finish();

    }


    public void cancelareFavorito(View view)
    {
        Intent intent = new Intent(this,NuovoPreferito.class);
        setResult(Activity.RESULT_CANCELED, intent);
        NuovoPreferito.this.finish();
    }
// Alla fine di NuovoPreferito

    public void openMap(View view){
        Intent intent = new Intent(NuovoPreferito.this, MapCercarePosto.class);
        NuovoPreferito.this.startActivityForResult(intent, REQUEST_POSTO);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_POSTO) {
            double[] newLoc = data.getDoubleArrayExtra("location");
            addLocation(newLoc);
        }
    }

    public void addLocation(double[] latlng){
        EditText editIndirizzo = (EditText) findViewById(R.id.indirizzo_favorito);
        editIndirizzo.setText(Furto.coordinateAIndirizzo(latlng[0],latlng[1]), TextView.BufferType.EDITABLE);
    }

}

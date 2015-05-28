package com.example.daniel.app0;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
/**
 * Created by nigel on 12/05/15.
 */
public class NuovoFavorito extends ActionBarActivity {
    private static final int REQUEST_STATE = 0;
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
        else   Toast.makeText(GestioneFavoriti.getAppContext(), "Si possono insererire solo fino a 3 favoriti ", Toast.LENGTH_SHORT).show();
*
* */

        MainActivity.addFavoriti(new Favoriti(nome.getText().toString(), indirizzo.getText().toString()));

        MainActivity.staticapi.fav(MainActivity.getArrayFavoriti());
        Intent intent = new Intent(this,NuovoFavorito.class);
        setResult(Activity.RESULT_OK, intent);

        NuovoFavorito.this.finish();

    }


    public void cancelareFavorito(View view)
    {
        Intent intent = new Intent(this,NuovoFavorito.class);
        setResult(Activity.RESULT_CANCELED, intent);
        NuovoFavorito.this.finish();
    }


}

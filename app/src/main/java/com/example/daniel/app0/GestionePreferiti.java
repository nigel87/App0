package com.example.daniel.app0;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nigel on 12/05/15.
 * Gestisce la visualizzazione della lista dei prefertiti
 */
public class GestionePreferiti extends ActionBarActivity {
    private static final int FAVORITI_STATE = 5;

    private static final int  ELIMINA_PREFERITO = 6;
    private static Context context;
    List<String> li= new ArrayList<>();
    ArrayAdapter adapter;


    //TODO: Grafica
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == Activity.RESULT_OK)
        {
            Preferiti favoriti= MainActivity.getArrayPreferiti().get(MainActivity.getArrayPreferiti().size()-1);
            setContentView(R.layout.preferiti_lista);
            setupToolbar();
            ListView listafavoriti = (ListView) findViewById(R.id.favlist);
            li.add(favoriti.getNome());
            ArrayAdapter adapter = new ArrayAdapter<>(getAppContext(), android.R.layout.list_content, li);

            listafavoriti.setAdapter(adapter);
            listafavoriti.setOnItemClickListener(new Gestisci());
        }

        if (resultCode == ELIMINA_PREFERITO)
        {
            adapter.clear();
            aggiungiListaFavorti();

        }



    }

    private void runFadeInAnimation() {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.slide_in_up);
        a.reset();
        RelativeLayout ll = (RelativeLayout) findViewById(R.id.favoriti_layout);
        ll.clearAnimation();
        ll.startAnimation(a);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    aggiungiListaFavorti();
     }


    private class Gestisci implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id)
        {
                Intent intent = new Intent(GestionePreferiti.this, GestionePreferiti.class);
                setResult(FAVORITI_STATE, intent);
                GestionePreferiti.this.finish();
                MainActivity.newPreferito(position);
        }
    }


    public static Context getAppContext(){return  GestionePreferiti.context;}


    // Aggiungere un nuovo favorito
    public void nuovoFavorito(View v)
    {
        Intent intent = new Intent(GestionePreferiti.this, NuovoPreferito.class);
        GestionePreferiti.this.startActivityForResult(intent, FAVORITI_STATE);
    }


    public void setupToolbar(){
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_preferiti, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete_preferiti) {//seleziono preferiti

            Intent intent = new Intent(GestionePreferiti.this, GestionePreferitiCancellaPreferito.class);
            GestionePreferiti.this.startActivityForResult(intent, FAVORITI_STATE);


            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void aggiungiListaFavorti(){



        List<Preferiti> arrayFavoriti = MainActivity.getArrayPreferiti();
        if (arrayFavoriti ==null)
            setContentView(R.layout.preferiti_vuoto);
        else
            setContentView(R.layout.preferiti_lista);

        runFadeInAnimation();

        GestionePreferiti.context = getApplicationContext();




        ListView listafavoriti = (ListView) findViewById(R.id.favlist);
        setupToolbar();

        if (arrayFavoriti !=null && arrayFavoriti.size()>0)
        {
            for (int i = 0; i < arrayFavoriti.size(); i++)
            {
                final Preferiti favoriti = arrayFavoriti.get(i);

                li.add(favoriti.getNome());
            }
            adapter = new ArrayAdapter<>(getAppContext(), R.layout.listview_item_row, li);
            listafavoriti.setAdapter(adapter);
            listafavoriti.setClickable(true);

            listafavoriti.setOnItemClickListener(new Gestisci());

        }





    }




}

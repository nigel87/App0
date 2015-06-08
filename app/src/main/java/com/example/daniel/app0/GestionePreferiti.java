package com.example.daniel.app0;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
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
 */
public class GestionePreferiti extends ActionBarActivity {
    private static final int FAVORITI_STATE = 5;
    private static Context context;
    private static List<Preferiti> arrayFavoriti;
    private android.support.v7.widget.Toolbar toolbar;
    List<String> li= new ArrayList<String>();




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
            ArrayAdapter adapter = new ArrayAdapter<>(getAppContext(), android.R.layout.simple_list_item_1, li);
            listafavoriti.setAdapter(adapter);
            listafavoriti.setOnItemClickListener(new Gestisci());
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
        arrayFavoriti=MainActivity.getArrayPreferiti();
        super.onCreate(savedInstanceState);
        if (arrayFavoriti==null)
            setContentView(R.layout.preferiti_vuoto);
        else
            setContentView(R.layout.preferiti_lista);

        runFadeInAnimation();

        GestionePreferiti.context = getApplicationContext();




        ListView listafavoriti = (ListView) findViewById(R.id.favlist);
        setupToolbar();

        if (arrayFavoriti!=null)
        {
            for (int i = 0; i < arrayFavoriti.size(); i++)
            {
                final Preferiti favoriti = arrayFavoriti.get(i);

                li.add(favoriti.getNome());
            }
            ArrayAdapter adapter = new ArrayAdapter<>(getAppContext(), android.R.layout.simple_list_item_1, li);
            listafavoriti.setAdapter(adapter);
            listafavoriti.setClickable(true);

            listafavoriti.setOnItemClickListener(new Gestisci());

        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

}

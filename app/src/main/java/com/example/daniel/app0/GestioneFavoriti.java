package com.example.daniel.app0;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nigel on 12/05/15.
 */
public class GestioneFavoriti extends ActionBarActivity {
    private static final int FAVORITI_STATE = 5;
    private static Context context;
    private static List<Favoriti> arrayFavoriti;
    private android.support.v7.widget.Toolbar toolbar;
    List<String> li= new ArrayList<String>();




    //TODO: Grafica
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == Activity.RESULT_OK)
        {
            Favoriti favoriti= MainActivity.getArrayFavoriti().get(MainActivity.getArrayFavoriti().size()-1);
            setContentView(R.layout.favoriti3);
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
        arrayFavoriti=MainActivity.getArrayFavoriti();
        super.onCreate(savedInstanceState);
        if (arrayFavoriti==null)
            setContentView(R.layout.favoriti2);
        else
            setContentView(R.layout.favoriti3);

        runFadeInAnimation();

        GestioneFavoriti.context = getApplicationContext();




        ListView listafavoriti = (ListView) findViewById(R.id.favlist);
        setupToolbar();

        if (arrayFavoriti!=null)
        {
            for (int i = 0; i < arrayFavoriti.size(); i++)
            {
                final Favoriti favoriti = arrayFavoriti.get(i);

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
                Intent intent = new Intent(GestioneFavoriti.this, GestioneFavoriti.class);
                setResult(FAVORITI_STATE, intent);
                GestioneFavoriti.this.finish();
                MainActivity.newFavoirto(position);
        }


    }


    public static Context getAppContext(){return  GestioneFavoriti.context;}


    // Aggiungere un nuovo favorito
    public void nuovoFavorito(View v)
    {
        Intent intent = new Intent(GestioneFavoriti.this, NuovoFavorito.class);
        GestioneFavoriti.this.startActivityForResult(intent, FAVORITI_STATE);
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

}

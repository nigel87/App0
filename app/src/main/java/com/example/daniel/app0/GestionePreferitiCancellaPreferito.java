package com.example.daniel.app0;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nigel on 09/06/15.
 * Classe per gestire l'eliminazione di un prefertito
 */
public class GestionePreferitiCancellaPreferito extends ActionBarActivity {

    private android.support.v7.widget.Toolbar toolbar;
    private final int ELIMINA_PREFERITO = 6;
    List li= new ArrayList<>();
    ListView lv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkbox);

        for (int i=0;i<MainActivity.getArrayPreferiti().size();i++)
            li.add(MainActivity.getArrayPreferiti().get(i).getNome());


        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, li);
        lv = (ListView)findViewById(R.id.genre_list);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lv.setAdapter(adapter);
    }

    public void anulla(View view)
    {
        Intent intent = new Intent(this,GestionePreferiti.class);
        setResult(Activity.RESULT_CANCELED, intent);
        GestionePreferitiCancellaPreferito.this.finish();
    }

    public void eliminaFav(View view)
    {
        SparseBooleanArray positions = lv.getCheckedItemPositions();
        int size= positions.size();
        for(int index = 0; index < size; index++)
        {
            MainActivity.getArrayPreferiti().remove(positions.keyAt(index));
            Toast.makeText(this, "Cancella preferito "+ li.get(positions.keyAt(index)), Toast.LENGTH_SHORT).show();
        }
        MainActivity.staticapi.fav(MainActivity.getArrayPreferiti());
        Intent intent = new Intent(this,GestionePreferiti.class);
        setResult(ELIMINA_PREFERITO, intent);
        GestionePreferitiCancellaPreferito.this.finish();

    }



}

package com.example.daniel.app0;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nigel on 09/06/15.
 */
public class GestionePreferitiCancellaPreferito extends ActionBarActivity {

    private android.support.v7.widget.Toolbar toolbar;
    List li= new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkbox);

        for (int i=0;i<MainActivity.getArrayPreferiti().size();i++)
            li.add(MainActivity.getArrayPreferiti().get(i).getNome());


        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, li);
        ListView lv= (ListView)findViewById(R.id.genre_list);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lv.setAdapter(adapter);
    }

    public void anulla(View view)
    {
        Intent intent = new Intent(this,NuovoPreferito.class);
        setResult(Activity.RESULT_CANCELED, intent);
        GestionePreferitiCancellaPreferito.this.finish();
    }

    public void eliminaFav(View view)
    {

     //   CheckBox cb=findViewById(android.R.id.checkbox);
    }



}

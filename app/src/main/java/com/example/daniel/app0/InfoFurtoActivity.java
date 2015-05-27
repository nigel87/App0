package com.example.daniel.app0;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Daniel on 25/05/2015.
 */
public class InfoFurtoActivity extends ActionBarActivity {
    Furto mFurto;
    private Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.info_furto);

        /*
        * Cerchiamo il id dil furto in Extra dil Bundle per sapere di che furto è il marker
         */
        int idFurto = getIntent().getExtras().getInt("idFurto", -1);

        for(int i = 0; i < MainActivity.arrayFurti.size(); i++){
            if(MainActivity.arrayFurti.get(i).mId == idFurto)
                mFurto = MainActivity.arrayFurti.get(i);
        }
        if(mFurto != null){
            createInfoView();
        }
        else{
            View newView = new View(MainActivity.getAppContext());
            cancelare(newView);
        }




    }

    public void createInfoView(){

        // Getting reference to the TextView
        TextView tvTitolo = (TextView) findViewById(R.id.titolo_furto);
        TextView tvTipo = (TextView) findViewById(R.id.tipo_furto);
        TextView tvIndirizzo = (TextView) findViewById(R.id.indirizzo_furto);
        TextView tvDate = (TextView) findViewById(R.id.date_furto);
        TextView tvOra = (TextView) findViewById(R.id.fascia_ora_furto);
        TextView tvDescrizione = (TextView) findViewById(R.id.descrizione_furto);
        TextView tvCommenti = (TextView) findViewById(R.id.all_commenti);

        // Setting
        tvTitolo.setText(mFurto.mTitolo);
        tvTipo.setText(mFurto.mTipo);
        tvIndirizzo.setText(mFurto.mIndirizzo);
        tvDate.setText(mFurto.mDate);
        tvOra.setText(mFurto.mOra);
        tvDescrizione.setText(mFurto.mDescrizione);
        tvCommenti.setText(mFurto.showCommenti());
    }

    public void enviareCommento(View view){

        EditText newCommento = (EditText) findViewById(R.id.commento_nuovo);
        TextView tvCommenti = (TextView) findViewById(R.id.all_commenti);
        String commentiAttuale;
        if(mFurto.mCommenti == null)
            commentiAttuale = "*" + newCommento.getText().toString();
        else
            commentiAttuale = tvCommenti.getText().toString() + "\n*" + newCommento.getText().toString();

        mFurto.addCommento(newCommento.getText().toString());
        MainActivity.staticapi.addCommento(mFurto.mId, newCommento.getText().toString());
        tvCommenti.setText(commentiAttuale);
        newCommento.getText().clear();
    }

    public void cancelare(View view){
        Intent intent = new Intent(this,InfoFurtoActivity.class);
        setResult(Activity.RESULT_CANCELED, intent);
        InfoFurtoActivity.this.finish();
    }
}
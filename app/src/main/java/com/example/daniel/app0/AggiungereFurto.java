package com.example.daniel.app0;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * Created by Daniel on 30/04/2015.
 */
public class AggiungereFurto extends ActionBarActivity {
    Furto newFurto;
    public Calendar mCalendar;
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private Button mDateButton;
    private static final int REQUEST_POSTO = 0;
    private Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.aggiungere_furto);

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




        mDateButton = (Button) findViewById(R.id.date_nuevo_furto);
        mCalendar = Calendar.getInstance();
        updateDateButtonText();
        fillSpinner(R.id.spinner_tipo_nuovo_furto, R.array.tipo_furto_array);
        fillSpinner(R.id.spinner_ora_nuovo_furto, R.array.ora_furto_array);
    }

    /* @Override
     public boolean onOptionsItemSelected(MenuItem item) {
         // Handle action bar item clicks here. The action bar will
         // automatically handle clicks on the Home/Up button, so long
         // as you specify a parent activity in AndroidManifest.xml.
         int id = item.getItemId();

         //noinspection SimplifiableIfStatement
         if (id == R.id.action_settings) {
             return true;
         }

         return super.onOptionsItemSelected(item);
     }

     */
    public void salvareFurto(View view){

        EditText titolo = (EditText) findViewById(R.id.titolo_nuevo_furto);
        Spinner tipo = (Spinner) findViewById(R.id.spinner_tipo_nuovo_furto);
        EditText newIndir = (EditText) findViewById(R.id.indirizzo_nuovo_furto);
        Spinner ora = (Spinner) findViewById(R.id.spinner_ora_nuovo_furto);
        EditText newDescrizione = (EditText) findViewById(R.id.descrizione_nuevo_furto);
        if(newIndir.getText().toString().matches("") /*&& newDate.getText().toString().matches("")*/)
            Toast.makeText(this, "Indirizzo e Date sono obligatori", Toast.LENGTH_SHORT).show();
        else {

            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
            int id = MainActivity.arrayFurti.size();
            //Furto(int newId, String newTitolo, String newTipo, String newIndirizzo, String newDate, String newOra, String newDescizione, String newDeviceId)
            newFurto = new Furto(
                    id,
                    titolo.getText().toString(),
                    tipo.getSelectedItem().toString(),
                    newIndir.getText().toString(),
                    dateFormat.format(mCalendar.getTime()),
                    ora.getSelectedItem().toString(),
                    newDescrizione.getText().toString(),
                    MainActivity.staticapi.deviceid);


            Toast.makeText(this, newFurto.mDescrizione, Toast.LENGTH_SHORT).show();
            MainActivity.addFurto(newFurto);

            Intent intent = new Intent(this,AggiungereFurto.class);
            setResult(Activity.RESULT_OK, intent);
            AggiungereFurto.this.finish();
        }

    }

    public void cancelare(View view){
        Intent intent = new Intent(this,AggiungereFurto.class);
        setResult(Activity.RESULT_CANCELED, intent);
        AggiungereFurto.this.finish();
    }




    /*
    * Mostra un dailogo (datepicker) per scegliere la data
    * */

    public void scegliData (View view){


        showDatePickerDialog(view);


    }

    private void showDatePickerDialog(View view) {
        DatePickerFragment dateFragment = new DatePickerFragment();
        dateFragment.show(getFragmentManager(), "datePicker");


    }
    public void updateDateButtonText() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        String dateForButton = dateFormat.format(mCalendar.getTime());
        mDateButton.setText(dateForButton);
    }

    @SuppressLint("ValidFragment")
    class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            mCalendar.set(Calendar.YEAR, year);
            mCalendar.set(Calendar.MONTH, month);
            mCalendar.set(Calendar.DAY_OF_MONTH, day);
            updateDateButtonText();
        }
    }



    public void fillSpinner( int idView, int idArray){
        /*Rellenamos el spinner de tipo di furto*/
        Spinner mSpinner = (Spinner) findViewById(idView);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                idArray, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mSpinner.setAdapter(adapter);
    }





    public void openMap(View view){
        Intent intent = new Intent(AggiungereFurto.this, MapCercarePosto.class);
        AggiungereFurto.this.startActivityForResult(intent, REQUEST_POSTO);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_POSTO) {
            if (resultCode == Activity.RESULT_OK) {
                double[] newLoc = data.getDoubleArrayExtra("location");
                addLocation(newLoc);
            }
        }
    }

    public void addLocation(double[] latlng){
        EditText editIndirizzo = (EditText) findViewById(R.id.indirizzo_nuovo_furto);
        editIndirizzo.setText(Furto.coordinateAIndirizzo(latlng[0],latlng[1]), TextView.BufferType.EDITABLE);
    }
}


        
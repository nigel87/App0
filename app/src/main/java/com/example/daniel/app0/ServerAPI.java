package com.example.daniel.app0;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by nigel on 21/05/15.
 * Fornisce le funzioni per chiamere le API e gestire la risposta del server
 */
public  class ServerAPI {

    public  String deviceid; //id unico del dispostivo
    private String model = android.os.Build.MODEL; // il modello del dispostivo
    private  double  initLat;
    private  double initLon;
    private boolean is_get = true;

    private final String FURTI_BASE_URL="http://www.sapienzaapps.it/furti/furti.php?";
    private final String ADDFURTO_BASE_URL="http://www.sapienzaapps.it/furti/addfurto.php?";
    private final String POLICE_BASE_URL="http://www.sapienzaapps.it/furti/police.php";
    private final String FAV_BASE_URL ="http://www.sapienzaapps.it/furti/fav.php" ;
    private final String COMMENTI_BASE_URL="http://www.sapienzaapps.it/furti/commenti.php";
    private final String ADDCOMMENTO_BASE_URL="http://www.sapienzaapps.it/furti/addcommento.php";

    private final int ADD_FURTO=1;
    private final int FURTI=2;
    private final int ADDCOMMENTO=3;
    private final int COMMENTI=4;
    private final int FAV=5;
    private final int ADD_FAV=7;
    private final int POLICE=6;

    private boolean is_furti_caricatti = false;




    public ServerAPI (String id)
    {
        deviceid =id;
    }

    public void police (double lat, double lon)
    {   initLat=lat;
        initLon=lon;
        is_get=false;//utilizza metodo post per ottenere la lista dei carabinieri
        String url=POLICE_BASE_URL;
        url=policeURL(url);
        new HttpAsyncTask(POLICE).execute(url);
    }

    public  void furti(double lat, double lon)
    {
        initLat=lat;
        initLon=lon;

        is_get=true;//utilizza metodo get per ottenere la lista dei furti
        String url=FURTI_BASE_URL;
        url=furtiURL(url);
        new HttpAsyncTask(FURTI).execute(url);
    }

    public void addfurto(Furto newFurto)
    {
        is_get=false;//utilizza metodo post per inserire un nuovo furto
        String url=ADDFURTO_BASE_URL;
        url=addfurtoURL(url, newFurto);
        new HttpAsyncTask(ADD_FURTO).execute(url);
    }

    //inserisce un nuovo favoirto
    public void fav( List<Favoriti> arrayfav)
    {   is_get=false;//utilizza metodo post per gestire i preferiti
        String url=FAV_BASE_URL;
        url=newfavURL(url, arrayfav);
//        Toast.makeText(MainActivity.getAppContext(), url, Toast.LENGTH_LONG).show();
        new HttpAsyncTask(ADD_FAV).execute(url);
    }

    //Ritorna l'elenco dei favortiti
    public void fav()
    {
        is_get=false;//utilizza metodo post per gestire i preferiti
        String url=FAV_BASE_URL;
        url=favURL(url);
        new HttpAsyncTask(FAV).execute(url);
    }


    public void addCommento(int fId, String comment){
        is_get=false;//utilizza metodo post per inserire un nuovo furto
        String url=ADDCOMMENTO_BASE_URL;
        url = addCommentoURL(url, fId, comment);
//        Toast.makeText(getBaseContext(), url, Toast.LENGTH_LONG).show();
        new HttpAsyncTask(ADDCOMMENTO).execute(url);
    }

    public void getCommenti(int fId){
        is_get=true;//utilizza metodo post per inserire un nuovo furto
        String url=COMMENTI_BASE_URL;
        url = getCommentiURL(url, fId);
//        Toast.makeText(getBaseContext(), url, Toast.LENGTH_LONG).show();
        new HttpAsyncTask(COMMENTI).execute(url);
    }


    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        private int tipo;
         public HttpAsyncTask(int t)
        {
            tipo=t;

        }

        @Override
        protected String doInBackground(String... urls) {


            return richiestaServerGetPost(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            try{
                if(result.equals("[]")==false)
                    convertRequest(result,tipo);

            }
            catch(JSONException e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            if(MainActivity.arrayFurti != null && !is_furti_caricatti){
                for(int i = 0; i < MainActivity.arrayFurti.size(); i++){
                    MainActivity.addMakerFurtoMap(MainActivity.arrayFurti.get(i));
                }
                is_furti_caricatti = true;
            }


        }
    }

    private String richiestaServerGetPost(String url){
        InputStream inputStream = null;
        String result = "";
        try {
            // crea HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse httpResponse;

            //passa i parametri in get oppure post in base al booleano is_get
            if (is_get)
             httpResponse = httpclient.execute(new HttpGet(url));
            else
                httpResponse = httpclient.execute(new HttpPost(url));

            // riceva la risposta come inputstream
            inputStream = httpResponse.getEntity().getContent();

            // comverti inputstream in stringa
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Errore nella comununicazione con il server!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());}
        return result;
    }


    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }



    private void convertRequest(String result,int tipoJson) throws JSONException {

        int mTipoJson=tipoJson;

        String id,descrizione, name,tipo, testo, titolo, date, device, created, slat, slon, ora,address,phone,updated;
        double lat, lon;
        JSONArray jsonArray = new JSONArray(result);

        switch (mTipoJson){
            case FAV:
                for(int i = 0; i < jsonArray.length(); i++)
                {
                    name    = jsonArray.getJSONObject(i).getString("favname");
                    slat    = jsonArray.getJSONObject(i).getString("lat");
                    slon    = jsonArray.getJSONObject(i).getString("lon");
                    lat = Double.parseDouble(slat);
                    lon=Double.parseDouble(slon);
                    Favoriti favoriti = new Favoriti(name,lat,lon);
                    MainActivity.addFavoriti(favoriti,false);
                }
                break;

            case ADD_FAV:
                MainActivity.aggiornaListaFavoriti();
                for(int i = 0; i < jsonArray.length(); i++)
                {
                    name    = jsonArray.getJSONObject(i).getString("favname");
                    slat    = jsonArray.getJSONObject(i).getString("lat");
                    slon    = jsonArray.getJSONObject(i).getString("lon");
                    lat = Double.parseDouble(slat);
                    lon=Double.parseDouble(slon);
                    Favoriti favoriti = new Favoriti(name,lat,lon);
                    MainActivity.addFavoriti(favoriti,false);
                }
                break;

            case FURTI:
                for(int i = 0; i < jsonArray.length(); i++){
                    id = jsonArray.getJSONObject(i).getString("fid");
                    titolo = jsonArray.getJSONObject(i).getString("titolo");
                    tipo = jsonArray.getJSONObject(i).getString("categoria");
                    descrizione = jsonArray.getJSONObject(i).getString("descr");
                    slat = jsonArray.getJSONObject(i).getString("lat");
                    slon = jsonArray.getJSONObject(i).getString("lon");
                    ora = jsonArray.getJSONObject(i).getString("fascia");
                    date = jsonArray.getJSONObject(i).getString("data");
                    device = jsonArray.getJSONObject(i).getString("deviceid");


                    //Furto(int newId, String newTitolo, String newTipo, String newIndirizzo, String newDate, String newOra, String newDescizione, String newDeviceId)
                    Furto newFurto = new Furto(Integer.parseInt(id), titolo, tipo, Furto.coordinateAIndirizzo(Double.parseDouble(slat), Double.parseDouble(slon)),date,ora,descrizione, device);

                    //     getCommenti(Integer.parseInt(id));
                    MainActivity.addFurto(newFurto);


                }
                break;

            case POLICE:
                tipo    = jsonArray.getJSONObject(0).getString("type");
                slat    = jsonArray.getJSONObject(0).getString("lat");
                slon    = jsonArray.getJSONObject(0).getString("lon");
                address = jsonArray.getJSONObject(0).getString("address");
                phone   = jsonArray.getJSONObject(0).getString("phone");
                updated   = jsonArray.getJSONObject(0).getString("updated");

                Polizia p = new Polizia(tipo,slat,slon,address,phone,updated);
                MainActivity.setPolizia(p);
                break;

            case COMMENTI:
                if (result.contains("idfurto")) {
                    id = jsonArray.getJSONObject(0).getString("idfurto");
                    int fId = Integer.parseInt(id);
                    Furto f = new Furto();
                    for (int i = 0; i < MainActivity.arrayFurti.size(); i++) {
                        if (MainActivity.arrayFurti.get(i).mId == fId)
                            f = MainActivity.arrayFurti.get(i);
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        testo = jsonArray.getJSONObject(i).getString("body");
                        created = jsonArray.getJSONObject(i).getString("created");
                        f.addCommento(testo);

                    }
                    MainActivity.remplaceFurto(f);
                }
                break;
        }
    }






    /**************************************************************
     ** Funzioni che modificano le URL aggiungendo i parametri   **
     ** neccessari per communicare con il Database               **
     **************************************************************/
    //TODO: Parametri opzionali



    /* Lista commissariati e stazioni carabinieri vicine
    * parametri in POST deviceid model version lat lon */
    private String policeURL(String url)
    {
        if(!url.endsWith("?"))
            url += "?";
        List<BasicNameValuePair> nameValuePairs = new ArrayList<>(1);
        nameValuePairs.add(new BasicNameValuePair("deviceid", deviceid));
        nameValuePairs.add(new BasicNameValuePair("model", model));
        nameValuePairs.add(new BasicNameValuePair("version", "1.0"));
        nameValuePairs.add(new BasicNameValuePair("lat", String.valueOf(initLat)));
        nameValuePairs.add(new BasicNameValuePair("lon",  String.valueOf(initLon)));

        String paramString = URLEncodedUtils.format(nameValuePairs, "utf-8");
        url += paramString;

        return url;
    }

    /* Inseririmento di un nuovo furto.
   * parametri in POST: deviceid model version lat lon categoria titolo data fascia description*/
    private String addfurtoURL(String url, Furto f)
    {
        if(!url.endsWith("?"))
            url += "?";

        List<BasicNameValuePair> nameValuePairs = new ArrayList<>(1);
        nameValuePairs.add(new BasicNameValuePair("deviceid", deviceid));
        nameValuePairs.add(new BasicNameValuePair("model", model));
        nameValuePairs.add(new BasicNameValuePair("version", "1.0"));
        nameValuePairs.add(new BasicNameValuePair("lat", String.valueOf(f.mLatitude)));
        nameValuePairs.add(new BasicNameValuePair("lon",  String.valueOf(f.mLongitude)));
        nameValuePairs.add(new BasicNameValuePair("categoria", f.mTipo));
        nameValuePairs.add(new BasicNameValuePair("titolo", "Titolo furto"));
        nameValuePairs.add(new BasicNameValuePair("data", f.mDate));
        nameValuePairs.add(new BasicNameValuePair("fascia", f.mOra));
        nameValuePairs.add(new BasicNameValuePair("description", f.mDescrizione));

        String paramString = URLEncodedUtils.format(nameValuePairs, "utf-8");
        url += paramString;

        return url;
    }

    /*Restituisce una lista di 10 furti
    * parametri in GET deviceid model version lat lon [categoria] [idfurto] [start]**/
    private String furtiURL(String url){
        if(!url.endsWith("?"))
            url += "?";
        List<BasicNameValuePair> nameValuePairs = new ArrayList<>(1);
        nameValuePairs.add(new BasicNameValuePair("deviceid", deviceid));
        nameValuePairs.add(new BasicNameValuePair("model", model));
        nameValuePairs.add(new BasicNameValuePair("version", "1.0"));
        nameValuePairs.add(new BasicNameValuePair("lat", String.valueOf(initLat)));
        nameValuePairs.add(new BasicNameValuePair("lon",  String.valueOf(initLon)));

        String paramString = URLEncodedUtils.format(nameValuePairs, "utf-8");
        url += paramString;

        return url;
    }

    private String favURL(String url) {
     /* fav.php Per ricevere la lista delle zone preferite
    parametri in POST deviceid model version
*/   if(!url.endsWith("?"))
            url += "?";
        List<BasicNameValuePair> nameValuePairs = new ArrayList<>(1);
        nameValuePairs.add(new BasicNameValuePair("deviceid", deviceid));
        nameValuePairs.add(new BasicNameValuePair("model", model));
        nameValuePairs.add(new BasicNameValuePair("version", "1.0"));

        String paramString = URLEncodedUtils.format(nameValuePairs, "utf-8");
        url += paramString;

        return url;
    }


    /*   fav.php Per aggiornare la lista delle zone preferite
    parametri in POST deviceid model version [favlist]
    Dove: favlist json contenente un elenco di coordinate lat,lon: se presente aggiorna i preferiti dell'utente sul server.
    Esempio: &favlist=[{"name":"casa","lat":42,"lon":12}, {"name":"ufficio","lat":43,"lon":13}]*/
    private String newfavURL(String url, List<Favoriti> arrayfav) {
        JSONArray fav=new JSONArray();
        try {
            for (int i=0;i<arrayfav.size();i++)
            {   JSONObject newfav = new JSONObject();

                Favoriti favoriti=arrayfav.get(i);
                newfav.put("name", favoriti.getNome());
                newfav.put("lat", favoriti.mLatitude);
                newfav.put("lon", favoriti.mLongitude);
                fav.put(newfav);
            }

        } catch (JSONException e) {e.printStackTrace();}

        try{   url=favURL(url)+"&favlist="+URLEncoder.encode(fav.toString(),"UTF-8");}
        catch (UnsupportedEncodingException e) { e.printStackTrace();}
        return url;
    }


    /*
*Questo script va chiamato per scaricare i commenti di un furto
*parametri in POST: deviceid model version [idfurto] [start]
*/
    private String getCommentiURL(String url, int fId){
        if(!url.endsWith("?"))
            url += "?";
        List nameValuePairs = new ArrayList(1);
        nameValuePairs.add(new BasicNameValuePair("deviceid", String.valueOf(deviceid)));
        nameValuePairs.add(new BasicNameValuePair("model", String.valueOf(model)));
        nameValuePairs.add(new BasicNameValuePair("version", "1.0"));
        nameValuePairs.add(new BasicNameValuePair("idfurto", String.valueOf(fId)));

        String paramString = URLEncodedUtils.format(nameValuePairs, "utf-8");
        url += paramString;
        return url;
    }
    /*
    *Questo script va chiamato per inserire un commento a un furto.
    *parametri in POST: deviceid model version idfurto testo
     */
    private String addCommentoURL(String url, int fId, String testo){
        if(!url.endsWith("?"))
            url += "?";
        List nameValuePairs = new ArrayList(1);
        nameValuePairs.add(new BasicNameValuePair("deviceid", String.valueOf(deviceid)));
        nameValuePairs.add(new BasicNameValuePair("model", String.valueOf(model)));
        nameValuePairs.add(new BasicNameValuePair("version", "1.0"));
        nameValuePairs.add(new BasicNameValuePair("idfurto", String.valueOf(fId)));
        nameValuePairs.add(new BasicNameValuePair("testo",  testo));

        String paramString = URLEncodedUtils.format(nameValuePairs, "utf-8");
        url += paramString;
        return url;
    }


}

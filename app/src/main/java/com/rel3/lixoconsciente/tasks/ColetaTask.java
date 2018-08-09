package com.rel3.lixoconsciente.tasks;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.rel3.lixoconsciente.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Felipe on 11/10/2016.
 */
public class ColetaTask extends AsyncTask<String, String, String> {

    private String retorno;
    private Activity activity;
    private ProgressDialog progress;
    private Context context;

    public ColetaTask(Activity activity, Context context){
        this.activity = activity;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        progress = new ProgressDialog(context);
        progress.setMessage("Buscando Pontos de Coleta...");
        progress.show();
    }

    @Override
    protected String doInBackground(String... params) {
        String retorno = null;

        try {
            URL url = new URL(params[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestProperty("content-Type", "application/json");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestMethod("GET");

            int statusHttp = conn.getResponseCode();

            StringBuilder sb = new StringBuilder();

            if(statusHttp == HttpURLConnection.HTTP_OK || statusHttp == HttpURLConnection.HTTP_CREATED){
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String linha = null;

                while((linha = reader.readLine()) != null){
                    sb.append(linha + "\n");
                }

                publishProgress("Pontos encontrados, abrindo mapa...");

                reader.close();

                retorno = sb.toString();

                Log.d("SAÍDA DE DADOS:", retorno);
            }


        } catch(Exception e){
            e.printStackTrace();
            Log.e("ERRO", e.getMessage());
        }

        return retorno;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        progress.setMessage(values[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        this.setRetorno(s);
        MapFragment mapFragment = (MapFragment) getFragmentManager();
        mapFragment.getMapAsync((OnMapReadyCallback) this.activity);

        progress.dismiss();
    }

    public Fragment getFragmentManager() {
        return this.activity.getFragmentManager().findFragmentById(R.id.map);
    }

    public String getRetorno() {
        return retorno;
    }

    public void setRetorno(String retorno) {
        this.retorno = retorno;
    }
}

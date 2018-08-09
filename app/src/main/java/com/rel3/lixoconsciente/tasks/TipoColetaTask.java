package com.rel3.lixoconsciente.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.rel3.lixoconsciente.model.TipoColeta;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Felipe on 12/10/2016.
 */
public class TipoColetaTask extends AsyncTask<String, Void, String> {

    private Activity activity;
    private Spinner spTipos;

    public TipoColetaTask(Activity activity, Spinner spinner){
        this.activity = activity;
        this.spTipos = spinner;
    }

    @Override
    protected String doInBackground(String... params) {
        String retorno= null;

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

                reader.close();

                retorno = sb.toString();

                Log.d("SAÍDA DE DADOS:", retorno);
            }


        } catch (Exception e){

        }

        return retorno;
    }

    @Override
    protected void onPostExecute(String s) {
        try {
            JSONArray lista = new JSONArray(s);
            TipoColeta coleta = new TipoColeta();
            List<TipoColeta> coletas = new ArrayList<>();
            JSONObject obj;

            coleta.setCodigo(0);
            coleta.setDescricao("Todos");
            coletas.add(coleta);

            for (int i = 0; i < lista.length(); i++){
                obj = lista.getJSONObject(i);
                coleta = new TipoColeta();
                coleta.setCodigo(obj.getInt("TipoColetaId"));
                coleta.setDescricao(obj.getString("Descricao"));

                coletas.add(coleta);
            }

            ArrayAdapter<TipoColeta> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, coletas);
            spTipos.setAdapter(adapter);

        } catch (Exception e){
            e.printStackTrace();
            Log.e("DEU ERRO", e.getMessage());
        }
    }
}

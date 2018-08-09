package com.rel3.lixoconsciente.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.rel3.lixoconsciente.view.MapaActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class EnderecoTask extends AsyncTask<String, String, String> {

    private Activity activity;
    private ProgressDialog progress;
    private Context context;

    public EnderecoTask(Activity activity, Context context){
        this.activity = activity;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        //super.onPreExecute();
        progress = new ProgressDialog(context);
        progress.setMessage("Buscando o endereço...");
        progress.show();
    }

    @Override
    protected String doInBackground(String... params) {
        String retorno = null;
        try {
            URL url = new URL(params[0]);

            HttpURLConnection client = (HttpURLConnection) url.openConnection();

            client.setRequestMethod("GET");

            // Exibe os dados retornados pela requisicao POST
            StringBuilder sb = new StringBuilder();

            int statusCodeHTTP = client.getResponseCode();

            if (statusCodeHTTP == HttpURLConnection.HTTP_OK) {

                BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream(), "utf-8"));
                String line = null;

                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }

                br.close();

                System.out.println(sb.toString());

                retorno = sb.toString();
            } else {
                BufferedReader br = new BufferedReader(new InputStreamReader(client.getErrorStream(), "utf-8"));
                String line = null;

                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }

                br.close();

                System.out.println(sb.toString());
            }
        } catch (Exception e){
            Log.e("ERRO:","Não foi possível completar a requisição:\n" + e.getMessage());
        }

        return retorno;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        //super.onProgressUpdate(values);
        progress.setMessage(values[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        try {
            JSONObject ob = new JSONObject(s);
            JSONArray lista = ob.getJSONArray("results");
            JSONObject obj6 = lista.getJSONObject(0);
            JSONObject obj7 = obj6.getJSONObject("geometry");
            JSONObject obj8 = obj7.getJSONObject("location");

            //JSONObject obj6 = new JSONObject(s);
            double latitude = obj8.getDouble("lat");
            double longitude = obj8.getDouble("lng");

            Intent intent = new Intent(activity, MapaActivity.class);
            intent.putExtra("lat", latitude);
            intent.putExtra("lon", longitude);
            activity.startActivity(intent);

            progress.dismiss();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}

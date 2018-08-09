package com.rel3.lixoconsciente.tasks;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.rel3.lixoconsciente.R;
import com.rel3.lixoconsciente.view.MapaActivity;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ReadTask extends AsyncTask<String, String, String>{

    private String retorno;
    private Activity activity;
    private ProgressDialog progress;
    private Context context;


    public ReadTask(Activity activity, Context context){
        this.activity = activity;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        progress = new ProgressDialog(context);
        progress.setMessage("Buscando o caminhão...");
        progress.show();
    }

    @Override
    protected String doInBackground(String... params) {
        StringBuilder sb = new StringBuilder();

        try {
            URL url = new URL(params[0]);
            //URL url = new URL("http://0f9f7d9e-e5e3-46c3-8bf6-4653bd13940e-bluemix.cloudant.com/lixoconsciente/_find");
            HttpURLConnection client = (HttpURLConnection) url.openConnection();
            //String json = "{\"selector\": { \"devCode\":1, \"timestamp\":{\"$eq\":0} },\"sort\": [{\"timestamp\": \"desc\"}],\"limit\": 50 }";
//            String json = "{\n" +
//                    "  \"selector\": {\n" +
//                    "    \"devCode\": {\n" +
//                    "      \"$eq\": \"REL-2016\"\n" +
//                    "    }\n" +
//                    "  },\n" +
//                    "  \"fields\": [\n" +
//                    "    \"devCode\",\n" +
//                    "    \"latitude\",\n" +
//                    "    \"longitude\"\n" +
//                    "  ],\"limit\": 1\n" +
//                    "}";

            String json = "{\n" +
                    "  \"selector\": {\n" +
                    "    \"_id\": {\n" +
                    "      \"$gt\": 0\n" +
                    "    }\n" +
                    "  },\"sort\": [{" +
                    "       \"_id\": \"desc\"" +
                    "}],\n" +
                    "  \"fields\": [\n" +
                    "    \"devCode\",\n" +
                    "    \"latitude\",\n" +
                    "    \"longitude\"\n" +
                    "  ]\n" +
                    "}";

            Log.d("JSON para filtro: ", json.toString());

            client.setRequestProperty("content-Type", "application/json");
            client.setRequestProperty("Accept-Charset", "UTF-8");
            client.setRequestMethod("GET");
            client.setDoOutput(true);
            client.setDoInput(true);

            //String username = "2d52a7b8-f952-496a-a652-f7f7e72e59a3-bluemix";
            //String password = "31205f873545ef6e8d65a13ef411cc3164a5e7b27d972232d6ebf2257a4e9fd5";

            String username = "63411d47-31ef-4f7d-aa82-fbf559c53536-bluemix";
            String password = "666a2d9457469a4f82317596cd307be21aacb9b0c438196df3d0aad2ee000ba3";


            String encodedPassword = username + ":" + password;
            String encoded = Base64.encodeToString(encodedPassword.getBytes(), Base64.NO_WRAP);
            client.setRequestProperty("Authorization", "Basic " + encoded);

            // Realiza a gravacao dos dados de entrada para a requisicao POST
            OutputStreamWriter wr = new OutputStreamWriter(client.getOutputStream());
            wr.write(json.toString());
            wr.close();

            int statusCodeHTTP = client.getResponseCode();

            if (statusCodeHTTP == HttpURLConnection.HTTP_OK) {

                BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream(), "utf-8"));
                String line = null;

                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }

                publishProgress("Dados localizados!");

                br.close();

                Log.d("Saída de Dados: ", sb.toString());
            } else {
                BufferedInputStream in = new BufferedInputStream(client.getErrorStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    Log.e("Erro", line);
                }
                reader.close();
                Log.e("Código do Erro: " , statusCodeHTTP + "\nMensagem de Erro: "
                        + client.getResponseMessage().toString() + "\n" + client.getErrorStream().toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Erro ", e.getMessage());
        }

        return sb.toString();
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

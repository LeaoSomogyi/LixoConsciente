package com.rel3.lixoconsciente.tasks;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Base64;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.rel3.lixoconsciente.R;
import com.rel3.lixoconsciente.util.Util;
import com.rel3.lixoconsciente.view.HomeActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.TimerTask;

public class LocalizacaoTask extends TimerTask implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private double minhaLatitude;
    private double minhaLongitude;
    private double latitude;
    private double longitude;
    private Context context;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLastLocation;

    private static String TAG = HomeActivity.class.getSimpleName();

    public LocalizacaoTask(Context context){
        this.context = context;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Location loc = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if(loc == null)
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLastLocation, this);
        else{
            minhaLatitude = loc.getLatitude();
            minhaLongitude = loc.getLongitude();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
    }

    @Override
    public void onLocationChanged(Location location) {
        minhaLatitude = location.getLatitude();
        minhaLongitude = location.getLongitude();
    }

    @Override
    public void run() {

        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        mLastLocation = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(60 * 1000)
                .setFastestInterval(30 * 1000);


        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }

        boolean temCaminhao = false;

        try {
            Log.d("Serviço:", "Iniciado!");

            URL url = new URL("http://63411d47-31ef-4f7d-aa82-fbf559c53536-bluemix.cloudant.com/lixo_consciente_hml/_find");
            //URL url = new URL("http://0f9f7d9e-e5e3-46c3-8bf6-4653bd13940e-bluemix.cloudant.com/lixoconsciente/_find");
            HttpURLConnection client = (HttpURLConnection) url.openConnection();
            //String json = "{\"selector\": { \"devCode\":1, \"timestamp\":{\"$gt\":0} },\"sort\": [{\"timestamp\": \"desc\"}],\"limit\": 50 }";
//            String json = "{\n" +
//                    "  \"selector\": {\n" +
//                    "    \"devCode\": {\n" +
//                    "      \"$eq\": 74791\n" +
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

            StringBuilder sb = new StringBuilder();

            int statusCodeHTTP = client.getResponseCode();

            if (statusCodeHTTP == HttpURLConnection.HTTP_OK) {

                BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream(), "utf-8"));
                String line = null;

                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }

                br.close();

                Log.d("Saída de Dados: ", sb.toString());
            }

            JSONObject obj = new JSONObject(sb.toString());
            JSONArray lista = obj.getJSONArray("docs");

            for (int i = 0; i < lista.length(); i++) {
                JSONObject map = lista.getJSONObject(i);
                latitude = map.getDouble("latitude");
                longitude = map.getDouble("longitude");

                double distancia = Util.calculaDistancia(minhaLatitude, minhaLongitude, latitude, longitude);

                if(distancia < 2000){
                    temCaminhao = true;
                    break;
                }
            }

//            JSONObject map = lista.getJSONObject(0);
//            latitude = map.getDouble("latitude");
//            longitude = map.getDouble("longitude");

            Log.d("latitude do celular", minhaLatitude + "");
            Log.d("longitude do celular", minhaLongitude + "");

            //double resultado = Util.calculaDistancia(minhaLatitude, minhaLongitude, latitude, longitude);

            if(temCaminhao == true){
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(context)
                                .setSmallIcon(R.drawable.notificacao)
                                .setContentTitle(context.getResources().getString(R.string.notificacaoTitulo))
                                .setContentText(context.getResources().getText(R.string.notificacaoTexto))
                                .setAutoCancel(true);

                Intent resultIntent = new Intent(context, HomeActivity.class);

                PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
//                stackBuilder.addParentStack(HomeActivity.class);
//                stackBuilder.addNextIntent(resultIntent);
//                PendingIntent resultPendingIntent =
//                        stackBuilder.getPendingIntent(
//                                0,
//                                PendingIntent.FLAG_UPDATE_CURRENT
//                        );
                mBuilder.setContentIntent(resultPendingIntent);

                NotificationManager mNotificationManager =
                        (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(13, mBuilder.build());
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}

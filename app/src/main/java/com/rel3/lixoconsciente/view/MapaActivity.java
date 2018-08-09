package com.rel3.lixoconsciente.view;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rel3.lixoconsciente.R;
import com.rel3.lixoconsciente.model.Coordenadas;
import com.rel3.lixoconsciente.services.NotificacaoService;
import com.rel3.lixoconsciente.tasks.ReadTask;
import com.rel3.lixoconsciente.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MapaActivity extends FragmentActivity implements OnMapReadyCallback {

    private double caminhaoLatitude;
    private double caminhaoLongitude;
    private double latitude;
    private double longitude;
    private ReadTask read;
    private boolean temCaminhao;
    private String placa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        Intent i = getIntent();
        Bundle aux = i.getExtras();

        setLatitude(aux.getDouble("lat"));
        setLongitude(aux.getDouble("lon"));

        Intent intent = new Intent(this, NotificacaoService.class);
        //intent.putExtra("lat", aux.getDouble("lat"));
        //intent.putExtra("lon", aux.getDouble("lon"));
//        intent.putExtra("lat", -23.5654217);
//        intent.putExtra("lon", -46.8129943);
//        Coordenadas.setLatitude(aux.getDouble("lat"));
//        Coordenadas.setLongitude(aux.getDouble("lon"));
//        Coordenadas.setLatitude(-23.5654217);
//        Coordenadas.setLongitude(-46.8129943);
        startService(intent);

        read = new ReadTask(this, MapaActivity.this);
        read.execute("http://63411d47-31ef-4f7d-aa82-fbf559c53536-bluemix.cloudant.com/lixo_consciente_hml/_find");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }



    private void ler() {

        try {
            JSONObject obj = new JSONObject(read.getRetorno());
            JSONArray lista = obj.getJSONArray("docs");

            for (int i = 0; i < lista.length(); i++){
                JSONObject map = lista.getJSONObject(i);
                double lat = map.getDouble("latitude");
                double lon = map.getDouble("longitude");
                placa = map.getString("devCode");

                double distancia = Util.calculaDistancia(this.getLatitude(), this.getLongitude(), lat, lon);

                if(distancia < 2000){
                    this.setCaminhaoLatitude(lat);
                    this.setCaminhaoLongitude(lon);
                    temCaminhao = true;
                    break;
                } else {
                    temCaminhao = false;
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        ler();

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setTrafficEnabled(true);
        googleMap.setIndoorEnabled(true);
        googleMap.setBuildingsEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(this.getLatitude(), this.getLongitude()))
                .title("Você está aqui!")
                .draggable(false)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.thrashman)));


        //double dist = Util.calculaDistancia(this.getLatitude(),this.getLongitude(), this.getCaminhaoLatitude(),this.getCaminhaoLongitude());

        if(temCaminhao == false){
            Toast.makeText(getApplicationContext(), "O caminhão está muito longe de você!", Toast.LENGTH_LONG).show();
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(this.getLatitude(), this.getLongitude()), 19));
        } else {
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(this.getCaminhaoLatitude(), this.getCaminhaoLongitude()))
                    .title("PLACA: " + placa)
                    .draggable(false)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icone_caminhao)));
            //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(this.getCaminhaoLatitude(), this.getCaminhaoLongitude()), 19));
        }

    }


    public double getCaminhaoLatitude() {
        return caminhaoLatitude;
    }

    public void setCaminhaoLatitude(double caminhaoLatitude) {
        this.caminhaoLatitude = caminhaoLatitude;
    }

    public double getCaminhaoLongitude() {
        return caminhaoLongitude;
    }

    public void setCaminhaoLongitude(double caminhaoLongitude) {
        this.caminhaoLongitude = caminhaoLongitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}

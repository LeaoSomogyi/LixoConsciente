package com.rel3.lixoconsciente.view;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rel3.lixoconsciente.R;
import com.rel3.lixoconsciente.model.Coleta;
import com.rel3.lixoconsciente.tasks.ColetaTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ColetaActivity extends FragmentActivity implements OnMapReadyCallback {

    private double latitude;
    private double longitude;
    private ColetaTask coleta;
    private List<Coleta> coletas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coleta);

        Intent i = getIntent();
        Bundle aux = i.getExtras();

        int codigo = aux.getInt("codigo");
        String raio = aux.getString("distancia");
        setLatitude(aux.getDouble("lat"));
        setLongitude(aux.getDouble("lon"));

        coleta = new ColetaTask(this, ColetaActivity.this);
        if(codigo == 0)
            coleta.execute("http://189.108.12.218:8889/WsColeta/api/Empresa/GetPorTipoColeta/?raio="+raio+"&lat="+getLatitude()+"&log="+getLongitude());
        else
            coleta.execute("http://189.108.12.218:8889/WsColeta/api/Empresa/GetPorTipoColeta/?idTipo="+codigo+"&raio="+raio+"&lat="+getLatitude()+"&log="+getLongitude());

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

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


        coletas = new ArrayList<Coleta>();
        Coleta c = null;
        try {
            JSONArray lista = new JSONArray(coleta.getRetorno());
            JSONObject obj;

            for (int i = 0; i < lista.length(); i++) {
                obj = lista.getJSONObject(i);
                c = new Coleta();

                c.setLatitude(obj.getDouble("Latitude"));
                c.setLongitude(obj.getDouble("Longitude"));
                c.setCobraColeta(obj.getBoolean("CobraColeta"));
                c.setColetaDomicilio(obj.getBoolean("ColetaDomicilio"));
                c.setEmail(obj.getString("Email"));
                c.setEmpresaId(obj.getInt("EmpresaId"));
                c.setEndereco(obj.getString("Endereco"));
                c.setFone(obj.getString("Fone"));
                c.setNome(obj.getString("Nome"));
                c.setNumero(obj.getString("Numero"));
                c.setSite(obj.getString("Site"));
                c.setTipoColeta(obj.getString("TipoColeta"));
                c.setTipoColetaId(obj.getInt("TipoColetaId"));
                c.setTipoFone(obj.getString("TipoFone"));

                coletas.add(c);
            }

        } catch (Exception e){
            e.printStackTrace();
            Log.e("ERRO", e.getMessage());
        }

        if(coletas.size() > 0){
            for (Coleta col : coletas) {
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                googleMap.setTrafficEnabled(true);
                googleMap.setIndoorEnabled(true);
                googleMap.setBuildingsEnabled(true);
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(col.getLatitude(), col.getLongitude()))
                        .title(col.getTipoColeta())
                        .draggable(false)
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icone_coleta)));
            }
        } else {
            Toast.makeText(this, "Não há locais de coleta próximos à você!", Toast.LENGTH_LONG).show();
        }

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(this.getLatitude(), this.getLongitude()), 19));


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}

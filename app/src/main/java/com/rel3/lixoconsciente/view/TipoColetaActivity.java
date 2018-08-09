package com.rel3.lixoconsciente.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.rel3.lixoconsciente.R;
import com.rel3.lixoconsciente.model.TipoColeta;
import com.rel3.lixoconsciente.tasks.TipoColetaTask;

import java.util.ArrayList;
import java.util.List;

public class TipoColetaActivity extends AppCompatActivity {

    private double latitude;
    private double longitude;
    private Spinner spTipos;
    private Spinner spKm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipo_coleta);

        Intent i = getIntent();
        Bundle aux = i.getExtras();

        setLatitude(aux.getDouble("lat"));
        setLongitude(aux.getDouble("lon"));

        spTipos = (Spinner)findViewById(R.id.spTipos);
        spKm = (Spinner) findViewById(R.id.spKm);

        carregaKm();

        TipoColetaTask task = new TipoColetaTask(this, spTipos);
        task.execute("http://189.108.12.218:8889/WsColeta/api/TipoColetas/GetTipoColetas");

    }

    private void carregaKm(){
        List<String> distancias = new ArrayList<>();

        for (int i = 1; i < 16; i++){
            distancias.add(String.valueOf(i));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, distancias);
        spKm.setAdapter(adapter);
    }

    public void buscarColetas(View v){
        TipoColeta tipo = (TipoColeta) spTipos.getSelectedItem();
        String distancia = (String)spKm.getSelectedItem();
        Intent intent = new Intent(this, ColetaActivity.class);

        if(tipo == null || tipo.getCodigo() == 0){
            intent.putExtra("lat", getLatitude());
            intent.putExtra("lon", getLongitude());
            intent.putExtra("distancia", distancia);
            intent.putExtra("codigo", 0);
        } else {
            intent.putExtra("lat", getLatitude());
            intent.putExtra("lon", getLongitude());
            intent.putExtra("distancia", distancia);
            intent.putExtra("codigo", tipo.getCodigo());
        }

        startActivity(intent);
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}

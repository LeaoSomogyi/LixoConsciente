package com.rel3.lixoconsciente.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

import com.rel3.lixoconsciente.R;

public class SplashScreenActivity extends Activity {

    private static int TIME_OUT = 4000;
    private final Context context = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                verificaStatusGPS();
            }
        }, TIME_OUT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1){
            verificaStatusGPS();
        }
    }

    public void verificaStatusGPS(){
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean internet_ligada;

        internet_ligada = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!internet_ligada) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setMessage(R.string.gps_desligado);
            dialog.setPositiveButton(R.string.btnSim, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(i, 1);
                }
            });
            dialog.setNegativeButton(R.string.btnNao, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            dialog.show();
        } else {
            Intent i = new Intent(SplashScreenActivity.this, HomeActivity.class);
            startActivity(i);

            finish();
        }
    }
}

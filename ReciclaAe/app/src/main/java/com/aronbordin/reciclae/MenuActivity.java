package com.aronbordin.reciclae;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.aronbordin.reciclae.adapter.Ponto;
import com.dd.processbutton.iml.ActionProcessButton;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener, LocationListener {
    private static final int CAT_OLEO = 1;
    private static final int CAT_PILHAS = 2;
    private static final int CAT_PLASTICO = 3;
    private static final int CAT_METAL = 4;
    private static final int CAT_PAPEL = 5;
    private static final int CAT_VIDRO = 6;
    private static final String GET_MAKERS = "http://reciclaae.aronbordin.com/api/pontos/?format=json&categorias=";
    private static Location location;
    private boolean isGPSEnabled = false;
    private boolean isNetEnabled = false;
    private LocationManager locationManager;
    private static boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        initTracking();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        locationManager.removeUpdates(MenuActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initTracking();
    }

    private void initTracking(){
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("GPS");
                builder.setMessage("Este aplicativo precisa ter o GPS ativo para uma melhor utilização!");
                builder.create().show();
                return;
            }
        }

        if (!isNetEnabled && !isGPSEnabled) {
            noTracking();
        } else {
            if (isGPSEnabled) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            } else {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
        }
    }

    private void noTracking() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Atenção!");
        builder.setMessage("Para um bom funcionamento do aplicativo, você precisa ativar seu GPS");
        builder.create().show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnOleo:
                getCategoria(CAT_OLEO, (ActionProcessButton) view);
                break;
            case R.id.btnPilha:
                getCategoria(CAT_PILHAS, (ActionProcessButton) view);
                break;
            case R.id.btnPlastico:
                getCategoria(CAT_PLASTICO, (ActionProcessButton) view);
                break;
            case R.id.btnMetal:
                getCategoria(CAT_METAL, (ActionProcessButton) view);
                break;
            case R.id.btnPapel:
                getCategoria(CAT_PAPEL, (ActionProcessButton) view);
                break;
            case R.id.btnVidro:
                getCategoria(CAT_VIDRO, (ActionProcessButton) view);
                break;
            case R.id.imgHelp:
                help();
                break;
            case R.id.btnTodos:
                getCategoria(-1, (ActionProcessButton) view);
        }

    }

    private void help() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sobre");
        builder.setView(getLayoutInflater().inflate(R.layout.help, null));
        builder.setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.create().show();
    }

    private void getCategoria(int cat, final ActionProcessButton btn) {
        if(isLoading)
            return;
        isLoading = true;
        btn.setProgress(1);

        String url;
        if(cat == -1){
            url = GET_MAKERS.replace("&categorias=", "");
        } else {
            url = GET_MAKERS + cat;
        }
        Ion.with(getApplicationContext())
                .load(url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @TargetApi(Build.VERSION_CODES.M)
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        isLoading = false;
                        btn.setProgress(0);
                        if (e != null) {
                            Toast.makeText(getApplicationContext(), "Falha ao buscar pontos de coleta!", Toast.LENGTH_LONG).show();
                            return;
                        }

                        JsonArray makers = result.getAsJsonArray("results");

                        if (makers.size() == 0) {
                            Toast.makeText(getApplicationContext(), "Nenhum ponto encontrado :(", Toast.LENGTH_LONG).show();
                            return;
                        }

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            }
                        }

                        locationManager.removeUpdates(MenuActivity.this);
                        ArrayList<Ponto> pontos;
                        Type type = new TypeToken<ArrayList<Ponto>>() {}.getType();
                        pontos = new Gson().fromJson(makers, type);
                        Intent intent = new Intent(MenuActivity.this, MapsActivity.class);
                        intent.putParcelableArrayListExtra("PONTOS", pontos);
                        intent.putExtra("LOCATION", location);
                        startActivity(intent);
                    }
                });
    }

    @Override
    public void onLocationChanged(Location new_location) {
        if(location != null){
            if(location.getAccuracy() > new_location.getAccuracy()){
                location = new_location;
            }
        } else {
            location = new_location;
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}

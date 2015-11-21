package com.aronbordin.reciclaae;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.aronbordin.reciclaae.adapter.Ponto;
import com.dd.processbutton.iml.ActionProcessButton;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener{
    private static final int CAT_OLEO = 1;
    private static final int CAT_PILHAS = 2;
    private static final int CAT_PLASTICO = 3;
    private static final int CAT_METAL = 4;
    private static final int CAT_PAPEL = 5;
    private static final int CAT_VIDRO = 6;
    private static final String GET_MAKERS = "http://192.168.0.20:8000/api/pontos/?format=json&categorias=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
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

    private void getCategoria(int cat, final ActionProcessButton btn){
        btn.setProgress(1);

        Ion.with(getApplicationContext())
                .load(GET_MAKERS + cat)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        btn.setProgress(0);
                        if(e != null){
                            Toast.makeText(getApplicationContext(), "Falha ao buscar pontos de coleta!", Toast.LENGTH_LONG).show();
                            return;
                        }

                        JsonArray makers = result.getAsJsonArray("results");

                        if(makers.size() == 0){
                            Toast.makeText(getApplicationContext(), "Nenhum ponto encontrado :(", Toast.LENGTH_LONG).show();
                            return;
                        }

                        ArrayList<Ponto> pontos;
                        Type type = new TypeToken<ArrayList<Ponto>>() {}.getType();
                        pontos = new Gson().fromJson(makers, type);
                        Intent intent = new Intent(MenuActivity.this, MapsActivity.class);
                        intent.putParcelableArrayListExtra("PONTOS", pontos);
                        startActivity(intent);
                    }
                });
    }
}

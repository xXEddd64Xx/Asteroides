package com.example.asteroides;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

public class Joc extends AppCompatActivity {
    private VistaJoc vistaJoc;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joc);

        vistaJoc = (VistaJoc) findViewById(R.id.VistaJoc);
        pref = PreferenceManager.getDefaultSharedPreferences(vistaJoc.getContext());

    }

    @Override
    protected void onPause() {
        super.onPause();
        vistaJoc.getThread().pausar();
        if (pref.getString("controls", "1").equals("2")) {
            vistaJoc.desactivarSensors();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        vistaJoc.getThread().reprendre();
        if (pref.getString("controls", "1").equals("2")) {
            vistaJoc.activarSensors(vistaJoc.getContext());
        }
    }
    @Override
    protected void onDestroy() {
        if (pref.getString("controls", "1").equals("2")) {
            vistaJoc.desactivarSensors();
        }
        vistaJoc.getThread().detenir();
        super.onDestroy();
    }
}
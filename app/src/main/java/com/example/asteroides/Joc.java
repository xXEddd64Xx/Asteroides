package com.example.asteroides;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class Joc extends AppCompatActivity {
    private VistaJoc vistaJoc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joc);

        vistaJoc = (VistaJoc) findViewById(R.id.VistaJoc);
    }

    @Override
    protected void onPause() {
        super.onPause();
        vistaJoc.desactivarSensors();
        vistaJoc.getThread().pausar();
    }
    @Override
    protected void onResume() {
        super.onResume();
        vistaJoc.activarSensors();
        vistaJoc.getThread().reprendre();
    }
    @Override
    protected void onDestroy() {
        vistaJoc.desactivarSensors();
        vistaJoc.getThread().detenir();
        super.onDestroy();
    }
}
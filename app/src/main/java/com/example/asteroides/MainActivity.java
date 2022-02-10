package com.example.asteroides;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    MediaPlayer mp;
    int pos;
    public static MagatzemPuntuacions magatzem= new MagatzemPuntuacionsList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mp = MediaPlayer.create(this, R.raw.menumusic);
        mp.start();

        setContentView(R.layout.activity_main);
        /*TextView text = (TextView) findViewById(R.id.titolAsteroides);
        Animation animacio = AnimationUtils.loadAnimation(this,
                R.anim.apareixer);
        text.startAnimation(animacio);*/

        Button btnPlay = (Button) findViewById(R.id.button);
        Animation botoPlay = AnimationUtils.loadAnimation(this,
                R.anim.apareixer);
        btnPlay.startAnimation(botoPlay);

        Button btnSett = (Button) findViewById(R.id.button2);
        Animation botoSett = AnimationUtils.loadAnimation(this,
                R.anim.despl_dreta);
        btnSett.startAnimation(botoSett);

        Button btnAbout = (Button) findViewById(R.id.button3);
        Animation botoAbout = AnimationUtils.loadAnimation(this,
                R.anim.prova1);
        btnAbout.startAnimation(botoAbout);

        Button btnScore = (Button) findViewById(R.id.button4);
        Animation botoScore = AnimationUtils.loadAnimation(this,
                R.anim.prova1);
        btnScore.startAnimation(botoScore);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mp.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mp.seekTo(pos);
    }

    @Override
    protected void onPause() {
        super.onPause();
        pos = mp.getCurrentPosition();
        mp.pause();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        pos = mp.getCurrentPosition();
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mp.seekTo(pos);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1,menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.preferencies){
            //arrancar activitat preferències
            iniciarPreferencies(null);
            return true;
        }
        if (id == R.id.sobre){
            //arrancar activitat sobre...
            iniciarSobre(null);
            return true;
        }
        if (id == R.id.info_preferencies){
            //arrancar activitat sobre...
            mostrarPreferencies(null);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void iniciarPuntuacions(View view) {
        Intent i = new Intent(this, Puntuacions.class);
        startActivity(i);
    }

    public void iniciarSobre(View view) {
        Intent i = new Intent(this, Sobre.class);
        startActivity(i);
    }

    public void iniciarPreferencies(View view) {
        Intent i = new Intent(this, PreferenciesActivity.class);
        startActivity(i);
    }

    public void iniciarJoc(View view) {
        Intent i = new Intent(this, Joc.class);
        startActivity(i);
    }

    public void mostrarPreferencies(View view){

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String s = "música: "+ pref.getBoolean("musica",true)
        +", gráfics: " + pref.getString("grafics","?");
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

}
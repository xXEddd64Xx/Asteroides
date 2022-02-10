package com.example.asteroides;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.graphics.drawable.shapes.RectShape;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebHistoryItem;

import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class VistaJoc extends View implements SensorEventListener {
    private float mX = 0;
    private float mY = 0;
    SensorManager mSensorManager;

    // //// MISIL //////
    private List<Grafic> Missils = new ArrayList<Grafic>();
    private static int PAS_VELOCITAT_MISSIL = 12;
    /*private boolean missilActiu = false;*/
    private List<Integer> tempsMissils = new ArrayList<Integer>();
    private Drawable drawableMissil;

    // //// ASTEROIDES //////

    // //// NAU //////
    private Grafic nau; // Gràfic de la nau
    private int girNau; // Angle de gir de la nau
    private float acceleracioNau; // Augment de velocitat

    // //// THREAD I TEMPS //////
    // Thread encarregat de processar el joc
    private ThreadJoc thread = new ThreadJoc();
    // Cada quan volem processar canvis (ms)
    private static int PERIODE_PROCES = 50;
    // Quan es va realitzar el darrer procés
    private long darrerProces = 0;

    // Increment estàndar de gir i acceleració
    private static final int PAS_GIR_NAU = 5;
    private static final float PAS_ACCELERACIO_NAU = 0.5f;
    private static final double MAX_VELOCITAT_NAU = 50;
    private List<Grafic> Asteroides; // Vector amb els Asteroides
    private int numAsteroides= 5; // Número inicial d&#39;asteroides
    private int numFragments = 3; // Fragments en que es divideix

    public VistaJoc(Context context, AttributeSet attrs) {
        super(context, attrs);
        Drawable drawableNave, drawableAsteroide;
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        if (pref.getString("controls", "1").equals("2")) {
            activarSensors(context);
        }

        if (pref.getString("grafics", "1").equals("0")) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            Path pathAsteroide = new Path();
            pathAsteroide.moveTo((float) 0.3, (float) 0.0);
            pathAsteroide.lineTo((float) 0.6, (float) 0.0);
            pathAsteroide.lineTo((float) 0.6, (float) 0.3);
            pathAsteroide.lineTo((float) 0.8, (float) 0.2);
            pathAsteroide.lineTo((float) 1.0, (float) 0.4);
            pathAsteroide.lineTo((float) 0.8, (float) 0.6);
            pathAsteroide.lineTo((float) 0.9, (float) 0.9);
            pathAsteroide.lineTo((float) 0.8, (float) 1.0);
            pathAsteroide.lineTo((float) 0.4, (float) 1.0);
            pathAsteroide.lineTo((float) 0.0, (float) 0.6);
            pathAsteroide.lineTo((float) 0.0, (float) 0.2);
            pathAsteroide.lineTo((float) 0.3, (float) 0.0);
            ShapeDrawable dAsteroide = new ShapeDrawable(new PathShape(pathAsteroide, 1, 1));
            dAsteroide.getPaint().setColor(Color.WHITE);
            dAsteroide.getPaint().setStyle(Paint.Style.STROKE);
            dAsteroide.setIntrinsicWidth(200);
            dAsteroide.setIntrinsicHeight(200);

            // MISSIL //
            ShapeDrawable dMissil = new ShapeDrawable(new RectShape());
            dMissil.getPaint().setColor(Color.WHITE);
            dMissil.getPaint().setStyle(Paint.Style.STROKE);
            dMissil.setIntrinsicWidth(15);
            dMissil.setIntrinsicHeight(3);
            drawableMissil = dMissil;

            drawableAsteroide = dAsteroide;
            setBackgroundColor(Color.BLACK);
            Path pathNau = new Path();
            pathNau.moveTo((float)0.0, (float)0.0);
            pathNau.lineTo((float) 1.0, (float) 0.5);
            pathNau.lineTo((float) 0.0, (float) 1.0);
            pathNau.lineTo((float) 0.0, (float) 0.0);
            ShapeDrawable drawableNau = new ShapeDrawable(new PathShape(pathNau, 1, 1));
            drawableNau.getPaint().setColor(Color.WHITE);
            drawableNau.getPaint().setStyle(Paint.Style.STROKE);
            drawableNau.setIntrinsicWidth(100);
            drawableNau.setIntrinsicHeight(75);

            drawableNave = drawableNau;
            setBackgroundColor(Color.BLACK);
        } else {
            setLayerType(View.LAYER_TYPE_HARDWARE, null);
            drawableAsteroide = ContextCompat.getDrawable(context, R.drawable.asteroide1);
            drawableNave = context.getResources().getDrawable(R.drawable.nau);
            drawableMissil = context.getResources().getDrawable(R.drawable.misil1);
        }

        Asteroides = new ArrayList<Grafic>();
        for (int i = 0; i < numAsteroides; i++) {
            Grafic asteroide = new Grafic(this, drawableAsteroide);
            asteroide.setIncY(Math.random() * 4 - 2);
            asteroide.setIncX(Math.random() * 4 - 2);
            asteroide.setAngle((int) (Math.random() * 360));
            asteroide.setRotacio((int) (Math.random() * 8 - 4));
            Asteroides.add(asteroide);
        }
        nau = new Grafic(this, drawableNave);
        //missil = new Grafic(this, drawableMissil);
    }

    public void activarSensors(Context context) {
        mSensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> listSensors = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if (!listSensors.isEmpty()) {
            Sensor accelerometerSensor = listSensors.get(0);
            mSensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);

        }
    }

    public void desactivarSensors() {
        mSensorManager.unregisterListener(this);
    }

    boolean dispar = false;
    public boolean onTouchEvent (MotionEvent event) {
        super.onTouchEvent(event);
        float x = event.getX();
        float y = event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    dispar = true;
                    break;
                case MotionEvent.ACTION_MOVE:
                    float dx = x - mX;
                    float dy = mY - y;
                    if ((dx>dy || (-dx)>dy) && dx!=0){
                        girNau = (int)dx;
                                dispar = false;
                    } else if (dy>dx && dy>0){
                        acceleracioNau = (int)(dy*0.5);
                    dispar = false;
            }
                    break;
                case MotionEvent.ACTION_UP:
                girNau = 0;
                acceleracioNau = 0;
                if (dispar){
                    ActivaMissil();
                }
                break;
            }
            mX=x; mY=y;
        return true;
        }

    public boolean onKeyDown(int codiTecla, KeyEvent event) {
        super.onKeyDown(codiTecla, event);
        // Processam la pulsación
        boolean processada = true;
        switch (codiTecla) {
            case KeyEvent.KEYCODE_DPAD_UP:
                acceleracioNau = +PAS_ACCELERACIO_NAU;
                break;
            /*case KeyEvent.KEYCODE_DPAD_DOWN:
                acceleracioNau = -PAS_ACCELERACIO_NAU;
                break;  */
            case KeyEvent.KEYCODE_DPAD_LEFT:
                girNau = -PAS_GIR_NAU;
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                girNau = +PAS_GIR_NAU;
                break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                ActivaMissil();
                break;
            default:
        // Si estem aquí, no hi ha pulsació que ens interessi
                processada = false;
                break;
        }
        return processada;
    }

    public boolean onKeyUp(int codiTecla, KeyEvent event) {
        super.onKeyUp(codiTecla, event);
        // Processam la pulsació
        boolean processada = true;
        switch (codiTecla) {
            case KeyEvent.KEYCODE_DPAD_UP:
                acceleracioNau = 0;
                break;
            /*case KeyEvent.KEYCODE_DPAD_DOWN:
                acceleracioNau = -PAS_ACCELERACIO_NAU;
                break;  */
            case KeyEvent.KEYCODE_DPAD_LEFT:
                girNau = 0;
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                girNau = 0;
                break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                ActivaMissil();
                break;
            default:
                // Si estem aquí, no hi ha pulsació que ens interessi
                processada = false;
                break;
        }
        return processada;
    }

    class ThreadJoc extends Thread {
        private boolean pausa, corrent;
        public synchronized void pausar() {
            pausa = true;
        }
        public synchronized void reprendre() {
            pausa = false;
            notify();
        }
        public void detenir() {
            corrent = false;
            if (pausa) reprendre();
        }
        @Override
        public void run() {
            corrent = true;
            while (corrent) {
                actualitzaFisica();
                synchronized (this) {
                    while (pausa)
                        try {
                            wait();
                        } catch (Exception e) {
                            // Excepció de l'app
                        }
                }
            }
        }
    }

    synchronized protected void actualitzaFisica(){
        long ara = System.currentTimeMillis();
        // No fer res fins a final del període
        if (darrerProces + PERIODE_PROCES > ara){
            return;
        }
        // Per a una execució en temps real calculam retard
        double retard = (ara - darrerProces) / PERIODE_PROCES;
        darrerProces = ara;
        // Actualitzam velocitat i direcció de la nau a partir de
        // girNau i acceleracioNau
        nau.setAngle((int) (nau.getAngle() + girNau * retard));
        double nIncX = nau.getIncX() + acceleracioNau *
                Math.cos(Math.toRadians(nau.getAngle())) * retard;
        double nIncY = nau.getIncY() + acceleracioNau *
                Math.sin(Math.toRadians(nau.getAngle())) * retard;
        // Actualitzam si el mòdul de la velocitat no excedeix el màxim
        if (Math.hypot(nIncX,nIncY) <= MAX_VELOCITAT_NAU){
            nau.setIncX(nIncX);
            nau.setIncY(nIncY);
        }
        // Actualitzam posicions X i Y
        nau.incrementaPos(retard);
        for (Grafic asteroide : Asteroides) {
            asteroide.incrementaPos(retard);
        }

        for (int i = 0; i < Missils.size(); i++) {
            Missils.get(i).incrementaPos(retard);
            int tempsMissil = tempsMissils.get(i).intValue();
            tempsMissil -= retard;
            tempsMissils.set(i, Integer.valueOf(tempsMissil));
            if (tempsMissils.get(i).intValue() < 0) {
                Missils.remove(i);
                tempsMissils.remove(i);
            } else {
                for (int m = 0; m < Asteroides.size(); m++)
                    if (Missils.get(i).verificaColisio(Asteroides.get(m))) {
                        destrueixAsteroide(m);
                        Missils.remove(i);
                        tempsMissils.remove(i);
                        break;
                    }
            }
        }

        
    }
    
    @Override protected void onSizeChanged(int ample, int alt, int ample_anter, int alt_anter) {
        super.onSizeChanged(ample, alt, ample_anter, alt_anter);
    // Un cop coneixem el nostre ample i alt.
        for (Grafic asteroide: Asteroides) {
            asteroide.setPosX(Math.random()* (ample-asteroide.getAmple()));
            asteroide.setPosY(Math.random()* (alt-asteroide.getAlt()));
        }
        nau.setPosX((ample-nau.getAmple())/2);
        nau.setPosY((alt-nau.getAlt())/2);
        darrerProces = System.currentTimeMillis();
        thread.start();
    }
    @Override synchronized protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Grafic asteroide: Asteroides) {
            asteroide.dibuixaGrafic(canvas);
        }
        nau.dibuixaGrafic(canvas);
        if (!Missils.isEmpty()) {
            for (Grafic m: Missils) {
                m.dibuixaGrafic(canvas);
            }
            /*for (int i = 0; i < Missils.size(); i++) {
                Missils.get(i).dibuixaGrafic(canvas);
            }*/
        }
    }

    private boolean hihaValorInicial = false;
    private float valorInicial=0;

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float valorGir = sensorEvent.values[1];
        float valorAcc = sensorEvent.values[2];
        if (!hihaValorInicial){
            valorInicial = valorAcc;
            hihaValorInicial = true;
        }
        if (!hihaValorInicial){
            valorInicial = valorGir;
            hihaValorInicial = true;
        }
        girNau=(int) (valorGir-valorInicial)/2 ;
        acceleracioNau=(int) (valorAcc-valorInicial)/3 ;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }
    private void destrueixAsteroide(int i) {
        Asteroides.remove(i);
    }
    private void ActivaMissil() {
        Grafic missil = new Grafic(this, drawableMissil);
        missil.setPosX(nau.getPosX());
        missil.setPosY(nau.getPosY());
        missil.setAngle(nau.getAngle());
        missil.setIncX(Math.cos(Math.toRadians(missil.getAngle())) * PAS_VELOCITAT_MISSIL);
        missil.setIncY(Math.sin(Math.toRadians(missil.getAngle())) * PAS_VELOCITAT_MISSIL);
        tempsMissils.add((int) Math.min(this.getWidth() / Math.abs(missil.getIncX()), this.getHeight() / Math.abs(missil.getIncY())) - 2);
        Missils.add(missil);
    }

    public ThreadJoc getThread() {
        return thread;
    }


}
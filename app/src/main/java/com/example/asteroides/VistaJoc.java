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
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebHistoryItem;

import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class VistaJoc extends View {
// //// ASTEROIDES //////
    // //// NAU //////
    private Grafic nau; // Gràfic de la nau
    private int girNau; // Angle de gir de la nau
    private float acceleracioNau; // Augment de velocitat

    // Increment estàndar de gir i acceleració
    private static final int PAS_GIR_NAU = 5;
    private static final float PAS_ACCELERACIO_NAU = 0.5f;
    private static final double MAX_VELOCITAT_NAU = 50;
    private List<Grafic> Asteroides; // Vector amb els Asteroides
    private int numAsteroides= 5; // Número inicial d&#39;asteroides
    private int numFragments = 3; // Fragments en que es divideix
    public VistaJoc(Context context, AttributeSet attrs) {
        super(context, attrs);
        Drawable drawableNave, drawableAsteroide, drawableMisil;
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
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
            ShapeDrawable dAsteroide = new ShapeDrawable(new
                    PathShape(pathAsteroide, 1, 1));
            dAsteroide.getPaint().setColor(Color.WHITE);
            dAsteroide.getPaint().setStyle(Paint.Style.STROKE);
            dAsteroide.setIntrinsicWidth(200);
            dAsteroide.setIntrinsicHeight(200);

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
            drawableAsteroide =
                    ContextCompat.getDrawable(context, R.drawable.asteroide1);
            drawableNave = context.getResources().getDrawable(R.drawable.nau);
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
    }
    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Grafic asteroide: Asteroides) {
            asteroide.dibuixaGrafic(canvas);
        }
        nau.dibuixaGrafic(canvas);
    }
}
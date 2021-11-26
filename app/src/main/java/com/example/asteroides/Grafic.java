package com.example.asteroides;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

class Grafic {
    private Drawable drawable; //Imatge que dibuixarem
    private double posX, posY; //Posició del centre de la imatge
    private double incX, incY; //Velocitat desplaçament
    private int angle, rotacio;//Àngle i velocitat de rotació
    private int ample, alt; //Dimensions de la imatge
    private int radiColisio; //Per determinar colisió
//On dibuixam el gràfic (emprada a view.ivalidate)

    private View view;
    // Per determinar l&#39;espai a borrar (view.ivalidate)
    public static final int MAX_VELOCITAT = 20;
    public Grafic(View view, Drawable drawable){
        this.view = view;
        this.drawable = drawable;
        ample = drawable.getIntrinsicWidth();
        alt = drawable.getIntrinsicHeight();
        radiColisio = (alt + ample)/4;
    }
    public void dibuixaGrafic(Canvas canvas){
        canvas.save();
        int x=(int) (posX+ ample /2);
        int y=(int) (posY+ alt /2);
        canvas.rotate((float) angle,(float) x,(float) y);
        drawable.setBounds((int)posX, (int)posY,
                (int)posX+ ample, (int)posY+ alt);
        drawable.draw(canvas);
        canvas.restore();
        int rInval = (int) Math.hypot(ample, alt)/2 + MAX_VELOCITAT;
        view.invalidate(x-rInval, y-rInval, x+rInval, y+rInval);
    }
    public void incrementaPos(double factor){
        posX+=incX * factor;
// Si sortim de la pantalla, corregim posició
        if(posX<-ample /2) {posX=view.getWidth()- ample /2;}
        if(posX>view.getWidth()- ample /2) {posX=-ample /2;}
        posY+=incY * factor;
        if(posY<-alt /2) {posY=view.getHeight()- alt /2;}
        if(posY>view.getHeight()- alt /2) {posY=-alt /2;}
        angle += rotacio * factor; //Actualitzam angle
    }
    public double distancia(Grafic g) {
        return Math.hypot(posX-g.posX, posY-g.posY);
    }
    public boolean verificaColisio(Grafic g) {
        return(distancia(g) < (radiColisio +g.radiColisio));
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public double getIncX() {
        return incX;
    }

    public void setIncX(double incX) {
        this.incX = incX;
    }

    public double getIncY() {
        return incY;
    }

    public void setIncY(double incY) {
        this.incY = incY;
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public int getRotacio() {
        return rotacio;
    }

    public void setRotacio(int rotacio) {
        this.rotacio = rotacio;
    }

    public int getAmple() {
        return ample;
    }

    public void setAmple(int ample) {
        this.ample = ample;
    }

    public int getAlt() {
        return alt;
    }

    public void setAlt(int alt) {
        this.alt = alt;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public int getRadiColisio() {
        return radiColisio;
    }

    public void setRadiColisio(int radiColisio) {
        this.radiColisio = radiColisio;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public static int getMaxVelocitat() {
        return MAX_VELOCITAT;
    }
}
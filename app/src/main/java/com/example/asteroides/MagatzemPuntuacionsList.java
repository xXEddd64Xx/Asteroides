package com.example.asteroides;

import java.util.ArrayList;
import java.util.List;

public class MagatzemPuntuacionsList implements MagatzemPuntuacions{
    private List<String> puntuacions;

    public MagatzemPuntuacionsList() {
        puntuacions= new ArrayList();
        puntuacions.add("123000 Pepe Domínguez");
        puntuacions.add("111000 Pedro Martínez");
        puntuacions.add("011000 Paco Pérez");
    }
    @Override public void guardarPuntuacio(int punts, String nom, long data) {
        puntuacions.add(0, punts + " "+ nom);
    }
    @Override public List<String> llistaPuntuacions(int quantitat)
    {
        return puntuacions;
    }
}
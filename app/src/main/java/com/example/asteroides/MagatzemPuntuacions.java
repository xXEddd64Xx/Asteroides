package com.example.asteroides;

import java.util.List;

public interface MagatzemPuntuacions {
    public void guardarPuntuacio(int punts, String nom, long data);
    public List<String> llistaPuntuacions(int quantitat);
}

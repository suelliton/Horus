package com.example.suelliton.horus;

import java.util.ArrayList;

public class Crescimento {
    private Integer pixelsFotoInicial;
    private ArrayList<Captura> capturas;
    public Crescimento(){

    }

    public Crescimento(ArrayList<Captura> capturas) {
        this.pixelsFotoInicial = 0;
        this.capturas = capturas;
    }

    public Integer getPixelsFotoInicial() {
        return pixelsFotoInicial;
    }

    public void setPixelsFotoInicial(Integer pixelsFotoInicial) {
        this.pixelsFotoInicial = pixelsFotoInicial;
    }

    public ArrayList<Captura> getCapturas() {
        return capturas;
    }

    public void setCapturas(ArrayList<Captura> capturas) {
        this.capturas = capturas;
    }
}

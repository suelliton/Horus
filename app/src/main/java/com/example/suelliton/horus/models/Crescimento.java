package com.example.suelliton.horus.models;

import java.util.ArrayList;

public class Crescimento {
    private Integer areaInicial;
    private ArrayList<Captura> capturas;
    public Crescimento(){

    }

    public Crescimento(ArrayList<Captura> capturas) {
        this.areaInicial = 0;
        this.capturas = capturas;
    }

    public Integer getAreaInicial() {
        return areaInicial;
    }

    public void setAreaInicial(Integer areaInicial) {
        this.areaInicial = areaInicial;
    }

    public ArrayList<Captura> getCapturas() {
        return capturas;
    }

    public void setCapturas(ArrayList<Captura> capturas) {
        this.capturas = capturas;
    }
}

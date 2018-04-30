package com.example.suelliton.horus;

import java.util.ArrayList;

public class Crescimento {
    private Integer pixelsAnterior;
    private ArrayList<Double> taxaCrescimento;
    public Crescimento(){

    }
    public Crescimento(ArrayList<Double> taxaCrescimento) {
        this.taxaCrescimento = taxaCrescimento;
        this.pixelsAnterior = 0;
    }

    public Integer getPixelsAnterior() {
        return pixelsAnterior;
    }

    public void setPixelsAnterior(Integer pixelsAnterior) {
        this.pixelsAnterior = pixelsAnterior;
    }

    public ArrayList<Double> getTaxaCrescimento() {
        return taxaCrescimento;
    }

    public void setTaxaCrescimento(ArrayList<Double> taxaCrescimento) {
        this.taxaCrescimento = taxaCrescimento;
    }
}

package com.example.suelliton.horus;

import java.util.ArrayList;

public class Crescimento {
    private ArrayList<Double> taxaCrescimento;
    public Crescimento(){

    }
    public Crescimento(ArrayList<Double> taxaCrescimento) {
        this.taxaCrescimento = taxaCrescimento;
    }

    public ArrayList<Double> getTaxaCrescimento() {
        return taxaCrescimento;
    }

    public void setTaxaCrescimento(ArrayList<Double> taxaCrescimento) {
        this.taxaCrescimento = taxaCrescimento;
    }
}

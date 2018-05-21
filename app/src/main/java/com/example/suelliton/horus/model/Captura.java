package com.example.suelliton.horus.model;

public class Captura {
    private String dataCaptura;
    private double taxaCrescimento;

    public Captura() {

    }

    public Captura(String dataCaptura, double taxaCrescimento) {
        this.dataCaptura = dataCaptura;
        this.taxaCrescimento = taxaCrescimento;
    }

    public String getDataCaptura() {
        return dataCaptura;
    }

    public void setDataCaptura(String dataCaptura) {
        this.dataCaptura = dataCaptura;
    }

    public double getTaxaCrescimento() {
        return taxaCrescimento;
    }

    public void setTaxaCrescimento(double taxaCrescimento) {
        this.taxaCrescimento = taxaCrescimento;
    }
}

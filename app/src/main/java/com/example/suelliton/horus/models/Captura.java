package com.example.suelliton.horus.models;

public class Captura {
    private String dataCaptura;
    private double areaVerde;
    private double percentualCrescimento;

    public Captura() {

    }

    public Captura(String dataCaptura, double areaVerde, double percentualCrescimento) {
        this.dataCaptura = dataCaptura;
        this.areaVerde = areaVerde;
        this.percentualCrescimento = percentualCrescimento;
    }

    public String getDataCaptura() {
        return dataCaptura;
    }

    public void setDataCaptura(String dataCaptura) {
        this.dataCaptura = dataCaptura;
    }

    public double getAreaVerde() {
        return areaVerde;
    }

    public void setAreaVerde(double areaVerde) {
        this.areaVerde = areaVerde;
    }

    public double getPercentualCrescimento() {
        return percentualCrescimento;
    }

    public void setPercentualCrescimento(double percentualCrescimento) {
        this.percentualCrescimento = percentualCrescimento;
    }
}

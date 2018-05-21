package com.example.suelliton.horus.models;

public class Micronutriente {

    private String nome;
    private double qtd;

    public Micronutriente() {
    }

    public Micronutriente(String nome, double qtd) {
        this.nome = nome;
        this.qtd = qtd;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getQtd() {
        return qtd;
    }

    public void setQtd(double qtd) {
        this.qtd = qtd;
    }
}

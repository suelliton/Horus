package com.example.suelliton.horus;

public class Micronutriente {

    private String nome;
    private Integer qtd;

    public Micronutriente() {
    }

    public Micronutriente(String nome, Integer qtd) {
        this.nome = nome;
        this.qtd = qtd;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getQtd() {
        return qtd;
    }

    public void setQtd(Integer qtd) {
        this.qtd = qtd;
    }
}

package com.example.suelliton.horus.models;

import java.util.ArrayList;

public class Experimento {

    private String nome;
    private String descricao;
    private String variedade;
    private Integer count;
    private String ultimaCaptura;
    private Crescimento crescimento;
    private boolean novaFoto;
    private Solucao nutrientes;
    private String dataTransplantio;
    private Integer idadePlantaTransplantio;
    private Integer idadePlantaAtual;
    private Integer tempoBombaLigado;
    private Integer tempoBombaDesLigado;
    private String status;
    private boolean sincronizado;





    public Experimento() {

    }
    public Experimento(String nome, String descricao, String variedade,Solucao nutrientes,
                       String dataTransplantio,Integer idadePlantaTransplantio,
                       Integer idadePlantaAtual, Integer tempoBombaLigado,
                       Integer tempoBombaDesLigado) {
        this.nome = nome;
        this.descricao = descricao;
        this.variedade = variedade;
        this.count = 0;
        this.ultimaCaptura = "";
        //ArrayList<Captura> lista = new ArrayList();
        //Captura captura = new Captura("",0.0,0.0);
        //lista.add(captura);
        //Captura captura2 = new Captura("15-05-2018",120.0);
        //lista.add(captura2);
        this.crescimento = new Crescimento(null);
        this.novaFoto = false;
        this.nutrientes = nutrientes;
        this.dataTransplantio = dataTransplantio;
        this.idadePlantaTransplantio = idadePlantaTransplantio;
        this.idadePlantaAtual = idadePlantaAtual;
        this.tempoBombaLigado = tempoBombaLigado;
        this.tempoBombaDesLigado = tempoBombaDesLigado;
        this.status = "ativo";
        this.sincronizado = true;

    }

    public boolean isSincronizado() {
        return sincronizado;
    }

    public void setSincronizado(boolean sincronizado) {
        this.sincronizado = sincronizado;
    }

    public Solucao getNutrientes() {
        return nutrientes;
    }

    public void setNutrientes(Solucao nutrientes) {
        this.nutrientes = nutrientes;
    }

    public String getDataTransplantio() {
        return dataTransplantio;
    }

    public void setDataTransplantio(String dataTransplantio) {
        this.dataTransplantio = dataTransplantio;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getIdadePlantaTransplantio() {
        return idadePlantaTransplantio;
    }

    public void setIdadePlantaTransplantio(Integer idadePlantaTransplantio) {
        this.idadePlantaTransplantio = idadePlantaTransplantio;
    }

    public Integer getIdadePlantaAtual() {
        return idadePlantaAtual;
    }

    public void setIdadePlantaAtual(Integer idadePlantaAtual) {
        this.idadePlantaAtual = idadePlantaAtual;
    }

    public Integer getTempoBombaLigado() {
        return tempoBombaLigado;
    }

    public void setTempoBombaLigado(Integer tempoBombaLigado) {
        this.tempoBombaLigado = tempoBombaLigado;
    }

    public Integer getTempoBombaDesLigado() {
        return tempoBombaDesLigado;
    }

    public void setTempoBombaDesLigado(Integer tempoBombaDesLigado) {
        this.tempoBombaDesLigado = tempoBombaDesLigado;
    }



    public boolean isNovaFoto() {
        return novaFoto;
    }

    public void setNovaFoto(boolean novaFoto) {
        this.novaFoto = novaFoto;
    }

    public Crescimento getCrescimento() {
        return crescimento;
    }

    public void setCrescimento(Crescimento crescimento) {
        this.crescimento = crescimento;
    }

    public String getUltimaCaptura() {
        return ultimaCaptura;
    }

    public void setUltimaCaptura(String ultimaCaptura) {
        this.ultimaCaptura = ultimaCaptura;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getVariedade() {
        return variedade;
    }

    public void setVariedade(String variedade) {
        this.variedade = variedade;
    }
}

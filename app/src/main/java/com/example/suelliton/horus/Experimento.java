package com.example.suelliton.horus;

import com.google.firebase.database.GenericTypeIndicator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Experimento {

    private String nome;
    private String descricao;
    private String variedade;
    private Integer count;
    private String ultimaCaptura;
    private Crescimento crescimento;
    private boolean novaFoto;
    private Nutriente nutrientes;
    private String dataTransplantio;
    private Integer idadePlantaTransplantio;
    private Integer idadePlantaAtual;
    private Integer tempoBombaLigado;
    private Integer tempoBombaDesLigado;





    public Experimento() {

    }
    public Experimento(String nome, String descricao, String variedade,Nutriente nutrientes,
                       String dataTransplantio,Integer idadePlantaTransplantio,
                       Integer idadePlantaAtual, Integer tempoBombaLigado,
                       Integer tempoBombaDesLigado) {
        this.nome = nome;
        this.descricao = descricao;
        this.variedade = variedade;
        this.count = 0;
        this.ultimaCaptura = "";
        ArrayList<Captura> lista = new ArrayList();
        Captura captura = new Captura("14-05-2018",90.0);
        lista.add(captura);
        Captura captura2 = new Captura("15-05-2018",120.0);
        lista.add(captura2);
        this.crescimento = new Crescimento(lista);
        this.novaFoto = false;
        this.nutrientes = nutrientes;
        this.dataTransplantio = dataTransplantio;
        this.idadePlantaTransplantio = idadePlantaTransplantio;
        this.idadePlantaAtual = idadePlantaAtual;
        this.tempoBombaLigado = tempoBombaLigado;
        this.tempoBombaDesLigado = tempoBombaDesLigado;



    }

    public Nutriente getNutrientes() {
        return nutrientes;
    }

    public void setNutrientes(Nutriente nutrientes) {
        this.nutrientes = nutrientes;
    }

    public String getDataTransplantio() {
        return dataTransplantio;
    }

    public void setDataTransplantio(String dataTransplantio) {
        this.dataTransplantio = dataTransplantio;
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

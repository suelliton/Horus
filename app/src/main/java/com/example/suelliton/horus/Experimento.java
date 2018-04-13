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
    private Integer pixelsAnterior;




    public Experimento() {

    }
    public Experimento(String nome, String descricao, String variedade) {
        this.nome = nome;
        this.descricao = descricao;
        this.variedade = variedade;
        this.count = 0;
        this.ultimaCaptura = "";
        ArrayList<Double> lista = new ArrayList();
        lista.add(123.0);
        this.crescimento = new Crescimento(lista);

        this.novaFoto = false;
        this.pixelsAnterior = 0;

    }

    public Integer getPixelsAnterior() {
        return pixelsAnterior;
    }

    public void setPixelsAnterior(Integer pixelsAnterior) {
        this.pixelsAnterior = pixelsAnterior;
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

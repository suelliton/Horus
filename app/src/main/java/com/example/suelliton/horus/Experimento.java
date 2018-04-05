package com.example.suelliton.horus;

public class Experimento {
    private Integer id;
    private String nome;
    private String descricao;
    private String variedade;
    private Integer count;
    public Experimento() {

    }
    public Experimento(String nome, String descricao, String variedade) {
        this.nome = nome;
        this.descricao = descricao;
        this.variedade = variedade;
        this.count = 0;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

package com.example.suelliton.horus.model;

import com.example.suelliton.horus.model.Macronutriente;
import com.example.suelliton.horus.model.Micronutriente;

import java.util.List;

public class Solucao {
    private List<Macronutriente> macronutrientes;
    private List<Micronutriente> micronutrientes;

    public Solucao() {
    }


    public Solucao(List<Macronutriente> macronutrientes, List<Micronutriente> micronutrientes) {
        this.macronutrientes = macronutrientes;
        this.micronutrientes = micronutrientes;
    }


    public List<Macronutriente> getMacronutrientes() {
        return macronutrientes;
    }

    public void setMacronutrientes(List<Macronutriente> macronutrientes) {
        this.macronutrientes = macronutrientes;
    }

    public List<Micronutriente> getMicronutrientes() {
        return micronutrientes;
    }

    public void setMicronutrientes(List<Micronutriente> micronutrientes) {
        this.micronutrientes = micronutrientes;
    }
}

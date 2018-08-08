package com.example.suelliton.horus.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suelliton.horus.DetalhesActivity;
import com.example.suelliton.horus.R;
import com.example.suelliton.horus.StorageActivity;
import com.example.suelliton.horus.models.Experimento;
import com.example.suelliton.horus.models.Macronutriente;
import com.example.suelliton.horus.models.Micronutriente;

import java.util.List;
public class MacronutrienteAdapter extends RecyclerView.Adapter {
    Context context;
    List<Macronutriente> listaMacronutrientes;

    public MacronutrienteAdapter(Context c, List<Macronutriente> lista){
        this.context = c;
        this.listaMacronutrientes = lista;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.inflate_nutricao, parent, false);

        MacronutrienteViewHolder holder = new MacronutrienteViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MacronutrienteViewHolder macronutrienteholder = (MacronutrienteViewHolder) holder;
        final Macronutriente macronutrienteEscolhido = listaMacronutrientes.get(position);

        macronutrienteholder.text_nome.setText(macronutrienteEscolhido.getNome());
        macronutrienteholder.text_quantidade.setText(""+macronutrienteEscolhido.getQtd());




    }


    @Override
    public int getItemCount() {
        return  listaMacronutrientes == null ? 0 :  listaMacronutrientes.size();
    }

    public class MacronutrienteViewHolder extends RecyclerView.ViewHolder {

        final TextView text_nome;
        final TextView text_quantidade;
        public MacronutrienteViewHolder(View v) {
            super(v);
            text_nome = v.findViewById(R.id.tx_nutriente);
            text_quantidade = v.findViewById(R.id.tx_quantidade);
        }
    }
}

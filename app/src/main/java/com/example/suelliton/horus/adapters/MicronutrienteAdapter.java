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
import com.example.suelliton.horus.models.Micronutriente;

import java.util.List;

public class MicronutrienteAdapter extends RecyclerView.Adapter {
    Context context;
    List<Micronutriente> listaMicronutrientes;

    public MicronutrienteAdapter(Context c, List<Micronutriente> lista){
        this.context = c;
        this.listaMicronutrientes = lista;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.inflate_nutricao, parent, false);

        MicronutrienteViewHolder holder = new MicronutrienteViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MicronutrienteViewHolder micronutrienteholder = (MicronutrienteViewHolder) holder;
        final Micronutriente micronutrienteEscolhido = listaMicronutrientes.get(position);

        micronutrienteholder.text_nome.setText(micronutrienteEscolhido.getNome());
        micronutrienteholder.text_quantidade.setText(micronutrienteEscolhido.getQtd()+" mg");




    }


    @Override
    public int getItemCount() {
        return  listaMicronutrientes == null ? 0 :  listaMicronutrientes.size();
    }

    public class MicronutrienteViewHolder extends RecyclerView.ViewHolder {

        final TextView text_nome;
        final TextView text_quantidade;
        public MicronutrienteViewHolder(View v) {
            super(v);
            text_nome = v.findViewById(R.id.tx_nutriente);
            text_quantidade = v.findViewById(R.id.tx_quantidade);
        }
    }
}

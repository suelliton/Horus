package com.example.suelliton.horus;

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

import java.util.List;

public class ExperimentoAdapter extends RecyclerView.Adapter {
    Context context;
    List<Experimento> listaExperimentos;

    public ExperimentoAdapter(Context c, List<Experimento> lista){
        this.context = c;
        this.listaExperimentos = lista;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.inflate_experimentos, parent, false);

        ExperimentoViewHolder holder = new ExperimentoViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ExperimentoViewHolder experimentoholder = (ExperimentoViewHolder) holder;
        final Experimento experimentoEscolhido = listaExperimentos.get(position);

        //objetos apenas para testes esses valores vao ser pegados diretamente pelo objeto registro quando tudo estiver no banco
        experimentoholder.text_nome.setText(experimentoEscolhido.getNome());
        experimentoholder.text_descricao.setText(experimentoEscolhido.getDescricao());

        experimentoholder.btn_adicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("nomeExp",experimentoEscolhido.getNome());
                bundle.putInt("count",experimentoEscolhido.getCount());
                Log.i("teste",experimentoEscolhido.getNome());
                Intent intent = new Intent(view.getContext(),StorageActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        experimentoholder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }


    @Override
    public int getItemCount() {
        return  listaExperimentos == null ? 0 :  listaExperimentos.size();
    }

    public class ExperimentoViewHolder extends RecyclerView.ViewHolder {

        final TextView text_nome;
        final TextView text_descricao;
        final LinearLayout row;
        final FloatingActionButton btn_adicionar;


        //  final TextView label_infestacaoTratamento;

        public ExperimentoViewHolder(View v) {
            super(v);
            text_nome = v.findViewById(R.id.label_nome);
            text_descricao = v.findViewById(R.id.label_descricao);
            btn_adicionar = v.findViewById(R.id.btnAdicionar);
            row = v.findViewById(R.id.reg_row);



        }
    }
}

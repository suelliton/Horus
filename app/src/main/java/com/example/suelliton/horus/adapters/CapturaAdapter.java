package com.example.suelliton.horus.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.suelliton.horus.R;
import com.example.suelliton.horus.models.Captura;

import java.util.Collections;
import java.util.List;

public class CapturaAdapter extends RecyclerView.Adapter{


    Context context;
    List<Captura> listaCapturas;
    String fragment ;

    public CapturaAdapter(Context c, List<Captura> listaCapturas, String fragment){
        this.context = c;
        this.listaCapturas = listaCapturas;
        this.fragment = fragment;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.capturas_inflate_recycler, parent, false);

        CapturaViewHolder holder = new CapturaViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        CapturaViewHolder capturaholder = (CapturaViewHolder) holder;
        Captura capturaEscolhida = listaCapturas.get(position);
        String[] dataFormatada  = capturaEscolhida.getDataCaptura().split("\n");
        String[] data = dataFormatada[0].split("-");
        String[] hora = dataFormatada[1].split(":");
        capturaholder.textview_dataCaptura.setText(data[0]+"/"+data[1]+" às " +hora[0]+":"+hora[1]);
        if(fragment.equals("area")){
            capturaholder.textview_taxaCrescimento.setText(""+capturaEscolhida.getAreaVerde()+" cm^2");
            //capturaholder.textview_taxaCrescimento.setTextColor(Color.argb(255, 0, 255, 136));
        }else if(fragment.equals("percentual")){
            capturaholder.textview_taxaCrescimento.setText(""+capturaEscolhida.getPercentualCrescimento()+" %");
            //capturaholder.textview_taxaCrescimento.setTextColor(Color.argb(255, 0, 191, 255));
        }



        capturaholder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: A IMPLEMENTAR QUANDO FOR PRECISO
            }
        });


    }


    @Override
    public int getItemCount() {
        return  listaCapturas == null ? 0 :  listaCapturas.size();
    }

    public class CapturaViewHolder extends RecyclerView.ViewHolder {

        final TextView textview_dataCaptura;
        final TextView textview_taxaCrescimento;

        // final TextView text_infestacaoTratamento;
        final LinearLayout row;


        //  final TextView label_infestacaoTratamento;

        public CapturaViewHolder(View v) {
            super(v);
            textview_dataCaptura = v.findViewById(R.id.textview_data_captura);
            textview_taxaCrescimento = v.findViewById(R.id.textview_taxa_crescimento);

            row = v.findViewById(R.id.captura_row);



        }
    }
}

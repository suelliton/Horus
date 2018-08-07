package com.example.suelliton.horus.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suelliton.horus.DetalhesActivity;
import com.example.suelliton.horus.R;
import com.example.suelliton.horus.StorageActivity;
import com.example.suelliton.horus.adapters.CapturaAdapter;
import com.example.suelliton.horus.models.Captura;
import com.example.suelliton.horus.models.Crescimento;
import com.example.suelliton.horus.models.Experimento;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;

import static com.example.suelliton.horus.DetalhesActivity.nomeExperimento;


public class FragmentArea extends Fragment {


    List<Captura> listaCapturas;


    FirebaseDatabase database;
    DatabaseReference experimentoReference ;
    ValueEventListener childValueExperimento;
    Experimento experimento;
    static TextView textArea;
    static FloatingActionButton btnAdicionar, btnExcluir;
    public static View ViewSnackApoio;
    CapturaAdapter capturaAdapter;
    View v;
    RecyclerView recyclerView;
    public static GraphView graph;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         v = inflater.inflate(R.layout.fragment_crescimento, container, false);
        Configuration configuration = getResources().getConfiguration();
/*
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){

            GraphView grafico =  v.findViewById(R.id.graph);
            grafico.setMinimumWidth(1000);
        }else{
            LinearLayoutCompat grafico =  v.findViewById(R.id.layoutGrafico);
            grafico.setMinimumHeight(500);
        }
*/
        database =  FirebaseDatabase.getInstance();
        //textArea = (TextView) findViewById(R.id.text_taxa);


        listaCapturas = new ArrayList<>();
        capturaAdapter = new CapturaAdapter(v.getContext(), listaCapturas,"area");
        recyclerView = v.findViewById(R.id.recycler_captura_fragment);
        recyclerView.setAdapter(capturaAdapter);


        experimentoReference = database.getReference().child(nomeExperimento);


        childValueExperimento = experimentoReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                listaCapturas.removeAll(listaCapturas);

                experimentoReference = database.getReference().child(nomeExperimento);

                experimento = dataSnapshot.getValue(Experimento.class);
                Crescimento crescimento =  dataSnapshot.getValue(Experimento.class).getCrescimento();

                if(crescimento.getCapturas() != null) {
                    for (Captura c : crescimento.getCapturas()) {
                        listaCapturas.add(c);
                    }
                }

                Log.i("teste",listaCapturas.toString());
                // textArea.setText("Ultima Area :" +listaCapturas.get(listaCapturas.size()-1).toString()+" %");
                DataPoint[] dataPointArea =  new DataPoint[listaCapturas.size()];

                int[] vetorAreas ;
                if(listaCapturas.size() > 0){
                    vetorAreas = new int[listaCapturas.size()];//vetor responsável por definir os tamanhos do grafico
                }else{
                    vetorAreas = new int[5];//vetor responsável por definir os tamanhos do grafico
                }

                for(int i=0; i < listaCapturas.size();i++){
                    dataPointArea[i] = new DataPoint(i+1, (Double) listaCapturas.get(i).getAreaVerde());
                    vetorAreas[i] = (int) listaCapturas.get(i).getAreaVerde();//esse vetor guarda os valores para saber o maximo para ajustar o grafico

                }
                capturaAdapter.notifyDataSetChanged();


                graph = (GraphView) v.findViewById(R.id.graph);
                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPointArea);
                series.setTitle("Área");
                series.setDrawBackground(true);
                series.setColor(Color.argb(255,0,150,136));
                series.setBackgroundColor(Color.argb(70,0,150,136 ));




                int maxTopGrafico = maxValueArray(vetorAreas);//calcula o maximo do vetor

                graph.getViewport().setYAxisBoundsManual(true);
                graph.getViewport().setMinY(0);
                graph.getViewport().setMaxY(maxTopGrafico+(maxTopGrafico/2));//seto a altura maxima do grafico com uma sobra de espaço


                graph.getViewport().setXAxisBoundsManual(true);

                int maxRightGrafico = 0;
                if(vetorAreas.length == 0){
                    maxRightGrafico =  45;
                }else{
                    maxRightGrafico = vetorAreas.length;
                }

                graph.getViewport().setMinX(1);
                graph.getViewport().setMaxX(maxRightGrafico);

                graph.getGridLabelRenderer().setNumHorizontalLabels(maxRightGrafico);
                graph.getViewport().setScalable(true);
                //graph.getViewport().setScalableY(true);
                //graph.setRotationX(5);

                graph.addSeries(series);
                graph.setFocusableInTouchMode(true);
                graph.setFocusable(true);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






        btnAdicionar= (FloatingActionButton) v.findViewById(R.id.btnAdicionar);
        ViewSnackApoio = btnAdicionar;//serve para disparar os snackbars
        btnAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("nomeExp",experimento.getNome());
                bundle.putInt("count",experimento.getCount());
                Log.i("teste",experimento.getNome());
                Intent intent = new Intent(v.getContext(),StorageActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        btnExcluir = (FloatingActionButton) v.findViewById(R.id.btnExcluir);
        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog();
            }
        });


        Log.i("rec", String.valueOf(listaCapturas.size()));

        //capturaAdapter = new CapturaAdapter(v.getContext(),listaCapturas);


        recyclerView.setAdapter(capturaAdapter);

        RecyclerView.LayoutManager layout = new LinearLayoutManager(v.getContext(),LinearLayoutManager.VERTICAL,true);

        recyclerView.setLayoutManager(layout);





        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.i("rec", String.valueOf(listaCapturas.size()));
    }

    public void createDialog(){
        AlertDialog.Builder dialogExcluir = new AlertDialog.Builder(getView().getContext());
        dialogExcluir.setIcon(R.mipmap.ic_launcher);
        dialogExcluir.setTitle("Titulo");
        dialogExcluir.setMessage("Deseja finalizar o experimento?");
        dialogExcluir.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                experimentoReference.child("status").setValue("desativado");
                Toast.makeText(getView().getContext(), "O experimento foi excluido com sucesso", Toast.LENGTH_LONG).show();
            }
        });
        dialogExcluir.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getView().getContext(), "Exclusão cancelada", Toast.LENGTH_LONG).show();
            }
        });
        AlertDialog criaalerta = dialogExcluir.create();
        criaalerta.show();
    }
    public int maxValueArray (int[] a){
        //Retorna o maior valor de um vetor
        if(a.length == 0){
            return 80;
        }else {
            int max = a[0]; //supõe-se que o maior elemento é primeiro
            for (int i = 1; i < a.length; i++) {
                //um valor maior foi encontrado
                if (max < a[i]) {
                    max = a[i]; //substitui o valor máximo
                }
            }

            return max; //retorna o valor máximo
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){//se chamou tela de novo experimento
            if(resultCode == 1){
                Snackbar.make(textArea.getRootView(), "Novo experimento adicionado", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }else if(resultCode == 2){
                Snackbar.make(textArea.getRootView(), "Operação cancelada", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

        }
    }


}

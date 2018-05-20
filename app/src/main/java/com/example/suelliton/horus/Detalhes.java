package com.example.suelliton.horus;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static com.example.suelliton.horus.Principal.ViewSnack;

public class Detalhes extends AppCompatActivity {
    ArrayList listaTaxas;
    List<Captura> listaCapturas;


    String nomeExperimento = "";
    private Integer count = 0;
    private FirebaseDatabase database;
    private DatabaseReference experimentoReference ;
    private ValueEventListener childValueExperimento;
    private TextView textTaxa ;
    private Experimento experimento;
    FloatingActionButton btnAdicionar, btnExcluir;
    static View ViewSnackApoio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);
        database =  FirebaseDatabase.getInstance();
        //textTaxa = (TextView) findViewById(R.id.text_taxa);


        Bundle bundle = getIntent().getExtras();
        nomeExperimento = bundle.getString("nomeExp");
        count = bundle.getInt("count");

        listaCapturas = new ArrayList<>();
        final CapturaAdapter capturaAdapter = new CapturaAdapter(this,listaCapturas);
        experimentoReference = database.getReference().child(nomeExperimento);

        childValueExperimento = experimentoReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                listaTaxas = new ArrayList<Double>();
                listaCapturas.removeAll(listaCapturas);

                experimento = dataSnapshot.getValue(Experimento.class);
                Crescimento crescimento =  dataSnapshot.getValue(Experimento.class).getCrescimento();
                for (Captura c:crescimento.getCapturas()) {
                    listaCapturas.add(c);
                }

                Log.i("teste",listaCapturas.toString());
               // textTaxa.setText("Ultima taxa :" +listaCapturas.get(listaCapturas.size()-1).toString()+" %");
                DataPoint[] dataPointTaxa =  new DataPoint[listaCapturas.size()];

                for(int i=0; i < listaCapturas.size();i++){
                    dataPointTaxa[i] = new DataPoint(i, (Double) listaCapturas.get(i).getTaxaCrescimento());

                }
                capturaAdapter.notifyDataSetChanged();


                GraphView graph = (GraphView) findViewById(R.id.graph);
                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPointTaxa);
                series.setTitle("Crescimento");
                series.setDrawBackground(true);
                series.setColor(Color.argb(255,0,150,136));
                series.setBackgroundColor(Color.argb(70,0,150,136 ));

                graph.getViewport().setYAxisBoundsManual(true);
                graph.getViewport().setMinY(0);
                graph.getViewport().setMaxY(1000);

                graph.getViewport().setXAxisBoundsManual(true);
                graph.getViewport().setMinX(0);
                graph.getViewport().setMaxX(35);

                graph.getViewport().setScalable(true);
                //graph.getViewport().setScalableY(true);
                graph.addSeries(series);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



         btnAdicionar= (FloatingActionButton) findViewById(R.id.btnAdicionar);
        ViewSnackApoio = btnAdicionar;//serve para disparar os snackbars
        btnAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("nomeExp",experimento.getNome());
                bundle.putInt("count",experimento.getCount());
                Log.i("teste",experimento.getNome());
                Intent intent = new Intent(Detalhes.this,StorageActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        btnExcluir = (FloatingActionButton) findViewById(R.id.btnExcluir);
        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                experimentoReference.child("status").setValue("desativado");
            }
        });


        Log.i("rec", String.valueOf(listaCapturas.size()));
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_captura);
        recyclerView.setAdapter(capturaAdapter);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layout);


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){//se chamou tela de novo experimento
            if(resultCode == 1){
                Snackbar.make(textTaxa.getRootView(), "Novo experimento adicionado", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }else if(resultCode == 2){
                Snackbar.make(textTaxa.getRootView(), "Operação cancelada", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

        }
    }





}

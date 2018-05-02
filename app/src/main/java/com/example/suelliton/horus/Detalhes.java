package com.example.suelliton.horus;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    String nomeExperimento = "";
    private Integer count = 0;
    private FirebaseDatabase database;
    private DatabaseReference experimentoReference ;
    private ValueEventListener childValueExperimento;
    private TextView textTaxa ;
    private Experimento experimento;
    FloatingActionButton btnAdicionar;
    static View ViewSnackApoio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);
        database =  FirebaseDatabase.getInstance();
        textTaxa = (TextView) findViewById(R.id.text_taxa);
        ViewSnackApoio = textTaxa;

        Bundle bundle = getIntent().getExtras();
        nomeExperimento = bundle.getString("nomeExp");
        count = bundle.getInt("count");


        experimentoReference = database.getReference().child(nomeExperimento);

        childValueExperimento = experimentoReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                listaTaxas = new ArrayList<Double>();
                experimento = dataSnapshot.getValue(Experimento.class);
                Crescimento crescimento =  dataSnapshot.getValue(Experimento.class).getCrescimento();
                listaTaxas = crescimento.getTaxaCrescimento();
                Log.i("teste",listaTaxas.toString());
                textTaxa.setText("Ultima taxa :" +listaTaxas.get(listaTaxas.size()-1).toString()+" %");
                DataPoint[] dataPointTaxa =  new DataPoint[listaTaxas.size()];

                for(int i=0; i < listaTaxas.size();i++){
                    dataPointTaxa[i] = new DataPoint(i, (Double) listaTaxas.get(i));

                }



                GraphView graph = (GraphView) findViewById(R.id.graph);
                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPointTaxa);

                graph.getViewport().setYAxisBoundsManual(true);
                graph.getViewport().setMinY(-200);
                graph.getViewport().setMaxY(200);

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

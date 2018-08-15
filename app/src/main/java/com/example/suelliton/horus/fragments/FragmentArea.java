package com.example.suelliton.horus.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.suelliton.horus.R;
import com.example.suelliton.horus.StorageActivity;
import com.example.suelliton.horus.adapters.CapturaAdapter;
import com.example.suelliton.horus.models.Captura;
import com.example.suelliton.horus.models.Crescimento;
import com.example.suelliton.horus.models.Experimento;
import com.example.suelliton.horus.models.Usuario;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.example.suelliton.horus.DetalhesActivity.nomeExperimento;
import static com.example.suelliton.horus.SplashActivity.LOGADO;

public class FragmentArea extends Fragment {


    List<Captura> listaCapturas;

    private LineChart mChart;
    FirebaseDatabase database;
    DatabaseReference usuarioReference ;
    ValueEventListener childValueUsuario;
    static TextView textArea;
    CapturaAdapter capturaAdapter;
    View v;
    RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         v = inflater.inflate(R.layout.fragment_grafico, container, false);

         Configuration configuration = getResources().getConfiguration();

        database =  FirebaseDatabase.getInstance();

        listaCapturas = new ArrayList<>();
        capturaAdapter = new CapturaAdapter(v.getContext(),  listaCapturas,"area");
        recyclerView = v.findViewById(R.id.recycler_captura_fragment);
        recyclerView.setAdapter(capturaAdapter);


        usuarioReference = database.getReference();


        childValueUsuario = usuarioReference.child(LOGADO).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                listaCapturas.removeAll(listaCapturas);

                for (Experimento e: dataSnapshot.getValue(Usuario.class).getExperimentos()) {
                    if(e.getNome().equals(nomeExperimento)){
                        if(e.getCrescimento().getCapturas() != null) {
                            for (Captura c : e.getCrescimento().getCapturas()) {
                                listaCapturas.add(c);
                            }
                        }
                    }

                }


                double[] vetorAreas ;
                if(listaCapturas.size() > 0) {

                    vetorAreas = new double[listaCapturas.size()];//vetor responsável por definir os tamanhos do grafico


                    for (int i = 0; i < listaCapturas.size(); i++) {
                        vetorAreas[i] = listaCapturas.get(i).getAreaVerde();//esse vetor guarda os valores para saber o maximo para ajustar o grafico

                    }


                    mChart = v.findViewById(R.id.chart1);
                    mChart.setViewPortOffsets(20, 0, 25, 0);
                    mChart.setBackgroundColor(Color.WHITE);
                    // no description text
                    mChart.getDescription().setEnabled(false);

                    // enable touch gestures
                    mChart.setTouchEnabled(true);

                    // enable scaling and dragging
                    mChart.setDragEnabled(true);
                    mChart.setScaleEnabled(true);

                    // if disabled, scaling can be done on x- and y-axis separately
                    mChart.setPinchZoom(false);

                    mChart.setDrawGridBackground(false);
                    mChart.setMaxHighlightDistance(300);

                    XAxis x = mChart.getXAxis();
                    x.setEnabled(true);
                    x.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);

                    x.setCenterAxisLabels(false);
                    x.setTextSize(10f);
                    if(vetorAreas.length < 15){
                        x.setLabelCount(vetorAreas.length,true);
                    }else{
                        x.setLabelCount(15);
                    }
                    x.setTextColor(Color.BLACK);
                    x.setDrawAxisLine(true);
                    x.setDrawGridLines(true);

                    YAxis y = mChart.getAxisLeft();

                    y.setLabelCount(10, false);
                    y.setTextColor(Color.BLACK);
                    y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
                    y.setDrawGridLines(true);
                    y.setAxisLineColor(Color.argb(255, 0, 255, 136));

                    mChart.getAxisRight().setEnabled(false);

                    setData(vetorAreas);

                    Legend l = mChart.getLegend();
                    l.setEnabled(false);

                    //mChart.animateXY(2000, 2000);
                    mChart.animateY(2000);


                    List<ILineDataSet> sets = mChart.getData().getDataSets();

                    for (ILineDataSet iSet : sets) {

                        LineDataSet set = (LineDataSet) iSet;
                        set.setColor(Color.argb(255, 0, 255, 136));
                        set.setDrawFilled(true);
                        set.setDrawValues(true);
                        set.setCircleColor(Color.argb(255, 0, 255, 136));
                    }


                    // dont forget to refresh the drawing
                    mChart.invalidate();
                }
                capturaAdapter.notifyDataSetChanged();



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        Log.i("rec", String.valueOf(listaCapturas.size()));


        recyclerView.setAdapter(capturaAdapter);

        RecyclerView.LayoutManager layout = new LinearLayoutManager(v.getContext(),LinearLayoutManager.VERTICAL,true);

        recyclerView.setLayoutManager(layout);


        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.i("rec", String.valueOf(listaCapturas.size()));
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

    private void setData(double [] valores) {

        ArrayList<Entry> yVals = new ArrayList<Entry>();

        for (int i = 0; i < valores.length; i++) {
            // float mult = (range + 1);
            //float val = (float) (Math.random() * mult) + 20;// + (float)
            // ((mult *
            // 0.1) / 10);

            yVals.add(new Entry(i+1, (float) valores[i]));
        }

        LineDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet)mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(yVals, "DataSet 1");

            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set1.setCubicIntensity(0.2f);
            //set1.setDrawFilled(true);
            set1.setDrawCircles(false);
            set1.setLineWidth(1.8f);
            set1.setCircleRadius(4f);
            set1.setCircleColor(Color.WHITE);
            set1.setHighLightColor(Color.rgb(244, 117, 117));
            set1.setColor(Color.WHITE);
            set1.setFillColor(Color.argb(155, 0, 255, 136));//cor embaixo da linha
            set1.setFillAlpha(100);//transparencia embaixo da linha
            set1.setDrawHorizontalHighlightIndicator(false);
            set1.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return -10;
                }
            });

            // create a data object with the datasets
            LineData data = new LineData(set1);

            data.setValueTextSize(9f);
            data.setDrawValues(false);

            // set data
            mChart.setData(data);
        }
    }


}

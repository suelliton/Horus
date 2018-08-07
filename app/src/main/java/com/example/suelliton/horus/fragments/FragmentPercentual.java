package com.example.suelliton.horus.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suelliton.horus.R;
import com.example.suelliton.horus.StorageActivity;
import com.example.suelliton.horus.adapters.CapturaAdapter;
import com.example.suelliton.horus.models.Captura;
import com.example.suelliton.horus.models.Crescimento;
import com.example.suelliton.horus.models.Experimento;
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
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;

import static com.example.suelliton.horus.DetalhesActivity.nomeExperimento;

public class FragmentPercentual extends Fragment {


    List<Captura> listaCapturas;

    private LineChart mChart;
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

        database =  FirebaseDatabase.getInstance();
        //textArea = (TextView) findViewById(R.id.text_taxa);


        listaCapturas = new ArrayList<>();
        capturaAdapter = new CapturaAdapter(v.getContext(), listaCapturas,"percentual");
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


                double[] vetorPercentuais ;
                if(listaCapturas.size() > 0) {
                    vetorPercentuais = new double[listaCapturas.size()];//vetor responsável por definir os tamanhos do grafico


                    for (int i = 0; i < listaCapturas.size(); i++) {
                        vetorPercentuais[i] = listaCapturas.get(i).getPercentualCrescimento();//esse vetor guarda os valores para saber o maximo para ajustar o grafico

                    }
                    capturaAdapter.notifyDataSetChanged();

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
                    if(vetorPercentuais.length < 15){
                        x.setLabelCount(vetorPercentuais.length,true);
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
                    y.setAxisLineColor(Color.argb(255, 30, 144, 255));

                    mChart.getAxisRight().setEnabled(false);

                    setData(vetorPercentuais);

                    Legend l = mChart.getLegend();
                    l.setEnabled(false);

                    mChart.animateXY(2000, 2000);


                    List<ILineDataSet> sets = mChart.getData().getDataSets();

                    for (ILineDataSet iSet : sets) {

                        LineDataSet set = (LineDataSet) iSet;
                        set.setColor(Color.argb(255, 0, 191, 255));
                        set.setDrawFilled(true);
                        set.setDrawValues(true);
                        set.setCircleColor(Color.argb(255, 0, 255, 136));
                    }


                    // dont forget to refresh the drawing
                    mChart.invalidate();
                }

                capturaAdapter.notifyDataSetChanged();

/*grphview
                graph = (GraphView) v.findViewById(R.id.graph);
                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPointTaxa);
                series.setTitle("Crescimento");
                series.setDrawBackground(true);
                series.setColor(Color.argb(255,0,191,255));
                series.setBackgroundColor(Color.argb(70,0,191,255 ));

                int maxTopGrafico = maxValueArray(vetorPercentuais);//calcula o maximo do vetor

                graph.getViewport().setYAxisBoundsManual(true);
                graph.getViewport().setMinY(0);
                graph.getViewport().setMaxY(maxTopGrafico+(maxTopGrafico/2));

                graph.getViewport().setXAxisBoundsManual(true);

                int maxRightGrafico = 0;
                if(vetorPercentuais.length == 0){
                    maxRightGrafico =  45;
                }else{
                    maxRightGrafico = vetorPercentuais.length;
                }
                graph.getViewport().setMinX(1);
                graph.getViewport().setMaxX(maxRightGrafico);
                graph.getGridLabelRenderer().setNumHorizontalLabels(maxRightGrafico);
                graph.getViewport().setScalable(true);
                //graph.getViewport().setScalableY(true);
                //graph.setRotationX(5);

                graph.addSeries(series);
                graph.setFocusableInTouchMode(true);
                graph.setFocusable(true);*/

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
            set1.setFillColor(Color.argb(255, 0, 191, 255));//cor embaixo da linha
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

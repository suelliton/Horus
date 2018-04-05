package com.example.suelliton.horus;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewExperiment extends AppCompatActivity {
    EditText nome;
    EditText descricao;
    EditText variedade;
    FloatingActionButton btnAdd;
    FirebaseDatabase database;
    DatabaseReference experimento ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_experiment);
        database = FirebaseDatabase.getInstance();

        nome = (EditText)findViewById(R.id.ed_nome);
        descricao = (EditText)findViewById(R.id.ed_descricao);
        variedade = (EditText)findViewById(R.id.ed_variedade);
        btnAdd = (FloatingActionButton) findViewById(R.id.btnAdd);

        setListeners();
    }
    public void setListeners(){
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String n = nome.getText().toString();
                String d = descricao.getText().toString();
                String v = variedade.getText().toString();
                Experimento exp = new Experimento(n,d,v);
                experimento = database.getReference(n);
                experimento.setValue(exp);
                Snackbar.make(view.getRootView(), "Novo experimento adicionado", Snackbar.LENGTH_LONG)
                      .setAction("Action", null).show();
                finish();
            }
        });
    }


}

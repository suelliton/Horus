package com.example.suelliton.horus;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NovoExperimento extends AppCompatActivity {
    EditText nome;
    EditText descricao;
    EditText variedade;
    FloatingActionButton btnAdd;
    FirebaseDatabase database;
    DatabaseReference experimento ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_experimento);
        database = FirebaseDatabase.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
        getSupportActionBar().setTitle("Add Experimento");



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
                setResult(1);
                finish();

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
                setResult(2);//operacao cancelada
                finish();
                //startActivity(new Intent(this, Principal.class));  //O efeito ao ser pressionado do botão (no caso abre a activity)
                //finishAffinity();  //Método para matar a activity e não deixa-lá indexada na pilhagem
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(2);//operacao cancelada
        finish();
    }
}

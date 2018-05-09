package com.example.suelliton.horus;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class NovoExperimento extends AppCompatActivity {
    EditText nome;
    EditText descricao;
    FloatingActionButton btnAdd;
    FirebaseDatabase database;
    DatabaseReference experimento ;
    Spinner spinner_idadePlantaTransplantio;
    Spinner spinner_tempoBombaLigado;
    Spinner spinner_tempoBombaDesligado;
    //spinner macronutrientes
    Spinner spinner_nitrato_de_calcio;
    Spinner spinner_nitrato_de_potassio;
    Spinner spinner_sulfato_de_magnesio;
    Spinner spinner_cloreto_de_potassio;
    Spinner spinner_MAP_purificado;
    Spinner spinner_MKP;
    Spinner spinner_sulfato_de_potassio;
    //spinner micronutrientes
    Spinner spinner_sulfato_de_manganes;
    Spinner spinner_sulfato_de_zinco;
    Spinner spinner_sulfato_de_cobre;
    Spinner spinner_acido_borico;
    Spinner spinner_molibdato_de_sodio;
    Spinner spinner_molibdato_de_amonio;
    Spinner spinner_feedta_na2;
    Spinner spinner_ferrilene;
    Spinner spinner_tenso_fe;
    Spinner spinner_dissolvine;
    Spinner spinner_rexolin_m48;


    CalendarView calendar;
    Experimento SAVE_EXPERIMENTO;
    String save_nome;
    String save_descricao;
    String save_variedade;
    String save_dataTransplantio;
    Integer save_idadePlantaTransplantio;
    Integer save_idadePlantaAtual;
    Integer save_tempoBombaLigado;
    Integer save_tempoBombaDesligado;
    Nutriente save_nutrientes;

    ArrayAdapter<String> adaptador;
    ArrayAdapter<String> adaptadorMiligramas;

    private static final String[] NUMEROS = new String[]{
            "1", "2","3", "4", "5", "6", "7", "8", "9", "10",
            "11","12","13","14","15","16","17","18","19","20","21","22","23","24","25",
            "26","27","28","29","30","31","32","33","34","35","36","37","38","39","40"
            ,"41","42","43","44","45","46","47","48","49","50"
            ,"51","52","53","54","55","56","57","58","59","60"};
    private static final String[] DIAS = new String[]{"1 dia","2 dias","3 dias","4 dias","5 dias"
            ,"6 dias","7 dias","8 dias","9 dias","10 dias","11 dias","12 dias","13 dias","14 dias"
            ,"15 dias","16 dias","17 dias","18 dias","19 dias","20 dias","21 dias","22 dias"
            ,"23 dias","24 dias","25 dias","26 dias","27 dias","28 dias","29 dias","30 dias"
            ,"31 dias","32 dias","33 dias","34 dias","35 dias","36 dias","37 dias","38 dias","39 dias"
            ,"40 dias","41 dias","42 dias","43 dias","44 dias","45 dias","46 dias","47 dias","48 dias"
            ,"49 dias","50 dias","51 dias","52 dias","53 dias","54 dias","55 dias","56 dias","57 dias"
            ,"58 dias","59 dias","60 dias"};
    private static final String[] MINUTOS = new String[]{"1 min", "2 min","3 min", "4 min", "5 min", "6 min", "7 min", "8 min", "9 min", "10 min",
            "11 min","12 min","13 min","14 min","15 min","16 min","17 min","18 min","19 min","20 min","21 min","22 min","23 min","24 min","25 min",
            "26 min","27 min","28 min","29 min","30 min","31 min","32 min","33 min","34 min","35 min","36 min","37 v","38 min","39 min","40 min"
            ,"41 min","42 min","43 min","44 min","45 min","46 min","47 min","48 min","49 min","50 min"
            ,"51 min","52 min","53 min","54 min","55 min","56 min","57 min","58 min","59 min","60 min"};
    private static final String[] MILIGRAMAS = new String[]{"1 mg", "2 mg","3 mg", "4 mg", "5 mg", "6 mg", "7 mg", "8 mg", "9 mg", "10 mg",
            "11 mg","12 mg","13 mg","14 mg","15 mg","16 mg","17 mg","18 mg","19 mg","20 mg","21 mg","22 mg","23 mg","24 mg","25 mg",
            "26 mg","27 mg","28 mg","29 mg","30 mg","31 mg","32 mg","33 mg","34 mg","35 mg","36 mg","37 mg","38 mg","39 mg","40 mg"
            ,"41 mg","42 mg","43 mg","44 mg","45 mg","46 mg","47 mg","48 mg","49 mg","50 mg"
            ,"51 mg","52 mg","53 mg","54 mg","55 mg","56 mg","57 mg","58 mg","59 mg","60 mg"};

    private List<Macronutriente> macronutrientes;
    private List<Micronutriente> micronutrientes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_experimento);
        database = FirebaseDatabase.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
        getSupportActionBar().setTitle("Adicionar experimento");

        macronutrientes = new ArrayList<>();
        micronutrientes = new ArrayList<>();

        nome = (EditText)findViewById(R.id.ed_nome);
        descricao = (EditText)findViewById(R.id.ed_descricao);
        btnAdd = (FloatingActionButton) findViewById(R.id.btnAdd);
        spinner_idadePlantaTransplantio = (Spinner) findViewById(R.id.sp_idadePlantaTransplantio);
        spinner_tempoBombaLigado = (Spinner) findViewById(R.id.sp_tempoBombaLigado);
        spinner_tempoBombaDesligado = (Spinner) findViewById(R.id.sp_tempoBombaDesligado);
        calendar = (CalendarView) findViewById(R.id.cv_dataTransplantio);

        setSpinnersMacro();
        setSpinnersMicro();
        setListeners();
    }

    public void setSpinner(Spinner spinner, final String nome,String tipo){

        if(tipo.equals("macro")) {

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    for (Macronutriente m : macronutrientes) {
                        if (m.getNome().equals(nome)) {
                            //macronutrientes.remove(m);//remove o macronutriente da lista
                            m.setQtd(Integer.parseInt(String.valueOf(adaptador.getItem(i)))); //atualiza qtd do macro
                            //macronutrientes.add(m);//adiciona novamente na lista
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });

        }else if(tipo.equals("micro")){

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    for (Micronutriente m : micronutrientes) {
                        if (m.getNome().equals(nome)) {
                            //macronutrientes.remove(m);//remove o macronutriente da lista
                            m.setQtd(Integer.parseInt(String.valueOf(adaptador.getItem(i)))); //atualiza qtd do macro
                            //macronutrientes.add(m);//adiciona novamente na lista
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });



        }

    }


    public void setSpinnersMacro(){

        spinner_nitrato_de_calcio = (Spinner) findViewById(R.id.spinner_nitrato_de_calcio);
        spinner_nitrato_de_potassio = (Spinner) findViewById(R.id.spinner_nitrato_de_potassio);
        spinner_sulfato_de_magnesio = (Spinner) findViewById(R.id.spinner_sulfato_de_magnesio);
        spinner_cloreto_de_potassio =(Spinner) findViewById(R.id.spinner_cloreto_de_potassio);
        spinner_MAP_purificado = (Spinner) findViewById(R.id.spinner_MAP_purificado);
        spinner_MKP = (Spinner) findViewById(R.id.spinner_MKP);
        spinner_sulfato_de_potassio =(Spinner) findViewById(R.id.spinner_sulfato_de_potassio);



        spinner_nitrato_de_calcio.setEnabled(false);
        spinner_nitrato_de_potassio.setEnabled(false);
        spinner_sulfato_de_magnesio.setEnabled(false);
        spinner_cloreto_de_potassio.setEnabled(false);
        spinner_MAP_purificado.setEnabled(false);
        spinner_MKP.setEnabled(false);
        spinner_sulfato_de_potassio.setEnabled(false);

        adaptadorMiligramas = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, MILIGRAMAS);

        spinner_nitrato_de_calcio.setAdapter(adaptadorMiligramas);
        spinner_nitrato_de_potassio.setAdapter(adaptadorMiligramas);
        spinner_sulfato_de_magnesio.setAdapter(adaptadorMiligramas);
        spinner_cloreto_de_potassio.setAdapter(adaptadorMiligramas);
        spinner_MAP_purificado.setAdapter(adaptadorMiligramas);
        spinner_MKP.setAdapter(adaptadorMiligramas);
        spinner_sulfato_de_potassio.setAdapter(adaptadorMiligramas);

        setSpinner(spinner_nitrato_de_calcio,"Nitrato de Cálcio","macro");
        setSpinner( spinner_nitrato_de_potassio,"Nitrato de Potássio","macro");
        setSpinner(spinner_sulfato_de_magnesio,"Sulfato de Magnésio","macro");
        setSpinner(spinner_cloreto_de_potassio,"Cloreto de Potássio","macro");
        setSpinner(spinner_MAP_purificado,"MAP purificado","macro");
        setSpinner(spinner_MKP,"MKP","macro");
        setSpinner(spinner_sulfato_de_potassio,"Sulfato de Potássio","macro");




    }

    public void setSpinnersMicro(){

        spinner_sulfato_de_manganes = (Spinner) findViewById(R.id.spinner_sulfato_de_manganes);
        spinner_sulfato_de_zinco = (Spinner) findViewById(R.id.spinner_sulfato_de_zinco);
        spinner_sulfato_de_cobre = (Spinner) findViewById(R.id.spinner_sulfato_de_cobre);
        spinner_acido_borico = (Spinner) findViewById(R.id.spinner_acido_borico);
        spinner_molibdato_de_sodio = (Spinner) findViewById(R.id.spinner_molibdato_de_sodio);
        spinner_molibdato_de_amonio = (Spinner) findViewById(R.id.spinner_molibdato_de_amonio);
        spinner_feedta_na2 = (Spinner) findViewById(R.id.spinner_feedta_na2);
        spinner_ferrilene = (Spinner) findViewById(R.id.spinner_ferrilene);
        spinner_tenso_fe = (Spinner) findViewById(R.id.spinner_tenso_fe);
        spinner_dissolvine = (Spinner) findViewById(R.id.spinner_dissolvine);
        spinner_rexolin_m48 = (Spinner) findViewById(R.id.spinner_rexolin_m48);

        spinner_sulfato_de_manganes.setEnabled(false);
        spinner_sulfato_de_zinco.setEnabled(false);
        spinner_sulfato_de_cobre.setEnabled(false);
        spinner_acido_borico.setEnabled(false);
        spinner_molibdato_de_sodio.setEnabled(false);
        spinner_molibdato_de_amonio.setEnabled(false);
        spinner_feedta_na2.setEnabled(false);
        spinner_ferrilene.setEnabled(false);
        spinner_tenso_fe.setEnabled(false);
        spinner_dissolvine.setEnabled(false) ;
        spinner_rexolin_m48.setEnabled(false);

        adaptadorMiligramas = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, MILIGRAMAS);

        spinner_sulfato_de_manganes.setAdapter(adaptadorMiligramas);
        spinner_sulfato_de_zinco.setAdapter(adaptadorMiligramas);
        spinner_sulfato_de_cobre.setAdapter(adaptadorMiligramas);
        spinner_acido_borico.setAdapter(adaptadorMiligramas);
        spinner_molibdato_de_sodio.setAdapter(adaptadorMiligramas);
        spinner_molibdato_de_amonio.setAdapter(adaptadorMiligramas);
        spinner_feedta_na2.setAdapter(adaptadorMiligramas);
        spinner_ferrilene.setAdapter(adaptadorMiligramas);
        spinner_tenso_fe.setAdapter(adaptadorMiligramas);
        spinner_dissolvine.setAdapter(adaptadorMiligramas) ;
        spinner_rexolin_m48.setAdapter(adaptadorMiligramas);

        setSpinner(spinner_sulfato_de_manganes,"Sulfato de Manganês","micro");
        setSpinner(spinner_sulfato_de_zinco,"Sulfato de Zinco","micro");
        setSpinner(spinner_sulfato_de_cobre,"Sulfato de Cobre","micro");
        setSpinner(spinner_acido_borico,"Ácido Bórico","micro");
        setSpinner(spinner_molibdato_de_sodio,"Molibdato de Sódio","micro");
        setSpinner(spinner_molibdato_de_amonio,"Molibdato de Amônio","micro");
        setSpinner(spinner_feedta_na2,"FeEDTA Na2","micro");
        setSpinner(spinner_ferrilene,"Ferrilene","micro");
        setSpinner(spinner_tenso_fe,"Tenso-Fe","micro");
        setSpinner(spinner_dissolvine,"Dissolvine","micro");
        setSpinner(spinner_rexolin_m48,"Rexolin M48","micro");
    }



    public void setListeners(){
        adaptador = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, NUMEROS);
        ArrayAdapter<String> adaptadorDias = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, DIAS);
        ArrayAdapter<String> adaptadorMinutos = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, MINUTOS);



        spinner_idadePlantaTransplantio.setAdapter(adaptadorDias);
        spinner_tempoBombaLigado.setAdapter(adaptadorMinutos);
        spinner_tempoBombaDesligado.setAdapter(adaptadorMinutos);

        spinner_idadePlantaTransplantio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("click", "onItemSelected: " + adapterView.getItemAtPosition(i));
                save_idadePlantaTransplantio =  Integer.parseInt(String.valueOf(adaptador.getItem(i)));
                save_idadePlantaAtual = Integer.parseInt(String.valueOf(adaptador.getItem(i)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spinner_tempoBombaLigado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                save_tempoBombaLigado = Integer.parseInt(String.valueOf(adaptador.getItem(i)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_tempoBombaDesligado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                save_tempoBombaDesligado = Integer.parseInt(String.valueOf(adaptador.getItem(i)));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save_nome = nome.getText().toString();
                save_descricao = descricao.getText().toString();
                save_dataTransplantio = convertMillisToDate(calendar.getDate());

                save_nutrientes =  new Nutriente(macronutrientes,micronutrientes);
                SAVE_EXPERIMENTO = new Experimento(save_nome,save_descricao,save_variedade,save_nutrientes,
                        save_dataTransplantio,save_idadePlantaTransplantio,save_idadePlantaAtual,
                        save_tempoBombaLigado,save_tempoBombaDesligado);
                experimento = database.getReference(nome.getText().toString());
                experimento.setValue(SAVE_EXPERIMENTO);
                setResult(1);
                finish();

            }
        });


        RadioGroup radioGroupAlface = (RadioGroup) findViewById(R.id.radioGroupAlface);
        radioGroupAlface.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton button = (RadioButton) group.findViewById(checkedId);
                save_variedade = button.getText().toString();
            }
        });


    }


    public void checkBox(int id,boolean checked, Spinner spinner,String tipo){

        if(tipo.equals("macro")) {

                CheckBox check = (CheckBox) findViewById(id);
                Log.i("check", check.getText().toString());

                if (checked) {
                    //adiciona macronutriente a lista
                    Macronutriente macro = new Macronutriente(check.getText().toString(), 0);
                    spinner.setEnabled(true);
                    macronutrientes.add(macro);
                } else {
                    //remove macronutriente da lista com o nome
                    for (int i = 0; i < macronutrientes.size(); i++) {
                        if (macronutrientes.get(i).getNome().equals(check.getText().toString())) {
                            macronutrientes.remove(macronutrientes.get(i));
                        }
                    }

                    spinner.setEnabled(false);
                }

                Log.i("check", String.valueOf(macronutrientes.size()));
                for (Macronutriente m : macronutrientes) {
                    Log.i("check", m.getNome());

                }
        }else if(tipo.equals("micro")){

                CheckBox check = (CheckBox) findViewById(id);
                Log.i("check", check.getText().toString());

                if (checked) {
                    //adiciona macronutriente a lista
                    Micronutriente micro = new Micronutriente(check.getText().toString(), 0);
                    spinner.setEnabled(true);
                    micronutrientes.add(micro);
                } else {
                    //remove macronutriente da lista com o nome
                    for (int i = 0; i < micronutrientes.size(); i++) {
                        if (micronutrientes.get(i).getNome().equals(check.getText().toString())) {
                            micronutrientes.remove(micronutrientes.get(i));
                        }
                    }
                    spinner.setEnabled(false);
                }

                Log.i("check", String.valueOf(micronutrientes.size()));
                for (Micronutriente m : micronutrientes) {
                    Log.i("check", m.getNome());
                }

            }



        }





    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        switch (view.getId()) {
            case R.id.checkbox_nitrato_de_calcio:
                checkBox(R.id.checkbox_nitrato_de_calcio, checked, spinner_nitrato_de_calcio, "macro");
                break;
            case R.id.checkbox_nitrato_de_potassio:
                checkBox(R.id.checkbox_nitrato_de_potassio, checked, spinner_nitrato_de_potassio, "macro");
                break;
            case R.id.checkbox_sulfato_de_magnesio:
                checkBox(R.id.checkbox_sulfato_de_magnesio, checked, spinner_sulfato_de_magnesio, "macro");
                break;
            case R.id.checkbox_cloreto_de_potassio:
                checkBox(R.id.checkbox_cloreto_de_potassio, checked, spinner_cloreto_de_potassio, "macro");
                break;
            case R.id.checkbox_MAP_purificado:
                checkBox(R.id.checkbox_MAP_purificado, checked, spinner_MAP_purificado, "macro");
                break;
            case R.id.checkbox_MKP:
                checkBox(R.id.checkbox_MKP, checked, spinner_MKP, "macro");
                break;
            case R.id.checkbox_sulfato_de_potassio:
                checkBox(R.id.checkbox_sulfato_de_potassio, checked, spinner_sulfato_de_potassio, "macro");
                break;


            case R.id.checkbox_sulfato_de_manganes:
                checkBox(R.id.checkbox_sulfato_de_manganes, checked, spinner_sulfato_de_manganes, "micro");
                break;
            case R.id.checkbox_sulfato_de_zinco:
                checkBox(R.id.checkbox_sulfato_de_zinco, checked, spinner_sulfato_de_zinco, "micro");
                break;
            case R.id.checkbox_sulfato_de_cobre:
                checkBox(R.id.checkbox_sulfato_de_cobre, checked, spinner_sulfato_de_cobre, "micro");
                break;
            case R.id.checkbox_acido_borico:
                checkBox(R.id.checkbox_acido_borico, checked, spinner_acido_borico, "micro");
                break;
            case R.id.checkbox_molibdato_de_sodio:
                checkBox(R.id.checkbox_molibdato_de_sodio, checked, spinner_molibdato_de_sodio, "micro");
                break;
            case R.id.checkbox_molibdato_de_amonio:
                checkBox(R.id.checkbox_molibdato_de_amonio, checked, spinner_molibdato_de_amonio, "micro");
                break;
            case R.id.checkbox_feedta_na2:
                checkBox(R.id.checkbox_feedta_na2, checked, spinner_feedta_na2, "micro");
                break;
            case R.id.checkbox_ferrilene:
                checkBox(R.id.checkbox_ferrilene, checked, spinner_ferrilene, "micro");
                break;
            case R.id.checkbox_tenso_fe:
                checkBox(R.id.checkbox_tenso_fe, checked, spinner_tenso_fe, "micro");
                break;
            case R.id.checkbox_dissolvine:
                checkBox(R.id.checkbox_dissolvine, checked, spinner_dissolvine, "micro");
                break;
            case R.id.checkbox_rexolin_m48:
                checkBox(R.id.checkbox_rexolin_m48, checked, spinner_rexolin_m48, "micro");
                break;

        }
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
    public String convertMillisToDate(long yourmilliseconds){

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);


        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("US/Central"));
        calendar.setTimeInMillis(yourmilliseconds);

        Log.i("click","GregorianCalendar -"+sdf.format(calendar.getTime()));


        return sdf.format(calendar.getTime());
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(2);//operacao cancelada
        finish();
    }
}

package com.example.suelliton.horus;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.suelliton.horus.model.Experimento;
import com.example.suelliton.horus.model.Macronutriente;
import com.example.suelliton.horus.model.Micronutriente;
import com.example.suelliton.horus.model.Solucao;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static java.lang.Integer.valueOf;

public class NovoExperimentoActivity extends AppCompatActivity {
    private FirebaseDatabase database ;
    private DatabaseReference experimentoReference ;
    private ValueEventListener childValueExperimento;
    List<Experimento> listaExperimentos;

    EditText nome;
    EditText descricao;
    FloatingActionButton btnAdd;
    Spinner spinner_idadePlantaTransplantio;
    Spinner spinner_tempoBombaLigado;
    Spinner spinner_tempoBombaDesligado;
    //spinner macronutrientes
    EditText edittext_N;
    EditText edittext_P;
    EditText edittext_K;
    EditText edittext_Ca;
    EditText edittext_Mg;
    EditText edittext_S;

    //spinner micronutrientes
    EditText edittext_Fe;
    EditText edittext_Mn;
    EditText edittext_Zn;
    EditText edittext_Cu;
    EditText edittext_B;
    EditText edittext_CI;
    EditText edittext_Mo;

    TextView textview_N;
    TextView textview_P;
    TextView textview_K;
    TextView textview_Ca;
    TextView textview_Mg;
    TextView textview_S;


    TextView textview_Fe;
    TextView textview_Mn;
    TextView textview_Zn;
    TextView textview_Cu;
    TextView textview_B;
    TextView textview_CI;
    TextView textview_Mo;

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
    Solucao save_solucao;

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

        experimentoReference = database.getReference();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
        getSupportActionBar().setTitle("Adicionar experimento");

        listaExperimentos = new ArrayList<>();

        macronutrientes = new ArrayList<>();
        micronutrientes = new ArrayList<>();

        nome = (EditText)findViewById(R.id.ed_nome);
        descricao = (EditText)findViewById(R.id.ed_descricao);
        btnAdd = (FloatingActionButton) findViewById(R.id.btnAdd);
        spinner_idadePlantaTransplantio = (Spinner) findViewById(R.id.sp_idadePlantaTransplantio);
        spinner_tempoBombaLigado = (Spinner) findViewById(R.id.sp_tempoBombaLigado);
        spinner_tempoBombaDesligado = (Spinner) findViewById(R.id.sp_tempoBombaDesligado);
        calendar = (CalendarView) findViewById(R.id.cv_dataTransplantio);

        setEditTextMacro();
        setEditTextMicro();
        setListeners();
        setTextViews();

        childValueExperimento = experimentoReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaExperimentos.removeAll(listaExperimentos);

                for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    Experimento experimento = snapshot.getValue(Experimento.class);//pega o objeto do firebase
                    listaExperimentos.add(experimento);//adiciona na lista que vai para o adapter
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
/*
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
*/
public void setTextViews(){
    textview_N = (TextView) findViewById(R.id.textview_N);
    textview_P = (TextView) findViewById(R.id.textview_P);
    textview_K = (TextView) findViewById(R.id.textview_K);
    textview_Ca = (TextView) findViewById(R.id.textview_Ca);
    textview_Mg = (TextView) findViewById(R.id.textview_Mg);
    textview_S = (TextView) findViewById(R.id.textview_S);


    textview_Fe = (TextView) findViewById(R.id.textview_Fe);
    textview_Mn = (TextView) findViewById(R.id.textview_Mn);
    textview_Zn = (TextView) findViewById(R.id.textview_Zn);
    textview_Cu = (TextView) findViewById(R.id.textview_Cu);
    textview_B = (TextView) findViewById(R.id.textview_B);
    textview_CI = (TextView) findViewById(R.id.textview_CI);
    textview_Mo = (TextView) findViewById(R.id.textview_Mo);

    textview_N.setEnabled(false);
    textview_P.setEnabled(false);
    textview_K.setEnabled(false);
    textview_Ca.setEnabled(false);
    textview_Mg.setEnabled(false);
    textview_S.setEnabled(false);

    textview_Fe.setEnabled(false);
    textview_Mn.setEnabled(false);
    textview_Zn.setEnabled(false);
    textview_Cu.setEnabled(false);
    textview_B.setEnabled(false);
    textview_CI.setEnabled(false);
    textview_Mo.setEnabled(false);


}

    public void setEditTextMacro(){

        edittext_N = (EditText) findViewById(R.id.edittext_N);
        edittext_P = (EditText) findViewById(R.id.edittext_P);
        edittext_K = (EditText) findViewById(R.id.edittext_K);
        edittext_Ca = (EditText) findViewById(R.id.edittext_Ca);
        edittext_Mg = (EditText) findViewById(R.id.edittext_Mg);
        edittext_S = (EditText) findViewById(R.id.edittext_S);

        edittext_N.setEnabled(false);
        edittext_P.setEnabled(false);
        edittext_K.setEnabled(false);
        edittext_Ca.setEnabled(false);
        edittext_Mg.setEnabled(false);
        edittext_S.setEnabled(false);

    }

    public void setEditTextMicro(){

        edittext_Fe = (EditText) findViewById(R.id.edittext_Fe);
        edittext_Mn = (EditText) findViewById(R.id.edittext_Mn);
        edittext_Zn = (EditText) findViewById(R.id.edittext_Zn);
        edittext_Cu = (EditText) findViewById(R.id.edittext_Cu);
        edittext_B = (EditText) findViewById(R.id.edittext_B);
        edittext_CI = (EditText) findViewById(R.id.edittext_CI);
        edittext_Mo = (EditText) findViewById(R.id.edittext_Mo);

        edittext_Fe.setEnabled(false);
        edittext_Mn.setEnabled(false);
        edittext_Zn.setEnabled(false);
        edittext_Cu.setEnabled(false);
        edittext_B.setEnabled(false);
        edittext_CI.setEnabled(false);
        edittext_Mo.setEnabled(false);

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
                getValueEditText();
                save_solucao =  new Solucao(macronutrientes,micronutrientes);
                if(validaCadastro()) {

                    SAVE_EXPERIMENTO = new Experimento(save_nome, save_descricao, save_variedade, save_solucao,
                            save_dataTransplantio, save_idadePlantaTransplantio, save_idadePlantaAtual,
                            save_tempoBombaLigado, save_tempoBombaDesligado);
                    experimentoReference = database.getReference(nome.getText().toString());
                    experimentoReference.setValue(SAVE_EXPERIMENTO);
                    setResult(1);
                    finish();
                }else{

                    Snackbar.make(textview_N, "O experimento não foi salvo!, escolha outro nome.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                }

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
    public boolean validaCadastro(){

        for (Experimento e: listaExperimentos) {
            Log.i("repetido","ja existe experimento com esse nome");
            if(e.getNome().equals(save_nome)){

            return false;
            }
        }

        return true;

    }

    public void getValueEditText(){

        for (Macronutriente m:macronutrientes  ) {
            switch (m.getNome()){
                case "N":
                    m.setQtd(Double.parseDouble(edittext_N.getText().toString()));
                break;
                case "P":
                    m.setQtd(Double.parseDouble(edittext_P.getText().toString()));
                    break;
                case "K":
                    m.setQtd(Double.parseDouble(edittext_K.getText().toString()));
                    break;
                case "Ca":
                    m.setQtd(Double.parseDouble(edittext_Ca.getText().toString()));
                    break;
                case "Mg":
                    m.setQtd(Double.parseDouble(edittext_Mg.getText().toString()));
                    break;
                case "S":
                    m.setQtd(Double.parseDouble(edittext_S.getText().toString()));
                    break;

            }
        }

        for (Micronutriente m:micronutrientes  ) {
            switch (m.getNome()){
                case "Fe":
                    m.setQtd(Double.parseDouble(edittext_Fe.getText().toString()));
                    break;
                case "Mn":
                    m.setQtd(Double.parseDouble(edittext_Mn.getText().toString()));
                    break;
                case "Zn":
                    m.setQtd(Double.parseDouble(edittext_Zn.getText().toString()));
                    break;
                case "Cu":
                    m.setQtd(Double.parseDouble(edittext_Cu.getText().toString()));
                    break;
                case "B":
                    m.setQtd(Double.parseDouble(edittext_B.getText().toString()));
                    break;
                case "CI":
                    m.setQtd(Double.parseDouble(edittext_CI.getText().toString()));
                    break;
                case "Mo":
                    m.setQtd(Double.parseDouble(edittext_Mo.getText().toString()));
                    break;

            }
        }

    }

    public void checkBox(int id,boolean checked, EditText edittext, TextView textview ,String tipo){

        if(tipo.equals("macro")) {

                CheckBox check = (CheckBox) findViewById(id);
                Log.i("check", check.getText().toString());

                if (checked) {
                    //adiciona macronutriente a lista
                    Macronutriente macro = new Macronutriente(check.getText().toString(), 0);
                    edittext.setEnabled(true);
                    textview.setEnabled(true);//habilita edittext
                    edittext.requestFocus();//dar foco no edittext
                    //chama teclado automático
                    InputMethodManager imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(edittext, InputMethodManager.SHOW_IMPLICIT);

                    macronutrientes.add(macro);
                } else {
                    //remove macronutriente da lista com o nome
                    for (int i = 0; i < macronutrientes.size(); i++) {
                        if (macronutrientes.get(i).getNome().equals(check.getText().toString())) {
                            macronutrientes.remove(macronutrientes.get(i));
                        }
                    }

                    edittext.setEnabled(false);
                    textview.setEnabled(false);
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
                    edittext.setEnabled(true);
                    textview.setEnabled(true);//habilita edittext
                    edittext.requestFocus();//dar foco no edittext
                    //chama teclado automático
                    InputMethodManager imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(edittext, InputMethodManager.SHOW_IMPLICIT);
                    micronutrientes.add(micro);
                } else {
                    //remove macronutriente da lista com o nome
                    for (int i = 0; i < micronutrientes.size(); i++) {
                        if (micronutrientes.get(i).getNome().equals(check.getText().toString())) {
                            micronutrientes.remove(micronutrientes.get(i));
                        }
                    }
                    edittext.setEnabled(false);
                    textview.setEnabled(false);
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
            //macronutriente
            case R.id.checkbox_N:
                checkBox(R.id.checkbox_N, checked, edittext_N, textview_N, "macro");
                break;
            case R.id.checkbox_P:
                checkBox(R.id.checkbox_P, checked, edittext_P,textview_P , "macro");
                break;
            case R.id.checkbox_K:
                checkBox(R.id.checkbox_K, checked, edittext_K,textview_K, "macro");
                break;
            case R.id.checkbox_Ca:
                checkBox(R.id.checkbox_Ca, checked, edittext_Ca,textview_Ca, "macro");
                break;
            case R.id.checkbox_Mg:
                checkBox(R.id.checkbox_Mg, checked, edittext_Mg, textview_Mg,"macro");
                break;
            case R.id.checkbox_S:
                checkBox(R.id.checkbox_S, checked, edittext_S,textview_S ,"macro");
                break;


            //micronutrientes
            case R.id.checkbox_Fe:
                checkBox(R.id.checkbox_Fe, checked, edittext_Fe, textview_Fe,"micro");
                break;
            case R.id.checkbox_Mn:
                checkBox(R.id.checkbox_Mn, checked, edittext_Mn, textview_Mn,"micro");
                break;
            case R.id.checkbox_Zn:
                checkBox(R.id.checkbox_Zn, checked, edittext_Zn, textview_Zn,"micro");
                break;
            case R.id.checkbox_Cu:
                checkBox(R.id.checkbox_Cu, checked, edittext_Cu, textview_Cu,"micro");
                break;
            case R.id.checkbox_B:
                checkBox(R.id.checkbox_B, checked, edittext_B,textview_B ,"micro");
                break;
            case R.id.checkbox_CI:
                checkBox(R.id.checkbox_CI, checked, edittext_CI,textview_CI ,"micro");
                break;
            case R.id.checkbox_Mo:
                checkBox(R.id.checkbox_Mo, checked, edittext_Mo, textview_Mo,"micro");
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

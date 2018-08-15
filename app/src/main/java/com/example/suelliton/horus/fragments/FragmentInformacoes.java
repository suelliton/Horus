package com.example.suelliton.horus.fragments;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suelliton.horus.R;
import com.example.suelliton.horus.adapters.MacronutrienteAdapter;
import com.example.suelliton.horus.adapters.MicronutrienteAdapter;
import com.example.suelliton.horus.models.Captura;
import com.example.suelliton.horus.models.Experimento;
import com.example.suelliton.horus.models.Macronutriente;
import com.example.suelliton.horus.models.Micronutriente;
import com.example.suelliton.horus.models.Solucao;
import com.example.suelliton.horus.models.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.suelliton.horus.DetalhesActivity.nomeExperimento;
import static com.example.suelliton.horus.SplashActivity.LOGADO;

public class FragmentInformacoes extends Fragment {

    static Button btnFinalizar;
    DatabaseReference usuarioReference ;
    FirebaseDatabase database;
    TextView nome;
    TextView descricao;
    TextView variedade;
    TextView idadeTransplantio;
    TextView tempoBombaLigada;
    TextView tempoBombaDesligada;
    TextView dataInicio;
    TextView idadeAtual;
    TextView quantidadeFotos;

    private ValueEventListener childValueUsuario;
    private MicronutrienteAdapter microAdapter;
    private MacronutrienteAdapter macroAdapter;
    private List<Experimento> listaExperimentos ;
    private   Usuario USUARIO_OBJETO_LOGADO;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_informacoes, container, false);
        database =  FirebaseDatabase.getInstance();
        usuarioReference = database.getReference();
        listaExperimentos = new ArrayList<>();

        btnFinalizar = (Button) v.findViewById(R.id.btn_finalizar);
        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog();
            }
        });



        nome = (TextView) v.findViewById(R.id.tx_nome);
        descricao = (TextView) v.findViewById(R.id.tx_descricao);
        variedade = (TextView) v.findViewById(R.id.tx_variedade);
        idadeTransplantio = (TextView) v.findViewById(R.id.tx_idade_transplantio);
        tempoBombaLigada = (TextView) v.findViewById(R.id.tx_tempo_bomba_ligada);
        tempoBombaDesligada = (TextView) v.findViewById(R.id.tx_tempo_bomba_desligada);
        dataInicio = (TextView) v.findViewById(R.id.tx_data_inicio);
        idadeAtual = (TextView) v.findViewById(R.id.tx_idade_atual);
        quantidadeFotos = (TextView) v.findViewById(R.id.tx_qtd_fotos);

        childValueUsuario = usuarioReference.child(LOGADO).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Experimento experimento = new Experimento();
                listaExperimentos.removeAll(listaExperimentos);
                for (Experimento e: dataSnapshot.getValue(Usuario.class).getExperimentos()) {
                    listaExperimentos.add(e);
                    if(e.getNome().equals(nomeExperimento)){
                        experimento = e;
                    }

                }

                nome.setText(experimento.getNome());
                descricao.setText(experimento.getDescricao());
                variedade.setText(experimento.getVariedade());
                idadeTransplantio.setText(""+experimento.getIdadePlantaTransplantio()+ " dias");
                tempoBombaLigada.setText(""+experimento.getTempoBombaLigado()+ " minutos");
                tempoBombaDesligada.setText(""+experimento.getTempoBombaDesLigado()+ " minutos");
                dataInicio.setText(""+experimento.getDataTransplantio());
                idadeAtual.setText(""+experimento.getIdadePlantaAtual()+ " dias");
                quantidadeFotos.setText(""+experimento.getCount() + " capturas");

                Solucao solucao = experimento.getNutrientes();

                if(solucao != null) {
                    List<Micronutriente> micros = experimento.getNutrientes().getMicronutrientes();
                    if(micros != null){

                        microAdapter = new MicronutrienteAdapter(v.getContext(), micros);
                        RecyclerView recyclerMicro = (RecyclerView) v.findViewById(R.id.recycler_micronutrintes);
                        recyclerMicro.setAdapter(microAdapter);
                        RecyclerView.LayoutManager layout = new LinearLayoutManager(v.getContext(), LinearLayoutManager.VERTICAL, false);
                        recyclerMicro.setLayoutManager(layout);
                    }
                    List<Macronutriente> macros = experimento.getNutrientes().getMacronutrientes();
                    if(macros != null) {
                        macroAdapter = new MacronutrienteAdapter(v.getContext(), macros);
                        RecyclerView recyclerMacro = (RecyclerView) v.findViewById(R.id.recycler_macronutrintes);
                        recyclerMacro.setAdapter(macroAdapter);
                        RecyclerView.LayoutManager layout2 = new LinearLayoutManager(v.getContext(), LinearLayoutManager.VERTICAL, false);
                        recyclerMacro.setLayoutManager(layout2);
                    }

                }else{
                    v.findViewById(R.id.tx_micro).setVisibility(View.INVISIBLE);//se não tiver dados escondo o titulo
                    v.findViewById(R.id.tx_macro).setVisibility(View.INVISIBLE);//se não tiver dados escondo o titulo
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return v;
    }
    public void createDialog(){
        AlertDialog.Builder dialogExcluir = new AlertDialog.Builder(getView().getContext());
        dialogExcluir.setIcon(R.mipmap.ic_launcher);
        dialogExcluir.setTitle("Finalizar Experimento");
        dialogExcluir.setMessage("Deseja finalizar o experimento?");
        dialogExcluir.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (Experimento e: listaExperimentos                 ) {
                    if(e.getNome().equals(nomeExperimento)){
                        e.setStatus("finalizado");
                    }
                }
                usuarioReference.child(LOGADO).child("experimentos").setValue(listaExperimentos);
                getActivity().finish();//mata a activity atual
                Toast.makeText(getView().getContext(), "O experimento foi finalizado com sucesso", Toast.LENGTH_LONG).show();
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


}

package com.example.suelliton.horus;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.suelliton.horus.adapters.ExperimentoAdapter;
import com.example.suelliton.horus.models.Experimento;
import com.example.suelliton.horus.utils.MeuRecyclerViewClickListener;
import com.example.suelliton.horus.utils.PermissionUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Principal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    // StorageActivity Permissions
    private static String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    private FirebaseDatabase database ;
    private DatabaseReference experimentoReference ;
    private ChildEventListener childEventExperimento;
    private ValueEventListener childValueExperimento;
    RecyclerView recyclerView;
    List<Experimento> listaExperimentos;
    Context context = this;
    static View ViewSnack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listaExperimentos = new ArrayList<>();
        try {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);//persistencia em disco local
        }catch (Exception e){
            Log.i("teste","nao inicializou persistencia");
        }

        database =  FirebaseDatabase.getInstance();
        experimentoReference = database.getReference();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ViewSnack = drawer;//serve de parametro pro snack achar a rootview


        FloatingActionButton adicionarNovo = (FloatingActionButton) findViewById(R.id.floatAdicionar);
        adicionarNovo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getApplicationContext(),NovoExperimentoActivity.class),1);
            }
        });
    }



    public void setValuesRecycler(){

        final ExperimentoAdapter experimentoAdapter = new ExperimentoAdapter(this,listaExperimentos);



        recyclerView = (RecyclerView) findViewById(R.id.recycler_experimentos);
        recyclerView.setAdapter(experimentoAdapter);


        childValueExperimento = experimentoReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaExperimentos.removeAll(listaExperimentos);



                for (DataSnapshot snapshot:dataSnapshot.getChildren()) {

                    Experimento experimento = snapshot.getValue(Experimento.class);//pega o objeto do firebase
                    if(experimento.getStatus().equals("ativo")) {
                        listaExperimentos.add(experimento);//adiciona na lista que vai para o adapter
                    }
                    experimentoAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        // experimentoReference.addValueEventListener(childValueExperimento);//nao precisa dessa linha senao duplica os dados


        RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layout);




    }


    public void setOnclickRecycler(){

        recyclerView.addOnItemTouchListener(new MeuRecyclerViewClickListener(Principal.this, recyclerView, new MeuRecyclerViewClickListener.OnItemClickListener() {


            @Override
            public void OnItemClick(View view, int i) {

                //finish();
            }

            @Override
            public void OnItemLongClick(View view, int i) {

            }

            @Override
            public void onItemClick(View view, int position) {

            }
        }));

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_novo_experimento) {
            startActivityForResult(new Intent(getApplicationContext(),NovoExperimentoActivity.class),1);
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){//se chamou tela de novo experimento
            if(resultCode == 1){
                Snackbar.make(ViewSnack, "Novo experimento adicionado", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }else if(resultCode == 2){
                Snackbar.make(ViewSnack, "Operação cancelada", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        PermissionUtils.validate(Principal.this,0, PERMISSIONS_STORAGE);
        setValuesRecycler();
        setOnclickRecycler();
    }

    @Override
    protected void onPause() {
        super.onPause();
        listaExperimentos.removeAll(listaExperimentos);
        Log.i("teste","tamanho do array: "+ listaExperimentos.size());
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int result : grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) {
                // Alguma permissão foi negada, agora é com você :-)
                //btn.setEnabled(false);
                alertAndFinish();
                return;
            }
        }
        // btn.setEnabled(true);
    }

    private void alertAndFinish() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name).setMessage("Para utilizar todas as funções desse aplicativo, você precisa aceitar o acesso ao armazenamento externo.");
        // Add the buttons
        builder.setNegativeButton("Fechar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //faz nada...
            }
        });
        builder.setPositiveButton("Permitir", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void vibrar(){
        Vibrator vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long milliseconds = 80;//'50' é o tempo em milissegundos, é basicamente o tempo de duração da vibração. portanto, quanto maior este numero, mais tempo de vibração você irá ter
        vb.vibrate(milliseconds);
    }


}

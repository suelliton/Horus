package com.example.suelliton.horus;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
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
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.suelliton.horus.adapters.ExperimentoAdapter;
import com.example.suelliton.horus.models.Experimento;
import com.example.suelliton.horus.models.Usuario;
import com.example.suelliton.horus.utils.MeuRecyclerViewClickListener;
import com.example.suelliton.horus.utils.MyDatabaseUtil;
import com.example.suelliton.horus.utils.PermissionUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.suelliton.horus.SplashActivity.LOGADO;

public class Principal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    // StorageActivity Permissions
    private static String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    ProgressBar progressBar;
    public static View ViewSnackApoio;
    public static boolean sincronizando = false;
    FloatingActionButton adicionarNovo;
    DrawerLayout drawer;
    CoordinatorLayout coordinator;
    //FloatingActionButton sincronizar;
    private FirebaseDatabase database ;
    private FirebaseStorage storage;
    private DatabaseReference usuarioReference ;
    private ChildEventListener childEventExperimento;
    private ValueEventListener childValueUsuario;
    ExperimentoAdapter experimentoAdapter;
    RecyclerView recyclerView;
    List<Experimento> listaExperimentos;
    Context context = this;
    static View ViewSnack;
    Menu m;
    private  Usuario USUARIO_OBJETO_LOGADO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        database = MyDatabaseUtil.getDatabase();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listaExperimentos = new ArrayList<>();


        storage = FirebaseStorage.getInstance();

        //minha referencia principal para pegar os experimentos é a do usuario logado
        usuarioReference = database.getReference();


        coordinator = (CoordinatorLayout) findViewById(R.id.coordinator_principal);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        ViewSnack = progressBar;//serve de parametro pro snack achar a rootview


        status();


    }

    public void status(){
        Handler handle = new Handler();
        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                setFloatSync();
                status();
            }
        }, 2000);


    }
    //VERIFICA SE EXISTE WIFI
    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected())
            return true;
        else
            return false;
    }

    //UPLOAD FIREBASE STREAM
    public void uploadFirebaseStream(StorageReference Ref,final Experimento e) throws FileNotFoundException {
        String DIRECTORYNAME = "/Camera/Horus/" + LOGADO+"/"+e.getNome() + "/";
        String FILENAME = e.getNome()+e.getCount().toString()+".jpg";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), DIRECTORYNAME);
        InputStream stream = new FileInputStream(new File(storageDir.getAbsolutePath() + "/" + FILENAME));

        UploadTask uploadTask = Ref.putStream(stream);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Log.i("envio","conseguiu enviar a foto");
                DatabaseReference dr = database.getReference(USUARIO_OBJETO_LOGADO.getUsername());

                List<Experimento> lista = USUARIO_OBJETO_LOGADO.getExperimentos();
                for ( Experimento ex:lista ) {
                    if(ex.getNome().equals(e.getNome())){
                        ex.setCount(e.getCount()+1);//tirei o +1
                        ex.setNovaFoto(true);
                        ex.setSincronizado(true);
                    }
                }
                USUARIO_OBJETO_LOGADO.setExperimentos(lista);
                dr.setValue(USUARIO_OBJETO_LOGADO);
                sincronizando = false;
            }
        });
    }

   @SuppressLint("ResourceType")
   public void setFloatSync(){

            if(m != null) {//tem que ter o menu da toolbar carregado pra não dar pau
                MenuItem item = m.findItem(R.id.action_sync);//instancio o menu para poder pegar o botao de sincronizar

                for (Experimento e : listaExperimentos) {//itero sobre a lista de experimentos
                    if (!e.isSincronizado()) {//se não estiver sincronizado
                        if (isOnline(Principal.this)) {//e não tiver internet
                            if (!sincronizando) {// e se não estiver sincronizando
                                progressBar.setVisibility(View.GONE);
                                item.setIcon(R.mipmap.ic_no_sync);
                                item.setEnabled(true);//habilito botao
                                if (!item.isEnabled()) {
                                    item.setEnabled(true);
                                }
                            } else {
                                progressBar.setVisibility(View.VISIBLE);
                            }
                        } else {
                            item.setIcon(R.mipmap.ic_no_sync_red);
                            item.setEnabled(false);
                        }
                        break;

                    } else {
                        progressBar.setVisibility(View.GONE);
                        if (isOnline(Principal.this)) {
                            item.setIcon(R.mipmap.ic_sync);
                        } else {
                            item.setIcon(R.mipmap.ic_sync_red);
                        }
                        item.setEnabled(false);
                    }
                }

                if(listaExperimentos.size() == 0){//se alista for vazia bloquei a o botao
                    item.setEnabled(false);
                }
            }

   }

    public void setValuesRecycler(){
        experimentoAdapter = new ExperimentoAdapter(this,listaExperimentos);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_experimentos);
        recyclerView.setAdapter(experimentoAdapter);


        childValueUsuario = usuarioReference.child(LOGADO).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                USUARIO_OBJETO_LOGADO = dataSnapshot.getValue(Usuario.class);
                listaExperimentos.removeAll(listaExperimentos);
                try {
                    if(dataSnapshot.getValue(Usuario.class).getExperimentos() != null) {
                        for (Experimento e : dataSnapshot.getValue(Usuario.class).getExperimentos()) {
                            if(e.getStatus().equals("ativo")) {
                                listaExperimentos.add(e);
                            }
                            setFloatSync();
                            experimentoAdapter.notifyDataSetChanged();
                        }
                    }
                }catch (Exception e){
                    Toast.makeText(context, "Erro no banco de dados, contate administrador.", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

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
        if (id == R.id.action_add) {
            startActivityForResult(new Intent(getApplicationContext(),NovoExperimentoActivity.class),1);
            return true;
        }else
            if (id == R.id.action_sync) {//ação quando action_sync é clicado
            sincronizaApp(progressBar);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
//metodo para modificar menu da toolbar
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        m = menu;//atribuo o menu a uma variavel global ue poderá ser acessada em outros lugares para mudar o icone do action_sync
        return super.onPrepareOptionsMenu(menu);

    }
    //resposnsável por sincronizar os dados manualmente com a nuvem atraves do botao action_sync da toolbar
    public void sincronizaApp(View view){

        for (Experimento e:listaExperimentos) {//itero na lista
            sincronizando =  true;//digo que está sincronizando para o icone se modificar e ativar a progressbar
            if(!e.isSincronizado()){//se o esperimento atual não estiver sincronizado entra ai
                if(isOnline(this)) {//verifica se tem internet se tiver tenta sincronizar
                    try {
                        StorageReference alfaceRef = storage.getReference(LOGADO+"/"+e.getNome() + "/" + e.getNome()+e.getCount().toString()+".jpg");

                        uploadFirebaseStream(alfaceRef,e);

                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                        Snackbar.make(view.getRootView(), "Erro!", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }

                }else{//senão avisa que não tem internet
                    Snackbar.make(view.getRootView(), "Conecte se a internet", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                }

            }
        }
        Snackbar.make(view.getRootView(), "Sincronizando experimentos", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_galeria) {

            // Handle the camera action
        } else if (id == R.id.nav_historico) {

        } else if (id == R.id.nav_colaborador) {

        } else if (id == R.id.nav_logout) {
            SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("usuarioLogado", "");
            editor.apply();
            LOGADO="";
            USUARIO_OBJETO_LOGADO = null;
            startActivity(new Intent(Principal.this,LoginActivity.class));

            finish();
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
                Toast.makeText(context, "Experimento adicionado", Toast.LENGTH_SHORT).show();
            }else if(resultCode == 2){
                Toast.makeText(context, "Cadastro cancelado", Toast.LENGTH_SHORT).show();
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
        if(childValueUsuario !=null){
            usuarioReference.removeEventListener(childValueUsuario);
            //Toast.makeText(this, " listener do Principal desativado", Toast.LENGTH_SHORT).show();
        }

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

    private void alertAndFinish(){
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

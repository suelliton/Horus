package com.example.suelliton.horus;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.suelliton.horus.models.Usuario;
import com.example.suelliton.horus.utils.MyDatabaseUtil;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {
    public static String LOGADO;
    private FirebaseDatabase database ;
    private DatabaseReference RootReference ;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        database = MyDatabaseUtil.getDatabase();

        RootReference = database.getReference();

        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
        LOGADO = sharedPreferences.getString("usuarioLogado", "");







    }

    @Override
    protected void onStart() {
        super.onStart();
        Query queryUsuario = RootReference.orderByChild("username").equalTo(LOGADO).limitToFirst(1);

        if (!LOGADO.equals("")) {
            progressBar.setVisibility(View.VISIBLE);

            queryUsuario.addChildEventListener(new ChildEventListener() {


                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    try {
                        Usuario usuario = dataSnapshot.getValue(Usuario.class);
                        if (usuario != null) {
                            //Toast.makeText(SplashActivity.this, "usuario logado : "+LOGADO, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SplashActivity.this, Principal.class));
                            finish();
                        }

                    } catch (Exception e) {
                        Toast.makeText(SplashActivity.this, "Erro no banco de dados, contate administrador.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            if (!isOnline(this)) {
                Toast.makeText(this, "Sem conexão", Toast.LENGTH_SHORT).show();
            }

        }else{
            Handler h = new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            },2000);
        }



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
}

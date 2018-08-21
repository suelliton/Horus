package com.example.suelliton.horus;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import static com.example.suelliton.horus.DetalhesActivity.nomeExperimento;
import static com.example.suelliton.horus.SplashActivity.LOGADO;

public class ResultActivity extends AppCompatActivity {
    FirebaseStorage storage ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        storage = FirebaseStorage.getInstance();
        Bundle bundle = getIntent().getExtras();
        int indice = bundle.getInt("indice")+1;

        final ProgressBar progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);

        final ProgressBar progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);

        final ImageView imageView1 = (ImageView) findViewById(R.id.imageViewOriginal);
        StorageReference storageReference = storage.getReference();
        StorageReference imagemReference = storageReference.child("/"+LOGADO+"/"+nomeExperimento+"/"+nomeExperimento+String.valueOf(indice)+".jpg");
        imagemReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imageView1);
                progressBar1.setVisibility(View.GONE);
            }
        });
        final ImageView imageView2 = (ImageView) findViewById(R.id.imageViewProcessada);

        StorageReference imagemReference2 = storageReference.child("/"+LOGADO+"/"+nomeExperimento+"/result/"+nomeExperimento+String.valueOf(indice)+".jpg");
        imagemReference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imageView2);
                progressBar2.setVisibility(View.GONE);
            }
        });



    }
}

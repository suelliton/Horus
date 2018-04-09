package com.example.suelliton.horus;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.app.PendingIntent.getActivity;
import static com.example.suelliton.horus.Principal.ViewSnack;

/**
 * Created by André Gomes on 11/10/2017.
 */

public class StorageActivity extends AppCompatActivity {
    private TextView titulo;
    private ImageView exibeFoto;
    private String FILENAME = "";//nome da imagem que vai ser salva
    private String DIRECTORYNAME = "";//nome do diretorio onde a foto vai ser salva
    private String pictureImagePath = "";
    private Intent cameraIntent;
    FirebaseStorage storage;
    String nomeExperimento = "";
    private Integer count = 0;
    private FirebaseDatabase database;
    FloatingActionButton btnUpload;
    FloatingActionButton btnTiraFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
        getSupportActionBar().setTitle("Adicionar foto");


        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        this.titulo = (TextView) findViewById(R.id.txtTitulo);

        Bundle bundle = getIntent().getExtras();
        nomeExperimento = bundle.getString("nomeExp");
        count = bundle.getInt("count");
        Log.i("teste2", "count : " + count);
        titulo.setText(nomeExperimento);

        FILENAME = nomeExperimento + count + ".jpg";
        DIRECTORYNAME = "/Camera/Horus/" + nomeExperimento + "/";


        exibeFoto = (ImageView) findViewById(R.id.exibeFoto);

        btnTiraFoto = (FloatingActionButton) findViewById(R.id.tiraFoto);

        btnUpload = (FloatingActionButton) findViewById(R.id.uploadFoto);
        btnUpload.setClickable(false);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnUpload.setClickable(false);
                try {
                    Snackbar.make(ViewSnack, "Fazendo upload para o firebase", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    StorageReference alfaceRef = storage.getReference(nomeExperimento + "/" + FILENAME);

                    uploadFirebaseStream(alfaceRef);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Snackbar.make(view, "Erro!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                DatabaseReference dr = database.getReference(nomeExperimento);
                dr.getRef().child("count").setValue(count + 1);//INCREMENTA A VARIAVEL DE CONTROLE
                count++;//INCREMENTA VARIAVEL LOCAL
                dr.getRef().child("ultimaCaptura").setValue(getDataAtual());//seta a hora da captura

                finish();

            }
        });


        btnTiraFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    takePhotoAndSave();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        //chama o método pra tirar foto
        try {
            takePhotoAndSave();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //UPLOAD FIREBASE STREAM
    public void uploadFirebaseStream(StorageReference Ref) throws FileNotFoundException {
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
            }
        });
    }

    public void uploadFirebaseBytes(StorageReference Ref) {
        Bitmap bitmap = null;
        try {
            bitmap = readPhoto();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = Ref.putBytes(data);
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
            }
        });
    }

    //TIRA A FOTO
    public void takePhotoAndSave() throws IOException {

        if (checkStorage() == false) {
            return;
        } else {
            File file = createImageFile();
            Uri outputFileUri = FileProvider.getUriForFile(StorageActivity.this, BuildConfig.APPLICATION_ID + ".provider", file);
            Log.i("Salvando", file.getAbsolutePath());
            cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            startActivityForResult(cameraIntent, 1);//numero requisição

        }
    }

    //LER A FOTO DA GALERIA
    public Bitmap readPhoto() throws IOException {
        Bitmap myBitmap = null;

        if (checkStorage() == false) {
            Toast.makeText(this, "Storage is busy", Toast.LENGTH_SHORT).show();
            return null;
        } else {
            //File imgFile = createImageFile();

            //Log.i("Imagem", imgFile.getAbsolutePath());
            //if (imgFile.exists()) {
            myBitmap = BitmapFactory.decodeFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + DIRECTORYNAME + FILENAME);
            //  Log.i("Testes", "Arquivo:" + imgFile.getAbsolutePath());
            // Log.i("teste2",myBitmap.toString());


            Bitmap bMapScaled = Bitmap.createScaledBitmap(myBitmap, 3715, 2786, true);


            exibeFoto.setImageBitmap(bMapScaled);
            //}
        }
        return myBitmap;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Toast.makeText(this, "Imagem salva com sucesso no armazenamento externo", Toast.LENGTH_SHORT).show();
            try {
                readPhoto();
            } catch (IOException e) {
                e.printStackTrace();
            }
            btnUpload.setClickable(true);

        } else {
            finish();//se apertar back estando na camera fecha a activity
        }

    }

    public String getDataAtual() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-\n HH:mm:ss");
        // OU
        SimpleDateFormat dateFormat_hora = new SimpleDateFormat("HH:mm:ss");

        Date data = new Date();

        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        Date data_atual = cal.getTime();

        String data_completa = dateFormat.format(data_atual);

        String hora_atual = dateFormat_hora.format(data_atual);

        Log.i("data_completa", data_completa);
        Log.i("data_atual", data_atual.toString());
        Log.i("hora_atual", hora_atual); // Esse é o que você quer

        return data_completa;
    }

    //ESTE MÉTODO CHECA SE O APP TEM PERMISSÃO PARA ACESSAR O ARMAZENAMENTO EXTERNO(GALERIA)
    public boolean checkStorage() {
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            return false;
        }
        return true;
    }

    //CHECA SE O ARMAZENAMENTO ESTÁ DISPONIVEL PARA ESCRITA
    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    //CHECA SE O ARMAZENAMENTO ESTÁ DISPONIVEL(MONTADO)
    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }


    //CRIA UM ARQUIVO DE IMAGEM
    private File createImageFile() throws IOException {
        //File sdcard = Environment.getExternalStorageDirectory().getAbsoluteFile();
        //File dir = new File(sdcard, "APP_NAME" + File.separator + "PASTA_1");
        //if (!dir.exists())
        //  dir.mkdirs();
        // return dir;
        File direct = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + DIRECTORYNAME);

        if (!direct.exists()) {
            if (direct.mkdir()) {
                Log.i("e", "Nao criou o diretorio");
            }
            ; //se não existir o diretorio e criado
        }

        // Create an image file name

        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + DIRECTORYNAME);

        pictureImagePath = storageDir.getAbsolutePath() + "/" + FILENAME;
        File image = new File(pictureImagePath);
        return image;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
                startActivity(new Intent(this, Principal.class));  //O efeito ao ser pressionado do botão (no caso abre a activity)
                finishAffinity();  //Método para matar a activity e não deixa-lá indexada na pilhagem
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}

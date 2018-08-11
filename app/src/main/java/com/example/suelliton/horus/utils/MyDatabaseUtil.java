package com.example.suelliton.horus.utils;

import android.util.Log;
import android.widget.Toast;

import com.example.suelliton.horus.LoginActivity;
import com.google.firebase.database.FirebaseDatabase;

public class MyDatabaseUtil {

    private static FirebaseDatabase mDatabase;

    public static FirebaseDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
            Log.i("persistencia", "FUNCIONOU A PERSISTENCIA PAI!!!");
            // ...
        }

        return mDatabase;

    }

}
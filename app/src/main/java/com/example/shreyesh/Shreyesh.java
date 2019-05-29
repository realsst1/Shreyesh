package com.example.shreyesh;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class Shreyesh extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}

package com.findenhub_project.app.application;

import android.app.Application;
import com.google.firebase.FirebaseApp;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Inicializa o Firebase com o google-services.json do projeto
        FirebaseApp.initializeApp(this);
    }
}

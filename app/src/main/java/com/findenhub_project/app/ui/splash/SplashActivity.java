package com.findenhub_project.app.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.findenhub_project.app.R;
import com.findenhub_project.app.data.remote.FirebaseAuthManager;
import com.findenhub_project.app.ui.main.MainActivity;
import com.findenhub_project.app.ui.onboarding.WelcomeActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY_MS = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Aguarda um breve momento para mostrar o logo e depois decide o destino
        new Handler(Looper.getMainLooper()).postDelayed(this::checkAuthState, SPLASH_DELAY_MS);
    }

    private void checkAuthState() {
        if (FirebaseAuthManager.getInstance().isLoggedIn()) {
            // Sessão Firebase ativa → vai direto para MainActivity
            startActivity(new Intent(this, MainActivity.class));
        } else {
            // Sem sessão → fluxo de boas-vindas
            startActivity(new Intent(this, WelcomeActivity.class));
        }
        finish(); // Remove a SplashActivity da pilha
    }
}
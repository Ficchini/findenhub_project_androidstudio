package com.findenhub_project.app.ui.onboarding;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.findenhub_project.app.R;
import com.findenhub_project.app.ui.auth.LoginActivity;
import com.findenhub_project.app.ui.auth.RegisterChoiceActivity;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        findViewById(R.id.btn_welcome_login).setOnClickListener(v ->
                startActivity(new Intent(this, LoginActivity.class))
        );

        findViewById(R.id.btn_welcome_register).setOnClickListener(v ->
                startActivity(new Intent(this, RegisterChoiceActivity.class))
        );
    }
}
// Pergunta ao usuário se é Cliente ou Fornecedor antes do cadastro.
package com.findenhub_project.app.ui.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.findenhub_project.app.R;
import com.findenhub_project.app.utils.Constants;

public class RegisterChoiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_choice);

        // Botão "Sou Cliente" → RegisterClientActivity
        findViewById(R.id.btn_choice_client).setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterClientActivity.class);
            startActivity(intent);
        });

        // Botão "Sou Fornecedor" → RegisterSupplierActivity
        findViewById(R.id.btn_choice_supplier).setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterSupplierActivity.class);
            startActivity(intent);
        });
    }
}
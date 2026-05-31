// Cadastro de novo usuário do tipo SUPPLIER.
package com.findenhub_project.app.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;

import com.findenhub_project.app.R;
import com.findenhub_project.app.data.model.User;
import com.findenhub_project.app.ui.main.MainActivity;
import com.findenhub_project.app.utils.Constants;
import com.findenhub_project.app.utils.SessionManager;
import com.findenhub_project.app.utils.Validators;

public class RegisterSupplierActivity extends AppCompatActivity {

    private TextInputEditText etName, etEmail, etCpf, etPassword, etCategory;
    private MaterialButton btnRegister, btnGoogle;
    private CircularProgressIndicator progressBar;

    private AuthViewModel viewModel;
    private GoogleSignInHelper googleSignInHelper;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_supplier);

        etName      = findViewById(R.id.et_supplier_name);
        etEmail     = findViewById(R.id.et_supplier_email);
        etCpf       = findViewById(R.id.et_supplier_cpf);
        etPassword  = findViewById(R.id.et_supplier_password);
        etCategory  = findViewById(R.id.et_supplier_category);
        btnRegister = findViewById(R.id.btn_supplier_register);
        btnGoogle   = findViewById(R.id.btn_supplier_google);
        progressBar = findViewById(R.id.progress_supplier_register);

        viewModel          = new ViewModelProvider(this).get(AuthViewModel.class);
        googleSignInHelper  = new GoogleSignInHelper(this);
        sessionManager     = new SessionManager(this);

        btnRegister.setOnClickListener(v -> attemptRegister());
        btnGoogle.setOnClickListener(v ->
                startActivityForResult(googleSignInHelper.getSignInIntent(), Constants.RC_GOOGLE_SIGN_IN)
        );

        viewModel.getLoginResult().observe(this, user -> {
            sessionManager.saveSession(user.getId(), user.getUserType(), user.getName());
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
        viewModel.getErrorMessage().observe(this, msg ->
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
        );
        viewModel.getIsLoading().observe(this, loading ->
                progressBar.setVisibility(loading ? View.VISIBLE : View.GONE)
        );
    }

    private void attemptRegister() {
        String name     = etName.getText() != null ? etName.getText().toString().trim() : "";
        String email    = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        String cpf      = etCpf.getText() != null ? etCpf.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString() : "";
        String category = etCategory.getText() != null ? etCategory.getText().toString().trim() : "";

        if (!Validators.isNotEmpty(name)) { etName.setError(getString(R.string.error_empty_fields)); return; }
        if (!Validators.isValidEmail(email)) { etEmail.setError(getString(R.string.error_invalid_email)); return; }
        if (!Validators.isValidCpf(cpf)) { etCpf.setError(getString(R.string.error_invalid_cpf)); return; }
        if (!Validators.isValidPassword(password)) { etPassword.setError(getString(R.string.error_weak_password)); return; }

        User user = new User("", name, email, cpf, Constants.USER_TYPE_SUPPLIER);
        user.setCategory(category);
        viewModel.register(email, password, user);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.RC_GOOGLE_SIGN_IN) {
            AuthCredential credential = googleSignInHelper.getCredentialFromIntent(data);
            if (credential != null) {
                viewModel.signInWithGoogle(credential, Constants.USER_TYPE_SUPPLIER);
            } else {
                Toast.makeText(this, R.string.error_generic, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
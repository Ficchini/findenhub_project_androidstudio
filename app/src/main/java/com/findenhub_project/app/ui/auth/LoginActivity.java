// Login com email/senha ou Google Sign-In.
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
import com.findenhub_project.app.data.remote.FirebaseCollections;
import com.findenhub_project.app.data.remote.FirestoreManager;
import com.findenhub_project.app.data.model.User;
import com.findenhub_project.app.ui.main.MainActivity;
import com.findenhub_project.app.utils.Constants;
import com.findenhub_project.app.utils.SessionManager;
import com.findenhub_project.app.utils.Validators;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etPassword;
    private MaterialButton btnLogin, btnGoogle;
    private CircularProgressIndicator progressBar;

    private AuthViewModel viewModel;
    private GoogleSignInHelper googleSignInHelper;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Views
        etEmail      = findViewById(R.id.et_login_email);
        etPassword   = findViewById(R.id.et_login_password);
        btnLogin     = findViewById(R.id.btn_login);
        btnGoogle    = findViewById(R.id.btn_google_login);
        progressBar  = findViewById(R.id.progress_login);

        viewModel         = new ViewModelProvider(this).get(AuthViewModel.class);
        googleSignInHelper = new GoogleSignInHelper(this);
        sessionManager    = new SessionManager(this);

        btnLogin.setOnClickListener(v -> attemptEmailLogin());
        btnGoogle.setOnClickListener(v -> launchGoogleSignIn());

        // "Não tem conta?" abre RegisterChoiceActivity
        findViewById(R.id.tv_login_register_cta).setOnClickListener(v ->
                startActivity(new Intent(this, RegisterChoiceActivity.class))
        );

        // Observa resultado do Google Sign-In
        viewModel.getLoginResult().observe(this, this::onLoginSuccess);
        viewModel.getErrorMessage().observe(this, this::showError);
        viewModel.getIsLoading().observe(this, loading ->
                progressBar.setVisibility(loading ? View.VISIBLE : View.GONE)
        );
    }

    private void attemptEmailLogin() {
        String email    = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString() : "";

        if (!Validators.isValidEmail(email)) {
            etEmail.setError(getString(R.string.error_invalid_email));
            return;
        }
        if (!Validators.isValidPassword(password)) {
            etPassword.setError(getString(R.string.error_weak_password));
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        viewModel.loginWithEmail(email, password, new com.findenhub_project.app.data.callback.FirestoreCallback<com.google.firebase.auth.FirebaseUser>() {
            @Override
            public void onSuccess(com.google.firebase.auth.FirebaseUser firebaseUser) {
                // Busca userType no Firestore para salvar na sessão
                fetchUserAndNavigate(firebaseUser.getUid());
            }
            @Override
            public void onFailure(Exception e) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    /** Busca o documento do usuário para obter o userType e navega para MainActivity */
    private void fetchUserAndNavigate(String uid) {
        FirestoreManager.getInstance()
                .document(FirebaseCollections.USERS, uid)
                .get()
                .addOnSuccessListener(snapshot -> {
                    progressBar.setVisibility(View.GONE);
                    if (snapshot.exists()) {
                        User user = snapshot.toObject(User.class);
                        if (user != null) onLoginSuccess(user);
                    } else {
                        showError("Dados do usuário não encontrados.");
                    }
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    showError(e.getMessage());
                });
    }

    private void launchGoogleSignIn() {
        startActivityForResult(googleSignInHelper.getSignInIntent(), Constants.RC_GOOGLE_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.RC_GOOGLE_SIGN_IN) {
            AuthCredential credential = googleSignInHelper.getCredentialFromIntent(data);
            if (credential != null) {
                // No login, o userType é CLIENT por padrão se o usuário for novo
                viewModel.signInWithGoogle(credential, Constants.USER_TYPE_CLIENT);
            } else {
                showError(getString(R.string.error_generic));
            }
        }
    }

    private void onLoginSuccess(User user) {
        sessionManager.saveSession(user.getId(), user.getUserType(), user.getName());
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
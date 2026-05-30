// Encapsula a configuração e o lançamento do fluxo Google Sign-In.

package com.findenhub_project.app.ui.auth;

import android.app.Activity;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

import com.findenhub_project.app.R;
import com.findenhub_project.app.utils.Constants;

public class GoogleSignInHelper {

    private final GoogleSignInClient googleSignInClient;

    public GoogleSignInHelper(Activity activity) {
        // Usa o Web Client ID do strings.xml (preenchido depois de adicionar o google-services.json)
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(activity, gso);
    }

    /** Retorna o Intent para iniciar o fluxo de login Google */
    public Intent getSignInIntent() {
        return googleSignInClient.getSignInIntent();
    }

    /**
     * Processa o resultado do onActivityResult após o fluxo Google Sign-In.
     * Retorna a AuthCredential do Firebase ou null em caso de erro.
     */
    public AuthCredential getCredentialFromIntent(Intent data) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            return GoogleAuthProvider.getCredential(account.getIdToken(), null);
        } catch (ApiException e) {
            return null;
        }
    }

    public void signOut() {
        googleSignInClient.signOut();
    }
}

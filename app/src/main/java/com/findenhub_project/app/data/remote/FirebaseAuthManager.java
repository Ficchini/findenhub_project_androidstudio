// Encapsula todas as operações do Firebase Authentication.

package com.findenhub_project.app.data.remote;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseAuthManager {

    private static FirebaseAuthManager instance;
    private final FirebaseAuth auth;

    private FirebaseAuthManager() {
        auth = FirebaseAuth.getInstance();
    }

    public static FirebaseAuthManager getInstance() {
        if (instance == null) instance = new FirebaseAuthManager();
        return instance;
    }

    /** Retorna o usuário autenticado atualmente ou null */
    public FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }

    public boolean isLoggedIn() {
        return getCurrentUser() != null;
    }

    /** Cadastro com email e senha */
    public Task<AuthResult> registerWithEmail(String email, String password) {
        return auth.createUserWithEmailAndPassword(email, password);
    }

    /** Login com email e senha */
    public Task<AuthResult> loginWithEmail(String email, String password) {
        return auth.signInWithEmailAndPassword(email, password);
    }

    /** Login/cadastro com credencial Google */
    public Task<AuthResult> signInWithCredential(AuthCredential credential) {
        return auth.signInWithCredential(credential);
    }

    public void logout() {
        auth.signOut();
    }

    public String getCurrentUserId() {
        FirebaseUser user = getCurrentUser();
        return user != null ? user.getUid() : null;
    }
}

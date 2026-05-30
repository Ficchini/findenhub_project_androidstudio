// Responsável pelo fluxo de autenticação + criação do documento no Firestore.

package com.findenhub_project.app.data.repository;


import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseUser;

import com.findenhub_project.app.data.callback.FirestoreCallback;
import com.findenhub_project.app.data.model.User;
import com.findenhub_project.app.data.remote.FirebaseAuthManager;
import com.findenhub_project.app.data.remote.FirebaseCollections;
import com.findenhub_project.app.data.remote.FirestoreManager;

public class AuthRepository {

    private final FirebaseAuthManager authManager = FirebaseAuthManager.getInstance();
    private final FirestoreManager    firestoreManager = FirestoreManager.getInstance();

    /** Cadastra com email/senha e salva o documento do usuário no Firestore */
    public void registerWithEmail(String email, String password, User user,
                                  FirestoreCallback<User> callback) {
        authManager.registerWithEmail(email, password)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser firebaseUser = authResult.getUser();
                    if (firebaseUser == null) {
                        callback.onFailure(new Exception("Usuário nulo após cadastro"));
                        return;
                    }
                    user.setId(firebaseUser.getUid());
                    saveUserToFirestore(user, callback);
                })
                .addOnFailureListener(callback::onFailure);
    }

    /** Autentica com email/senha — não cria documento (usuário já existe) */
    public void loginWithEmail(String email, String password,
                               FirestoreCallback<FirebaseUser> callback) {
        authManager.loginWithEmail(email, password)
                .addOnSuccessListener(authResult -> callback.onSuccess(authResult.getUser()))
                .addOnFailureListener(callback::onFailure);
    }

    /** Login ou cadastro via Google Sign-In + salva no Firestore se for novo */
    public void signInWithGoogle(AuthCredential credential, String userType,
                                 FirestoreCallback<User> callback) {
        authManager.signInWithCredential(credential)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser firebaseUser = authResult.getUser();
                    if (firebaseUser == null) {
                        callback.onFailure(new Exception("Usuário nulo após Google Sign-In"));
                        return;
                    }
                    // Verifica se o documento já existe
                    firestoreManager.document(FirebaseCollections.USERS, firebaseUser.getUid())
                            .get()
                            .addOnSuccessListener(snapshot -> {
                                if (snapshot.exists()) {
                                    // Usuário já cadastrado — retorna dados existentes
                                    User existing = snapshot.toObject(User.class);
                                    callback.onSuccess(existing);
                                } else {
                                    // Novo usuário Google — cria documento
                                    User newUser = new User(
                                            firebaseUser.getUid(),
                                            firebaseUser.getDisplayName() != null
                                                    ? firebaseUser.getDisplayName() : "",
                                            firebaseUser.getEmail() != null
                                                    ? firebaseUser.getEmail() : "",
                                            "",
                                            userType
                                    );
                                    if (firebaseUser.getPhotoUrl() != null) {
                                        newUser.setPhotoUrl(firebaseUser.getPhotoUrl().toString());
                                    }
                                    saveUserToFirestore(newUser, callback);
                                }
                            })
                            .addOnFailureListener(callback::onFailure);
                })
                .addOnFailureListener(callback::onFailure);
    }

    /** Persiste o objeto User no Firestore em users/{uid} */
    private void saveUserToFirestore(User user, FirestoreCallback<User> callback) {
        firestoreManager.set(FirebaseCollections.USERS, user.getId(), user)
                .addOnSuccessListener(unused -> callback.onSuccess(user))
                .addOnFailureListener(callback::onFailure);
    }
}

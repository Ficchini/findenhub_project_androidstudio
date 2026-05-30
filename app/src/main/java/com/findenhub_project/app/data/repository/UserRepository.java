// Leitura e atualização de dados do usuário no Firestore.

package com.findenhub_project.app.data.repository;

import java.util.HashMap;
import java.util.Map;

import com.findenhub_project.app.data.callback.FirestoreCallback;
import com.findenhub_project.app.data.model.User;
import com.findenhub_project.app.data.remote.FirebaseCollections;
import com.findenhub_project.app.data.remote.FirestoreManager;

public class UserRepository {

    private final FirestoreManager db = FirestoreManager.getInstance();

    /** Busca o documento do usuário por UID */
    public void getUserById(String uid, FirestoreCallback<User> callback) {
        db.document(FirebaseCollections.USERS, uid)
                .get()
                .addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        User user = snapshot.toObject(User.class);
                        callback.onSuccess(user);
                    } else {
                        callback.onFailure(new Exception("Usuário não encontrado"));
                    }
                })
                .addOnFailureListener(callback::onFailure);
    }

    /** Atualiza nome e telefone do usuário */
    public void updateUserProfile(String uid, String name, String phone,
                                  FirestoreCallback<Void> callback) {
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", name);
        fields.put("phone", phone);

        db.update(FirebaseCollections.USERS, uid, fields)
                .addOnSuccessListener(unused -> callback.onSuccess(null))
                .addOnFailureListener(callback::onFailure);
    }
}

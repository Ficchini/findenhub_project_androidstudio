// Gerencia favoritos

package com.findenhub_project.app.data.repository;

import com.google.firebase.firestore.DocumentReference;

import java.util.List;

import com.findenhub_project.app.data.callback.FirestoreCallback;
import com.findenhub_project.app.data.model.Favorite;
import com.findenhub_project.app.data.remote.FirebaseCollections;
import com.findenhub_project.app.data.remote.FirestoreManager;

public class FavoriteRepository {

    private final FirestoreManager db = FirestoreManager.getInstance();

    public void addFavorite(Favorite favorite, FirestoreCallback<Favorite> callback) {
        DocumentReference ref = db.newDocument(FirebaseCollections.FAVORITES);
        favorite.setId(ref.getId());
        ref.set(favorite)
                .addOnSuccessListener(unused -> callback.onSuccess(favorite))
                .addOnFailureListener(callback::onFailure);
    }

    public void getFavoritesByClient(String clientId, FirestoreCallback<List<Favorite>> callback) {
        db.collection(FirebaseCollections.FAVORITES)
                .whereEqualTo("clientId", clientId)
                .get()
                .addOnSuccessListener(snapshots -> callback.onSuccess(snapshots.toObjects(Favorite.class)))
                .addOnFailureListener(callback::onFailure);
    }

    public void removeFavorite(String favoriteId, FirestoreCallback<Void> callback) {
        db.delete(FirebaseCollections.FAVORITES, favoriteId)
                .addOnSuccessListener(unused -> callback.onSuccess(null))
                .addOnFailureListener(callback::onFailure);
    }
}

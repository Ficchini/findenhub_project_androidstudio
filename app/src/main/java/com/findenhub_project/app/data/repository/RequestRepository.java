// Gerencia solicitações de proposta entre cliente e fornecedor.

package com.findenhub_project.app.data.repository;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.findenhub_project.app.data.callback.FirestoreCallback;
import com.findenhub_project.app.data.model.Request;
import com.findenhub_project.app.data.remote.FirebaseCollections;
import com.findenhub_project.app.data.remote.FirestoreManager;

public class RequestRepository {

    private final FirestoreManager db = FirestoreManager.getInstance();

    public void createRequest(Request request, FirestoreCallback<Request> callback) {
        DocumentReference ref = db.newDocument(FirebaseCollections.REQUESTS);
        request.setId(ref.getId());
        ref.set(request)
                .addOnSuccessListener(unused -> callback.onSuccess(request))
                .addOnFailureListener(callback::onFailure);
    }

    /** Lista solicitações recebidas por um fornecedor */
    public void getRequestsBySupplier(String supplierId, FirestoreCallback<List<Request>> callback) {
        db.collection(FirebaseCollections.REQUESTS)
                .whereEqualTo("supplierId", supplierId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(snapshots -> callback.onSuccess(snapshots.toObjects(Request.class)))
                .addOnFailureListener(callback::onFailure);
    }

    /** Atualiza o status de uma solicitação (ACCEPTED / REJECTED) */
    public void updateRequestStatus(String requestId, String newStatus,
                                    FirestoreCallback<Void> callback) {
        Map<String, Object> fields = new HashMap<>();
        fields.put("status", newStatus);

        db.update(FirebaseCollections.REQUESTS, requestId, fields)
                .addOnSuccessListener(unused -> callback.onSuccess(null))
                .addOnFailureListener(callback::onFailure);
    }
}

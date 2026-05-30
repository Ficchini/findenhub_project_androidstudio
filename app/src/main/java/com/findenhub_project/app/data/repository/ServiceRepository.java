// CRUD de serviços do fornecedor.

package com.findenhub_project.app.data.repository;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.findenhub_project.app.data.callback.FirestoreCallback;
import com.findenhub_project.app.data.model.Service;
import com.findenhub_project.app.data.remote.FirebaseCollections;
import com.findenhub_project.app.data.remote.FirestoreManager;

public class ServiceRepository {

    private final FirestoreManager db = FirestoreManager.getInstance();

    public void createService(Service service, FirestoreCallback<Service> callback) {
        DocumentReference ref = db.newDocument(FirebaseCollections.SERVICES);
        service.setId(ref.getId());
        ref.set(service)
                .addOnSuccessListener(unused -> callback.onSuccess(service))
                .addOnFailureListener(callback::onFailure);
    }

    /** Lista serviços de um fornecedor específico */
    public void getServicesBySupplier(String supplierId, FirestoreCallback<List<Service>> callback) {
        db.collection(FirebaseCollections.SERVICES)
                .whereEqualTo("supplierId", supplierId)
                .whereEqualTo("active", true)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(snapshots -> callback.onSuccess(snapshots.toObjects(Service.class)))
                .addOnFailureListener(callback::onFailure);
    }

    /** Lista todos os serviços ativos (para clientes explorarem) */
    public void getAllActiveServices(FirestoreCallback<List<Service>> callback) {
        db.collection(FirebaseCollections.SERVICES)
                .whereEqualTo("active", true)
                .get()
                .addOnSuccessListener(snapshots -> callback.onSuccess(snapshots.toObjects(Service.class)))
                .addOnFailureListener(callback::onFailure);
    }

    public void updateService(Service service, FirestoreCallback<Void> callback) {
        Map<String, Object> fields = new HashMap<>();
        fields.put("title", service.getTitle());
        fields.put("category", service.getCategory());
        fields.put("description", service.getDescription());
        fields.put("priceBase", service.getPriceBase());
        fields.put("city", service.getCity());

        db.update(FirebaseCollections.SERVICES, service.getId(), fields)
                .addOnSuccessListener(unused -> callback.onSuccess(null))
                .addOnFailureListener(callback::onFailure);
    }

    public void deleteService(String serviceId, FirestoreCallback<Void> callback) {
        db.delete(FirebaseCollections.SERVICES, serviceId)
                .addOnSuccessListener(unused -> callback.onSuccess(null))
                .addOnFailureListener(callback::onFailure);
    }
}

// CRUD de eventos no Firestore.

package com.findenhub_project.app.data.repository;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;

import java.util.List;

import com.findenhub_project.app.data.callback.FirestoreCallback;
import com.findenhub_project.app.data.model.Event;
import com.findenhub_project.app.data.remote.FirebaseCollections;
import com.findenhub_project.app.data.remote.FirestoreManager;

public class EventRepository {

    private final FirestoreManager db = FirestoreManager.getInstance();

    /** Cria um novo evento — o ID é gerado pelo Firestore */
    public void createEvent(Event event, FirestoreCallback<Event> callback) {
        DocumentReference ref = db.newDocument(FirebaseCollections.EVENTS);
        event.setId(ref.getId());
        ref.set(event)
                .addOnSuccessListener(unused -> callback.onSuccess(event))
                .addOnFailureListener(callback::onFailure);
    }

    /** Lista todos os eventos de um cliente específico */
    public void getEventsByClient(String clientId, FirestoreCallback<List<Event>> callback) {
        db.collection(FirebaseCollections.EVENTS)
                .whereEqualTo("clientId", clientId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(snapshots -> {
                    List<Event> events = snapshots.toObjects(Event.class);
                    callback.onSuccess(events);
                })
                .addOnFailureListener(callback::onFailure);
    }

    /** Busca um evento por ID */
    public void getEventById(String eventId, FirestoreCallback<Event> callback) {
        db.document(FirebaseCollections.EVENTS, eventId)
                .get()
                .addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        callback.onSuccess(snapshot.toObject(Event.class));
                    } else {
                        callback.onFailure(new Exception("Evento não encontrado"));
                    }
                })
                .addOnFailureListener(callback::onFailure);
    }

    /** Exclui um evento */
    public void deleteEvent(String eventId, FirestoreCallback<Void> callback) {
        db.delete(FirebaseCollections.EVENTS, eventId)
                .addOnSuccessListener(unused -> callback.onSuccess(null))
                .addOnFailureListener(callback::onFailure);
    }
}

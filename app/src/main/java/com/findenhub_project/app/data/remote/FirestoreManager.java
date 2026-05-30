// Encapsula o acesso ao Cloud Firestore, expondo operações CRUD genéricas.

package com.findenhub_project.app.data.remote;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class FirestoreManager {

    private static FirestoreManager instance;
    private final FirebaseFirestore db;

    private FirestoreManager() {
        db = FirebaseFirestore.getInstance();
    }

    public static FirestoreManager getInstance() {
        if (instance == null) instance = new FirestoreManager();
        return instance;
    }

    public FirebaseFirestore getDb() { return db; }

    /** Referência para uma coleção */
    public CollectionReference collection(String name) {
        return db.collection(name);
    }

    /** Referência para um documento específico */
    public DocumentReference document(String collection, String docId) {
        return db.collection(collection).document(docId);
    }

    /** Salva ou sobrescreve um documento com ID definido */
    public Task<Void> set(String collection, String docId, Object data) {
        return db.collection(collection).document(docId).set(data);
    }

    /** Cria documento com ID gerado automaticamente e retorna a referência */
    public DocumentReference newDocument(String collection) {
        return db.collection(collection).document();
    }

    /** Atualiza campos de um documento existente */
    public Task<Void> update(String collection, String docId, java.util.Map<String, Object> fields) {
        return db.collection(collection).document(docId).update(fields);
    }

    /** Exclui um documento */
    public Task<Void> delete(String collection, String docId) {
        return db.collection(collection).document(docId).delete();
    }
}

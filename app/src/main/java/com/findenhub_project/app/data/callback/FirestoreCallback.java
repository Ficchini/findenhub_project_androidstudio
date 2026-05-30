// Interface genérica de callback para operações assíncronas no Firestore.
package com.findenhub_project.app.data.callback;

public interface FirestoreCallback<T> {
    void onSuccess(T result);
    void onFailure(Exception e);
}

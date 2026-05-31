// ViewModel compartilhado pelas telas de autenticação.
// Mantém LiveData com resultado de cada operação para as Activities observarem.

package com.findenhub_project.app.ui.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseUser;

import com.findenhub_project.app.data.model.User;
import com.findenhub_project.app.data.repository.AuthRepository;

public class AuthViewModel extends ViewModel {

    private final AuthRepository authRepository = new AuthRepository();

    // LiveData de resultado de login (User = dados do Firestore)
    private final MutableLiveData<User>      loginResult  = new MutableLiveData<>();
    private final MutableLiveData<String>    errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean>   isLoading    = new MutableLiveData<>(false);

    public LiveData<User>    getLoginResult()  { return loginResult; }
    public LiveData<String>  getErrorMessage() { return errorMessage; }
    public LiveData<Boolean> getIsLoading()    { return isLoading; }

    /** Cadastro email/senha como Cliente ou Fornecedor */
    public void register(String email, String password, User user) {
        isLoading.setValue(true);
        authRepository.registerWithEmail(email, password, user, new com.findenhub_project.app.data.callback.FirestoreCallback<User>() {
            @Override public void onSuccess(User result) {
                isLoading.postValue(false);
                loginResult.postValue(result);
            }
            @Override public void onFailure(Exception e) {
                isLoading.postValue(false);
                errorMessage.postValue(e.getMessage());
            }
        });
    }

    /** Login com email e senha — após sucesso, o caller busca userType no Firestore */
    public void loginWithEmail(String email, String password,
                               com.findenhub_project.app.data.callback.FirestoreCallback<FirebaseUser> callback) {
        isLoading.setValue(true);
        authRepository.loginWithEmail(email, password, new com.findenhub_project.app.data.callback.FirestoreCallback<FirebaseUser>() {
            @Override public void onSuccess(FirebaseUser result) {
                isLoading.postValue(false);
                callback.onSuccess(result);
            }
            @Override public void onFailure(Exception e) {
                isLoading.postValue(false);
                errorMessage.postValue(e.getMessage());
                callback.onFailure(e);
            }
        });
    }

    /** Login com Google */
    public void signInWithGoogle(AuthCredential credential, String userType) {
        isLoading.setValue(true);
        authRepository.signInWithGoogle(credential, userType, new com.findenhub_project.app.data.callback.FirestoreCallback<User>() {
            @Override public void onSuccess(User result) {
                isLoading.postValue(false);
                loginResult.postValue(result);
            }
            @Override public void onFailure(Exception e) {
                isLoading.postValue(false);
                errorMessage.postValue(e.getMessage());
            }
        });
    }
}

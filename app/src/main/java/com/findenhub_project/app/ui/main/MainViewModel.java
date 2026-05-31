// ViewModel da MainActivity: carrega dados do usuário logado.
package com.findenhub_project.app.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.findenhub_project.app.data.callback.FirestoreCallback;
import com.findenhub_project.app.data.model.User;
import com.findenhub_project.app.data.repository.UserRepository;

public class MainViewModel extends ViewModel {

    private final UserRepository userRepository = new UserRepository();
    private final MutableLiveData<User> currentUser = new MutableLiveData<>();

    public LiveData<User> getCurrentUser() { return currentUser; }

    public void loadUser(String uid) {
        userRepository.getUserById(uid, new FirestoreCallback<User>() {
            @Override public void onSuccess(User result) { currentUser.postValue(result); }
            @Override public void onFailure(Exception e) { /* silencioso na splash */ }
        });
    }
}

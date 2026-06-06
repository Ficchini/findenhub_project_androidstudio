package com.findenhub_project.app.ui.client.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import com.findenhub_project.app.R;
import com.findenhub_project.app.data.callback.FirestoreCallback;
import com.findenhub_project.app.data.model.User;
import com.findenhub_project.app.data.remote.FirebaseAuthManager;
import com.findenhub_project.app.data.repository.UserRepository;
import com.findenhub_project.app.ui.auth.LoginActivity;
import com.findenhub_project.app.utils.SessionManager;

public class ClientProfileFragment extends Fragment {

    private TextInputEditText etName, etPhone;
    private MaterialButton btnSave, btnLogout;
    private SessionManager sessionManager;
    private final UserRepository userRepository = new UserRepository();
    private String userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_client_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etName    = view.findViewById(R.id.et_client_profile_name);
        etPhone   = view.findViewById(R.id.et_client_profile_phone);
        btnSave   = view.findViewById(R.id.btn_client_profile_save);
        btnLogout = view.findViewById(R.id.btn_client_logout);

        sessionManager = new SessionManager(requireContext());
        userId = FirebaseAuthManager.getInstance().getCurrentUserId();

        loadUserData();

        btnSave.setOnClickListener(v -> saveProfile());
        btnLogout.setOnClickListener(v -> logout());
    }

    private void loadUserData() {
        if (userId == null) return;
        userRepository.getUserById(userId, new FirestoreCallback<User>() {
            @Override public void onSuccess(User user) {
                if (!isAdded()) return;
                etName.setText(user.getName());
                etPhone.setText(user.getPhone());
            }
            @Override public void onFailure(Exception e) { /* silencioso */ }
        });
    }

    private void saveProfile() {
        String name  = etName.getText() != null ? etName.getText().toString().trim() : "";
        String phone = etPhone.getText() != null ? etPhone.getText().toString().trim() : "";

        if (userId == null) return;

        userRepository.updateUserProfile(userId, name, phone, new FirestoreCallback<Void>() {
            @Override public void onSuccess(Void result) {
                if (!isAdded()) return;
                sessionManager.saveSession(userId,
                        com.findenhub_project.app.utils.Constants.USER_TYPE_CLIENT, name);
                Toast.makeText(requireContext(), "Perfil atualizado!", Toast.LENGTH_SHORT).show();
            }
            @Override public void onFailure(Exception e) {
                if (!isAdded()) return;
                Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logout() {
        FirebaseAuthManager.getInstance().logout();
        sessionManager.clearAll(requireActivity());
        Intent intent = new Intent(requireContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}

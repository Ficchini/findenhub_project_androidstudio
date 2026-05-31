package com.findenhub_project.app.ui.supplier.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.findenhub_project.app.R;
import com.findenhub_project.app.data.callback.FirestoreCallback;
import com.findenhub_project.app.data.model.Request;
import com.findenhub_project.app.data.remote.FirebaseAuthManager;
import com.findenhub_project.app.data.repository.RequestRepository;
import com.findenhub_project.app.ui.supplier.services.CreateServiceActivity;
import com.findenhub_project.app.utils.SessionManager;

import java.util.List;

public class SupplierDashboardFragment extends Fragment {

    private final RequestRepository requestRepository = new RequestRepository();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_supplier_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SessionManager session = new SessionManager(requireContext());
        TextView tvGreeting = view.findViewById(R.id.tv_supplier_greeting);
        String name = session.getUserName() != null ? session.getUserName() : "fornecedor";
        tvGreeting.setText(getString(R.string.greeting_prefix) + name + "!");

        view.findViewById(R.id.btn_supplier_dashboard_add_service).setOnClickListener(v ->
                startActivity(new Intent(requireContext(), CreateServiceActivity.class))
        );

        loadRequestCount(view);
    }

    private void loadRequestCount(View view) {
        String uid = FirebaseAuthManager.getInstance().getCurrentUserId();
        if (uid == null) return;

        TextView tvCount = view.findViewById(R.id.tv_supplier_requests_count);

        requestRepository.getRequestsBySupplier(uid, new FirestoreCallback<List<Request>>() {
            @Override public void onSuccess(List<Request> result) {
                if (!isAdded()) return;
                long pending = result.stream()
                        .filter(r -> "PENDING".equals(r.getStatus())).count();
                tvCount.setText(getString(R.string.requests_count_prefix) + pending);
            }
            @Override public void onFailure(Exception e) { /* silencioso */ }
        });
    }
}

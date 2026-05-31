package com.findenhub_project.app.ui.supplier.requests;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.findenhub_project.app.R;
import com.findenhub_project.app.data.callback.FirestoreCallback;
import com.findenhub_project.app.data.model.Request;
import com.findenhub_project.app.data.remote.FirebaseAuthManager;
import com.findenhub_project.app.data.repository.RequestRepository;
import com.findenhub_project.app.ui.common.adapters.RequestAdapter;
import com.findenhub_project.app.utils.Constants;

public class SupplierRequestsFragment extends Fragment {

    private RecyclerView recyclerView;
    private RequestAdapter adapter;
    private TextView tvEmpty;
    private final RequestRepository requestRepository = new RequestRepository();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_supplier_requests, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rv_supplier_requests);
        tvEmpty      = view.findViewById(R.id.tv_supplier_requests_empty);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new RequestAdapter(new ArrayList<>(), new RequestAdapter.OnRequestActionListener() {
            @Override
            public void onAccept(Request request) {
                updateStatus(request, Constants.STATUS_ACCEPTED);
            }
            @Override
            public void onReject(Request request) {
                updateStatus(request, Constants.STATUS_REJECTED);
            }
        });
        recyclerView.setAdapter(adapter);

        loadRequests();
    }

    private void loadRequests() {
        String uid = FirebaseAuthManager.getInstance().getCurrentUserId();
        if (uid == null) return;

        requestRepository.getRequestsBySupplier(uid, new FirestoreCallback<List<Request>>() {
            @Override public void onSuccess(List<Request> result) {
                if (!isAdded()) return;
                adapter.updateList(result);
                tvEmpty.setVisibility(result.isEmpty() ? View.VISIBLE : View.GONE);
            }
            @Override public void onFailure(Exception e) {
                if (!isAdded()) return;
                Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateStatus(Request request, String newStatus) {
        requestRepository.updateRequestStatus(request.getId(), newStatus, new FirestoreCallback<Void>() {
            @Override public void onSuccess(Void result) {
                if (!isAdded()) return;
                request.setStatus(newStatus);
                adapter.notifyDataSetChanged();
                String msg = Constants.STATUS_ACCEPTED.equals(newStatus)
                        ? "Solicitação aceita!" : "Solicitação recusada.";
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
            }
            @Override public void onFailure(Exception e) {
                if (!isAdded()) return;
                Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

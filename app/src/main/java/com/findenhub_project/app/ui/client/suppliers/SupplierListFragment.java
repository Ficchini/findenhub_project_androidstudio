// Lista fornecedores ativos disponíveis para o cliente explorar.
package com.findenhub_project.app.ui.client.suppliers;

import android.content.Intent;
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
import com.findenhub_project.app.data.model.Service;
import com.findenhub_project.app.data.repository.ServiceRepository;
import com.findenhub_project.app.ui.common.adapters.ServiceAdapter;
import com.findenhub_project.app.utils.Constants;

public class SupplierListFragment extends Fragment {

    private RecyclerView recyclerView;
    private ServiceAdapter adapter;
    private TextView tvEmpty;
    private final ServiceRepository serviceRepository = new ServiceRepository();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_supplier_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rv_supplier_list);
        tvEmpty      = view.findViewById(R.id.tv_supplier_list_empty);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new ServiceAdapter(new ArrayList<>(), service -> {
            Intent intent = new Intent(requireContext(), SupplierDetailsActivity.class);
            intent.putExtra(Constants.EXTRA_SERVICE_ID, service.getId());
            intent.putExtra(Constants.EXTRA_SUPPLIER_ID, service.getSupplierId());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        loadServices();
    }

    private void loadServices() {
        serviceRepository.getAllActiveServices(new FirestoreCallback<List<Service>>() {
            @Override public void onSuccess(List<Service> result) {
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
}
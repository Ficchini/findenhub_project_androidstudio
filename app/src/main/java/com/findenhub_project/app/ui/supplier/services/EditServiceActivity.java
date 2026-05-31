package com.findenhub_project.app.ui.supplier.services;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;

import com.findenhub_project.app.R;
import com.findenhub_project.app.data.callback.FirestoreCallback;
import com.findenhub_project.app.data.model.Service;
import com.findenhub_project.app.data.remote.FirebaseCollections;
import com.findenhub_project.app.data.remote.FirestoreManager;
import com.findenhub_project.app.data.repository.ServiceRepository;
import com.findenhub_project.app.utils.Constants;

public class EditServiceActivity extends AppCompatActivity {

    private TextInputEditText etTitle, etCategory, etDescription, etPrice, etCity;
    private MaterialButton btnUpdate, btnDelete;
    private CircularProgressIndicator progressBar;

    private String serviceId;
    private Service currentService;
    private final ServiceRepository serviceRepository = new ServiceRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_service);

        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        serviceId = getIntent().getStringExtra(Constants.EXTRA_SERVICE_ID);

        etTitle       = findViewById(R.id.et_edit_service_title);
        etCategory    = findViewById(R.id.et_edit_service_category);
        etDescription = findViewById(R.id.et_edit_service_description);
        etPrice       = findViewById(R.id.et_edit_service_price);
        etCity        = findViewById(R.id.et_edit_service_city);
        btnUpdate     = findViewById(R.id.btn_update_service);
        btnDelete     = findViewById(R.id.btn_delete_service);
        progressBar   = findViewById(R.id.progress_edit_service);

        btnUpdate.setOnClickListener(v -> updateService());
        btnDelete.setOnClickListener(v -> confirmDelete());

        loadService();
    }

    private void loadService() {
        if (serviceId == null) { finish(); return; }
        progressBar.setVisibility(View.VISIBLE);

        FirestoreManager.getInstance()
                .document(FirebaseCollections.SERVICES, serviceId)
                .get()
                .addOnSuccessListener(snapshot -> {
                    progressBar.setVisibility(View.GONE);
                    if (snapshot.exists()) {
                        currentService = snapshot.toObject(Service.class);
                        if (currentService != null) bindService(currentService);
                    }
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void bindService(Service service) {
        etTitle.setText(service.getTitle());
        etCategory.setText(service.getCategory());
        etDescription.setText(service.getDescription());
        etPrice.setText(service.getPriceBase());
        etCity.setText(service.getCity());
    }

    private void updateService() {
        if (currentService == null) return;

        currentService.setTitle(etTitle.getText() != null ? etTitle.getText().toString().trim() : "");
        currentService.setCategory(etCategory.getText() != null ? etCategory.getText().toString().trim() : "");
        currentService.setDescription(etDescription.getText() != null ? etDescription.getText().toString().trim() : "");
        currentService.setPriceBase(etPrice.getText() != null ? etPrice.getText().toString().trim() : "");
        currentService.setCity(etCity.getText() != null ? etCity.getText().toString().trim() : "");

        progressBar.setVisibility(View.VISIBLE);
        serviceRepository.updateService(currentService, new FirestoreCallback<Void>() {
            @Override public void onSuccess(Void result) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(EditServiceActivity.this, "Serviço atualizado!", Toast.LENGTH_SHORT).show();
                finish();
            }
            @Override public void onFailure(Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(EditServiceActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.confirm_delete)
                .setPositiveButton(R.string.btn_confirm, (d, w) -> deleteService())
                .setNegativeButton(R.string.btn_cancel, null)
                .show();
    }

    private void deleteService() {
        progressBar.setVisibility(View.VISIBLE);
        serviceRepository.deleteService(serviceId, new FirestoreCallback<Void>() {
            @Override public void onSuccess(Void result) {
                progressBar.setVisibility(View.GONE);
                finish();
            }
            @Override public void onFailure(Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(EditServiceActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() { finish(); return true; }
}
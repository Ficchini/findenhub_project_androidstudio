// ui/client/suppliers/SupplierDetailsActivity.java
// Exibe perfil do fornecedor + serviço selecionado.
// Permite ao cliente escolher um evento existente antes de enviar a proposta.
package com.findenhub_project.app.ui.client.suppliers;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import com.findenhub_project.app.R;
import com.findenhub_project.app.data.callback.FirestoreCallback;
import com.findenhub_project.app.data.model.Event;
import com.findenhub_project.app.data.model.Request;
import com.findenhub_project.app.data.model.Service;
import com.findenhub_project.app.data.model.User;
import com.findenhub_project.app.data.remote.FirebaseAuthManager;
import com.findenhub_project.app.data.remote.FirebaseCollections;
import com.findenhub_project.app.data.remote.FirestoreManager;
import com.findenhub_project.app.data.repository.EventRepository;
import com.findenhub_project.app.data.repository.RequestRepository;
import com.findenhub_project.app.data.repository.UserRepository;
import com.findenhub_project.app.utils.Constants;

public class SupplierDetailsActivity extends AppCompatActivity {

    private TextView tvSupplierName, tvSupplierCategory, tvSupplierCity,
            tvSupplierDescription, tvSelectedEvent;
    private TextInputEditText etProposalMessage;
    private MaterialButton btnSelectEvent, btnSendProposal;
    private CircularProgressIndicator progressBar;

    private String supplierId, serviceId;
    private String selectedEventId   = null;
    private String selectedEventTitle = null;

    private List<Event> clientEvents = new ArrayList<>();

    private final UserRepository    userRepository    = new UserRepository();
    private final EventRepository   eventRepository   = new EventRepository();
    private final RequestRepository requestRepository = new RequestRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_details);

        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        supplierId = getIntent().getStringExtra(Constants.EXTRA_SUPPLIER_ID);
        serviceId  = getIntent().getStringExtra(Constants.EXTRA_SERVICE_ID);

        tvSupplierName        = findViewById(R.id.tv_supplier_detail_name);
        tvSupplierCategory    = findViewById(R.id.tv_supplier_detail_category);
        tvSupplierCity        = findViewById(R.id.tv_supplier_detail_city);
        tvSupplierDescription = findViewById(R.id.tv_supplier_detail_description);
        tvSelectedEvent       = findViewById(R.id.tv_selected_event_label);
        etProposalMessage     = findViewById(R.id.et_proposal_message);
        btnSelectEvent        = findViewById(R.id.btn_select_event);
        btnSendProposal       = findViewById(R.id.btn_send_proposal);
        progressBar           = findViewById(R.id.progress_supplier_details);

        btnSelectEvent.setOnClickListener(v -> showEventPickerDialog());
        btnSendProposal.setOnClickListener(v -> sendProposal());

        loadSupplierDetails();
        loadClientEvents();
    }

    // ── Carrega nome e categoria do fornecedor ────────────────────────────────

    private void loadSupplierDetails() {
        progressBar.setVisibility(View.VISIBLE);

        userRepository.getUserById(supplierId, new FirestoreCallback<User>() {
            @Override public void onSuccess(User user) {
                tvSupplierName.setText(user.getName());
                tvSupplierCategory.setText(user.getCategory());
                progressBar.setVisibility(View.GONE);
            }
            @Override public void onFailure(Exception e) {
                progressBar.setVisibility(View.GONE);
            }
        });

        FirestoreManager.getInstance()
                .document(FirebaseCollections.SERVICES, serviceId)
                .get()
                .addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        Service service = snapshot.toObject(Service.class);
                        if (service != null) {
                            tvSupplierDescription.setText(service.getDescription());
                            tvSupplierCity.setText(service.getCity());
                        }
                    }
                });
    }

    // ── Pré-carrega os eventos do cliente para o picker ───────────────────────

    private void loadClientEvents() {
        String uid = FirebaseAuthManager.getInstance().getCurrentUserId();
        if (uid == null) return;

        eventRepository.getEventsByClient(uid, new FirestoreCallback<List<Event>>() {
            @Override public void onSuccess(List<Event> result) {
                clientEvents = result;
            }
            @Override public void onFailure(Exception e) { /* silencioso */ }
        });
    }

    // ── Dialog para escolher o evento ────────────────────────────────────────

    private void showEventPickerDialog() {
        if (clientEvents.isEmpty()) {
            Toast.makeText(this, "Você não tem eventos criados. Crie um evento primeiro.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        // Monta array de títulos para exibir no dialog
        String[] titles = new String[clientEvents.size()];
        for (int i = 0; i < clientEvents.size(); i++) {
            titles[i] = clientEvents.get(i).getTitle();
        }

        new AlertDialog.Builder(this)
                .setTitle("Selecione o evento")
                .setItems(titles, (dialog, which) -> {
                    Event chosen = clientEvents.get(which);
                    selectedEventId    = chosen.getId();
                    selectedEventTitle = chosen.getTitle();
                    tvSelectedEvent.setText("Evento: " + selectedEventTitle);
                    tvSelectedEvent.setVisibility(View.VISIBLE);
                })
                .setNegativeButton(R.string.btn_cancel, null)
                .show();
    }

    // ── Envia a proposta ──────────────────────────────────────────────────────

    private void sendProposal() {
        String clientId = FirebaseAuthManager.getInstance().getCurrentUserId();
        if (clientId == null) return;

        if (selectedEventId == null) {
            Toast.makeText(this, "Selecione um evento antes de enviar.", Toast.LENGTH_SHORT).show();
            return;
        }

        String message = etProposalMessage.getText() != null
                ? etProposalMessage.getText().toString().trim()
                : "";

        progressBar.setVisibility(View.VISIBLE);
        btnSendProposal.setEnabled(false);

        Request request = new Request(selectedEventId, clientId, supplierId, serviceId, message);

        requestRepository.createRequest(request, new FirestoreCallback<Request>() {
            @Override public void onSuccess(Request result) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(SupplierDetailsActivity.this,
                        R.string.success_proposal_sent, Toast.LENGTH_SHORT).show();
                finish();
            }
            @Override public void onFailure(Exception e) {
                progressBar.setVisibility(View.GONE);
                btnSendProposal.setEnabled(true);
                Toast.makeText(SupplierDetailsActivity.this,
                        e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() { finish(); return true; }
}
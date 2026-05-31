// Exibe o perfil do fornecedor e permite enviar uma solicitação.
package com.findenhub_project.app.ui.client.suppliers;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;

import com.findenhub_project.app.R;
import com.findenhub_project.app.data.callback.FirestoreCallback;
import com.findenhub_project.app.data.model.Request;
import com.findenhub_project.app.data.model.Service;
import com.findenhub_project.app.data.model.User;
import com.findenhub_project.app.data.remote.FirebaseAuthManager;
import com.findenhub_project.app.data.repository.RequestRepository;
import com.findenhub_project.app.data.repository.ServiceRepository;
import com.findenhub_project.app.data.repository.UserRepository;
import com.findenhub_project.app.utils.Constants;

public class SupplierDetailsActivity extends AppCompatActivity {

    private TextView tvSupplierName, tvSupplierCategory, tvSupplierCity, tvSupplierDescription;
    private TextInputEditText etProposalMessage;
    private MaterialButton btnSendProposal;
    private CircularProgressIndicator progressBar;

    private String supplierId, serviceId;
    private Service currentService;

    private final ServiceRepository  serviceRepository  = new ServiceRepository();
    private final UserRepository     userRepository     = new UserRepository();
    private final RequestRepository  requestRepository  = new RequestRepository();

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
        etProposalMessage     = findViewById(R.id.et_proposal_message);
        btnSendProposal       = findViewById(R.id.btn_send_proposal);
        progressBar           = findViewById(R.id.progress_supplier_details);

        btnSendProposal.setOnClickListener(v -> sendProposal());

        loadSupplierDetails();
    }

    private void loadSupplierDetails() {
        progressBar.setVisibility(View.VISIBLE);

        // Carrega o serviço para exibir dados
        // Em um MVP mais avançado, o nome do fornecedor viria do documento users/{supplierId}
        userRepository.getUserById(supplierId, new FirestoreCallback<User>() {
            @Override public void onSuccess(User user) {
                progressBar.setVisibility(View.GONE);
                tvSupplierName.setText(user.getName());
                tvSupplierCategory.setText(user.getCategory());
            }
            @Override public void onFailure(Exception e) {
                progressBar.setVisibility(View.GONE);
            }
        });

        // Carrega detalhes do serviço
        // Usando getAllActiveServices seria mais eficiente buscar por ID diretamente:
        // Para MVP, buscamos o serviço pelo ID via FirestoreManager diretamente
        com.findenhub_project.app.data.remote.FirestoreManager.getInstance()
                .document(com.findenhub_project.app.data.remote.FirebaseCollections.SERVICES, serviceId)
                .get()
                .addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        currentService = snapshot.toObject(Service.class);
                        if (currentService != null) {
                            tvSupplierDescription.setText(currentService.getDescription());
                            tvSupplierCity.setText(currentService.getCity());
                        }
                    }
                });
    }

    private void sendProposal() {
        String clientId = FirebaseAuthManager.getInstance().getCurrentUserId();
        if (clientId == null) return;

        String message = etProposalMessage.getText() != null
                ? etProposalMessage.getText().toString().trim()
                : "";

        progressBar.setVisibility(View.VISIBLE);
        btnSendProposal.setEnabled(false);

        // eventId vazio para simplificar — em fluxo completo, o cliente selecionaria o evento
        Request request = new Request("", clientId, supplierId, serviceId, message);

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
                Toast.makeText(SupplierDetailsActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() { finish(); return true; }
}
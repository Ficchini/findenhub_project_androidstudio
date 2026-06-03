// ui/client/events/EditEventActivity.java
// Edita um evento existente. Recebe o eventId via Intent, carrega os dados,
// permite edição e salva as alterações no Firestore.
package com.findenhub_project.app.ui.client.events;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

import com.findenhub_project.app.R;
import com.findenhub_project.app.data.callback.FirestoreCallback;
import com.findenhub_project.app.data.model.Event;
import com.findenhub_project.app.data.remote.FirebaseCollections;
import com.findenhub_project.app.data.remote.FirestoreManager;
import com.findenhub_project.app.data.repository.EventRepository;
import com.findenhub_project.app.utils.Constants;
import com.findenhub_project.app.utils.Validators;

public class EditEventActivity extends AppCompatActivity {

    private TextInputEditText etTitle, etCategory, etDate, etLocation, etBudget, etDescription;
    private MaterialButton btnSave;
    private CircularProgressIndicator progressBar;

    private String eventId;
    private final EventRepository eventRepository = new EventRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Editar Evento");
        }

        eventId = getIntent().getStringExtra(Constants.EXTRA_EVENT_ID);

        etTitle       = findViewById(R.id.et_edit_event_title);
        etCategory    = findViewById(R.id.et_edit_event_category);
        etDate        = findViewById(R.id.et_edit_event_date);
        etLocation    = findViewById(R.id.et_edit_event_location);
        etBudget      = findViewById(R.id.et_edit_event_budget);
        etDescription = findViewById(R.id.et_edit_event_description);
        btnSave       = findViewById(R.id.btn_save_edit_event);
        progressBar   = findViewById(R.id.progress_edit_event);

        btnSave.setOnClickListener(v -> saveChanges());

        loadEvent();
    }

    private void loadEvent() {
        if (eventId == null) { finish(); return; }
        progressBar.setVisibility(View.VISIBLE);

        eventRepository.getEventById(eventId, new FirestoreCallback<Event>() {
            @Override public void onSuccess(Event event) {
                progressBar.setVisibility(View.GONE);
                etTitle.setText(event.getTitle());
                etCategory.setText(event.getCategory());
                etDate.setText(event.getDate());
                etLocation.setText(event.getLocation());
                etBudget.setText(event.getBudget());
                etDescription.setText(event.getDescription());
            }
            @Override public void onFailure(Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(EditEventActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void saveChanges() {
        String title       = etTitle.getText() != null ? etTitle.getText().toString().trim() : "";
        String category    = etCategory.getText() != null ? etCategory.getText().toString().trim() : "";
        String date        = etDate.getText() != null ? etDate.getText().toString().trim() : "";
        String location    = etLocation.getText() != null ? etLocation.getText().toString().trim() : "";
        String budget      = etBudget.getText() != null ? etBudget.getText().toString().trim() : "";
        String description = etDescription.getText() != null ? etDescription.getText().toString().trim() : "";

        if (!Validators.isNotEmpty(title)) { etTitle.setError(getString(R.string.error_empty_fields)); return; }
        if (!Validators.isNotEmpty(date))  { etDate.setError(getString(R.string.error_empty_fields)); return; }

        progressBar.setVisibility(View.VISIBLE);
        btnSave.setEnabled(false);

        Map<String, Object> fields = new HashMap<>();
        fields.put("title",       title);
        fields.put("category",    category);
        fields.put("date",        date);
        fields.put("location",    location);
        fields.put("budget",      budget);
        fields.put("description", description);

        FirestoreManager.getInstance()
                .update(FirebaseCollections.EVENTS, eventId, fields)
                .addOnSuccessListener(unused -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Evento atualizado!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    btnSave.setEnabled(true);
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    @Override
    public boolean onSupportNavigateUp() { finish(); return true; }
}
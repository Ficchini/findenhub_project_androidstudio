// Formulário para criar um novo evento.
package com.findenhub_project.app.ui.client.events;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;

import com.findenhub_project.app.R;
import com.findenhub_project.app.data.callback.FirestoreCallback;
import com.findenhub_project.app.data.model.Event;
import com.findenhub_project.app.data.remote.FirebaseAuthManager;
import com.findenhub_project.app.data.repository.EventRepository;
import com.findenhub_project.app.utils.Validators;

public class CreateEventActivity extends AppCompatActivity {

    private TextInputEditText etTitle, etCategory, etDate, etLocation, etBudget, etDescription;
    private MaterialButton btnSave;
    private CircularProgressIndicator progressBar;

    private final EventRepository eventRepository = new EventRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        // Habilita botão de voltar na ActionBar
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etTitle       = findViewById(R.id.et_event_title);
        etCategory    = findViewById(R.id.et_event_category);
        etDate        = findViewById(R.id.et_event_date);
        etLocation    = findViewById(R.id.et_event_location);
        etBudget      = findViewById(R.id.et_event_budget);
        etDescription = findViewById(R.id.et_event_description);
        btnSave       = findViewById(R.id.btn_save_event);
        progressBar   = findViewById(R.id.progress_create_event);

        btnSave.setOnClickListener(v -> attemptCreateEvent());
    }

    private void attemptCreateEvent() {
        String title       = etTitle.getText() != null ? etTitle.getText().toString().trim() : "";
        String category    = etCategory.getText() != null ? etCategory.getText().toString().trim() : "";
        String date        = etDate.getText() != null ? etDate.getText().toString().trim() : "";
        String location    = etLocation.getText() != null ? etLocation.getText().toString().trim() : "";
        String budget      = etBudget.getText() != null ? etBudget.getText().toString().trim() : "";
        String description = etDescription.getText() != null ? etDescription.getText().toString().trim() : "";

        if (!Validators.isNotEmpty(title)) { etTitle.setError(getString(R.string.error_empty_fields)); return; }
        if (!Validators.isNotEmpty(date))  { etDate.setError(getString(R.string.error_empty_fields)); return; }

        String clientId = FirebaseAuthManager.getInstance().getCurrentUserId();
        if (clientId == null) return;

        progressBar.setVisibility(View.VISIBLE);
        btnSave.setEnabled(false);

        Event event = new Event(clientId, title, category, date, location, budget, description);

        eventRepository.createEvent(event, new FirestoreCallback<Event>() {
            @Override public void onSuccess(Event result) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(CreateEventActivity.this,
                        R.string.success_event_created, Toast.LENGTH_SHORT).show();
                finish();
            }
            @Override public void onFailure(Exception e) {
                progressBar.setVisibility(View.GONE);
                btnSave.setEnabled(true);
                Toast.makeText(CreateEventActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
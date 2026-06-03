// Detalhe de um evento com opções de editar, excluir e ver fornecedores.
package com.findenhub_project.app.ui.client.events;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import com.findenhub_project.app.R;
import com.findenhub_project.app.data.callback.FirestoreCallback;
import com.findenhub_project.app.data.model.Event;
import com.findenhub_project.app.data.repository.EventRepository;
import com.findenhub_project.app.utils.Constants;

public class EventDetailsActivity extends AppCompatActivity {

    private TextView tvTitle, tvCategory, tvDate, tvLocation, tvBudget, tvDescription;
    private MaterialButton btnEdit, btnDelete, btnSeeSuppliers;
    private CircularProgressIndicator progressBar;

    private String eventId;
    private Event currentEvent;
    private final EventRepository eventRepository = new EventRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        eventId = getIntent().getStringExtra(Constants.EXTRA_EVENT_ID);

        tvTitle       = findViewById(R.id.tv_event_detail_title);
        tvCategory    = findViewById(R.id.tv_event_detail_category);
        tvDate        = findViewById(R.id.tv_event_detail_date);
        tvLocation    = findViewById(R.id.tv_event_detail_location);
        tvBudget      = findViewById(R.id.tv_event_detail_budget);
        tvDescription = findViewById(R.id.tv_event_detail_description);
        btnEdit       = findViewById(R.id.btn_event_edit);
        btnDelete     = findViewById(R.id.btn_event_delete);
        btnSeeSuppliers = findViewById(R.id.btn_event_see_suppliers);
        progressBar   = findViewById(R.id.progress_event_details);

        btnDelete.setOnClickListener(v -> confirmDelete());
        // Editar: reabre CreateEventActivity pré-preenchida (simplificado para MVP)
        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditEventActivity.class);
            intent.putExtra(Constants.EXTRA_EVENT_ID, eventId);
            startActivity(intent);
        });
        btnSeeSuppliers.setOnClickListener(v ->
                Toast.makeText(this, "Veja a aba Fornecedores!", Toast.LENGTH_SHORT).show()
        );

        loadEventDetails();
    }

    private void loadEventDetails() {
        if (eventId == null) { finish(); return; }
        progressBar.setVisibility(View.VISIBLE);

        eventRepository.getEventById(eventId, new FirestoreCallback<Event>() {
            @Override public void onSuccess(Event result) {
                progressBar.setVisibility(View.GONE);
                currentEvent = result;
                bindEvent(result);
            }
            @Override public void onFailure(Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(EventDetailsActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void bindEvent(Event event) {
        tvTitle.setText(event.getTitle());
        tvCategory.setText(event.getCategory());
        tvDate.setText(event.getDate());
        tvLocation.setText(event.getLocation());
        tvBudget.setText(event.getBudget());
        tvDescription.setText(event.getDescription());
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.confirm_delete)
                .setPositiveButton(R.string.btn_confirm, (d, w) -> deleteEvent())
                .setNegativeButton(R.string.btn_cancel, null)
                .show();
    }

    private void deleteEvent() {
        progressBar.setVisibility(View.VISIBLE);
        eventRepository.deleteEvent(eventId, new FirestoreCallback<Void>() {
            @Override public void onSuccess(Void result) {
                progressBar.setVisibility(View.GONE);
                finish();
            }
            @Override public void onFailure(Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(EventDetailsActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() { finish(); return true; }
}
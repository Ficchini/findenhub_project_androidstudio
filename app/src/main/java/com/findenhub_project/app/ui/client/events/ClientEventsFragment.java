// Lista os eventos do cliente com FAB para criar novo.
package com.findenhub_project.app.ui.client.events;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import com.findenhub_project.app.R;
import com.findenhub_project.app.data.callback.FirestoreCallback;
import com.findenhub_project.app.data.model.Event;
import com.findenhub_project.app.data.remote.FirebaseAuthManager;
import com.findenhub_project.app.data.repository.EventRepository;
import com.findenhub_project.app.ui.common.adapters.EventAdapter;
import com.findenhub_project.app.utils.Constants;

import java.util.List;

public class ClientEventsFragment extends Fragment {

    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private TextView tvEmpty;
    private final EventRepository eventRepository = new EventRepository();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_client_events, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rv_client_events);
        tvEmpty      = view.findViewById(R.id.tv_client_events_empty);
        FloatingActionButton fab = view.findViewById(R.id.fab_create_event);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new EventAdapter(new ArrayList<>(), event -> {
            // Abre EventDetailsActivity passando o ID do evento
            Intent intent = new Intent(requireContext(), EventDetailsActivity.class);
            intent.putExtra(Constants.EXTRA_EVENT_ID, event.getId());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), CreateEventActivity.class))
        );

        loadEvents();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadEvents(); // Recarrega ao voltar de CreateEventActivity
    }

    private void loadEvents() {
        String uid = FirebaseAuthManager.getInstance().getCurrentUserId();
        if (uid == null) return;

        eventRepository.getEventsByClient(uid, new FirestoreCallback<List<Event>>() {
            @Override
            public void onSuccess(List<Event> result) {
                if (!isAdded()) return;
                adapter.updateList(result);
                tvEmpty.setVisibility(result.isEmpty() ? View.VISIBLE : View.GONE);
            }
            @Override
            public void onFailure(Exception e) {
                if (!isAdded()) return;
                Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

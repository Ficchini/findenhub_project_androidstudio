package com.findenhub_project.app.ui.client.dashboard;

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
import com.findenhub_project.app.ui.client.events.CreateEventActivity;
import com.findenhub_project.app.utils.SessionManager;

public class ClientDashboardFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_client_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Saudação com nome do usuário
        SessionManager session = new SessionManager(requireContext());
        TextView tvGreeting = view.findViewById(R.id.tv_client_greeting);
        String name = session.getUserName() != null ? session.getUserName() : "usuário";
        tvGreeting.setText(getString(R.string.greeting_prefix) + name + "!");

        // Botão "Criar evento"
        view.findViewById(R.id.btn_dashboard_create_event).setOnClickListener(v ->
                startActivity(new Intent(requireContext(), CreateEventActivity.class))
        );

        // Botão "Explorar fornecedores" — muda para a aba de fornecedores via BottomNav
        view.findViewById(R.id.btn_dashboard_explore_suppliers).setOnClickListener(v -> {
            // Navega via BottomNavigationView na MainActivity
            requireActivity()
                    .findViewById(R.id.bottom_nav)
                    .performClick(); // Será substituído pela seleção do item correto abaixo
            com.google.android.material.bottomnavigation.BottomNavigationView nav =
                    requireActivity().findViewById(R.id.bottom_nav);
            nav.setSelectedItemId(R.id.nav_client_suppliers);
        });
    }
}

// Roteador principal: carrega BottomNav + Fragments de acordo com userType.
package com.findenhub_project.app.ui.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.findenhub_project.app.R;
import com.findenhub_project.app.data.remote.FirebaseAuthManager;
import com.findenhub_project.app.ui.auth.LoginActivity;
import com.findenhub_project.app.ui.client.dashboard.ClientDashboardFragment;
import com.findenhub_project.app.ui.client.events.ClientEventsFragment;
import com.findenhub_project.app.ui.client.profile.ClientProfileFragment;
import com.findenhub_project.app.ui.client.suppliers.SupplierListFragment;
import com.findenhub_project.app.ui.supplier.dashboard.SupplierDashboardFragment;
import com.findenhub_project.app.ui.supplier.profile.SupplierProfileFragment;
import com.findenhub_project.app.ui.supplier.requests.SupplierRequestsFragment;
import com.findenhub_project.app.ui.supplier.services.SupplierServicesFragment;
import com.findenhub_project.app.utils.Constants;
import com.findenhub_project.app.utils.SessionManager;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);

        // Valida sessão
        if (!FirebaseAuthManager.getInstance().isLoggedIn() || !sessionManager.hasSession()) {
            redirectToLogin();
            return;
        }

        bottomNav = findViewById(R.id.bottom_nav);
        String userType = sessionManager.getUserType();

        if (Constants.USER_TYPE_CLIENT.equals(userType)) {
            setupClientNavigation();
        } else {
            setupSupplierNavigation();
        }
    }

    // ── Configuração de navegação para CLIENT ──────────────────────────────────

    private void setupClientNavigation() {
        bottomNav.inflateMenu(R.menu.menu_client_bottom_nav);
        loadFragment(new ClientDashboardFragment()); // Fragment inicial

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_client_home)      return loadFragment(new ClientDashboardFragment());
            if (id == R.id.nav_client_events)    return loadFragment(new ClientEventsFragment());
            if (id == R.id.nav_client_suppliers) return loadFragment(new SupplierListFragment());
            if (id == R.id.nav_client_profile)   return loadFragment(new ClientProfileFragment());
            return false;
        });
    }

    // ── Configuração de navegação para SUPPLIER ────────────────────────────────

    private void setupSupplierNavigation() {
        bottomNav.inflateMenu(R.menu.menu_supplier_bottom_nav);
        loadFragment(new SupplierDashboardFragment()); // Fragment inicial

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_supplier_home)     return loadFragment(new SupplierDashboardFragment());
            if (id == R.id.nav_supplier_services) return loadFragment(new SupplierServicesFragment());
            if (id == R.id.nav_supplier_requests) return loadFragment(new SupplierRequestsFragment());
            if (id == R.id.nav_supplier_profile)  return loadFragment(new SupplierProfileFragment());
            return false;
        });
    }

    /** Substitui o fragment no container */
    private boolean loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
        return true;
    }

    private void redirectToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
package com.findenhub_project.app.utils;
// Centraliza todas as constantes do projeto para evitar strings espalhadas.
public class Constants {
    // Tipos de usuário salvos no Firestore
    public static final String USER_TYPE_CLIENT   = "CLIENT";
    public static final String USER_TYPE_SUPPLIER = "SUPPLIER";

    // Status de solicitação (Request)
    public static final String STATUS_PENDING  = "PENDING";
    public static final String STATUS_ACCEPTED = "ACCEPTED";
    public static final String STATUS_REJECTED = "REJECTED";

    // Chaves para Intent extras
    public static final String EXTRA_EVENT_ID    = "extra_event_id";
    public static final String EXTRA_SUPPLIER_ID = "extra_supplier_id";
    public static final String EXTRA_SERVICE_ID  = "extra_service_id";
    public static final String EXTRA_REQUEST_ID  = "extra_request_id";
    public static final String EXTRA_USER_TYPE   = "extra_user_type";

    // SharedPreferences
    public static final String PREF_NAME      = "event_market_prefs";
    public static final String PREF_USER_TYPE = "pref_user_type";
    public static final String PREF_USER_ID   = "pref_user_id";
    public static final String PREF_USER_NAME = "pref_user_name";

    // Request codes
    public static final int RC_GOOGLE_SIGN_IN = 9001;
}

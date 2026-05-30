// Gerencia a sessão do usuário via SharedPreferences.
// Complementa o Firebase Auth (que já persiste sessão internamente),
// guardando userType localmente para evitar consulta ao Firestore a cada abertura

package com.findenhub_project.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private final SharedPreferences prefs;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveSession(String userId, String userType, String userName) {
        prefs.edit()
                .putString(Constants.PREF_USER_ID, userId)
                .putString(Constants.PREF_USER_TYPE, userType)
                .putString(Constants.PREF_USER_NAME, userName)
                .apply();
    }

    public String getUserType() {
        return prefs.getString(Constants.PREF_USER_TYPE, null);
    }

    public String getUserId() {
        return prefs.getString(Constants.PREF_USER_ID, null);
    }

    public String getUserName() {
        return prefs.getString(Constants.PREF_USER_NAME, null);
    }

    public boolean hasSession() {
        return getUserId() != null && getUserType() != null;
    }

    public void clearSession() {
        prefs.edit().clear().apply();
    }
}

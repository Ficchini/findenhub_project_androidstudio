// Validações reutilizáveis de formulário.

package com.findenhub_project.app.utils;

import android.util.Patterns;

public class Validators {

    public static boolean isValidEmail(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches();
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    // Valida CPF — verifica formato e dígitos verificadores
    public static boolean isValidCpf(String cpf) {
        if (cpf == null) return false;
        // Remove pontuação
        cpf = cpf.replaceAll("[^0-9]", "");
        if (cpf.length() != 11) return false;
        // Rejeita sequências repetidas (ex: 111.111.111-11)
        if (cpf.matches("(\\d)\\1{10}")) return false;

        int sum = 0;
        for (int i = 0; i < 9; i++) sum += (cpf.charAt(i) - '0') * (10 - i);
        int first = 11 - (sum % 11);
        if (first >= 10) first = 0;
        if (first != (cpf.charAt(9) - '0')) return false;

        sum = 0;
        for (int i = 0; i < 10; i++) sum += (cpf.charAt(i) - '0') * (11 - i);
        int second = 11 - (sum % 11);
        if (second >= 10) second = 0;
        return second == (cpf.charAt(10) - '0');
    }

    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }
}

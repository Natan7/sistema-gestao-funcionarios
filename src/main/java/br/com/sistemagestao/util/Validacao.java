package br.com.sistemagestao.util;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public final class Validacao {

    private static final Pattern EMAIL = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private Validacao() {
    }

    public static boolean emailValido(String email) {
        return email != null && EMAIL.matcher(email.trim()).matches();
    }

    public static void validarFuncionario(String nome, java.time.LocalDate admissao, BigDecimal salario) {
        if (nome == null || nome.trim().length() < 2) {
            throw new IllegalArgumentException("Informe um nome com pelo menos 2 caracteres.");
        }
        if (admissao == null) {
            throw new IllegalArgumentException("Informe a data de admissão no formato DD/MM/AAAA.");
        }
        if (salario == null || salario.signum() <= 0) {
            throw new IllegalArgumentException("O salário deve ser maior que zero.");
        }
    }
}
package br.com.sistemagestao.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.regex.Pattern;

public final class Validacao {

    private static final Pattern EMAIL = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final LocalDate DATA_ADMISSAO_MINIMA = LocalDate.of(1991, 1, 1);

    private Validacao() {
    }

    public static boolean emailValido(String email) {
        return email != null && !contemEspacos(email) && EMAIL.matcher(email).matches();
    }

    public static boolean contemEspacos(String texto) {
        if (texto == null) {
            return false;
        }
        return texto.codePoints().anyMatch(caractere -> Character.isWhitespace(caractere) || Character.isSpaceChar(caractere));
    }

    public static boolean contemEspacos(char[] texto) {
        if (texto == null) {
            return false;
        }
        for (char caractere : texto) {
            if (Character.isWhitespace(caractere) || Character.isSpaceChar(caractere)) {
                return true;
            }
        }
        return false;
    }

    public static void validarFuncionario(String nome, LocalDate admissao, BigDecimal salario) {
        validarFuncionario(nome, admissao, salario, LocalDate.now());
    }

    static void validarFuncionario(String nome, LocalDate admissao, BigDecimal salario, LocalDate hoje) {
        if (nome == null || nome.trim().length() < 2) {
            throw new IllegalArgumentException("Informe um nome com pelo menos 2 caracteres.");
        }
        if (admissao == null) {
            throw new IllegalArgumentException("Informe a data de admissão no formato DD/MM/AAAA.");
        }
        if (admissao.isBefore(DATA_ADMISSAO_MINIMA) || admissao.isAfter(hoje)) {
            throw new IllegalArgumentException("A data de admissão deve ser posterior a 1990 e não pode ser futura.");
        }
        if (salario == null || salario.signum() <= 0) {
            throw new IllegalArgumentException("O salário deve ser maior que zero.");
        }
    }
}
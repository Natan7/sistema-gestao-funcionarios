package br.com.sistemagestao.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public final class Criptografia {

    private Criptografia() {
    }

    public static String sha256(char[] senha) {
        return sha256(new String(senha));
    }

    public static String sha256(String texto) {
        try {
            byte[] hash = MessageDigest.getInstance("SHA-256").digest(texto.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("Algoritmo SHA-256 indisponível.", exception);
        }
    }

    public static boolean comparar(String hashInformado, String hashArmazenado) {
        return MessageDigest.isEqual(hashInformado.getBytes(StandardCharsets.US_ASCII), hashArmazenado.getBytes(StandardCharsets.US_ASCII));
    }
}
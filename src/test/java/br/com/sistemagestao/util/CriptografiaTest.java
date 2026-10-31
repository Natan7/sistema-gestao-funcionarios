package br.com.sistemagestao.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class CriptografiaTest {

    @Test
    void geraHashSha256Hexadecimal() {
        assertEquals("5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8", Criptografia.sha256("password"));
    }

    @Test
    void comparaHashesSemComparacaoStringDireta() {
        String hash = Criptografia.sha256("senha-segura");
        assertTrue(Criptografia.comparar(hash, hash));
    }
}
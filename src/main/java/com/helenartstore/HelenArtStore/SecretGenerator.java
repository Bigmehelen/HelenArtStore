package com.helenartstore.HelenArtStore;

import java.util.Base64;
import java.security.SecureRandom;

public class SecretGenerator {
    public static void main(String[] args) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[64]; // 512 bits
        secureRandom.nextBytes(key);
        String base64Key = Base64.getEncoder().encodeToString(key);
        System.out.println("Your JWT Secret Key:");
        System.out.println(base64Key);
    }
}

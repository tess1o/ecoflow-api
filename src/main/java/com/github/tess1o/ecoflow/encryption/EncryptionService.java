package com.github.tess1o.encryption;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionService {

    public static final String HMAC_SHA_256 = "HmacSHA256";

    public String encryptHmacSHA256(String message, String secret) {
        try {
            Mac sha256HMAC = Mac.getInstance(HMAC_SHA_256);
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), HMAC_SHA_256);
            sha256HMAC.init(secret_key);
            return toHexString(sha256HMAC.doFinal(message.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String toHexString(byte[] bytes) {
        if (bytes == null) {
            return null;
        }

        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = String.format("%02x", b);
            hexString.append(hex);
        }
        return hexString.toString();
    }
}

package com.github.tess1o.ecoflow.encryption;

import com.github.tess1o.ecoflow.exceptions.EcoflowException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EncryptionServiceTest {

    /**
     * Tests for the encryptHmacSHA256 method in the EncryptionService class.
     * This method generates a HMAC SHA-256 based hash of the given message
     * using the provided secret key and returns the hash in hexadecimal format.
     */

    //from https://developer-eu.ecoflow.com/us/document/generalInfo
    @Test
    void shouldReturnCorrectHmacSHA256HashForGivenMessageAndSecret() {
        // Arrange
        EncryptionService encryptionService = new EncryptionService();
        String message = "params.cmdSet=11&params.eps=0&params.id=24&sn=123456789&accessKey=Fp4SvIprYSDPXtYJidEtUAd1o&nonce=345164&timestamp=1671171709428";
        String secret = "WIbFEKre0s6sLnh4ei7SPUeYnptHG6V";

        // Act
        String result = encryptionService.encryptHmacSHA256(message, secret);

        // Assert
        assertEquals("07c13b65e037faf3b153d51613638fa80003c4c38d2407379a7f52851af1473e", result);
    }

    @Test
    void shouldReturnDifferentHashForDifferentMessagesWithSameSecret() {
        // Arrange
        EncryptionService encryptionService = new EncryptionService();
        String message1 = "Hello, World!";
        String message2 = "Goodbye, World!";
        String secret = "test-secret";

        // Act
        String result1 = encryptionService.encryptHmacSHA256(message1, secret);
        String result2 = encryptionService.encryptHmacSHA256(message2, secret);

        // Assert
        assertNotEquals(result1, result2);
    }

    @Test
    void shouldReturnDifferentHashForSameMessageWithDifferentSecrets() {
        // Arrange
        EncryptionService encryptionService = new EncryptionService();
        String message = "Hello, World!";
        String secret1 = "test-secret1";
        String secret2 = "test-secret2";

        // Act
        String result1 = encryptionService.encryptHmacSHA256(message, secret1);
        String result2 = encryptionService.encryptHmacSHA256(message, secret2);

        // Assert
        assertNotEquals(result1, result2);
    }

    @Test
    void shouldThrowExceptionWhenSecretIsNull() {
        // Arrange
        EncryptionService encryptionService = new EncryptionService();
        String message = "Hello, World!";
        String secret = null;

        // Act & Assert
        assertThrows(EcoflowException.class, () -> encryptionService.encryptHmacSHA256(message, secret));
    }

    @Test
    void shouldThrowExceptionWhenMessageIsNull() {
        // Arrange
        EncryptionService encryptionService = new EncryptionService();
        String message = null;
        String secret = "test-secret";

        // Act & Assert
        assertThrows(EcoflowException.class, () -> encryptionService.encryptHmacSHA256(message, secret));
    }

    @Test
    void shouldThrowExceptionWhenMessageAndSecretAreNull() {
        // Arrange
        EncryptionService encryptionService = new EncryptionService();
        String message = null;
        String secret = null;

        // Act & Assert
        assertThrows(EcoflowException.class, () -> encryptionService.encryptHmacSHA256(message, secret));
    }
}
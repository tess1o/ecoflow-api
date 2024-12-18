package com.github.tess1o.ecoflow.encryption;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EncryptionServiceTest {

    /**
     * Tests for the encryptHmacSHA256 method in the EncryptionService class.
     * This method generates a HMAC SHA-256 based hash of the given message
     * using the provided secret key and returns the hash in hexadecimal format.
     */

    @Test
    void shouldReturnCorrectHmacSHA256HashForGivenMessageAndSecret() {
        // Arrange
        EncryptionService encryptionService = new EncryptionService();
        String message = "Hello, World!";
        String secret = "test-secret";

        // Act
        String result = encryptionService.encryptHmacSHA256(message, secret);

        // Assert
        assertEquals("dffd6021bb2bd5b0af6766c53ebe8b81295f8c70177b93c1f6e5f7df5c3c2e29", result);
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
        assertThrows(RuntimeException.class, () -> encryptionService.encryptHmacSHA256(message, secret));
    }

    @Test
    void shouldThrowExceptionWhenMessageIsNull() {
        // Arrange
        EncryptionService encryptionService = new EncryptionService();
        String message = null;
        String secret = "test-secret";

        // Act & Assert
        assertThrows(RuntimeException.class, () -> encryptionService.encryptHmacSHA256(message, secret));
    }

    @Test
    void shouldThrowExceptionWhenMessageAndSecretAreNull() {
        // Arrange
        EncryptionService encryptionService = new EncryptionService();
        String message = null;
        String secret = null;

        // Act & Assert
        assertThrows(RuntimeException.class, () -> encryptionService.encryptHmacSHA256(message, secret));
    }
}
package com.github.tess1o.ecoflow.client.http;

import static org.junit.jupiter.api.Assertions.*;

import com.github.tess1o.ecoflow.exceptions.EcoflowInvalidParameterException;
import org.junit.jupiter.api.Test;
import com.github.tess1o.ecoflow.encryption.EncryptionService;

class RequestParametersTest {

    @Test
    void testSignWithValidParameters() {
        String queryString = "key1=value1&key2=value2";
        String accessKey = "accessKey123";
        String nonce = "12345";
        String timestamp = "1672531200000";
        String secretKey = "secretKey";

        RequestParameters params = new RequestParameters(queryString, accessKey, nonce, timestamp);
        String expectedSignature = new EncryptionService().encryptHmacSHA256(
                "key1=value1&key2=value2&accessKey=accessKey123&nonce=12345&timestamp=1672531200000", secretKey);

        assertEquals(expectedSignature, params.sign(secretKey));
    }

    @Test
    void testSignWithEmptyQueryString() {
        String queryString = "";
        String accessKey = "accessKey123";
        String secretKey = "secretKey";

        RequestParameters params = new RequestParameters(queryString, accessKey);
        String keyValueString = String.format(
                "accessKey=%s&nonce=%s&timestamp=%s", params.getAccessKey(), params.getNonce(), params.getTimestamp());
        String expectedSignature = new EncryptionService().encryptHmacSHA256(keyValueString, secretKey);

        assertEquals(expectedSignature, params.sign(secretKey));
    }

    @Test
    void testSignWithNullSecretKey() {
        String queryString = "key1=value1";
        String accessKey = "accessKey123";

        RequestParameters params = new RequestParameters(queryString, accessKey);

        assertThrows(EcoflowInvalidParameterException.class, () -> params.sign(null));
    }
}
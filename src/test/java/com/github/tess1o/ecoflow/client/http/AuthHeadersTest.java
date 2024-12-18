package com.github.tess1o.ecoflow.client.http;

import static org.junit.jupiter.api.Assertions.*;

import com.github.tess1o.ecoflow.exceptions.EcoflowInvalidParameterException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import com.github.tess1o.ecoflow.encryption.EncryptionService;

class AuthHeadersTest {

    @Test
    void testSignWithValidParameters() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("key1", "value1");
        jsonObject.put("key2", "value2");
        QueryString queryString = new QueryString(jsonObject);
        String accessKey = "accessKey123";
        String nonce = "12345";
        String timestamp = "1672531200000";
        String secretKey = "secretKey";

        AuthHeaders params = new AuthHeaders(queryString, accessKey, secretKey, nonce, timestamp);
        String expectedSignature = new EncryptionService().encryptHmacSHA256(
                "key1=value1&key2=value2&accessKey=accessKey123&nonce=12345&timestamp=1672531200000", secretKey);

        assertEquals(expectedSignature, params.getSign());
    }

    @Test
    void testSignWithEmptyQueryString() {
        QueryString queryString = new QueryString(new JSONObject());
        String accessKey = "accessKey123";
        String secretKey = "secretKey";

        AuthHeaders params = new AuthHeaders(queryString, accessKey, secretKey);
        String keyValueString = String.format(
                "accessKey=%s&nonce=%s&timestamp=%s", params.getAccessKey(), params.getNonce(), params.getTimestamp());
        String expectedSignature = new EncryptionService().encryptHmacSHA256(keyValueString, secretKey);

        assertEquals(expectedSignature, params.getSign());
    }

    @Test
    void testSignWithNullSecretKey() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("key1", "value1");
        jsonObject.put("key2", "value2");
        QueryString queryString = new QueryString(jsonObject);
        String accessKey = "accessKey123";

        assertThrows(EcoflowInvalidParameterException.class, () -> new AuthHeaders(queryString, accessKey, null));
    }
}
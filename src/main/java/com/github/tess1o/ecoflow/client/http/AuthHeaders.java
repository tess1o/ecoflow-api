package com.github.tess1o.ecoflow.client.http;

import com.github.tess1o.ecoflow.encryption.EncryptionService;
import com.github.tess1o.ecoflow.exceptions.EcoflowInvalidParameterException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AuthHeaders {
    private final QueryString queryString;
    private final String accessKey;
    private final String nonce;
    private final String timestamp;
    private final String sign;

    public AuthHeaders(QueryString queryString, String accessKey, String secretKey, String nonce, String timestamp) {
        this.queryString = queryString;
        this.accessKey = accessKey;
        this.nonce = nonce;
        this.timestamp = timestamp;
        this.sign = sign(secretKey);
    }

    public AuthHeaders(QueryString queryString, String accessKey, String secretKey) {
        this.queryString = queryString;
        this.accessKey = accessKey;
        this.nonce = generateNonce();
        this.timestamp = generateTimestamp();
        this.sign = sign(secretKey);
    }

    public AuthHeaders(String accessKey, String secretKey) {
        this.queryString = null;
        this.accessKey = accessKey;
        this.nonce = generateNonce();
        this.timestamp = generateTimestamp();
        this.sign = sign(secretKey);
    }

    public QueryString getQueryString() {
        return queryString;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public String getNonce() {
        return nonce;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getSign() {
        return sign;
    }

    private String sign(String secretKey) {
        if (secretKey == null || secretKey.isEmpty()) {
            throw new EcoflowInvalidParameterException("secretKey cannot be null or empty");
        }
        String keyValueString = getKeyValueString();
        return new EncryptionService().encryptHmacSHA256(keyValueString, secretKey);
    }

    private String generateNonce() {
        int low = 10000;
        int high = 1000000;
        int random = low + (int) (Math.random() * (high - low));
        return String.valueOf(random);
    }

    private String generateTimestamp() {
        return String.valueOf(System.currentTimeMillis());
    }

    private String getKeyValueString() {
        if (queryString == null || queryString.isEmpty()) {
            return String.format("accessKey=%s&nonce=%s&timestamp=%s", accessKey, nonce, timestamp);
        }
        return String.format("%s&accessKey=%s&nonce=%s&timestamp=%s", queryString.toQueryString(), accessKey, nonce, timestamp);
    }

    public String[] toHeadersArray() {
        Map<String, String> headers = new HashMap<>();
        headers.put("accessKey", accessKey);
        headers.put("nonce", nonce);
        headers.put("timestamp", timestamp);
        headers.put("sign", sign);

        return headers.entrySet().stream()
                .flatMap(entry -> java.util.stream.Stream.of(entry.getKey(), entry.getValue()))
                .toArray(String[]::new);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        AuthHeaders that = (AuthHeaders) o;
        return Objects.equals(queryString, that.queryString) &&
                Objects.equals(accessKey, that.accessKey) &&
                Objects.equals(nonce, that.nonce) &&
                Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(queryString);
        result = 31 * result + Objects.hashCode(accessKey);
        result = 31 * result + Objects.hashCode(nonce);
        result = 31 * result + Objects.hashCode(timestamp);
        return result;
    }

    @Override
    public String toString() {
        return "RequestParameters{" +
                "queryString='" + queryString + '\'' +
                ", accessKey='" + accessKey + '\'' +
                ", nonce='" + nonce + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}


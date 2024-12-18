package com.github.tess1o.ecoflow.client.http;

import com.github.tess1o.ecoflow.encryption.EncryptionService;
import com.github.tess1o.ecoflow.exceptions.EcoflowInvalidParameterException;

import java.util.Objects;

public class RequestParameters {
    private final String queryString;
    private final String accessKey;
    private final String nonce;
    private final String timestamp;

    public RequestParameters(String queryString, String accessKey, String nonce, String timestamp) {
        this.queryString = queryString;
        this.accessKey = accessKey;
        this.nonce = nonce;
        this.timestamp = timestamp;
    }

    public RequestParameters(String accessKey) {
        this.queryString = "";
        this.accessKey = accessKey;
        this.nonce = generateNonce();
        this.timestamp = generateTimestamp();
    }

    public RequestParameters(String queryString, String accessKey) {
        this.accessKey = accessKey;
        this.queryString = queryString;
        this.nonce = generateNonce();
        this.timestamp = generateTimestamp();
    }

    public String getQueryString() {
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

    public String sign(String secretKey) {
        if (secretKey == null || secretKey.isEmpty()) {
            throw new EcoflowInvalidParameterException("secretKey cannot be null or empty");
        }
        String keyValueString = getKeyValueString();
        return new EncryptionService().encryptHmacSHA256(keyValueString, secretKey);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        RequestParameters that = (RequestParameters) o;
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
        return String.format("%s&accessKey=%s&nonce=%s&timestamp=%s", queryString, accessKey, nonce, timestamp);
    }
}


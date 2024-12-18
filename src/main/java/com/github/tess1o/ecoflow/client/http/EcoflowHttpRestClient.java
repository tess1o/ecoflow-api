package com.github.tess1o.ecoflow.client.http;

import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class EcoflowHttpClient implements com.github.tess1o.ecoflow.client.http.HttpClient {

    private final String baseUrl;
    private final String accessToken;
    private final String secretToken;
    private final HttpClient httpClient;

    private static final String DEFAULT_BASE_URL = "https://api.ecoflow.com";

    public EcoflowHttpClient(String baseUrl, String accessToken, String secretToken, HttpClient httpClient) {
        this.baseUrl = baseUrl;
        this.accessToken = accessToken;
        this.secretToken = secretToken;
        this.httpClient = httpClient;
    }

    public EcoflowHttpClient(String secretToken, String accessToken, String baseUrl, HttpClient.Builder httpClientBuilder) {
        this.secretToken = secretToken;
        this.accessToken = accessToken;
        this.baseUrl = baseUrl;
        this.httpClient = httpClientBuilder.build();
    }

    public EcoflowHttpClient(String secretToken, String accessToken, String baseUrl) {
        this.secretToken = secretToken;
        this.accessToken = accessToken;
        this.baseUrl = baseUrl;
        this.httpClient = HttpClient.newHttpClient();
    }

    public EcoflowHttpClient(String accessToken, String secretToken) {
        this(DEFAULT_BASE_URL, accessToken, secretToken, HttpClient.newHttpClient());
    }

    public HttpResponse<String> executeGet(String url) throws Exception {
        RequestParameters reqParams = new RequestParameters(accessToken);
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + url))
                .GET();
        HttpRequest request = setDefaultHeaders(requestBuilder, reqParams).build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> executeGet(String url, JSONObject queryParams) throws Exception {
        String queryString = new QueryString(queryParams).get();
        RequestParameters reqParams = new RequestParameters(queryString, accessToken);

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + url + "?" + queryString))
                .GET();

        HttpRequest request = setDefaultHeaders(requestBuilder, reqParams).build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> executePost(String url, JSONObject queryParams) throws Exception {
        String queryString = new QueryString(queryParams).get();
        RequestParameters reqParams = new RequestParameters(queryString, accessToken);
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + url + "?" + queryString))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(queryParams.toString()));
        HttpRequest request = setDefaultHeaders(requestBuilder, reqParams).build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpRequest.Builder setDefaultHeaders(HttpRequest.Builder builder, RequestParameters reqParams) {
        String sign = reqParams.sign(secretToken);
        return builder
                .header("accessKey", accessToken)
                .header("nonce", reqParams.getNonce())
                .header("timestamp", reqParams.getTimestamp())
                .header("sign", sign);
    }
}

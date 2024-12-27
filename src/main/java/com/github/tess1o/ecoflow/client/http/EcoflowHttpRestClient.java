package com.github.tess1o.ecoflow.client.http;

import com.github.tess1o.ecoflow.exceptions.EcoflowHttpException;
import com.github.tess1o.ecoflow.exceptions.EcoflowInvalidParameterException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class EcoflowHttpRestClient implements HttpRestClient {

    private static final String DEFAULT_BASE_URL = "https://api.ecoflow.com";

    private final String baseUrl;
    private final String accessToken;
    private final String secretToken;
    private final HttpClient httpClient;

    public EcoflowHttpRestClient(String baseUrl, String accessToken, String secretToken, HttpClient httpClient) {
        this.baseUrl = baseUrl;
        this.accessToken = accessToken;
        this.secretToken = secretToken;
        this.httpClient = httpClient;
    }

    public EcoflowHttpRestClient(String baseUrl, String accessToken, String secretToken, HttpClient.Builder httpClientBuilder) {
        this.baseUrl = baseUrl;
        this.accessToken = accessToken;
        this.secretToken = secretToken;
        this.httpClient = httpClientBuilder.build();
    }

    public EcoflowHttpRestClient(String baseUrl, String accessToken, String secretToken) {
        this.baseUrl = baseUrl;
        this.accessToken = accessToken;
        this.secretToken = secretToken;
        this.httpClient = HttpClient.newHttpClient();
    }

    public EcoflowHttpRestClient(String accessToken, String secretToken) {
        this(DEFAULT_BASE_URL, accessToken, secretToken, HttpClient.newHttpClient());
    }

    public HttpResponse<String> get(String url, QueryString query) throws EcoflowHttpException {
        return prepareAndSendRequest(url, query, "GET");
    }

    public HttpResponse<String> post(String url, QueryString query) throws EcoflowHttpException {
        return prepareAndSendRequest(url, query, "POST");
    }

    @Override
    public HttpResponse<String> put(String url, QueryString query) {
        return prepareAndSendRequest(url, query, "PUT");
    }

    @Override
    public HttpResponse<String> delete(String url, QueryString query) {
        return prepareAndSendRequest(url, query, "DELETE");
    }

    private HttpRequest prepareRequest(String url, QueryString queryParams, String method) {
        if ((method.equals("POST") || method.equals("PUT")) && queryParams == null) {
            throw new EcoflowInvalidParameterException("Body request for POST and PUT requests are mandatory");
        }
        String fullUrl = (queryParams == null || queryParams.isEmpty()) ? baseUrl + url : baseUrl + url + "?" + queryParams.toQueryString();
        AuthHeaders authHeaders = queryParams == null ? new AuthHeaders(accessToken, secretToken) : new AuthHeaders(queryParams, accessToken, secretToken);
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(fullUrl));

        requestBuilder = switch (method) {
            case "GET" -> requestBuilder.GET();
            case "POST" -> requestBuilder.header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(queryParams.toString()));
            case "PUT" -> requestBuilder.header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(queryParams.toString()));
            case "DELETE" -> requestBuilder.DELETE();
            default -> requestBuilder;
        };

        return requestBuilder.headers(authHeaders.toHeadersArray()).build();
    }

    private HttpResponse<String> prepareAndSendRequest(String url, QueryString query, String method) {
        HttpRequest request = prepareRequest(url, query, method);
        return sendRequest(request);
    }

    private HttpResponse<String> sendRequest(HttpRequest request) {
        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new EcoflowHttpException(e);
        }
    }
}

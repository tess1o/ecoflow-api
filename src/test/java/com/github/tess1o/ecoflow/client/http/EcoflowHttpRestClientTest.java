package com.github.tess1o.ecoflow.client.http;

import com.github.tess1o.ecoflow.exceptions.EcoflowHttpException;
import com.github.tess1o.ecoflow.exceptions.EcoflowInvalidParameterException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EcoflowHttpRestClientTest {

    /**
     * Tests for the `get` method in the `EcoflowHttpRestClient` class.
     * <p>
     * The `get` method is responsible for making HTTP GET requests to the Ecoflow API.
     * It builds the request using the provided URL and query parameters, performs authentication,
     * and utilizes the `HttpClient` to send the request. It returns the HTTP response as a string.
     */

    @Test
    void testGetSuccessfulResponse() throws Exception {
        String url = "/test-endpoint";
        JSONObject queryParams = new JSONObject().put("key", "value");
        String expectedResponseBody = "{ \"success\": true }";

        HttpClient mockHttpClient = Mockito.mock(HttpClient.class);
        HttpResponse<String> mockResponse = Mockito.mock(HttpResponse.class);

        when(mockResponse.body()).thenReturn(expectedResponseBody);
        when(mockResponse.statusCode()).thenReturn(200);
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(mockResponse);

        EcoflowHttpRestClient client = new EcoflowHttpRestClient("testToken", "testSecret", "https://api.test.com", mockHttpClient);
        HttpResponse<String> response = client.get(url, queryParams);

        assertEquals(200, response.statusCode());
        assertEquals(expectedResponseBody, response.body());
    }

    @Test
    void testGetWithInvalidQueryParams() {
        String url = "/test-endpoint";

        HttpClient mockHttpClient = Mockito.mock(HttpClient.class);
        EcoflowHttpRestClient client = new EcoflowHttpRestClient("testToken", "testSecret", "https://api.test.com", mockHttpClient);

        assertThrows(EcoflowHttpException.class, () -> client.get(url, null));
    }

    @Test
    void testGetThrowsEcoflowHttpExceptionOnIOException() throws Exception {
        String url = "/test-endpoint";
        JSONObject queryParams = new JSONObject().put("key", "value");

        HttpClient mockHttpClient = Mockito.mock(HttpClient.class);

        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenThrow(new java.io.IOException("Simulated IO error"));

        EcoflowHttpRestClient client = new EcoflowHttpRestClient("testToken", "testSecret", "https://api.test.com", mockHttpClient);

        assertThrows(EcoflowHttpException.class, () -> client.get(url, queryParams));
    }

    @Test
    void testGetThrowsEcoflowHttpExceptionOnInterruptedException() throws Exception {
        String url = "/test-endpoint";
        JSONObject queryParams = new JSONObject().put("key", "value");

        HttpClient mockHttpClient = Mockito.mock(HttpClient.class);

        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenThrow(new InterruptedException("Simulated interruption"));

        EcoflowHttpRestClient client = new EcoflowHttpRestClient("testToken", "testSecret", "https://api.test.com", mockHttpClient);

        assertThrows(EcoflowHttpException.class, () -> client.get(url, queryParams));
    }

    @Test
    void testGetGeneratesCorrectHttpRequest() throws Exception {
        String url = "/test-endpoint";
        JSONObject queryParams = new JSONObject().put("key", "value");

        HttpClient mockHttpClient = Mockito.mock(HttpClient.class);
        HttpResponse<String> mockResponse = Mockito.mock(HttpResponse.class);

        when(mockResponse.body()).thenReturn("{ \"status\": \"ok\" }");
        when(mockResponse.statusCode()).thenReturn(200);
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(mockResponse);

        EcoflowHttpRestClient client = new EcoflowHttpRestClient("testToken", "testSecret", "https://api.test.com", mockHttpClient);

        client.get(url, queryParams);

        verify(mockHttpClient, times(1)).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
        HttpRequest capturedRequest = Mockito.mock(HttpRequest.class);
        verify(mockHttpClient).send(captureHttpRequest(capturedRequest), any(HttpResponse.BodyHandler.class));

        assertEquals("https://api.test.com/test-endpoint?key=value", capturedRequest.uri().toString());
        assertEquals("GET", capturedRequest.method());
    }

    private static HttpRequest.BodyHandler<String> captureHttpRequest(HttpRequest httpRequest) {
        return HttpResponse.BodyHandlers.ofString();
    }
}
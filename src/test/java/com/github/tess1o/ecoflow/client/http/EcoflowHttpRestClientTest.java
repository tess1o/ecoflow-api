package com.github.tess1o.ecoflow.client.http;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.http.HttpResponse;

class EcoflowHttpRestClientTest {

    private WireMockServer wireMockServer;
    private final static String SERVER_URL = "http://localhost:9541";

    @BeforeEach
    void setUp() {
        wireMockServer = new WireMockServer(9541);
        wireMockServer.start();
        WireMock.configureFor("localhost", 9541);
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void testGetRequestWithoutQueryString() {
        final String accessToken = "accessToken1";
        final String secretToken = "secretToken1";
        final String apiUrl = "/iot-open/sign/device/list";

        stubFor(get(apiUrl)
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                )
        );

        HttpRestClient client = new EcoflowHttpRestClient(SERVER_URL, accessToken, secretToken);
        HttpResponse<String> status = client.get(apiUrl);

        // Verify the request
        verify(getRequestedFor(urlEqualTo(apiUrl))
                .withHeader("accessKey", matching(accessToken))
                .withHeader("timestamp", matching(".*"))
                .withHeader("nonce", matching(".*"))
                .withHeader("sign", matching(".*")));

        // Assert response
        assertEquals(200, status.statusCode());
        assertEquals("application/json", status.headers().firstValue("Content-Type").orElse(null));
    }

    @Test
    void testGetRequest404Error() {
        final String accessToken = "accessToken1";
        final String secretToken = "secretToken1";
        final String apiUrl = "/not-existing-api";

        stubFor(get(apiUrl).willReturn(aResponse().withStatus(404)));

        HttpRestClient client = new EcoflowHttpRestClient(SERVER_URL, accessToken, secretToken);
        HttpResponse<String> status = client.get(apiUrl);

        // Verify the request
        verify(getRequestedFor(urlEqualTo(apiUrl))
                .withHeader("accessKey", matching(accessToken))
                .withHeader("timestamp", matching(".*"))
                .withHeader("nonce", matching(".*"))
                .withHeader("sign", matching(".*")));

        // Assert response
        assertEquals(404, status.statusCode());
    }

    @Test
    void testGetRequestWithQueryString() {
        final String accessToken = "accessToken1";
        final String secretToken = "secretToken1";
        final JSONObject queryParams = new JSONObject();
        queryParams.put("sn", "device1");

        final String apiUrl = "/iot-open/sign/device/quota";

        stubFor(get(urlPathEqualTo(apiUrl))
                .withQueryParam("sn", equalTo("device1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                )
        );

        HttpRestClient client = new EcoflowHttpRestClient(SERVER_URL, accessToken, secretToken);
        HttpResponse<String> status = client.get(apiUrl, queryParams);

        // Verify the request
        verify(getRequestedFor(urlPathEqualTo(apiUrl))
                .withQueryParam("sn", equalTo("device1"))
                .withHeader("accessKey", matching(accessToken))
                .withHeader("timestamp", matching(".*"))
                .withHeader("nonce", matching(".*"))
                .withHeader("sign", matching(".*")));

        // Assert response
        assertEquals(200, status.statusCode());
        assertEquals("application/json", status.headers().firstValue("Content-Type").orElse(null));
    }

    @Test
    void testPostRequestWithQueryString() {
        final String accessToken = "accessToken1";
        final String secretToken = "secretToken1";
        final JSONObject queryParams = new JSONObject();
        queryParams.put("sn", "device1");

        final String apiUrl = "/iot-open/sign/device/quota";

        stubFor(post(urlPathEqualTo(apiUrl))
                .withQueryParam("sn", equalTo("device1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                )
        );

        HttpRestClient client = new EcoflowHttpRestClient(SERVER_URL, accessToken, secretToken);
        HttpResponse<String> status = client.post(apiUrl, queryParams);

        // Verify the request
        verify(postRequestedFor(urlPathEqualTo(apiUrl))
                .withQueryParam("sn", equalTo("device1"))
                .withRequestBody(equalToJson(queryParams.toString()))
                .withHeader("accessKey", matching(accessToken))
                .withHeader("timestamp", matching(".*"))
                .withHeader("nonce", matching(".*"))
                .withHeader("sign", matching(".*")));

        // Assert response
        assertEquals(200, status.statusCode());
        assertEquals("application/json", status.headers().firstValue("Content-Type").orElse(null));
    }


}
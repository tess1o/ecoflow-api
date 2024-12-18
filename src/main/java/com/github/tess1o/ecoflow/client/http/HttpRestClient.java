package com.github.tess1o.ecoflow.client.http;

import org.json.JSONObject;

import java.net.http.HttpResponse;

public interface HttpRestClient {

    HttpResponse<String> get(String url, JSONObject queryParams);

    HttpResponse<String> post(String url, JSONObject queryParams);

    default HttpResponse<String> get(String url) {
        return get(url, null);
    }
}

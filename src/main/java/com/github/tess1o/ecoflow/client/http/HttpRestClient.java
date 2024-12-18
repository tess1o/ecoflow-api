package com.github.tess1o.ecoflow.client.http;

import org.json.JSONObject;

import java.net.http.HttpResponse;

public interface HttpClient {

    HttpResponse<String> executeGet(String url) throws Exception;

    HttpResponse<String> executeGet(String url, JSONObject queryParams) throws Exception;

    HttpResponse<String> executePost(String url, JSONObject queryParams) throws Exception;
}

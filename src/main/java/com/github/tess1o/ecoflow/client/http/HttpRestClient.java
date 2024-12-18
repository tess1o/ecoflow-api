package com.github.tess1o.ecoflow.client.http;

import java.net.http.HttpResponse;

public interface HttpRestClient {

    HttpResponse<String> get(String url, QueryString queryParams);

    HttpResponse<String> post(String url, QueryString queryParams);

    HttpResponse<String> put(String url, QueryString queryParams);

    HttpResponse<String> delete(String url, QueryString queryParams);

    default HttpResponse<String> get(String url) {
        return get(url, null);
    }
}

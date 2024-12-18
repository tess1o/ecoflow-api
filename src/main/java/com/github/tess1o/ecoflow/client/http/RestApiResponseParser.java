package com.github.tess1o.ecoflow.client.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.tess1o.ecoflow.client.http.response.EcoflowApiResponse;
import com.github.tess1o.ecoflow.client.http.response.EcoflowDevice;

import java.util.List;
import java.util.Map;

public interface RestApiResponseParser {
    <T> EcoflowApiResponse<T> parseResponse(String json, TypeReference<EcoflowApiResponse<T>> typeRef);

    EcoflowApiResponse<List<EcoflowDevice>> parseDevicesResponse(String json);

    EcoflowApiResponse<Map<String, Object>> parseParamsResponse(String json);
}

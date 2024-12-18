package com.github.tess1o.ecoflow.client.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.tess1o.ecoflow.client.http.response.EcoflowApiResponse;
import com.github.tess1o.ecoflow.client.http.response.EcoflowDevice;
import com.github.tess1o.ecoflow.exceptions.EcoflowResponseParserException;

import java.util.List;
import java.util.Map;

public class EcoflowRestApiResponseParser implements RestApiResponseParser {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final TypeReference<EcoflowApiResponse<List<EcoflowDevice>>> ECOFLOW_DEVICE_LIST_TYPE_REF = new TypeReference<>() {
    };
    private static final TypeReference<EcoflowApiResponse<Map<String, Object>>> ECOFLOW_PARAMS_TYPE_DEF = new TypeReference<>() {
    };

    public <T> EcoflowApiResponse<T> parseResponse(String json, TypeReference<EcoflowApiResponse<T>> typeRef) {
        try {
            return objectMapper.readValue(json, typeRef);
        } catch (JsonProcessingException e) {
            throw new EcoflowResponseParserException(e);
        }
    }

    public EcoflowApiResponse<List<EcoflowDevice>> parseDevicesResponse(String json) {
        return parseResponse(json, ECOFLOW_DEVICE_LIST_TYPE_REF);
    }

    public EcoflowApiResponse<Map<String, Object>> parseParamsResponse(String json) {
        return parseResponse(json, ECOFLOW_PARAMS_TYPE_DEF);
    }
}

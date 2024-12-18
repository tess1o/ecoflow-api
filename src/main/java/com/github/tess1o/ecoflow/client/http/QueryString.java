package com.github.tess1o.ecoflow.client.http;

import com.github.tess1o.ecoflow.exceptions.EcoflowInvalidParameterException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class QueryString {

    private final JSONObject request;
    private volatile String queryString;

    public QueryString(JSONObject request) {
        this.request = request;
    }

    public QueryString(Map<String, Object> jsonAsMap) {
        this.request = new JSONObject(jsonAsMap);
    }

    public QueryString(String json) {
        this.request = new JSONObject(json);
    }

    public boolean isEmpty() {
        return this.request == null || this.request.isEmpty();
    }

    public String toQueryString() {
        if (queryString == null) {
            synchronized (this) { // Thread-safety if needed
                if (queryString == null) {
                    queryString = calculateQueryString();
                }
            }
        }
        return queryString;
    }

    private String calculateQueryString() {
        SortedMap<String, Object> query = requestToMap(this.request);
        if (query.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        query.keySet().forEach(key -> builder.append(key)
                .append("=")
                .append(query.get(key))
                .append("&")
        );

        if (builder.isEmpty()) {
            return "";
        }
        return builder.substring(0, builder.length() - 1);
    }

    private SortedMap<String, Object> requestToMap(JSONObject jsonObject) {
        if (jsonObject == null || jsonObject.isEmpty()) {
            throw new EcoflowInvalidParameterException("parameters are empty");
        }
        SortedMap<String, Object> map = new TreeMap<>();
        for (String key : jsonObject.keySet()) {
            Object value = jsonObject.get(key);
            map.putAll(getByObject(key, value));
        }
        return map;
    }

    private Map<String, Object> getByObject(String key, Object value) {
        if (key == null || key.isEmpty() || value == null) {
            throw new EcoflowInvalidParameterException("parameters are empty");
        }
        if (value instanceof JSONArray) {
            return getByJsonArray(key, (JSONArray) value);
        } else if (value instanceof JSONObject) {
            return getByJSONObject(key, (JSONObject) value);
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put(key, value);
            return map;
        }
    }

    private Map<String, Object> getByJsonArray(String key, JSONArray value) {
        if (key == null || key.isEmpty() || value == null || value.isEmpty()) {
            throw new EcoflowInvalidParameterException("either key or value is null or empty in the json array");
        }
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < value.length(); i++) {
            map.putAll(getByObject(getArrayKey(key, i), value.get(i)));
        }
        return map;
    }

    private Map<String, Object> getByJSONObject(String key, JSONObject value) {
        if (key == null || key.isEmpty() || value == null || value.isEmpty()) {
            throw new EcoflowInvalidParameterException("either key or value is null or empty in the json object");
        }
        Map<String, Object> map = new HashMap<>();
        for (String innerKey : value.keySet()) {
            map.putAll(getByObject(getObjectKey(key, innerKey), value.get(innerKey)));
        }
        return map;
    }

    private String getObjectKey(String key, String innerKey) {
        return key + "." + innerKey;
    }

    private String getArrayKey(String key, int index) {
        return key + "[" + index + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        QueryString that = (QueryString) o;
        return Objects.equals(request, that.request);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(request);
    }

    @Override
    public String toString() {
        return queryString;
    }
}
